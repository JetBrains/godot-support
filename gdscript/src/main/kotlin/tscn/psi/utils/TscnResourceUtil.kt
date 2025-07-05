package tscn.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import gdscript.psi.utils.PsiGdResourceUtil
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnResourceHeader

/**
 * Resource line utils
 * [ext_resource type="Script" path="res://Asd.gd" id="1_3apro"]
 */
object TscnResourceUtil {

    fun getId(element: TscnResourceHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getId();

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_ID);
    }

    fun getPath(element: TscnResourceHeader): String {
        val stub = element.stub;
        if (stub != null) return stub.getPath();

        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_PATH);
    }

    fun getType(element: TscnResourceHeader): String {
        return TscnHeaderUtils.getValue(element.headerValueList, TscnHeaderUtils.HL_TYPE);
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
