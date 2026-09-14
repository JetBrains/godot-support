package com.jetbrains.godot.gdscript.embeddedDocs

import gdscript.embeddedDocs.GdCoreDocPipeline
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * Tests the accept and the reject rules of the core pipeline, and the write step.
 *
 * The pipeline must recover per class. It must reject only when the set cannot serve the reader.
 */
@RunWith(JUnit4::class)
class GdCoreDocPipelineTest {

    @Test
    fun testRejectsAnUnreadableBinary() {
        val missing = Files.createTempFile("gd-core-pipeline-missing", ".bin")
        Files.delete(missing)

        val rejected = rejected(GdCoreDocPipeline.build(missing) { })

        assertEquals(GdCoreDocPipeline.Outcome.RejectReason.INPUT_UNAVAILABLE, rejected.reason)
        assertEquals(0, rejected.candidateCount)
    }

    @Test
    fun testRejectsABinaryWithoutDocumentation() = withBinary(ByteArray(4096) { 0x55 }) { binary ->
        val rejected = rejected(GdCoreDocPipeline.build(binary) { })

        assertEquals(GdCoreDocPipeline.Outcome.RejectReason.NO_CANDIDATE_FOUND, rejected.reason)
        assertEquals(0, rejected.candidateCount)
    }

    @Test
    fun testRejectsASetThatMissesARequiredClass() {
        val blob = (classDocument("Object") + classDocument("Node") + classDocument("RefCounted")).toByteArray()

        withBinary(zlib(blob)) { binary ->
            val rejected = rejected(GdCoreDocPipeline.build(binary) { })

            assertEquals(GdCoreDocPipeline.Outcome.RejectReason.REQUIRED_CLASS_MISSING, rejected.reason)
            assertEquals(setOf("@GDScript"), rejected.missingClasses)
        }
    }

    @Test
    fun testRejectsWhenTheExtractorExceedsAStreamCap() {
        val payload = classDocument("Object").toByteArray() + ByteArray(8193 * 8192) { 'x'.code.toByte() }

        withBinary(zlib(payload)) { binary ->
            val rejected = rejected(GdCoreDocPipeline.build(binary) { })

            assertEquals(GdCoreDocPipeline.Outcome.RejectReason.STREAM_CAP_EXCEEDED, rejected.reason)
            assertEquals(1, rejected.candidateCount)
        }
    }

    @Test
    fun testRejectsWhenTheExtractorReachesItsTotalCap() {
        // Two streams of 48 MiB pass, and the third crosses the 128 MiB total cap.
        val payload = classDocument("Object").toByteArray() + ByteArray(48 * 1024 * 1024) { 'x'.code.toByte() }
        val stream = zlib(payload)

        withBinary(stream + stream + stream) { binary ->
            val rejected = rejected(GdCoreDocPipeline.build(binary) { })

            assertEquals(GdCoreDocPipeline.Outcome.RejectReason.TOTAL_CAP_EXCEEDED, rejected.reason)
            assertNull(rejected.candidateCount)
        }
    }

    @Test
    fun testRejectsWhenCandidatesFailValidation() = withBinary(zlib("plain text payload".toByteArray())) { binary ->
        val rejected = rejected(GdCoreDocPipeline.build(binary) { })

        assertEquals(GdCoreDocPipeline.Outcome.RejectReason.NO_CANDIDATE_VALIDATED, rejected.reason)
        assertEquals(1, rejected.candidateCount)
    }

    @Test
    fun testRejectsWhenTheExtractorReachesItsScanGuard() {
        val candidates = ByteArray(2 * 250_001) { index -> if (index % 2 == 0) 0x78.toByte() else 0x9C.toByte() }
        val payload = zlib(classDocument("Node").toByteArray()) + byteArrayOf(0) + candidates

        withBinary(payload) { binary ->
            val rejected = rejected(GdCoreDocPipeline.build(binary) { })

            assertEquals(GdCoreDocPipeline.Outcome.RejectReason.SCAN_GUARD_EXCEEDED, rejected.reason)
            assertEquals(250_001, rejected.candidateCount)
        }
    }

