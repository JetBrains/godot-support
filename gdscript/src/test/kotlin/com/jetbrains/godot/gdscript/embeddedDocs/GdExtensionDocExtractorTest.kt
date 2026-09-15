package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.embeddedDocs.GdExtensionDocExtractor
import gdscript.embeddedDocs.GdExtensionManifestParser
import gdscript.library.GdSdkPathManager
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.DeflaterOutputStream

@RunWith(JUnit4::class)
class GdExtensionDocExtractorTest : BasePlatformTestCase() {

    @JvmField
    @Rule
    val manifestRootFolder = TemporaryFolder()

    @Test
    fun testExtractsBlobDocumentation() = runBlocking {
        val root = manifestRootFolder.root.toPath()
        val manifest = root.resolve("extension.gdextension")
        val binary = root.resolve("libextension.so")
        Files.writeString(manifest, manifestText)
        Files.write(binary, ByteArray(512) { 0x55 } + zlib(classXml("BlobClass", "from blob").toByteArray()))

        val result = GdExtensionDocExtractor.extractBlobDocs(manifest, project, features, projectBasePath = root)

        assertEquals(GdExtensionDocExtractor.Result.Extracted, result)
        val directory = blobDocsDir(manifest)
        assertEquals(classXml("BlobClass", "from blob"), Files.readString(directory.resolve("BlobClass.xml")))

        val secondManifest = root.resolve("second.gdextension")
        val secondBinary = root.resolve("second.so")
        Files.writeString(secondManifest, manifestText.replace("libextension.so", "second.so"))
        Files.write(secondBinary, ByteArray(512) { 0x55 } + zlib(classXml("SecondClass", "second blob").toByteArray()))
        assertEquals(
            GdExtensionDocExtractor.Result.Extracted,
            GdExtensionDocExtractor.extractBlobDocs(secondManifest, project, features, projectBasePath = root),
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

        val emptyResult = GdExtensionDocExtractor.extractBlobDocs(emptyManifest, project, features, projectBasePath = root)

        assertEquals(GdExtensionDocExtractor.Result.NotFound, emptyResult)
        assertFalse(Files.exists(emptyDirectory))
    }

    private fun blobDocsDir(manifest: Path): Path {
        val manifestId = GdExtensionDocExtractor.manifestId(manifest, manifestRootFolder.root.toPath())
        return GdSdkPathManager.getProjectExtensionBlobDocsDir(project, manifestId)!!
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
