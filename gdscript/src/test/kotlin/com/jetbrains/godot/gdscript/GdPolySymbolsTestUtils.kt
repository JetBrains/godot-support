package com.jetbrains.godot.gdscript

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.replaceService
import gdscript.library.GdSdkDocsTracker
import gdscript.library.GdSdkFilesProvider
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

object GdPolySymbolsTestUtils {
    /**
     * Replaces [GdSdkFilesProvider] with a provider that returns the XML files under [directories].
     * The original provider comes back when [parentDisposable] is disposed.
     *
     * The SDK caches live on the project and depend only on [GdSdkDocsTracker]. A light project is shared
     * between test classes, so each provider swap must invalidate these caches.
     */
    fun registerSdk(project: Project, directories: List<Path>, parentDisposable: Disposable) {
        val paths = getAllFiles(directories)
        val lfs = LocalFileSystem.getInstance()
        val files: Collection<VirtualFile> = paths.mapNotNull { lfs.refreshAndFindFileByNioFile(it) }
        val tracker = GdSdkDocsTracker.getInstance(project)
        // Disposer runs children in reverse order, so this runs after replaceService restores the original provider.
        Disposer.register(parentDisposable) {
            if (!project.isDisposed) tracker.docsChanged()
        }
        project.replaceService(GdSdkFilesProvider::class.java, object : GdSdkFilesProvider {
            override fun getAllSdkFiles(): Collection<VirtualFile> = files
            override fun getAllCoreSdkFiles(): Collection<VirtualFile> = files
        }, parentDisposable)
        tracker.docsChanged()
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