    @Test
    fun testDoesNotSweepAfterDroppingAStreamThatExceedsItsCap() {
        val oversized = classDocument("Dropped").toByteArray() + ByteArray(8193 * 8192) { 'x'.code.toByte() }

        withBinary(zlib(requiredBlob()) + zlib(oversized)) { binary ->
            val ready = ready(GdCoreDocPipeline.build(binary) { })

            assertFalse(ready.sweepStale)
            assertNotNull(ready.recoveryReport)
            assertTrue(
                ready.recoveryReport!!,
                ready.recoveryReport!!.contains(
                    "the extractor dropped at least one stream because it exceeded the per-stream cap, so the class set may be incomplete"
                )
            )
        }
    }

    @Test
    fun testSweepsOnlyAfterACleanExtraction() {
        withBinary(zlib(requiredBlob())) { binary ->
            val ready = ready(GdCoreDocPipeline.build(binary) { })
            assertTrue(ready.sweepStale)
            assertNull(ready.recoveryReport)
        }

        val malformed = "<class name=\"Broken\"> and this is not a document".toByteArray()
        withBinary(zlib(requiredBlob()) + zlib(malformed)) { binary ->
            val ready = ready(GdCoreDocPipeline.build(binary) { })
            assertFalse(ready.sweepStale)
            assertNotNull(ready.recoveryReport)
        }
    }

    @Test
    fun testKeepsTheGoodDocumentsWhenOneDocumentIsMalformed() {
        // The second stream carries the class marker, so the extractor accepts it, but it has no XML declaration.
        val malformed = "<class name=\"Broken\"> and this is not a document".toByteArray()

        withBinary(zlib(requiredBlob()) + zlib(malformed)) { binary ->
            val ready = ready(GdCoreDocPipeline.build(binary) { })

            assertEquals(listOf("@GDScript.xml", "Node.xml", "Object.xml", "RefCounted.xml"), ready.files.map { it.fileName })
            assertNotNull("The pipeline must report the dropped document.", ready.recoveryReport)
            assertTrue(ready.recoveryReport!!, ready.recoveryReport!!.contains("dropped a malformed document"))
        }
    }

    @Test
    fun testKeepsTheFirstDocumentWhenTwoStreamsDisagree() {
        val other = classDocument("Node", brief = "A different text.")

        withBinary(zlib(requiredBlob()) + zlib(other.toByteArray())) { binary ->
            val ready = ready(GdCoreDocPipeline.build(binary) { })

            val node = ready.files.single { it.fileName == "Node.xml" }
            assertTrue(node.bytes.decodeToString().contains("The brief text of Node."))
            assertTrue(ready.recoveryReport!!, ready.recoveryReport!!.contains("kept the first variant of class Node"))
        }
    }

    @Test
    fun testDropsADocumentWhoseClassNameIsNotAFileName() {
        val unsafe = classDocument("Bad/Name")

        withBinary(zlib(requiredBlob() + unsafe.toByteArray())) { binary ->
            val ready = ready(GdCoreDocPipeline.build(binary) { })

            assertEquals(4, ready.files.size)
            assertTrue(ready.recoveryReport!!, ready.recoveryReport!!.contains("Bad/Name"))
        }
    }

    @Test
    fun testFileNameForAcceptsAGodotClassName() {
        assertEquals("Node2D.xml", GdCoreDocPipeline.fileNameFor("Node2D"))
        assertEquals("@GDScript.xml", GdCoreDocPipeline.fileNameFor("@GDScript"))
        assertEquals("Node_3D-x.xml", GdCoreDocPipeline.fileNameFor("Node_3D-x"))
    }

    @Test
    fun testFileNameForRefusesAnUnsafeClassName() {
        assertNull(GdCoreDocPipeline.fileNameFor(""))
        assertNull(GdCoreDocPipeline.fileNameFor("."))
        assertNull(GdCoreDocPipeline.fileNameFor(".."))
        assertNull(GdCoreDocPipeline.fileNameFor("a/b"))
        assertNull(GdCoreDocPipeline.fileNameFor("a\\b"))
        assertNull(GdCoreDocPipeline.fileNameFor("a b"))
        assertNull(GdCoreDocPipeline.fileNameFor("a:b"))
    }

