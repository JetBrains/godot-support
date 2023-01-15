package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

object GdLibraryManager {

    val LIBRARY_NAME = "GdScript_SDK"

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


//        val project = ProjectManager.getInstance().defaultProject;
//        val table = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
//
//        val lib = table.getLibraryByName(LIBRARY_NAME);
//        if (lib != null) {
//            // Haven't found out how to clear old roots
//            table.removeLibrary(lib);
//        }
//
//        if (!path.isNullOrBlank()) {
//            ApplicationManager.getApplication().invokeAndWait {
//                runWriteAction {
//                    val newLib = table.createLibrary("fg6hgdf54gffh54");
//                    val libModel = newLib.modifiableModel;
//                    val vfPath = VfsUtil.findFileByIoFile(File(path), true);
//                    libModel.addRoot(vfPath!!, OrderRootType.SOURCES);
//                    libModel.commit();
//                    val modules = ModuleManager.getInstance(project)
//                    val md = modules.getModifiableModel()
//                    val module = md.newModule(path, "dfbgcvb")
//                    md.commit()
////                    val module = ModuleManager.getInstance(project).modules.first();
//                    val rootModel = ModuleRootManager.getInstance(module).modifiableModel;
//                    rootModel.addLibraryEntry(newLib);
//                    rootModel.commit();
//                }
//            }
//        }
    }

}
