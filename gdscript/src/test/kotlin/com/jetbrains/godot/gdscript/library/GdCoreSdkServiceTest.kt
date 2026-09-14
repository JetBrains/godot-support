package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.library.GdCoreSdkService
import gdscript.library.GdSdkIntegrityValidator
import gdscript.library.GdSdkPathManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicReference
import java.util.zip.DeflaterOutputStream
import kotlin.time.Duration.Companion.milliseconds

/**
 * Tests the two promises of the shared core documentation service.
 *
 * The stamp becomes valid only after the new set is visible in the VFS and the trackers moved.
 * One lock serializes every write of the shared directory, so two callers never write it together.
 *
 * The test body runs off the EDT, because the service suspends on a VFS refresh that needs a free EDT.
 */
@RunWith(JUnit4::class)
class GdCoreSdkServiceTest : BasePlatformTestCase() {

    // A version that no real Godot build uses, so the test cannot disturb a real documentation set.
    private val version: Version = Version.parseVersion("99.7")!!

    private val coreSdkDir: Path get() = GdSdkPathManager.getCoreSdkDir(version)
    private val stampFile: Path get() = GdSdkPathManager.getCoreSdkStampFile(version)

    private lateinit var binary: Path

    override fun runInDispatchThread(): Boolean = false

    override fun setUp() {
        super.setUp()
        removeCoreDirectory()
        binary = Files.createTempFile("gd-core-service", ".bin")
        Files.write(binary, ByteArray(512) { 0x55 } + zlib(fixtureBlob()))
    }

    override fun tearDown() {
        try {
            Files.deleteIfExists(binary)
            removeCoreDirectory()
        } catch (e: Throwable) {
            addSuppressedException(e)
        } finally {
            super.tearDown()
        }
    }

