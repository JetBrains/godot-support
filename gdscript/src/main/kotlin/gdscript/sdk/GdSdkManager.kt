package gdscript.sdk

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.VfsImplUtil
import java.io.File

@Deprecated("lib")
object GdSdkManager {

    fun setupSdkIfNeeded() {
        val projectSdks = ProjectJdkTable.getInstance().allJdks;
        if (projectSdks.any { it.sdkType is GdSdkType }) return;

        val newSdkName = SdkConfigurationUtil.createUniqueSdkName(GdSdkType, "", projectSdks.toList());
        val newJdk = ProjectJdkImpl(newSdkName, GdSdkType);
        GdSdkType.setupSdkPaths(newJdk);

        ApplicationManager.getApplication().invokeAndWait {
            runWriteAction {
                if (ProjectJdkTable.getInstance().allJdks.any { it.sdkType is GdSdkType }) return@runWriteAction;
                ProjectJdkTable.getInstance().addJdk(newJdk);
            }
        }
    }

    fun setClassPath(path: String?, project: Project) {
        val projectSdks = ProjectJdkTable.getInstance().allJdks;
        val sdk = projectSdks.find { it.sdkType is GdSdkType }?.sdkModificator ?: return;

        if (sdk !is ProjectJdkImpl) return;
        sdk.removeAllRoots();
        if (path != null && path.isNotBlank()) {
            val vfPath = VfsUtil.findFileByIoFile(File(path), true);
            sdk.addRoot(vfPath!!, OrderRootType.SOURCES);
//            val pathUrl = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, path);
//            sdk.addRoot(pathUrl, OrderRootType.SOURCES);
            val module = ModuleManager.getInstance(project).modules.first()
            val rootModel = ModuleRootManager.getInstance(module).modifiableModel
//            rootModel.addLibraryEntry(newLib)
            rootModel.commit()
        }
        sdk.commitChanges();
    }

}
