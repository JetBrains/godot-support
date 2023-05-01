package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.VfsUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import javax.swing.JComboBox


object GdLibraryManager {

    val LIBRARY_NAME = "GdScript_SDK"

    @OptIn(DelicateCoroutinesApi::class)
    fun listRemoteSdks(cb: JComboBox<String>) {
        GlobalScope.launch {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://gitlab.com/api/v4/projects/28295487/repository/tree?path=sdk"))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            // https://github.com/Kotlin/kotlinx.serialization/issues/993
            @Suppress("PROVIDED_RUNTIME_TOO_LOW")
            @Serializable
            data class GdSdk(val id: String, val name: String, val type: String, val path: String, val mode: String)

            Json.decodeFromString<Array<GdSdk>>(response.body())
                .map { it.name.removePrefix("GdSdk ").removeSuffix(".7z") }
                .sortedDescending()
                .forEach { cb.addItem(it) }
        }
    }

    fun setUpLibrary(project: Project, path: String?) {
        val table = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
        val virtualFile = VfsUtil.findFileByIoFile(File(path!!), true);

        ApplicationManager.getApplication().invokeAndWait {
            runWriteAction {
                val lib = table.getLibraryByName(LIBRARY_NAME);
                if (lib != null) {
                    table.removeLibrary(lib);
                }

                val newLib = table.createLibrary(LIBRARY_NAME);
                val libModel = newLib.modifiableModel;
                libModel.addRoot(virtualFile!!.url, OrderRootType.SOURCES);
                libModel.commit();

                val module = ModuleManager.getInstance(project).modules.first()
                val rootModel = ModuleRootManager.getInstance(module).modifiableModel
                rootModel.addLibraryEntry(newLib)
                rootModel.commit()
            }
        }
    }

}
