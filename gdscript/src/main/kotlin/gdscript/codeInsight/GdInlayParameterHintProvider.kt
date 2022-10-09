package gdscript.codeInsight

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.HintInfo.MethodInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.containers.toArray
import gdscript.psi.GdCallEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.utils.PsiGdSignalUtil
import gdscript.reference.GdClassMemberReference

class GdInlayParameterHintProvider : InlayParameterHintsProvider {

    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element is GdCallEx) {
            val id = PsiTreeUtil.findChildOfType(element, GdRefIdNm::class.java) ?: return null;
            val declaration = GdClassMemberReference(id).resolveDeclaration() ?: return null;
            if (declaration is GdMethodDeclTl) {
                val name = declaration.name.orEmpty();
                if (name == "emit") {
                    val signal = PsiGdSignalUtil.getDeclaration(element);
                    if (signal != null) {
                        return MethodInfo(name, signal.parameters.toList());
                    }
                }

                return MethodInfo(name, declaration.parameters.keys.toList());
            }
        }
        return null
    }

    override fun getParameterHints(element: PsiElement): List<InlayInfo> {
        if (element is GdCallEx) {
            val id = PsiTreeUtil.findChildOfType(element, GdRefIdNm::class.java) ?: return emptyList();
            val method = GdClassMemberReference(id).resolveDeclaration() ?: return emptyList();
            if (method is GdMethodDeclTl) {
                val name = method.name.orEmpty();
                var params = method.parameters.keys.toArray(emptyArray());
                if (name == "emit") {
                    val signal = PsiGdSignalUtil.getDeclaration(element);
                    if (signal != null) {
                        params = signal.parameters;
                    }
                }

                return element.argList?.exprList?.mapIndexed { i, it ->
                    InlayInfo(
                        if (params.size > i) params[i] else "",
                        it.startOffset,
                        false,
                    )
                } ?: emptyList();
            }
        }

        return emptyList()
    }

    override fun canShowHintsWhenDisabled(): Boolean {
        return true
    }

    override fun getDefaultBlackList(): MutableSet<String> {
        return mutableSetOf();
    }

}
