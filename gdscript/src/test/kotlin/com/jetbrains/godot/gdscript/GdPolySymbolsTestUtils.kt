package com.jetbrains.godot.gdscript

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.registerServiceInstance
import gdscript.library.GdSdkFilesProvider
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

object GdPolySymbolsTestUtils {
    /**
     * Register a custom GdSdkFilesProvider that returns files from myFixture
     */
    fun registerSdk(project: Project, directories: List<Path>) {
        val paths = getAllFiles(directories)
        val lfs = LocalFileSystem.getInstance()
        val files: Collection<VirtualFile> = paths.mapNotNull { lfs.refreshAndFindFileByNioFile(it) }
        project.registerServiceInstance(GdSdkFilesProvider::class.java, object : GdSdkFilesProvider {
            override fun getAllSdkFiles(): Collection<VirtualFile> = files
            override fun getAllCoreSdkFiles(): Collection<VirtualFile> = files
        })
    }

    private fun getAllFiles(directories: List<Path>): List<Path> {
        val files = mutableListOf<Path>()

        directories.filter { it.isDirectory() }.forEach { dir ->
            Files.walk(dir).use { stream ->
                stream.filter { it.isRegularFile() && it.extension == "xml" }
                    .forEach { files.add(it) }
            }
        }

        return files
    }
}
