package gdscript.library

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

class GdSdkFilesProviderImpl(private val project: Project) : GdSdkFilesProvider {

    override fun getAllSdkFiles(): Collection<VirtualFile> {
        return GdDocClassesFoldersService.getInstance(project).currentFolders()
            .flatMap { walkXmlVirtualFiles(it) }
            .plus(getAllCoreSdkFiles())
    }

    override fun getAllCoreSdkFiles(): Collection<VirtualFile> {
        val version = GdSdkUtil.getGodotVersion(project)
        val sdkDir = GdSdkPathManager.getCoreSdkDir(version)
        val sdkDirVf = VirtualFileManager.getInstance().refreshAndFindFileByNioPath(sdkDir) ?: return emptyList()
        return walkXmlVirtualFiles(sdkDirVf)
    }

    private fun walkXmlVirtualFiles(folder: VirtualFile): List<VirtualFile> {
        if (!folder.isValid) return emptyList()
        val result = mutableListOf<VirtualFile>()
        VfsUtilCore.iterateChildrenRecursively(folder, null) { virtualFile ->
            if (!virtualFile.isDirectory && virtualFile.extension == "xml") {
                result.add(virtualFile)
            }
            true
        }
        return result
    }
}
