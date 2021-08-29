package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*

object PsiGdMethodDeclUtil {

    fun collectParentsMethods(file: PsiFile): MutableMap<String, GdMethodDeclTl> {
        val methods: MutableMap<String, GdMethodDeclTl> = mutableMapOf();
        var parentName: String? = PsiTreeUtil.getChildOfType(file, GdInheritance::class.java)?.inheritanceName;

        while (parentName !== null) {
            val parent = GdClassNamingIndex.get(parentName, file.project, GlobalSearchScope.allScope(file.project))
                .firstOrNull();
            if (parent === null) {
                break;
            }

            PsiTreeUtil.findChildrenOfType(parent.containingFile, GdMethodDeclTl::class.java).forEach {
                val name = it.name.orEmpty();
                if (!methods.containsKey(name)) {
                    methods[name] = it;
                }
            }

            parentName = parent.parentName;
        }

        return methods;
    }

    fun getMethodName(element: GdMethodDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.methodIdNmi?.name;
    }

    fun getReturnType(element: GdMethodDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return element.returnHint?.returnHintVal?.text ?: "";
    }

    fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }

        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

    fun setName(element: GdMethodIdNmi, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!);
            element.node.replaceChild(keyNode, id.node);
        }

        return element;
    }

    fun getName(element: GdMethodIdNmi): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return valueNode?.text ?: "";
    }

    fun getNameIdentifier(element: GdMethodIdNmi): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return keyNode?.psi;
    }

    fun isConstructor(element: GdMethodDeclTl): Boolean = element.name == "_init";

}
