package com.jetbrains.godot.gdscript.listener

import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.listener.GdExtensionsChangeDetector
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/GDExtensions"])
@TestApplication
class GdExtensionsChangeDetectorTest {

    private val gd: GdFixture by gdLightFixture("gdscript")

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
        val addons = gd.fixture.tempDirFixture.findOrCreateDir("addons/fusion")
        val basePath = addons.parent.parent.path

        assertTrue(GdExtensionsChangeDetector.mayChangeExtensions(basePath, VFileDeleteEvent(this, addons)))

        val cache = gd.fixture.tempDirFixture.findOrCreateDir(".godot/imported")
        assertFalse(GdExtensionsChangeDetector.mayChangeExtensions(basePath, VFileDeleteEvent(this, cache)))
    }

    @Test
    fun testEventsOutsideTheProjectAreIgnored() {
        val outside = gd.fixture.tempDirFixture.findOrCreateDir("outside")
        val basePath = gd.fixture.tempDirFixture.findOrCreateDir("project").path

        assertFalse(GdExtensionsChangeDetector.mayChangeExtensions(basePath, VFileDeleteEvent(this, outside)))
    }

    private companion object {
        private const val BASE_PATH = "/project"
    }
}
