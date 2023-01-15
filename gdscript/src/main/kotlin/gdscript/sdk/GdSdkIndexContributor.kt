package gdscript.sdk

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.IndexableSetContributor
import gdscript.settings.GdSettingsState
import java.io.File

object GdSdkIndexContributor : IndexableSetContributor() {

    val roots = mutableSetOf<VirtualFile>();

    override fun getAdditionalProjectRootsToIndex(project: Project): MutableSet<VirtualFile> {
        val path = GdSettingsState.getInstance().state.sdkPath;
        if (!path.isNullOrBlank()) {
            val virtualFile = VfsUtil.findFileByIoFile(File(path), true);
            roots.add(virtualFile!!);
        }

        return roots;
    }

    override fun getAdditionalRootsToIndex(): MutableSet<VirtualFile> {
        return mutableSetOf();
    }

}
