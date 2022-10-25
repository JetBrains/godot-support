package gdscript.psi.utils

import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*
import gdscript.utils.ElementTypeUtil

object PsiGdMethodDeclUtil {

    fun collectParentsMethods(file: PsiFile): MutableMap<String, GdMethodDeclTl> {
        val methods: MutableMap<String, GdMethodDeclTl> = mutableMapOf();
        var parentName: String? = PsiTreeUtil.getChildOfType(file, GdInheritance::class.java)?.inheritanceName; // TODO ii

        while (parentName !== null) {
            val parent = PsiGdInheritanceUtil.getPsiFile(parentName, file.project);
            if (parent === null) {
                break;
            }

            PsiTreeUtil.findChildrenOfType(parent, GdMethodDeclTl::class.java).forEach {
                val name = it.name.orEmpty();
                if (!methods.containsKey(name)) {
                    methods[name] = it;
                }
            }

            parentName = PsiGdInheritanceUtil.getParentName(parent);
        }

        return methods;
    }

    fun isStatic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub;
        if (stub !== null) {
            return stub.isStatic();
        }

        return ElementTypeUtil.hasChildOfType(element, GdTypes.STATIC);
    }

    fun isVariadic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub;
        if (stub !== null) {
            return stub.isVariadic();
        }

        return ElementTypeUtil.hasChildOfType(element, GdTypes.VARARG);
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

    fun getReturnType(element: GdParam): String {
        return element.typed?.typeHintNmList?.first()?.text ?: "";
    }

    fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }

        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

    fun isConstructor(element: GdMethodDeclTl): Boolean {
        return element.name == "_init"
                || PsiGdClassUtil.getClass(element) == element.name;

//        return element.name == "_init"
//                || PsiGdClassUtil.getClassName(element) == element.name;
    }

}
