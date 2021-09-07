package gdscript.psi.utils

import com.intellij.psi.PsiIdentifier
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdSignalDeclTl

object PsiGdSignalUtil {

    fun getName(element: GdSignalDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.signalIdNmi?.name ?: "";
    }

    fun getParameters(element: GdSignalDeclTl): Array<String> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }

        return PsiTreeUtil.findChildrenOfType(element.signalParList, PsiIdentifier::class.java).map {
            it.text
        }.toTypedArray()
    }

}