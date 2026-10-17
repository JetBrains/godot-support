package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Files
import java.nio.file.Path

@TestApplication
@Tag(TeamCityTags.Plugins.Godot.General)
class OpenInGodotActionTest {
    @TempDir
    lateinit var projectDir: Path

    private val action = OpenInGodotAction()
    private val baseDirectory: VirtualFile
        get() = requireNotNull(LocalFileSystem.getInstance().refreshAndFindFileByNioFile(projectDir))

    @ParameterizedTest
    @ValueSource(strings = ["sln", "slnx", "slnf", "csproj", "fsproj", "vcxproj", "uid", "import", "remap", "old"])
    fun `hides excluded extensions`(extension: String) {
        val file = createVirtualFile("assets/file.$extension")

        assertFalse(action.fileShouldBeShown(file, baseDirectory), file.path)
    }

    @ParameterizedTest
    @ValueSource(strings = ["iml", "ipr", "iws"])
    fun `hides IDE project and workspace files`(extension: String) {
        val file = createVirtualFile("nested/project.$extension")

        assertFalse(action.fileShouldBeShown(file, baseDirectory), file.path)
    }

    @Test
    fun `hides excluded file names`() {
        val file = createVirtualFile(".gitignore")

        assertFalse(action.fileShouldBeShown(file, baseDirectory), file.path)
    }

    @ParameterizedTest
    @ValueSource(strings = [".idea", "nested/.idea", ".godot", "nested/.godot"])
    fun `hides IDE and Godot data directories`(path: String) {
        val directory = createVirtualFile(path, isDirectory = true)

        assertFalse(action.fileShouldBeShown(directory, baseDirectory), directory.path)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            ".idea/workspace.xml",
            ".idea/nested/scene.tscn",
            "nested/.idea/scene.tscn",
            ".godot/scene.tscn",
            ".godot/editor/filesystem_cache10",
            "nested/.godot/editor/scene.tscn",
        ]
    )
    fun `hides files inside IDE and Godot data directories`(path: String) {
        val file = createVirtualFile(path)

        assertFalse(action.fileShouldBeShown(file, baseDirectory), file.path)
    }

    @ParameterizedTest
    @ValueSource(strings = [".idea/nested", "nested/.idea/nested", ".godot/editor", "nested/.godot/editor"])
    fun `hides directories inside IDE and Godot data directories`(path: String) {
        val directory = createVirtualFile(path, isDirectory = true)

        assertFalse(action.fileShouldBeShown(directory, baseDirectory), directory.path)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "scene.tscn",
            "resources/material.tres",
            "script.gd",
            "script.cs",
            "project.godot",
            "icon.png",
            "README",
            "scene.tscn.uid.backup",
            "gitignore.txt",
        ]
    )
    fun `shows other files`(path: String) {
        val file = createVirtualFile(path)

        assertTrue(action.fileShouldBeShown(file, baseDirectory), file.path)
    }

    @ParameterizedTest
    @ValueSource(strings = ["scenes", "scenes/nested", "assets.with.dots"])
    fun `shows other directories`(path: String) {
        val directory = createVirtualFile(path, isDirectory = true)

        assertTrue(action.fileShouldBeShown(directory, baseDirectory), directory.path)
    }

    @ParameterizedTest
    @ValueSource(strings = [".hidden", ".idea-backup", ".godot-backup", "assets/.hidden", ".hidden/nested"])
    fun `hides dot directories`(path: String) {
        val directory = createVirtualFile(path, isDirectory = true)

        assertFalse(action.fileShouldBeShown(directory, baseDirectory), directory.path)
    }

    @ParameterizedTest
    @ValueSource(strings = [".hidden/scene.tscn", "assets/.hidden/nested/scene.tscn", "assets/.hidden.gd"])
    fun `hides files with dot names or ancestors`(path: String) {
        val file = createVirtualFile(path)

        assertFalse(action.fileShouldBeShown(file, baseDirectory), file.path)
    }

    @ParameterizedTest
    @ValueSource(strings = [".hidden/project", ".hidden-project"])
    fun `ignores dot names at or above the project root`(path: String) {
        val root = createVirtualFile(path, isDirectory = true)
        val file = createVirtualFile("$path/scenes/scene.tscn")

        assertTrue(action.fileShouldBeShown(file, root), file.path)
        assertTrue(action.fileShouldBeShown(root, root), root.path)
    }

    private fun createVirtualFile(path: String, isDirectory: Boolean = false): VirtualFile {
        val filePath = projectDir.resolve(path)
        if (isDirectory) {
            Files.createDirectories(filePath)
        } else {
            Files.createDirectories(filePath.parent)
            Files.createFile(filePath)
        }
        return requireNotNull(LocalFileSystem.getInstance().refreshAndFindFileByNioFile(filePath))
    }
}