package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFileManager

object GdLibraryManager {

    val LIBRARY_NAME = "GdScript_SDK"

    fun setUpLibrary(path: String?) {
        val project = ProjectManager.getInstance().defaultProject;
        val table = LibraryTablesRegistrar.getInstance().getLibraryTable(project);

        var lib = table.getLibraryByName(LIBRARY_NAME);
        if (lib != null) {
            // Haven't found out how to clear old roots
            table.removeLibrary(lib);
        }
        lib = table.createLibrary(LIBRARY_NAME);

        if (!path.isNullOrBlank()) {
            val pathUrl = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, path);
            ApplicationManager.getApplication().invokeAndWait {
                runWriteAction {
                    val libModel = lib.modifiableModel;
                    libModel.addRoot(pathUrl, OrderRootType.SOURCES);
                    libModel.commit();
                }
            }
        }
    }

}
