package tscn.psi.search.processor

import com.intellij.psi.PsiElement
import com.intellij.psi.search.TextOccurenceProcessor
import tscn.psi.TscnConnectionHeader

class TscnConnectionHeaderCollectingProcessor : TextOccurenceProcessor {

    val result = mutableListOf<TscnConnectionHeader>()

    override fun execute(element: PsiElement, offsetInElement: Int): Boolean {
        if (element is TscnConnectionHeader) {
            result.add(element)
        }
        // always continue
        return true
    }

}