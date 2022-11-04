package gdscript.psi.utils

import gdscript.psi.*

object PsiGdClassVarUtil {

    fun getReturnType(element: GdClassVarDeclTl): String {
        // TODO cannot use indices while indexing error
        // https://intellij-support.jetbrains.com/hc/en-us/community/posts/8407508105362-Indexing-process-should-not-rely-on-non-indexed-file-data
//        val stub = element.stub;
//        if (stub !== null) {
//            return stub.returnType();
//        }

        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
