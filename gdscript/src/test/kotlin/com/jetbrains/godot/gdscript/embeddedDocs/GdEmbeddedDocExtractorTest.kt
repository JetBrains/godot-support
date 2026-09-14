package com.jetbrains.godot.gdscript.embeddedDocs

import gdscript.embeddedDocs.GdDocXmlSplitter
import gdscript.embeddedDocs.GdEmbeddedDocExtractor
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.DeflaterOutputStream

@RunWith(JUnit4::class)
class GdEmbeddedDocExtractorTest {
    private val document = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\"></class>\n".toByteArray()

    @Test
    fun testFindsValidBlobAtNonZeroOffset() = withFixture(byteArrayOf(1, 2, 3, 4, 5) + zlib(document) + byteArrayOf(6, 7)) { path ->
        val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.Found

        assertEquals(1, result.streams.size)
        val stream = result.streams.single()
        assertEquals(5L, stream.fileOffset)
        assertEquals(zlib(document).size.toLong(), stream.compressedLength)
        assertEquals(document.size, stream.uncompressedLength)
        assertArrayEquals(document, stream.bytes)
    }

    @Test
    fun testFindsMagicPairAcrossChunkBoundary() {
        val filler = ByteArray(GdEmbeddedDocExtractor.SCAN_CHUNK_SIZE - 1) { 0x55 }
        withFixture(filler + zlib(document)) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.Found

            assertEquals(GdEmbeddedDocExtractor.SCAN_CHUNK_SIZE.toLong() - 1, result.streams.single().fileOffset)
            assertArrayEquals(document, result.streams.single().bytes)
        }
    }

    @Test
    fun testRejectsNonXmlDecoy() = assertNoValidatedCandidate(zlib("plain text payload".toByteArray()))

    @Test
    fun testFindsMarkerAtNonZeroWindowOffset() {
        val payload = ByteArray(17) { 'x'.code.toByte() } + "<class name=\"Node\"></class>".toByteArray()
        withFixture(zlib(payload)) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.Found

            assertArrayEquals(payload, result.streams.single().bytes)
        }
    }

    @Test
    fun testRejectsMarkerOutsideFirst64Bytes() {
        val payload = ByteArray(65) { 'x'.code.toByte() } + document
        assertNoValidatedCandidate(zlib(payload))
    }

    @Test
    fun testRejectsHeaderThatFailsMod31Check() = withFixture(byteArrayOf(0x78, 0x9D.toByte()) + ByteArray(256)) { path ->
        val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.NotFound

        assertEquals(GdEmbeddedDocExtractor.NotFoundReason.NO_CANDIDATE_FOUND, result.reason)
        assertEquals(0, result.candidateCount)
    }

    @Test
    fun testRejectsTruncatedStream() {
        val compressed = zlib(document)
        assertNoValidatedCandidate(compressed.copyOf(compressed.size - 3))
    }

    @Test
    fun testReportsStreamCap() {
        val compressed = ByteArrayOutputStream().also { bytes ->
            DeflaterOutputStream(bytes).use { output ->
                output.write(document)
                val block = ByteArray(8192) { 'x'.code.toByte() }
                repeat(8193) { output.write(block) }
            }
        }.toByteArray()

        withFixture(compressed) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.NotFound
            assertEquals(GdEmbeddedDocExtractor.NotFoundReason.CAP_EXCEEDED, result.reason)
            assertEquals(1, result.candidateCount)
        }
    }

    @Test
    fun testReportsAcceptedTotalCapBeforeAThirdStreamCompletes() {
        val payload = document + ByteArray(48 * 1024 * 1024) { 'x'.code.toByte() }
        val compressed = zlib(payload)

        withFixture(compressed + compressed + compressed) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.Found

            assertEquals(2, result.streams.size)
            assertTrue(result.acceptedTotalExceeded)
            assertEquals(2L * payload.size, result.streams.sumOf { it.uncompressedLength.toLong() })
        }
    }

    @Test
    fun testScansRealisticCandidateDensityToTheEnd() {
        val candidateCount = 200_000
        val candidates = ByteArray(2 * candidateCount) { index -> if (index % 2 == 0) 0x78.toByte() else 0x9C.toByte() }

        withFixture(candidates + byteArrayOf(0) + zlib(document)) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.Found

            assertEquals(candidateCount + 1, result.candidateCount)
            assertEquals((candidates.size + 1).toLong(), result.streams.single().fileOffset)
            assertArrayEquals(document, result.streams.single().bytes)
        }
    }

    @Test
    fun testReportsGuardAfterAnEarlierMatch() {
        val candidates = ByteArray(2 * 250_000) { index -> if (index % 2 == 0) 0x78.toByte() else 0x9C.toByte() }

        withFixture(zlib(document) + byteArrayOf(0) + candidates) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.NotFound

            assertEquals(GdEmbeddedDocExtractor.NotFoundReason.SCAN_GUARD_EXCEEDED, result.reason)
            assertEquals(250_001, result.candidateCount)
        }
    }

    @Test
    fun testRealGodotBinaryWhenAvailable() {
        val configuredPath = System.getenv("GODOT_BINARY_PATH")
        when (val outcome = GodotBinaryFromEnvironment.resolve(configuredPath)) {
            GodotBinaryFromEnvironment.Outcome.NotConfigured ->
                assumeTrue("Set GODOT_BINARY_PATH to run the real Godot binary extraction test.", false)
            is GodotBinaryFromEnvironment.Outcome.Invalid -> fail(outcome.message)
            is GodotBinaryFromEnvironment.Outcome.Ready -> {
                val binary = outcome.binary
                val result = GdEmbeddedDocExtractor.extract(binary) as GdEmbeddedDocExtractor.Result.Found
                val splitResults = result.streams.map { GdDocXmlSplitter.split(it.bytes) }
                val documents = splitResults.flatMap { it.documents }

                assertTrue("The real binary must contain more than 500 class documents.", documents.size > 500)
                assertEquals(0, splitResults.sumOf { it.conflicts.size })
                assertEquals(0, splitResults.sumOf { it.malformedDocuments.size })
                assertTrue("Every document must have a class root element.", documents.all { document ->
                    GdDocXmlSplitter.split(document.content).documents.singleOrNull()?.className == document.className
                })
            }
        }
    }

    @Test
    fun testReportsUnavailableInput() {
        val path = Files.createTempFile("gd-embedded-docs-missing", ".bin")
        Files.delete(path)

        val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.NotFound

        assertEquals(GdEmbeddedDocExtractor.NotFoundReason.INPUT_UNAVAILABLE, result.reason)
        assertEquals(0, result.candidateCount)
    }

    @Test
    fun testRethrowsCancellation() {
        val cancellation = IllegalStateException("cancel")
        withFixture(ByteArray(2048) { 0x55 }) { path ->
            try {
                GdEmbeddedDocExtractor.extract(path) { throw cancellation }
                throw AssertionError("The extractor did not rethrow the cancellation.")
            } catch (e: IllegalStateException) {
                assertTrue(e === cancellation)
            }
        }
    }

    @Test
    fun testRethrowsCancellationInsideInflateLoop() {
        val payload = document + ByteArray(1024 * 1024) { 'x'.code.toByte() }
        val cancellation = IllegalStateException("cancel during inflate")
        val checks = AtomicInteger()
        withFixture(zlib(payload)) { path ->
            try {
                GdEmbeddedDocExtractor.extract(path) {
                    if (checks.incrementAndGet() == 10) throw cancellation
                }
                throw AssertionError("The extractor did not rethrow the cancellation.")
            } catch (e: IllegalStateException) {
                assertTrue(e === cancellation)
                assertEquals(10, checks.get())
            }
        }
    }

    @Test
    fun testCollectsEveryValidBlob() {
        val second = document.decodeToString().replace("Node", "Resource").toByteArray()
        val firstCompressed = zlib(document)
        val secondCompressed = zlib(second)
        withFixture(byteArrayOf(1, 2) + firstCompressed + byteArrayOf(3, 4, 5) + secondCompressed) { path ->
            val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.Found

            assertEquals(2, result.streams.size)
            assertEquals(listOf(2L, (2 + firstCompressed.size + 3).toLong()), result.streams.map { it.fileOffset })
            assertArrayEquals(document, result.streams[0].bytes)
            assertArrayEquals(second, result.streams[1].bytes)
        }
    }

    @Test
    fun testReportsNoCandidate() = withFixture(ByteArray(2048) { 0x55 }) { path ->
        val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.NotFound

        assertEquals(GdEmbeddedDocExtractor.NotFoundReason.NO_CANDIDATE_FOUND, result.reason)
        assertEquals(0, result.candidateCount)
    }

    private fun assertNoValidatedCandidate(bytes: ByteArray) = withFixture(bytes) { path ->
        val result = GdEmbeddedDocExtractor.extract(path) as GdEmbeddedDocExtractor.Result.NotFound
        assertEquals(GdEmbeddedDocExtractor.NotFoundReason.NO_CANDIDATE_VALIDATED, result.reason)
        assertTrue(result.candidateCount >= 1)
    }

    private fun zlib(bytes: ByteArray): ByteArray = ByteArrayOutputStream().also { result ->
        DeflaterOutputStream(result).use { it.write(bytes) }
    }.toByteArray()

    private fun withFixture(bytes: ByteArray, action: (java.nio.file.Path) -> Unit) {
        val path = Files.createTempFile("gd-embedded-docs", ".bin")
        try {
            Files.write(path, bytes)
            action(path)
        } finally {
            Files.deleteIfExists(path)
        }
    }
}
