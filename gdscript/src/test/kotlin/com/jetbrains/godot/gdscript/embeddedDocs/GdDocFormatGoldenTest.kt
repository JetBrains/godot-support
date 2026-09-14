package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.openapi.application.PathManager
import gdscript.embeddedDocs.GdCoreDocPipeline
import gdscript.embeddedDocs.GdDocFormat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
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
@RunWith(JUnit4::class)
class GdDocFormatGoldenTest {

    // One row per format version. Keep an old row, because it documents the history.
    private val goldenHashes: Map<Int, String> = mapOf(
        1 to "01828750abdf40042d898eaeea602f0d9a59399e867a9c9c00efe9a8b2ce7231",
    )

    @Test
    fun testPipelineOutputMatchesTheGoldenHash() {
        val expected = goldenHashes[GdDocFormat.VERSION]
        assertNotNull("Add a golden hash row for format version ${GdDocFormat.VERSION}.", expected)

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
        assertTrue("Every file must hold bytes.", outcome.files.all { it.bytes.isNotEmpty() })
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
            assertTrue("The pipeline must accept the fixture: $outcome", outcome is GdCoreDocPipeline.Outcome.Ready)
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
