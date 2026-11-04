package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.library.GdSdkFilesProviderImpl
import gdscript.library.GdSdkPathManager
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
@TestApplication
class GdSdkFilesProviderImplTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/highlighting")

    @Test
    fun testDocClassesWinOverPublishedExtensionDocumentation() {
        cleanProjectDocs()
        val docClasses = GdSdkPathManager.getProjectSingletonsDocDir(gd.project)!!
        val published = GdSdkPathManager.getProjectExtensionsDir(gd.project)!!
        Files.createDirectories(docClasses)
        Files.createDirectories(published)
        Files.writeString(docClasses.resolve("Shared.xml"), classXml("doc_classes"))
        Files.writeString(published.resolve("Shared.xml"), classXml("published"))
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(GdSdkPathManager.getProjectExtensionsRoot(gd.project)!!)

        val shared = GdSdkFilesProviderImpl(gd.project).getAllSdkFiles().single { it.name == "Shared.xml" }

        assertTrue(String(shared.contentsToByteArray()).contains("doc_classes"))
        assertFalse(String(shared.contentsToByteArray()).contains("published"))
    }

    /** The generated docs directory outlives a single light-fixture test method, so each test starts from a clean slate. */
    private fun cleanProjectDocs() {
        val root = GdSdkPathManager.getProjectExtensionsRoot(gd.project) ?: return
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
