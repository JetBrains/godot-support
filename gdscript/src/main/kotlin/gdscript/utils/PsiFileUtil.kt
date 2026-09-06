package gdscript.utils

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.utils.VirtualFileUtil.resourcePath
import kotlin.io.path.name

object PsiFileUtil {

    fun PsiFile.isInSdk(): Boolean {
        if (this.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY) != null)
            return GdPolySymbolQueriesUtil.getSdkClassSymbol(project, name) != null

        return ProjectFileIndex.getInstance(this.project).isInLibrary(this.virtualFile) // TODO delete after deleting the whole PSI implementation
    }

    /**
     *  In case of relative paths ("debug/frames.gd") resource is not indexed -> thus convert relative path to absolute "res://root/debug/frames.gg"
     */
    fun String.toAbsoluteResource(element: PsiElement, project: Project): String {
        if (this.startsWith("res://") || this.startsWith("\"res://")) return this

        val thisPath = this.trim('"').toNioPathOrNull()?.normalize() ?: return this
        val dirPath = element.containingFile.originalFile.virtualFile?.parent ?: return this

        FilenameIndex.getVirtualFilesByName(thisPath.name, GlobalSearchScope.allScope(project)).find {
            try {
                // todo: use VfsUtilCore#getRelativePath
                val relative = dirPath.toNioPath().relativize(it.toNioPath())
                return@find relative == thisPath
            }
            catch (e: Exception) {
                thisLogger().trace(e)
            }
            false
        }?.let {
            return it.resourcePath()
        }

        return this
    }

}
