package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableModelsProvider
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.impl.libraries.LibraryEx.ModifiableModelEx
import com.intellij.openapi.roots.impl.libraries.LibraryImpl
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.workspaceModel.ide.impl.legacyBridge.library.LibraryBridgeImpl
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

    fun registerSdk(path: String): Library {
        val name = path.replace('\\', '/').substringAfterLast("/")
        val type = OrderRootType.SOURCES
        val app = ApplicationManager.getApplication()
        val modifier = LibraryTablesRegistrar.getInstance().libraryTable.modifiableModel
        val library = modifier.createLibrary(name, GdLibraryKind)
        val libModifier = library.modifiableModel as ModifiableModelEx
        val props = libModifier.properties as GdLibraryProperties
        props.path = path
        props.version = name.sdkToVersion()
        libModifier.properties = props
        libModifier.addRoot(path, type)

        app.invokeAndWait {
            runWriteAction {
                libModifier.commit()
                modifier.commit()
            }
        }

        return library
    }

    fun setUpLibrary(project: Project, path: String) {
//        val table = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
//        val virtualFile = VfsUtil.findFileByIoFile(File(path), true)
//        val name = path.substringAfterLast("/")
//        val type = OrderRootType.SOURCES
//
//        ApplicationManager.getApplication().invokeAndWait {
//            runWriteAction {
//                val state = GdApplicationSettingsState.getInstance().state
//                val lib = table.getLibraryByName(name)
//                if (lib != null) {
//                    table.removeLibrary(lib)
//                    lib.rootProvider.getUrls(type).forEach {
//                        state.sdkPaths.remove(it)
//                    }
//                }
//                    val newLib = table.createLibrary(name)
//                    val libModel = newLib.modifiableModel
//                    libModel.addRoot(virtualFile!!.url, type)
//                    libModel.commit()
//                    state.sdkPaths.add(path)
//
//                    val module = ModuleManager.getInstance(project).modules.first()
//                    val rootModel = ModuleRootManager.getInstance(module).modifiableModel
//                    rootModel.addLibraryEntry(newLib)
//                    rootModel.commit()
//            }
//        }
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
