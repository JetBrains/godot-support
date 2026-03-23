package gdscript.library

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
interface GdSdkFilesProvider {
    fun getAllSdkFiles(): Collection<VirtualFile>
    fun getAllCoreSdkFiles(): Collection<VirtualFile>

    companion object {
        fun getInstance(project: Project): GdSdkFilesProvider = project.getService(GdSdkFilesProvider::class.java)
    }
}
