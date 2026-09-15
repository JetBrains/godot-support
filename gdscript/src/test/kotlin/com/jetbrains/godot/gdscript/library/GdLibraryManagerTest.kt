package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.util.Version
import com.intellij.openapi.util.io.IoTestUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.system.LowLevelLocalMachineAccess
import com.intellij.util.system.OS
import gdscript.library.GdLibraryManager
import gdscript.library.GdSdkFingerprints
import gdscript.library.GdSdkIntegrityValidator
import gdscript.library.GdSdkPathManager
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream
import kotlin.time.Duration.Companion.milliseconds

/**
 * Checks SDK publication, fallback, and refresh behavior.
 *
 * The tests run off the EDT because a VFS refresh needs a free EDT.
 */
@RunWith(JUnit4::class)
class GdLibraryManagerTest : BasePlatformTestCase() {

    // A version that no real Godot build uses, so the test cannot disturb a real documentation set.
    private val version: Version = Version.parseVersion("99.8")!!

    private val coreSdkDir: Path get() = GdSdkPathManager.getCoreSdkDir(version)
    private val stampFile: Path get() = GdSdkPathManager.getCoreSdkStampFile(version)

    // The Godot project directory the test pretends the SDK was loaded for; unrelated to the IDE test project.
    private lateinit var projectBasePath: Path
    private lateinit var fakeGodot: Path
    private lateinit var fakeGodotMode: Path
    private lateinit var singletonStarted: Path
    private lateinit var singletonRelease: Path

    override fun runInDispatchThread(): Boolean = false

    override fun setUp() {
        super.setUp()
        // A run that crashed earlier can leave a core stamp, and testFailedCoreStepWritesNoStamp checks that none exists.
        Files.deleteIfExists(stampFile)
        removeProjectDocs()
        projectBasePath = Files.createTempDirectory("gd-library-manager-project")
        fakeGodotMode = Files.createTempFile("gd-library-manager-mode", ".txt")
        singletonStarted = Files.createTempFile("gd-library-manager-singleton-started", ".tmp")
        singletonRelease = Files.createTempFile("gd-library-manager-singleton-release", ".tmp")
        Files.delete(singletonStarted)
        Files.delete(singletonRelease)
        fakeGodot = createFakeGodot(fakeGodotMode, singletonStarted, singletonRelease)
    }

    override fun tearDown() {
        try {
            Files.deleteIfExists(stampFile)
            if (Files.exists(coreSdkDir)) {
                Files.walk(coreSdkDir).use { paths ->
                    paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
                }
            }
            deleteRecursively(projectBasePath)
            Files.deleteIfExists(fakeGodot)
            Files.deleteIfExists(fakeGodotMode)
            Files.deleteIfExists(singletonStarted)
            Files.deleteIfExists(singletonRelease)
            removeProjectDocs()
            LocalFileSystem.getInstance().refreshAndFindFileByNioFile(coreSdkDir)
        } catch (e: Throwable) {
            addSuppressedException(e)
        } finally {
            super.tearDown()
        }
    }

    @Test
    fun testDoctoolRunWritesTheRawStamp() {
        IoTestUtil.assumeUnix() // The fake Godot is a POSIX shell script.
        runBlocking {
            val extensionsRoot = GdSdkPathManager.getProjectExtensionsRoot(project)!!
            assertFalse("No extension directory must exist before the first generation.", Files.exists(extensionsRoot))
            Files.writeString(fakeGodotMode, "success")

            generate(fakeGodot)

            val rawDir = GdSdkPathManager.getProjectExtensionsRawDir(project)!!
            assertTrue("The raw directory must exist before Godot starts.", Files.isDirectory(rawDir))
            assertTrue(
                "The first doctool run must write a valid raw directory stamp.",
                GdSdkIntegrityValidator.hasValidStamp(
                    GdSdkPathManager.getProjectExtensionsStampFile(project)!!,
                    GdSdkFingerprints.ofExtensionDeclarations(projectBasePath),
                    rawDir,
                ),
            )
        }
    }

