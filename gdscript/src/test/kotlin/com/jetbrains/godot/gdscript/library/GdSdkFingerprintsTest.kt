package com.jetbrains.godot.gdscript.library

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.library.GdSdkFingerprints
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting
import kotlin.io.path.deleteRecursively
import kotlin.io.path.writeText

@RunWith(JUnit4::class)
class GdSdkFingerprintsTest : BasePlatformTestCase() {

    private lateinit var projectPath: Path
    private lateinit var docsDir: Path

    override fun setUp() {
        super.setUp()
        projectPath = Files.createTempDirectory("gd-project")
        projectPath.resolve("project.godot").writeText("config_version=5")

        docsDir = Files.createTempDirectory("gd-doctool")
        writeDoc("Node.xml")
        writeDoc("doc_classes/Fusion.xml")
    }

    @OptIn(ExperimentalPathApi::class)
    override fun tearDown() {
        try {
            projectPath.deleteRecursively()
            docsDir.deleteRecursively()
        } catch (e: Throwable) {
            addSuppressedException(e)
        } finally {
            super.tearDown()
        }
    }

    @Test
    fun testAddingAndRemovingAnExtensionChangesTheDeclarationsFingerprint() {
        val withoutExtensions = GdSdkFingerprints.ofExtensionDeclarations(projectPath)
        assertTrue(withoutExtensions.isEmpty())

        val declaration = projectPath.resolve("addons").resolve("fusion").also { it.createDirectories() }
            .resolve("fusion.gdextension")
        declaration.writeText("[configuration]\nentry_symbol = \"fusion_init\"\n")

        val withExtension = GdSdkFingerprints.ofExtensionDeclarations(projectPath)
        assertTrue(withExtension, withExtension.contains("fusion.gdextension"))
        // Nothing changed in between, so the fingerprint has to be stable.
        assertEquals(withExtension, GdSdkFingerprints.ofExtensionDeclarations(projectPath))

        declaration.deleteExisting()
        assertEquals(withoutExtensions, GdSdkFingerprints.ofExtensionDeclarations(projectPath))
    }

    @Test
    fun testEditingAnExtensionChangesTheDeclarationsFingerprint() {
        val declaration = projectPath.resolve("fusion.gdextension")
        declaration.writeText("[configuration]\n")
        val before = GdSdkFingerprints.ofExtensionDeclarations(projectPath)

        declaration.writeText("[configuration]\ncompatibility_minimum = \"4.2\"\n")
        assertFalse(before == GdSdkFingerprints.ofExtensionDeclarations(projectPath))
    }

    /** godot-cpp - i.e. most of the extensions out there - declares the extension inside its build output directory. */
    @Test
    fun testDeclarationInABuildOutputIsFound() {
        projectPath.resolve("bin").also { it.createDirectories() }
            .resolve("example.gdextension").writeText("[configuration]\n")

        val fingerprint = GdSdkFingerprints.ofExtensionDeclarations(projectPath)
        assertTrue(fingerprint, fingerprint.contains("example.gdextension"))
    }

    /** Cloning a repository or switching a branch rewrites the timestamps of files that did not change at all. */
    @Test
    fun testTouchingADeclarationKeepsTheDeclarationsFingerprint() {
        val declaration = projectPath.resolve("fusion.gdextension")
        declaration.writeText("[configuration]\n")
        val before = GdSdkFingerprints.ofExtensionDeclarations(projectPath)

        Files.setLastModifiedTime(declaration, FileTime.fromMillis(Files.getLastModifiedTime(declaration).toMillis() + 10_000))
        assertEquals(before, GdSdkFingerprints.ofExtensionDeclarations(projectPath))
    }

    /** The engine caches hold copies of the declarations, and they are rewritten all the time. */
    @Test
    fun testEngineCacheIsNotScanned() {
        projectPath.resolve(".godot").also { it.createDirectories() }
            .resolve("cached.gdextension").writeText("[configuration]\n")

        assertTrue(GdSdkFingerprints.ofExtensionDeclarations(projectPath).isEmpty())
    }

    @Test
    fun testGeneratedDocsFingerprintIsStableWhileNothingChanges() {
        assertEquals(GdSdkFingerprints.ofGeneratedDocs(docsDir), GdSdkFingerprints.ofGeneratedDocs(docsDir))
    }

    /** The case the input-only stamp missed: a single generated XML deleted by hand. */
    @Test
    fun testRemovingASingleDocChangesTheGeneratedDocsFingerprint() {
        val before = GdSdkFingerprints.ofGeneratedDocs(docsDir)

        docsDir.resolve("doc_classes/Fusion.xml").deleteExisting()
        assertFalse(before == GdSdkFingerprints.ofGeneratedDocs(docsDir))
    }

    @Test
    fun testEditingADocChangesTheGeneratedDocsFingerprint() {
        val before = GdSdkFingerprints.ofGeneratedDocs(docsDir)

        docsDir.resolve("Node.xml").writeText("<class name=\"Node\"><methods/></class>")
        assertFalse(before == GdSdkFingerprints.ofGeneratedDocs(docsDir))
    }

    @Test
    fun testAddingADocChangesTheGeneratedDocsFingerprint() {
        val before = GdSdkFingerprints.ofGeneratedDocs(docsDir)

        writeDoc("doc_classes/Extra.xml")
        assertFalse(before == GdSdkFingerprints.ofGeneratedDocs(docsDir))
    }

    /** A wiped output directory must not look like the one the docs were generated into. */
    @Test
    fun testMissingDirectoryDiffersFromAGeneratedOne() {
        val before = GdSdkFingerprints.ofGeneratedDocs(docsDir)
        val missing = GdSdkFingerprints.ofGeneratedDocs(docsDir.resolve("nowhere"))

        assertFalse(before == missing)
        // A missing and an empty directory hold the same - nothing - so they are described the same way.
        assertEquals(missing, GdSdkFingerprints.ofGeneratedDocs(docsDir.resolve("empty").also { it.createDirectories() }))
    }

    private fun writeDoc(relativePath: String) {
        val file = docsDir.resolve(relativePath)
        file.parent.createDirectories()
        file.writeText("<class name=\"${file.fileName}\"/>")
    }
}
