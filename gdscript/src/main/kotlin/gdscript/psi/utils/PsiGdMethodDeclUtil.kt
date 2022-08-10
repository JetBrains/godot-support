package gdscript.psi.utils

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

    fun isStatic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub;
        if (stub !== null) {
            return stub.isStatic();
        }

        return element.firstChild.text.equals("static");
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

    fun isConstructor(element: GdMethodDeclTl): Boolean = element.name == "_init";

}
