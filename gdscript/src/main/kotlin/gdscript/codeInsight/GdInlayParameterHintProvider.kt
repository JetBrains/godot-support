package gdscript.codeInsight

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.HintInfo.MethodInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import gdscript.psi.GdCallEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdRefIdNm
import gdscript.reference.GdClassMemberReference

class GdInlayParameterHintProvider : InlayParameterHintsProvider {

    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element is GdCallEx) {
            val id = PsiTreeUtil.findChildOfType(element, GdRefIdNm::class.java) ?: return null;
            val method = GdClassMemberReference(id).resolveDeclaration() ?: return null;
            if (method is GdMethodDeclTl) {
                return MethodInfo(method.name.orEmpty(), method.parameters.keys.toList());
            }
        }
        return null
    }

    override fun getParameterHints(element: PsiElement): List<InlayInfo> {
        if (element is GdCallEx) {
            val id = PsiTreeUtil.findChildOfType(element, GdRefIdNm::class.java) ?: return emptyList();
            val method = GdClassMemberReference(id).resolveDeclaration() ?: return emptyList();
            if (method is GdMethodDeclTl) {
                val params = method.paramList?.paramList ?: return emptyList();

                return element.argList?.exprList?.mapIndexed { i, it ->
                    InlayInfo(
                        params[i]?.varNmi?.name.orEmpty(),
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
