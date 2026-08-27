package com.jetbrains.godot.gdscript.listener

import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.listener.GdExtensionsChangeDetector
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdExtensionsChangeDetectorTest : BasePlatformTestCase() {

    @Test
    fun testExtensionDeclarationsAreDetected() {
        assertTrue(GdExtensionsChangeDetector.isExtensionDefinitionPath("/project/addons/fusion/fusion.gdextension"))
        assertTrue(GdExtensionsChangeDetector.isExtensionDefinitionPath("/project/fusion.GDExtension"))
        // The engine rewrites this one whenever the set of loaded extensions changes.
        assertTrue(GdExtensionsChangeDetector.isExtensionDefinitionPath("/project/.godot/extension_list.cfg"))
    }

    @Test
    fun testUnrelatedFilesAreIgnored() {
        assertFalse(GdExtensionsChangeDetector.isExtensionDefinitionPath("/project/player.gd"))
        assertFalse(GdExtensionsChangeDetector.isExtensionDefinitionPath("/project/project.godot"))
        // The generated docs must not trigger a regeneration of themselves.
        assertFalse(GdExtensionsChangeDetector.isExtensionDefinitionPath("/project/.idea/doctool/gdextensions/Fusion.xml"))
    }

    /**
     * Adding or removing an addon is reported as a single directory event, so those are followed as well: only the
     * directories that cannot hold an extension the engine would load are skipped.
     */
    @Test
    fun testChurningDirectoriesAreSkipped() {
        assertTrue(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/.godot/imported/icon.svg-abc.ctex"))
        assertTrue(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/.git/objects/ab"))
        assertTrue(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/.idea/doctool/gdextensions"))

        assertFalse(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/addons/fusion"))
        assertFalse(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/scenes"))
        // The godot-cpp based extensions - i.e. most of them - keep their declaration in a build output directory.
        assertFalse(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/bin"))
        assertFalse(GdExtensionsChangeDetector.isInSkippedDirectory(BASE_PATH, "/project/addons/fusion/bin"))
        // The Godot project directory itself is never skipped, whatever it is called.
        assertFalse(GdExtensionsChangeDetector.isInSkippedDirectory("/home/.idea-projects/project", "/home/.idea-projects/project/bin"))
    }

    /**
     * The one case that matters in practice: an addon is installed by copying a folder in and removed by deleting it,
     * and the VFS reports that as a single event for the folder - the declarations inside it are never mentioned.
     */
    @Test
    fun testFolderEventsInsideTheProjectAreDetected() {
        val addons = myFixture.tempDirFixture.findOrCreateDir("addons/fusion")
        val basePath = addons.parent.parent.path

        assertTrue(GdExtensionsChangeDetector.mayChangeExtensions(basePath, VFileDeleteEvent(this, addons)))

        val cache = myFixture.tempDirFixture.findOrCreateDir(".godot/imported")
        assertFalse(GdExtensionsChangeDetector.mayChangeExtensions(basePath, VFileDeleteEvent(this, cache)))
    }

    @Test
    fun testEventsOutsideTheProjectAreIgnored() {
        val outside = myFixture.tempDirFixture.findOrCreateDir("outside")
        val basePath = myFixture.tempDirFixture.findOrCreateDir("project").path

        assertFalse(GdExtensionsChangeDetector.mayChangeExtensions(basePath, VFileDeleteEvent(this, outside)))
    }

    private companion object {
        private const val BASE_PATH = "/project"
    }
}
