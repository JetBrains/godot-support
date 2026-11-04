package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.embeddedDocs.GdExtensionDocExtractor
import gdscript.embeddedDocs.GdExtensionManifestParser
import gdscript.library.GdSdkPathManager
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/GDExtensions"])
@TestApplication
class GdExtensionDocExtractorTest {

    private val gd: GdFixture by gdLightFixture("gdscript/embeddedDocs")

    @TempDir
    lateinit var manifestRootFolder: Path

    @Test
    fun testExtractsBlobDocumentation() = runBlocking {
        val root = manifestRootFolder
        val manifest = root.resolve("extension.gdextension")
        val binary = root.resolve("libextension.so")
        Files.writeString(manifest, manifestText)
        Files.write(binary, ByteArray(512) { 0x55 } + zlib(classXml("BlobClass", "from blob").toByteArray()))

        val result = GdExtensionDocExtractor.extractBlobDocs(manifest, gd.project, features, projectBasePath = root)

        assertEquals(GdExtensionDocExtractor.Result.Extracted, result)
        val directory = blobDocsDir(manifest)
        assertEquals(classXml("BlobClass", "from blob"), Files.readString(directory.resolve("BlobClass.xml")))

        val secondManifest = root.resolve("second.gdextension")
        val secondBinary = root.resolve("second.so")
        Files.writeString(secondManifest, manifestText.replace("libextension.so", "second.so"))
        Files.write(secondBinary, ByteArray(512) { 0x55 } + zlib(classXml("SecondClass", "second blob").toByteArray()))
        assertEquals(
            GdExtensionDocExtractor.Result.Extracted,
            GdExtensionDocExtractor.extractBlobDocs(secondManifest, gd.project, features, projectBasePath = root),
        )
        val secondDirectory = blobDocsDir(secondManifest)
        assertFalse(directory == secondDirectory)
        assertEquals(classXml("SecondClass", "second blob"), Files.readString(secondDirectory.resolve("SecondClass.xml")))

        val emptyManifest = root.resolve("empty.gdextension")
        val emptyBinary = root.resolve("empty.so")
        Files.writeString(emptyManifest, manifestText.replace("libextension.so", "empty.so"))
        Files.write(emptyBinary, ByteArray(512) { 0x55 })
        val emptyDirectory = blobDocsDir(emptyManifest)
        deleteRecursively(emptyDirectory)

        val emptyResult = GdExtensionDocExtractor.extractBlobDocs(emptyManifest, gd.project, features, projectBasePath = root)

        assertEquals(GdExtensionDocExtractor.Result.NotFound, emptyResult)
        assertFalse(Files.exists(emptyDirectory))
    }

    private fun blobDocsDir(manifest: Path): Path {
        val manifestId = GdExtensionDocExtractor.manifestId(manifest, manifestRootFolder)
        return GdSdkPathManager.getProjectExtensionBlobDocsDir(gd.project, manifestId)!!
    }

    private fun classXml(name: String, description: String): String =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<class name=\"$name\"><brief_description>$description</brief_description></class>"

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) return
        Files.walk(path).use { paths -> paths.sorted(Comparator.reverseOrder()).forEach(Files::deleteIfExists) }
    }

    private fun zlib(bytes: ByteArray): ByteArray = ByteArrayOutputStream().also { output ->
        DeflaterOutputStream(output).use { it.write(bytes) }
    }.toByteArray()

    private companion object {
        val features = GdExtensionManifestParser.ActiveFeatureTags("linux", "x86_64", debug = true)
        const val manifestText = """
            [libraries]
            linux.debug.x86_64 = "res://libextension.so"
        """
    }
}
