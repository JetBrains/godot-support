package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.*
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.impl.libraries.LibraryEx.ModifiableModelEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import gdscript.model.GdHistory
import gdscript.model.GdSdk
import gdscript.utils.GdSdkUtil.COMMIT_HISTORY_PLACEHOLDER
import gdscript.utils.GdSdkUtil.COMMIT_HISTORY_URL
import gdscript.utils.GdSdkUtil.SDKs_URL
import gdscript.utils.GdSdkUtil.sdkToVersion
import gdscript.utils.GdSdkUtil.versionToSdkName
import gdscript.utils.GdSdkUtil.versionToSdkUrl
import gdscript.utils.GdSdkUtil.versionToSdkZip
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipFile

object GdLibraryManager {

    val LIBRARY_NAME = "GdSdk"
    val jsonParser = Json { ignoreUnknownKeys = true }

    fun listAvailableSdks(): List<String> {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(SDKs_URL))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return jsonParser.decodeFromString<Array<GdSdk>>(response.body())
            .map { it.name.sdkToVersion() }
            .sortedDescending()
    }

    fun listRegisteredSdks(project: Project): Iterable<Library> {
        return LibraryTablesRegistrar
            .getInstance()
            .getLibraryTable(project)
            .modifiableModel
            .libraries
            .filter {
                (it as LibraryEx).kind is GdLibraryKind
            }
    }

    fun libDate(version: String): String {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(COMMIT_HISTORY_URL.replace(COMMIT_HISTORY_PLACEHOLDER, version)))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return jsonParser.decodeFromString<Array<GdHistory>>(response.body())
            .firstOrNull()?.commit?.committed_date
            ?: ""
    }

    fun registerSdk(path: String, project: Project) {
        val name = path.replace('\\', '/').substringAfterLast("/")
        val modifier = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel

        if (modifier.getLibraryByName(name) != null) return
        val library = modifier.createLibrary(name, GdLibraryKind)
        val libModifier = library.modifiableModel as ModifiableModelEx

        libModifier.addRoot("file://$path", OrderRootType.SOURCES)

        ApplicationManager.getApplication().invokeAndWait {
            runWriteAction {
                libModifier.commit()
                modifier.commit()

                val module = ModuleManager.getInstance(project).modules.first()
                ModuleRootModificationUtil.addDependency(module, library)
            }
        }
    }

    fun clearSdks(project: Project) {
        ApplicationManager.getApplication().invokeAndWait {
            runWriteAction {
                val modifier = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel
                val tableModel = ApplicationManager
                    .getApplication()
                    .getService(ModifiableModelsProvider::class.java)
                    .getLibraryTableModifiableModel(project)

                modifier.getLibraryByName(LIBRARY_NAME)?.let { modifier.removeLibrary(it) }
                listRegisteredSdks(project).forEach {
                    it.rootProvider.getFiles(OrderRootType.SOURCES).forEach { dir ->
                        try {
                            FileUtils.deleteDirectory(File(dir.path))
                        } catch (e: Error) {
                        }
                    }
                    modifier.removeLibrary(it)
                    tableModel.removeLibrary(it)
                }
                try {
                    modifier.commit()
                } catch (e: Error) {
                }
                try {
                    tableModel.commit()
                } catch (e: Error) {
                }

                val module = ModuleManager.getInstance(project!!).modules.first()
                val rootModel = ModuleRootManager.getInstance(module).modifiableModel
                rootModel.orderEntries.forEach {
                    if (it is LibraryOrderEntry && it.libraryName == LIBRARY_NAME) {
                    }
                    rootModel.removeOrderEntry(it)
                }
                rootModel.commit()
            }
        }
    }

    fun download(version: String, directory: String) {
        val zipName = "$directory/${version.versionToSdkZip()}"
        val zipPath = Paths.get(zipName)
        val sdkName = version.versionToSdkName()

        try {
            Files.createDirectory(Paths.get(directory, sdkName))
        } catch (e: Exception) {
        }
        val dir = Paths.get(directory, sdkName).toFile()

        URL(version.versionToSdkUrl())
            .openStream()
            .use { Files.copy(it, zipPath) }

        ZipFile(zipName).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    File(dir, entry.name).outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }

        Files.delete(zipPath)
    }

}
