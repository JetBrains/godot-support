package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.ModifiableModelsProvider
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.impl.libraries.LibraryEx.ModifiableModelEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import gdscript.utils.GdSdkUtil.SDKs_URL
import gdscript.utils.GdSdkUtil.sdkToVersion
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

    val LIBRARY_NAME = "GdSdk"

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
            class GdSdk(val id: String, val name: String, val type: String, val path: String, val mode: String)

            Json.decodeFromString<Array<GdSdk>>(response.body())
                .map { it.name.sdkToVersion() }
                .sortedDescending()
                .forEach { cb.addItem(it) }
        }
    }

    fun listRegisteredSdks(): Iterable<Library> {
        return ApplicationManager
            .getApplication()
            .getService(ModifiableModelsProvider::class.java)
            .libraryTableModifiableModel.libraries.filter {
                (it as LibraryEx).kind is GdLibraryKind
            }
    }

    fun getLibrary(path: String): Library? {
        return listRegisteredSdks().find {
            ((it as LibraryEx).modifiableModel.properties as GdLibraryProperties).path == path
        }
    }

    fun registerSdk(path: String): Library {
        val name = path.replace('\\', '/').substringAfterLast("/")
        val app = ApplicationManager.getApplication()
        val modifier = LibraryTablesRegistrar.getInstance().libraryTable.modifiableModel
        val library = modifier.createLibrary(name, GdLibraryKind)
        val libModifier = library.modifiableModel as ModifiableModelEx
        val props = libModifier.properties as GdLibraryProperties
        props.path = path
        props.version = name.sdkToVersion()
        libModifier.properties = props
        libModifier.addRoot("file://$path", OrderRootType.SOURCES)

        app.invokeAndWait {
            runWriteAction {
                libModifier.commit()
                modifier.commit()
            }
        }

        return library
    }

    fun setUpLibrary(project: Project, path: String) {
        if (path.isBlank()) return

        getLibrary(path) ?: return

        ApplicationManager.getApplication().invokeAndWait {
            runWriteAction {
                var modifier = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel
                modifier.getLibraryByName(LIBRARY_NAME)?.let { modifier.removeLibrary(it) }
                modifier.commit()

                modifier = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel
                val library = modifier.createLibrary(LIBRARY_NAME)
                val libraryModifier = library.modifiableModel
                libraryModifier.addRoot("file://$path", OrderRootType.SOURCES)
                libraryModifier.commit()
                modifier.commit()

                val module = ModuleManager.getInstance(project).modules.first()
                val rootModel = ModuleRootManager.getInstance(module).modifiableModel
                rootModel.orderEntries.forEach {
                    if (it is LibraryOrderEntry && it.libraryName == LIBRARY_NAME)
                        rootModel.removeOrderEntry(it)
                }
                rootModel.addLibraryEntry(library)
                rootModel.commit()
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
                    File(dir, entry.name).outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }

        Files.delete(zipPath)
    }

}
