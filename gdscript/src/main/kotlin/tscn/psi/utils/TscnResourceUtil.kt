package tscn.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.utils.PsiGdResourceUtil
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnResourceHeader

/**
 * Resource line utils
 * [ext_resource type="Script" path="res://Asd.gd" id="1_3apro"]
 */
object TscnResourceUtil {

    fun getId(element: TscnResourceHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getId()

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_ID)
    }

    fun getUid(element: TscnResourceHeader): String {
        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_UID)
    }

    fun getPath(element: TscnResourceHeader): String {
        val stub = element.stub
        if (stub != null) return stub.getPath()

        val path = TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_PATH)
        if (path.isEmpty()) {
            val uid = getUid(element)
            if (uid.isNotEmpty()) {
                val file = GdFileResIndex.getFiles(uid, element.project).firstOrNull()
                if (file != null) return PsiGdResourceUtil.resourcePath(file)
            }
        }
        return path
    }

    fun getType(element: TscnResourceHeader): String {
        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_TYPE)
    }

    @Deprecated("list list below - script might be in multiple .tscn")
    fun findTscnByResource(element: PsiElement): TscnResourceHeader? {
        val resource = PsiGdResourceUtil.resourcePath(element.containingFile.originalFile.virtualFile)

        return TscnResourceIndex.INSTANCE.getGlobally(resource, element).firstOrNull()
    }

    fun findTscnByResources(element: PsiElement): Collection<TscnResourceHeader> {
        val resource = PsiGdResourceUtil.resourcePath(element.containingFile.originalFile.virtualFile)

        return findTscnByResources(resource, element.project)
    }

    fun findTscnByResources(resourcePath: String, project: Project): Collection<TscnResourceHeader> {
        return TscnResourceIndex.INSTANCE.getGlobally(resourcePath, project)
    }
}
