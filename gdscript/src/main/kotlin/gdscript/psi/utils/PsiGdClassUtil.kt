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

    @Deprecated("použij metodu getClass")
    fun getClassName(element: PsiElement): String? {
        val cl = PsiGdTreeUtil.findFirstPrecedingElement(element) {
            it is GdClassDeclTl
        } as GdClassDeclTl?;
        if (cl != null) return cl.classNameNmi?.name;

        return PsiTreeUtil
            .getChildOfType(element.containingFile, GdClassNaming::class.java)
            ?.classname;
    }

    fun setName(element: GdClassNameNmi?, newName: String?): PsiElement? {
        if (element == null) return null;

        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getName(element: GdClassNameNmi?): String {
        if (element == null) return "";

        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

    fun getNameIdentifier(element: GdClassNameNmi): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return keyNode?.psi
    }

    @Deprecated("use .text")
    fun getInheritanceName(element: GdInheritance?): String {
        if (element == null) return ""

        val valueNode = element.node.findChildByType(GdTypes.INHERITANCE_ID_NM)
        return valueNode?.text?.trim('"') ?: ""
    }

    fun isInner(element: GdClassNameNmi): Boolean {
        return element.parent is GdClassDeclTl;
    }

    @Deprecated("getParentClassElement")
    fun getParentContainer(element: PsiElement): PsiElement {
        if (element is GdClassDeclTl) {
            return element;
        }

        return PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassDeclTl::class.java) ?: element.containingFile;
    }

    /**
     * Returns one of: GdClassDecl, GdClassNaming, GdFile
     */
    fun getParentClassElement(element: PsiElement): PsiElement {
        val inner = PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassDeclTl::class.java);
        if (inner != null) return inner;

        return PsiTreeUtil.getStubChildOfType(element.containingFile, GdClassNaming::class.java)
            ?: element.containingFile;
    }

    fun getClass(element: PsiElement): String? {
        val container = getParentContainer(element);
        if (container is GdClassDeclTl) {
            return container.classNameNmi?.name;
        }

        return PsiTreeUtil.getStubChildOfType(element, GdClassNaming::class.java)?.classname;
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
