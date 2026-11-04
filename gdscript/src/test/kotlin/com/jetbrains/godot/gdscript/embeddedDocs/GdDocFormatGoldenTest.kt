package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.openapi.application.PathManager
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.embeddedDocs.GdCoreDocPipeline
import gdscript.embeddedDocs.GdDocFormat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.zip.DeflaterOutputStream

/**
 * Pins the bytes that the core documentation pipeline writes for a checked-in fixture.
 *
 * The hash covers the extractor, splitter, and pipeline for one Linux-shaped input.
 * It excludes compressed bytes because a JDK change can alter their form.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Documentation"])
class GdDocFormatGoldenTest {

    // One row per format version. Keep an old row, because it documents the history.
    private val goldenHashes: Map<Int, String> = mapOf(
        1 to "01828750abdf40042d898eaeea602f0d9a59399e867a9c9c00efe9a8b2ce7231",
    )

    @Test
    fun testPipelineOutputMatchesTheGoldenHash() {
        val expected = goldenHashes[GdDocFormat.VERSION]
        assertNotNull(expected, "Add a golden hash row for format version ${GdDocFormat.VERSION}.")

        val files = build().files.map { it.fileName to it.bytes }
        assertEquals(GdDocFormat.VERSION to expected, GdDocFormat.VERSION to hash(files))
    }

    @Test
    fun testPipelineAcceptsTheFixtureAndNamesEveryRequiredClass() {
        val outcome = build()

        assertEquals(
            listOf("@GDScript.xml", "Node.xml", "Object.xml", "RefCounted.xml"),
            outcome.files.map { it.fileName },
        )
        assertTrue(outcome.files.all { it.bytes.isNotEmpty() }, "Every file must hold bytes.")
        assertEquals(null, outcome.recoveryReport)
    }

    private fun build(): GdCoreDocPipeline.Outcome.Ready {
        val blob = Files.readAllBytes(testDataPath("gdscript/embeddedDocs/coreDocsBlob.xml"))
        // The filler proves that the pipeline finds the stream at a non-zero offset, as it does in a real executable.
        val binaryBytes = ByteArray(1024) { 0x55 } + zlib(blob) + ByteArray(64) { 0x2A }
        val binary = Files.createTempFile("gd-doc-format-golden", ".bin")
        try {
            Files.write(binary, binaryBytes)
            val outcome = GdCoreDocPipeline.build(binary) { }
            assertTrue(outcome is GdCoreDocPipeline.Outcome.Ready, "The pipeline must accept the fixture: $outcome")
            return outcome as GdCoreDocPipeline.Outcome.Ready
        }
        finally {
            Files.deleteIfExists(binary)
        }
    }

    private fun hash(pairs: List<Pair<String, ByteArray>>): String {
        val digest = MessageDigest.getInstance("SHA-256")
        for ((name, bytes) in pairs.sortedBy { it.first }) {
            digest.update(name.toByteArray(Charsets.UTF_8))
            digest.update(0)
            digest.update(bytes)
            digest.update(0)
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun zlib(bytes: ByteArray): ByteArray = ByteArrayOutputStream().also { result ->
        DeflaterOutputStream(result).use { it.write(bytes) }
    }.toByteArray()

    private fun testDataPath(relativePath: String): Path {
        val home = PathManager.getHomeDirFor(javaClass)
        if (home != null) return home.resolve("dotnet/Plugins/godot-support/gdscript/src/test/testData").resolve(relativePath)
        return PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test/testData").resolve(relativePath)
    }
}