    @Test
    fun testFailedCoreStepWritesNoStamp() = runBlocking {
        // A good set is already on disk from an earlier session.
        Files.createDirectories(coreSdkDir)
        Files.writeString(
            coreSdkDir.resolve("Node.xml"),
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\" inherits=\"Object\"></class>",
        )

        // Valid GDExtension stamps keep the doctool and merge steps out of this test.
        val extensionsStamp = GdSdkFingerprints.ofExtensionDeclarations(projectBasePath)
        val rawDir = GdSdkPathManager.getProjectExtensionsRawDir(project)!!
        val extensionsDir = GdSdkPathManager.getProjectExtensionsDir(project)!!
        val blobRoot = GdSdkPathManager.getProjectExtensionsBlobRoot(project)!!
        GdSdkIntegrityValidator.writeStamp(GdSdkPathManager.getProjectExtensionsStampFile(project)!!, extensionsStamp, rawDir)
        val mergeInput = extensionsStamp + "\n" + GdSdkFingerprints.ofSmallFilesByContent(blobRoot) + "\n" +
            GdSdkFingerprints.ofSmallFilesByContent(rawDir)
        GdSdkIntegrityValidator.writeStamp(GdSdkPathManager.getProjectExtensionsMergeStampFile(project)!!, mergeInput, extensionsDir)

        // The executable is gone, so the core step reads no attributes and returns FAILED.
        val missingBinary = Files.createTempFile("gd-library-manager-missing", ".bin")
        Files.delete(missingBinary)
        assertNull(GdSdkIntegrityValidator.coreStamp(version, missingBinary))

        generate(missingBinary)

        assertFalse("The core step must write no stamp.", Files.exists(stampFile))
        assertTrue("The core step must keep the earlier set.", Files.exists(coreSdkDir.resolve("Node.xml")))
    }

    @Test
    fun testBlobDocsArePublishedWithoutGodot() = runBlocking {
        installBlobExtension()

        generate(null)

        val publishedClass = publishedClass()
        assertContainsText("The blob class must be published without Godot.", Files.readString(publishedClass), "blob prose")
        val blobVirtualFile = LocalFileSystem.getInstance().findFileByNioFile(publishedClass)
        assertNotNull("The blob class must be visible in the VFS.", blobVirtualFile)
        assertContainsText(
            "The VFS must contain the blob class.",
            blobVirtualFile!!.contentsToByteArray().decodeToString(),
            "blob prose",
        )
    }

    @Test
    fun testMergeKeepsDoctoolStructureAndBlobProse() {
        IoTestUtil.assumeUnix() // The fake Godot is a POSIX shell script.
        runBlocking {
            installBlobExtension()
            Files.writeString(fakeGodotMode, "success")

            generate(fakeGodot)

            val merged = Files.readString(publishedClass())
            assertContainsText("The merge must keep the doctool structure.", merged, "inherits=\"RawBase\"")
            assertContainsText("The merge must copy the blob prose.", merged, "blob prose")
            assertRawOnlyClassPublished("A raw-only class must be published.")
        }
    }

    @Test
    fun testExtensionDocsBecomeVisibleBeforeTheSingletonStep() {
        IoTestUtil.assumeUnix() // The fake Godot is a POSIX shell script.
        runBlocking {
            // The VFS first caches the blob-only content, so only a refresh can show the merged content.
            installBlobExtension()
            generate(null)
            val blobVirtualFile = LocalFileSystem.getInstance().findFileByNioFile(publishedClass())
            assertNotNull("The blob class must be visible in the VFS.", blobVirtualFile)
            assertContainsText(
                "The VFS must initially contain the blob class.",
                blobVirtualFile!!.contentsToByteArray().decodeToString(),
                "blob prose",
            )

            Files.writeString(fakeGodotMode, "wait-for-singleton")
            val generation = async { generate(fakeGodot) }
            try {
                withTimeout(TIMEOUT_MILLIS) {
                    while (!Files.exists(singletonStarted)) delay(10.milliseconds)
                }

                val mergeStampFile = GdSdkPathManager.getProjectExtensionsMergeStampFile(project)!!
                assertTrue("The merge stamp must exist before the singleton step finishes.", Files.exists(mergeStampFile))
                val mergedVirtualFile = LocalFileSystem.getInstance().findFileByNioFile(publishedClass())
                assertNotNull("The merged class must stay visible in the VFS.", mergedVirtualFile)
                assertContainsText(
                    "The extension refresh must expose the merge before the singleton step finishes.",
                    mergedVirtualFile!!.contentsToByteArray().decodeToString(),
                    "inherits=\"RawBase\"",
                )
            }
            finally {
                Files.writeString(singletonRelease, "release")
                generation.await()
            }
        }
    }

    @Test
    fun testFailedDoctoolFallsBackToTheBlob() {
        IoTestUtil.assumeUnix() // The fake Godot is a POSIX shell script.
        runBlocking {
            installBlobExtension()
            Files.writeString(fakeGodotMode, "success")
            generate(fakeGodot)
            assertRawOnlyClassPublished("The successful run must publish the raw-only class.")

            Files.deleteIfExists(GdSdkPathManager.getProjectExtensionsStampFile(project)!!)
            Files.writeString(fakeGodotMode, "fail")
            generate(fakeGodot)

            assertContainsText("A failed doctool run must fall back to the blob.", Files.readString(publishedClass()), "blob prose")
            assertFalse("Partial raw output must not be published.", Files.exists(publishedDir().resolve("Partial.xml")))
            assertFalse("Stale raw-only output must not survive.", Files.exists(publishedDir().resolve("RawOnly.xml")))
        }
    }