    @Test
    fun testWritesTheStampOnlyAfterTheSetIsVisible() = runBlocking {
        // The empty directory enters the VFS first, and its child list loads, so the refresh inside the
        // service compares an empty list with four files and reports a create event for each one.
        Files.createDirectories(coreSdkDir)
        val vfsDirectory = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(coreSdkDir)
        assertNotNull("The core directory must enter the VFS.", vfsDirectory)
        assertEquals("The core directory must start empty.", 0, vfsDirectory!!.children.size)

        val stamp = GdSdkIntegrityValidator.coreStamp(version, binary)!!
        val running = AtomicReference<Job?>(null)
        val seen = java.util.concurrent.CopyOnWriteArrayList<String>()
        // The VFS path uses '/' on every OS, so it matches the event paths also on Windows.
        val directoryPath = vfsDirectory.path
        ApplicationManager.getApplication().messageBus.connect(testRootDisposable)
            .subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
                override fun after(events: List<VFileEvent>) {
                    // The event arrives while the service makes the new set visible, which is after the
                    // write and before the stamp. A cancellation here must leave the stamp invalid.
                    val matched = events.map { it.path }.filter { it == directoryPath || it.startsWith("$directoryPath/") }
                    if (matched.isEmpty()) return
                    seen.addAll(matched)
                    running.get()?.cancel()
                }
            })

        // The join happens outside withTimeout, so a blocking uncancellable call can outlive the timeout.
        val deferred = async(Dispatchers.Default) { GdCoreSdkService.getInstance().ensureCoreDocs(version, binary) }
        running.set(deferred)

        var cancelled = false
        try {
            withTimeout(TIMEOUT_MILLIS.milliseconds) { deferred.await() }
        }
        catch (e: TimeoutCancellationException) {
            throw e
        }
        catch (_: CancellationException) {
            cancelled = true
        }

        assertTrue("The run must be cancelled while the new set becomes visible. Seen events: $seen", cancelled)
        assertTrue("The write must have happened, or the test misses the window.", Files.exists(coreSdkDir.resolve("Node.xml")))
        assertFalse(
            "A cancelled run must leave the stamp invalid, so a later run retries.",
            GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp),
        )
    }

    @Test
    fun testTwoConcurrentCallsSerialize() = runBlocking {
        val stamp = GdSdkIntegrityValidator.coreStamp(version, binary)!!
        assertFalse(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp))

        val results = withTimeout(TIMEOUT_MILLIS.milliseconds) {
            val first = async(Dispatchers.Default) { GdCoreSdkService.getInstance().ensureCoreDocs(version, binary) }
            val second = async(Dispatchers.Default) { GdCoreSdkService.getInstance().ensureCoreDocs(version, binary) }
            listOf(first.await(), second.await())
        }

        // One call writes the set and the stamp. The other waits for the lock and then sees the valid stamp.
        assertEquals("Exactly one call must write: $results", 1, results.count { it == GdCoreSdkService.Result.WRITTEN })
        assertEquals("Exactly one call must find the set up to date: $results", 1, results.count { it == GdCoreSdkService.Result.UP_TO_DATE })
        assertTrue(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp))
    }

    @Test
    fun testADegradedExtractionAddsItsFilesAndDeletesNothing() = runBlocking {
        // A large set is already on disk. The degraded binary describes only the four required classes.
        // The user must end up with both sets, never with less documentation than before.
        Files.createDirectories(coreSdkDir)
        val existing = (1..100).map { coreSdkDir.resolve("Class$it.xml") }
        existing.forEach { Files.writeString(it, "<class name=\"stale\"/>") }

        withDegradedBinary { degraded ->
            val stamp = GdSdkIntegrityValidator.coreStamp(version, degraded)!!
            val result = withTimeout(TIMEOUT_MILLIS.milliseconds) {
                GdCoreSdkService.getInstance().ensureCoreDocs(version, degraded)
            }

            assertEquals(GdCoreSdkService.Result.WRITTEN, result)
            assertTrue("A degraded extraction must delete nothing.", existing.all { Files.exists(it) })
            assertTrue("The new files must land.", Files.exists(coreSdkDir.resolve("Node.xml")))
            assertTrue(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp))
        }
    }

    @Test
    fun testADegradedExtractionNeverRefusesForever() {
        // A refusal that depends only on the binary and the directory repeats for good, because nothing
        // clears either one. This set of four files also breaks any threshold that rounds below one.
        Files.createDirectories(coreSdkDir)
        val existing = (1..4).map { coreSdkDir.resolve("Stale$it.xml") }
        existing.forEach { Files.writeString(it, "<class name=\"stale\"/>") }

        runBlocking {
            withDegradedBinary { degraded ->
                val first = withTimeout(TIMEOUT_MILLIS.milliseconds) {
                    GdCoreSdkService.getInstance().ensureCoreDocs(version, degraded)
                }
                val second = withTimeout(TIMEOUT_MILLIS.milliseconds) {
                    GdCoreSdkService.getInstance().ensureCoreDocs(version, degraded)
                }

                assertEquals("The first run must publish what it has.", GdCoreSdkService.Result.WRITTEN, first)
                assertEquals("The second run must find the stamp valid, so no run repeats the scan.", GdCoreSdkService.Result.UP_TO_DATE, second)
                assertTrue("The earlier files must stay.", existing.all { Files.exists(it) })
                assertTrue(Files.exists(coreSdkDir.resolve("Node.xml")))
            }
        }
    }

    /** A binary whose blob holds the four required classes plus one stream that the splitter refuses. */
    private inline fun withDegradedBinary(action: (Path) -> Unit) {
        val degraded = Files.createTempFile("gd-core-service-degraded", ".bin")
        try {
            val malformed = "<class name=\"Broken\"> and this is not a document".toByteArray()
            Files.write(degraded, zlib(fixtureBlob()) + zlib(malformed))
            action(degraded)
        }
        finally {
            Files.deleteIfExists(degraded)
        }
    }

    private fun removeCoreDirectory() {
        Files.deleteIfExists(stampFile)
        if (!Files.exists(coreSdkDir)) return
        Files.walk(coreSdkDir).use { paths ->
            paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
        }
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(coreSdkDir)
    }

    private fun fixtureBlob(): ByteArray = Files.readAllBytes(testDataPath("gdscript/embeddedDocs/coreDocsBlob.xml"))

    private fun zlib(bytes: ByteArray): ByteArray = ByteArrayOutputStream().also { result ->
        DeflaterOutputStream(result).use { it.write(bytes) }
    }.toByteArray()

    private fun testDataPath(relativePath: String): Path {
        val home = PathManager.getHomeDirFor(javaClass)
        if (home != null) return home.resolve("dotnet/Plugins/godot-support/gdscript/src/test/testData").resolve(relativePath)
        return PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test/testData").resolve(relativePath)
    }

    private companion object {
        const val TIMEOUT_MILLIS = 60_000L
    }
}
