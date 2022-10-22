package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*
import gdscript.psi.GdElementFactory.identifier
import gdscript.psi.impl.GdClassNamingElementType

object PsiGdClassUtil {

    fun listClassNaming(project: Project): List<GdClassNaming> {
        return mapFiles(PsiGdFileUtil.gdFiles(project));
    }

    fun getClassName(element: PsiElement): String? {
        return PsiTreeUtil
            .getChildOfType(element.containingFile, GdClassNaming::class.java)
            ?.classname;
    }

    fun setName(element: GdClassNameNm?, newName: String?): PsiElement? {
        if (element == null) return null;

        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getName(element: GdClassNameNm?): String {
        if (element == null) return "";

        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

    fun getNameIdentifier(element: GdClassNameNm): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return keyNode?.psi
    }

    fun getInheritanceName(element: GdInheritance?): String {
        if (element == null) return ""

        val valueNode = element.node.findChildByType(GdTypes.INHERITANCE_ID_NMI)
        return valueNode?.text?.trim('"') ?: ""
    }

    private fun mapFiles(files: Collection<GdFile>): List<GdClassNaming> {
        return files.map {
            val stub = it.getGreenStub();
            if (stub !== null) {
                val naming = stub.findChildStubByType(GdClassNamingElementType)
                naming?.psi
            } else {
                PsiTreeUtil.findChildOfType(it, GdClassNaming::class.java)
            }
        }.filterNotNull();
    }

}