    @Test
    fun testRemovedBlobRemovesItsPublishedClass() = runBlocking {
        installBlobExtension()
        generate(null)
        assertTrue("The blob class must be published first.", Files.exists(publishedClass()))

        // The library keeps its name but no longer holds a documentation blob.
        Files.write(projectBasePath.resolve(LIBRARY_FILE_NAME), ByteArray(513) { 0x55 })
        generate(null)

        assertFalse("A removed blob must remove its published class.", Files.exists(publishedClass()))
    }

    private suspend fun generate(godotPath: Path?) = withTimeout(TIMEOUT_MILLIS) {
        GdLibraryManager.generateSdkIfNeeded(version, project, godotPath, projectBasePath)
    }

    /** Adds a manifest and a library whose documentation blob describes the class `Shared`. */
    private fun installBlobExtension() {
        Files.writeString(projectBasePath.resolve("extension.gdextension"), manifestText())
        Files.write(projectBasePath.resolve(LIBRARY_FILE_NAME), ByteArray(512) { 0x55 } + zlib(blobClassXml().toByteArray()))
    }

    private fun publishedDir(): Path = GdSdkPathManager.getProjectExtensionsDir(project)!!

    private fun publishedClass(): Path = publishedDir().resolve("Shared.xml")

    /** Fails with [message], the [expected] fragment, and the full [text] when [text] does not hold [expected]. */
    private fun assertContainsText(message: String, text: String, expected: String) {
        assertTrue("$message\nExpected fragment: $expected\nActual text:\n$text", text.contains(expected))
    }

    /** Fails with [message] and the list of published files when the published directory does not hold `RawOnly.xml`. */
    private fun assertRawOnlyClassPublished(message: String) {
        val fileName = "RawOnly.xml"
        val directory = publishedDir()
        if (Files.exists(directory.resolve(fileName))) return
        val published = if (Files.isDirectory(directory)) {
            Files.list(directory).use { paths -> paths.map { it.fileName.toString() }.sorted().toList() }
        } else {
            emptyList()
        }
        fail("$message\nExpected file: $fileName\nPublished files: $published")
    }

    @OptIn(LowLevelLocalMachineAccess::class)
    private fun manifestText(): String {
        val platform = when (OS.CURRENT) {
            OS.Windows -> "windows"
            OS.macOS -> "macos"
            else -> "linux"
        }
        val architecture = when (System.getProperty("os.arch")) {
            "aarch64", "arm64" -> "arm64"
            else -> "x86_64"
        }
        return "[libraries]\n$platform.debug.$architecture = \"res://$LIBRARY_FILE_NAME\"\n"
    }

    private fun blobClassXml(): String =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<class name=\"Shared\"><brief_description>blob prose</brief_description><description>blob prose</description></class>"

    private fun createFakeGodot(modeFile: Path, singletonStartedFile: Path, singletonReleaseFile: Path): Path {
        val executable = Files.createTempFile("gd-library-manager-godot", ".sh")
        Files.writeString(
            executable,
            """
                #!/bin/sh
                mode=${'$'}(cat "${modeFile.toAbsolutePath()}")
                if [ "${'$'}1" = "--doctool" ]; then
                  if [ ! -d "${'$'}2" ]; then
                    echo 'The doctool output directory must exist.' >&2
                    exit 9
                  fi
                  output="${'$'}2/doc_classes"
                  mkdir -p "${'$'}output"
                  if [ "${'$'}mode" = "fail" ]; then
                    echo '<class name="Partial"><description>partial</description></class>' > "${'$'}output/Partial.xml"
                    exit 7
                  fi
                  echo '<class name="Shared" inherits="RawBase"><brief_description>raw</brief_description><description>raw</description></class>' > "${'$'}output/Shared.xml"
                  echo '<class name="RawOnly"><description>raw only</description></class>' > "${'$'}output/RawOnly.xml"
                  exit 0
                fi
                if [ "${'$'}mode" = "wait-for-singleton" ]; then
                  touch "${singletonStartedFile.toAbsolutePath()}"
                  while [ ! -f "${singletonReleaseFile.toAbsolutePath()}" ]; do sleep 0.01; done
                fi
                echo 'SINGLETON|Engine|Object|0'
            """.trimIndent(),
        )
        assertTrue("The fake Godot executable must be executable.", executable.toFile().setExecutable(true))
        return executable
    }

    private fun zlib(bytes: ByteArray): ByteArray = ByteArrayOutputStream().also { output ->
        DeflaterOutputStream(output).use { it.write(bytes) }
    }.toByteArray()

    private fun removeProjectDocs() {
        val root = GdSdkPathManager.getProjectExtensionsRoot(project) ?: return
        deleteRecursively(root)
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(root)
    }

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) return
        Files.walk(path).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach(Files::deleteIfExists) }
    }

    private companion object {
        val TIMEOUT_MILLIS = 60_000L.milliseconds
        const val LIBRARY_FILE_NAME = "libextension.so"
    }
}
