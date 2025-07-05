package tscn.psi.search.processor

import com.intellij.psi.PsiElement
import com.intellij.psi.search.TextOccurenceProcessor
import tscn.psi.TscnResourceHeader

class TscnResourceHeaderCollectingProcessor : TextOccurenceProcessor {

    val result = mutableListOf<TscnResourceHeader>()

    override fun execute(element: PsiElement, offsetInElement: Int): Boolean {
        if (element is TscnResourceHeader) {
            result.add(element)
        }
        return true
    }

}