    @Test
    fun testWriteCoreDocsRemovesAStaleXmlFileAndKeepsEveryOtherFile() {
        val directory = Files.createTempDirectory("gd-core-docs")
        try {
            directory.resolve("Stale.xml").writeText("<class name=\"Stale\"/>")
            directory.resolve("keep.txt").writeText("keep me")

            GdCoreDocPipeline.writeCoreDocs(
                directory,
                listOf(GdCoreDocPipeline.ClassFile("Node.xml", "<class name=\"Node\"/>".toByteArray())),
                sweepStale = true,
            )

            assertEquals("<class name=\"Node\"/>", directory.resolve("Node.xml").readText())
            assertFalse(Files.exists(directory.resolve("Stale.xml")))
            assertTrue(Files.exists(directory.resolve("keep.txt")))
        }
        finally {
            Files.walk(directory).use { paths ->
                paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
            }
        }
    }

    @Test
    fun testWriteCoreDocsKeepsAStaleXmlFileWhenTheSweepIsOff() {
        // A degraded extraction can miss a class that the directory already documents, so it must not delete.
        val directory = Files.createTempDirectory("gd-core-docs-no-sweep")
        try {
            directory.resolve("Stale.xml").writeText("<class name=\"Stale\"/>")

            GdCoreDocPipeline.writeCoreDocs(directory, files("Node"), sweepStale = false)

            assertTrue("The earlier class file must stay.", Files.exists(directory.resolve("Stale.xml")))
            assertTrue(Files.exists(directory.resolve("Node.xml")))
        }
        finally {
            Files.walk(directory).use { paths ->
                paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
            }
        }
    }

    @Test
    fun testWriteCoreDocsCreatesTheDirectory() {
        val root = Files.createTempDirectory("gd-core-docs-new")
        val directory = root.resolve("4.5")
        try {
            GdCoreDocPipeline.writeCoreDocs(directory, listOf(GdCoreDocPipeline.ClassFile("Node.xml", "x".toByteArray())), sweepStale = true)

            assertTrue(Files.isDirectory(directory))
            assertEquals("x", directory.resolve("Node.xml").readText())
        }
        finally {
            Files.walk(root).use { paths ->
                paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
            }
        }
    }

    private fun files(vararg classNames: String): List<GdCoreDocPipeline.ClassFile> =
        classNames.map { GdCoreDocPipeline.ClassFile("$it.xml", "<class name=\"$it\"/>".toByteArray()) }

    private fun requiredBlob(): ByteArray =
        (classDocument("Object") + classDocument("Node") + classDocument("RefCounted") + classDocument("@GDScript"))
            .toByteArray()

    private fun classDocument(name: String, brief: String = "The brief text of $name."): String =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<class name=\"$name\" inherits=\"\">\n" +
            "\t<brief_description>$brief</brief_description>\n" +
            "\t<description></description>\n" +
            "\t<tutorials></tutorials>\n" +
            "</class>\n"

    private fun rejected(outcome: GdCoreDocPipeline.Outcome): GdCoreDocPipeline.Outcome.Rejected {
        assertTrue("The pipeline must reject the input: $outcome", outcome is GdCoreDocPipeline.Outcome.Rejected)
        return outcome as GdCoreDocPipeline.Outcome.Rejected
    }

    private fun ready(outcome: GdCoreDocPipeline.Outcome): GdCoreDocPipeline.Outcome.Ready {
        assertTrue("The pipeline must accept the input: $outcome", outcome is GdCoreDocPipeline.Outcome.Ready)
        return outcome as GdCoreDocPipeline.Outcome.Ready
    }

    private fun zlib(bytes: ByteArray): ByteArray = ByteArrayOutputStream().also { result ->
        DeflaterOutputStream(result).use { it.write(bytes) }
    }.toByteArray()

    private fun withBinary(bytes: ByteArray, action: (Path) -> Unit) {
        val binary = Files.createTempFile("gd-core-pipeline", ".bin")
        try {
            Files.write(binary, bytes)
            action(binary)
        }
        finally {
            Files.deleteIfExists(binary)
        }
    }
}
