package com.jetbrains.godot.gdscript.library

import GdProjectService
import com.intellij.openapi.application.edtWriteAction
import com.intellij.openapi.application.readAction
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.common.waitUntil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdFixture
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.godot.community.GdProjectGodotService.GodotProjectInfo
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.library.GdSdkFilesProvider
import gdscript.library.GdSdkPathManager
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

/**
 * The SDK caches follow the engine version in `project.godot`.
 * The test sets no Godot executable, so the docs load generates nothing.
 * The versions 9.1 and 9.2 do not exist, so the test does not touch real core docs.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
@TestApplication
class GdSdkVersionChangeTest {
    private val gd: GdFixture by gdFixture("gdscript/project")

    private val oldVersion = Version(9, 1, 0)
    private val newVersion = Version(9, 2, 0)

    @BeforeEach
    fun createCoreDocs() {
        deleteCoreDocs()
        writeMarker(oldVersion, OLD_MARKER)
        writeMarker(newVersion, NEW_MARKER)
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(GdSdkPathManager.getCoreSdkDocsRoot())
    }

    @AfterEach
    fun deleteCoreDocs() {
        for (version in listOf(oldVersion, newVersion)) {
            deleteRecursively(GdSdkPathManager.getCoreSdkDir(version))
        }
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(GdSdkPathManager.getCoreSdkDocsRoot())
    }

    @Test
    fun testUnknownVersionThenKnown() = timeoutRunBlocking {
        val file = configureProject("[application]\nconfig/name=\"Test\"")
        awaitProjectInfo(GodotProjectInfo(null))

        assertTrue(readAction { GdSdkFilesProvider.getInstance(gd.project).getAllCoreSdkFiles() }.isEmpty())
        assertFalse(hasSdkClass(NEW_MARKER))

        rewrite(file, projectGodot(newVersion))
        awaitProjectInfo(GodotProjectInfo(newVersion))

        waitUntil("The SDK classes must contain $NEW_MARKER") { hasSdkClass(NEW_MARKER) }
    }

    @Test
    fun testOldVersionThenNew() = timeoutRunBlocking {
        val file = configureProject(projectGodot(oldVersion))
        awaitProjectInfo(GodotProjectInfo(oldVersion))
        waitUntil("The SDK classes must contain $OLD_MARKER") { hasSdkClass(OLD_MARKER) }

        rewrite(file, projectGodot(newVersion))
        awaitProjectInfo(GodotProjectInfo(newVersion))

        waitUntil("The SDK classes must contain $NEW_MARKER") { hasSdkClass(NEW_MARKER) }
        assertFalse(hasSdkClass(OLD_MARKER))
    }

    @Test
    fun testReplaceByRename() = timeoutRunBlocking {
        val file = configureProject(projectGodot(oldVersion))
        awaitProjectInfo(GodotProjectInfo(oldVersion))
        waitUntil("The SDK classes must contain $OLD_MARKER") { hasSdkClass(OLD_MARKER) }

        // The Godot editor can write a temporary file and then rename it to project.godot.
        val parent = file.parent
        edtWriteAction {
            val temporary = parent.createChildData(this, "project.godot.tmp")
            VfsUtil.saveText(temporary, projectGodot(newVersion))
            file.delete(this)
            temporary.rename(this, "project.godot")
        }
        awaitProjectInfo(GodotProjectInfo(newVersion))

        waitUntil("The SDK classes must contain $NEW_MARKER") { hasSdkClass(NEW_MARKER) }
        assertFalse(hasSdkClass(OLD_MARKER))
    }

    private fun configureProject(text: String): VirtualFile {
        val file = gd.fixture.addFileToProject("project.godot", text).virtualFile
        GdProjectService.getInstance(gd.project).discoverProject(file.parent)
        return file
    }

    /** Writes the file on the disk past the VFS, as the Godot editor does, and then refreshes the VFS. */
    private fun rewrite(file: VirtualFile, text: String) {
        @Suppress("UseOptimizedEelFunctions")
        Files.writeString(file.toNioPath(), text)
        VfsUtil.markDirtyAndRefresh(false, false, false, file)
    }

    private suspend fun awaitProjectInfo(expected: GodotProjectInfo) {
        GdProjectGodotService.getInstance(gd.project).projectInfoFlow.first { it == expected }
    }

    private suspend fun hasSdkClass(name: String): Boolean =
        readAction { GdPolySymbolQueriesUtil.getSdkClassSymbol(gd.project, name) != null }

    private fun projectGodot(version: Version): String =
        "[application]\nconfig/name=\"Test\"\nconfig/features=PackedStringArray(\"${version.major}.${version.minor}\", \"Forward Plus\")"

    private fun writeMarker(version: Version, className: String) {
        val dir = GdSdkPathManager.getCoreSdkDir(version)
        Files.createDirectories(dir)
        @Suppress("UseOptimizedEelFunctions")
        Files.writeString(dir.resolve("$className.xml"), "<?xml version=\"1.0\"?><class name=\"$className\"></class>")
    }

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) return
        Files.walk(path).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach(Files::deleteIfExists) }
    }

    private companion object {
        const val OLD_MARKER = "OnlyInOldVersion"
        const val NEW_MARKER = "OnlyInNewVersion"
    }
}
