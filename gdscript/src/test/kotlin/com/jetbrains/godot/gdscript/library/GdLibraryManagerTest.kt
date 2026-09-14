package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.util.Version
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.library.GdLibraryManager
import gdscript.library.GdSdkFingerprints
import gdscript.library.GdSdkIntegrityValidator
import gdscript.library.GdSdkPathManager
import gdscript.polySymbols.scope.GdSdkSymbolsModificationTracker
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path

/**
 * Checks that a failed core step refreshes the existing directory.
 *
 * The refresh makes the directory visible and updates the project symbol tracker.
 * The test runs off the EDT because the refresh needs a free EDT.
 */
@RunWith(JUnit4::class)
class GdLibraryManagerTest : BasePlatformTestCase() {

    // A version that no real Godot build uses, so the test cannot disturb a real documentation set.
    private val version: Version = Version.parseVersion("99.8")!!

    private val coreSdkDir: Path get() = GdSdkPathManager.getCoreSdkDir(version)
    private val stampFile: Path get() = GdSdkPathManager.getCoreSdkStampFile(version)

    // The Godot project directory the test pretends the SDK was loaded for; unrelated to the IDE test project.
    private lateinit var projectBasePath: Path

    override fun runInDispatchThread(): Boolean = false

    override fun setUp() {
        super.setUp()
        projectBasePath = Files.createTempDirectory("gd-library-manager-project")
    }

    override fun tearDown() {
        try {
            Files.deleteIfExists(stampFile)
            if (Files.exists(coreSdkDir)) {
                Files.walk(coreSdkDir).use { paths ->
                    paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
                }
            }
            Files.walk(projectBasePath).use { paths ->
                paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
            }
        }
        finally {
            super.tearDown()
        }
    }

    @Test
    fun testAFailedCoreStepStillRefreshesTheExistingDirectory() = runBlocking {
        // A good set is already on disk from an earlier session.
        Files.createDirectories(coreSdkDir)
        Files.writeString(
            coreSdkDir.resolve("Node.xml"),
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\" inherits=\"Object\"></class>",
        )

        // A valid GDExtension stamp keeps step 2 of the SDK generation out of this test, so no process starts.
        val extensionsStampFile = GdSdkPathManager.getProjectExtensionsStampFile(project)
        val extensionsDir = GdSdkPathManager.getProjectExtensionsDir(project)
        assertNotNull("The project must name a GDExtension stamp file.", extensionsStampFile)
        assertNotNull("The project must name a GDExtension docs directory.", extensionsDir)
        GdSdkIntegrityValidator.writeStamp(extensionsStampFile!!, GdSdkFingerprints.ofExtensionDeclarations(projectBasePath), extensionsDir!!)

        // The executable is gone, so the core step reads no attributes and returns FAILED.
        val missingBinary = Files.createTempFile("gd-library-manager-missing", ".bin")
        Files.delete(missingBinary)
        assertNull(GdSdkIntegrityValidator.coreStamp(version, missingBinary))

        val tracker = GdSdkSymbolsModificationTracker.getInstance(project)
        val before = tracker.modificationCount

        withTimeout(TIMEOUT_MILLIS) { GdLibraryManager.generateSdkIfNeeded(version, project, missingBinary, projectBasePath) }

        assertFalse("The core step must write no stamp.", GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp()))
        assertTrue(
            "A core step that writes nothing must still refresh the existing directory, which bumps the tracker.",
            tracker.modificationCount > before,
        )
    }

    private fun stamp() = GdSdkIntegrityValidator.CoreStamp(
        formatVersion = 1,
        godotVersion = version.toString(),
        binarySize = 0,
        binaryModifiedMillis = 0,
    )

    private companion object {
        const val TIMEOUT_MILLIS = 60_000L
    }
}
