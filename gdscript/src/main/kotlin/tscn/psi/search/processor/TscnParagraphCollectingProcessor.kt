package tscn.psi.search.processor

import com.intellij.psi.PsiElement
import com.intellij.psi.search.TextOccurenceProcessor
import com.intellij.psi.util.PsiTreeUtil
import tscn.psi.TscnHeaderValue
import tscn.psi.TscnParagraph
import tscn.psi.utils.TscnHeaderUtils

class TscnParagraphCollectingProcessor(val type: String) : TextOccurenceProcessor {

    val result = mutableListOf<TscnParagraph>()

    override fun execute(element: PsiElement, offsetInElement: Int): Boolean {
        if (element is TscnParagraph) {
            val headerValues = PsiTreeUtil.getStubChildrenOfTypeAsList(element.header, TscnHeaderValue::class.java)
            // check for the correct type
            if (TscnHeaderUtils.getValue(headerValues, "type") == type) {
                result.add(element)
            }
        }
        // always continue
        return true;
    }

}