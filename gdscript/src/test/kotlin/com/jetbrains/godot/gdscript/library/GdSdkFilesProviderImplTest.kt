package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.library.GdSdkFilesProviderImpl
import gdscript.library.GdSdkPathManager
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path

@RunWith(JUnit4::class)
class GdSdkFilesProviderImplTest : BasePlatformTestCase() {

    @Test
    fun testDocClassesWinOverPublishedExtensionDocumentation() {
        cleanProjectDocs()
        val docClasses = GdSdkPathManager.getProjectSingletonsDocDir(project)!!
        val published = GdSdkPathManager.getProjectExtensionsDir(project)!!
        Files.createDirectories(docClasses)
        Files.createDirectories(published)
        Files.writeString(docClasses.resolve("Shared.xml"), classXml("doc_classes"))
        Files.writeString(published.resolve("Shared.xml"), classXml("published"))
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(GdSdkPathManager.getProjectExtensionsRoot(project)!!)

        val shared = GdSdkFilesProviderImpl(project).getAllSdkFiles().single { it.name == "Shared.xml" }

        assertTrue(String(shared.contentsToByteArray()).contains("doc_classes"))
        assertFalse(String(shared.contentsToByteArray()).contains("published"))
    }

    /** The generated docs directory outlives a single light-fixture test method, so each test starts from a clean slate. */
    private fun cleanProjectDocs() {
        val root = GdSdkPathManager.getProjectExtensionsRoot(project) ?: return
        deleteRecursively(root)
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(root)
    }

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) return
        Files.walk(path).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach(Files::deleteIfExists) }
    }

    private fun classXml(source: String): String =
        "<?xml version=\"1.0\"?><class name=\"Shared\"><brief_description>$source</brief_description></class>"
}
