package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.VfsUtil
import gdscript.utils.GdSdkUtil.SDKs_URL
import gdscript.utils.GdSdkUtil.sdkZipToVersion
import gdscript.utils.GdSdkUtil.versionToSdkName
import gdscript.utils.GdSdkUtil.versionToSdkUrl
import gdscript.utils.GdSdkUtil.versionToSdkZip
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipFile
import javax.swing.JComboBox


object GdLibraryManager {

    val LIBRARY_PREFIX = "GdScript_"

    @OptIn(DelicateCoroutinesApi::class)
    fun listRemoteSdks(cb: JComboBox<String>) {
        GlobalScope.launch {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create(SDKs_URL))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            // https://github.com/Kotlin/kotlinx.serialization/issues/993
            @Suppress("PROVIDED_RUNTIME_TOO_LOW")
            @Serializable
            data class GdSdk(val id: String, val name: String, val type: String, val path: String, val mode: String)

            Json.decodeFromString<Array<GdSdk>>(response.body())
                .map { it.name.sdkZipToVersion() }
                .sortedDescending()
                .forEach { cb.addItem(it) }
        }
    }

    fun setUpLibrary(project: Project, path: String?) {
        val table = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
        val virtualFile = VfsUtil.findFileByIoFile(File(path!!), true);

        ApplicationManager.getApplication().invokeAndWait {
            runWriteAction {
                val version = path.substringAfterLast("/")
                val name = "$LIBRARY_PREFIX$version"
                val lib = table.getLibraryByName(name)
                if (lib != null) {
                    table.removeLibrary(lib)
                }

//                val newLib = table.createLibrary(LIBRARY_NAME);
//                val libModel = newLib.modifiableModel;
//                libModel.addRoot(virtualFile!!.url, OrderRootType.SOURCES);
//                libModel.commit();
//
//                val module = ModuleManager.getInstance(project).modules.first()
//                val rootModel = ModuleRootManager.getInstance(module).modifiableModel
//                rootModel.addLibraryEntry(newLib)
//                rootModel.commit()
            }
        }
    }

    fun download(version: String, directory: String) {
        val zipName = "$directory/${version.versionToSdkZip()}"
        val zipPath = Paths.get(zipName)
        val sdkName = version.versionToSdkName()

        Files.createDirectory(Paths.get(directory, sdkName))
        val dir = Paths.get(directory, sdkName).toFile()

        URL(version.versionToSdkUrl())
            .openStream()
            .use { Files.copy(it, zipPath) }

        ZipFile(zipName).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    File(dir, entry.name.substringAfter("/")).outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }

        Files.delete(zipPath)
    }

}
