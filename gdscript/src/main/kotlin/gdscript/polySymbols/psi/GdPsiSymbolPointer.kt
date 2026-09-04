package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil

/**
 * A [Pointer] to the symbol that [element] declares, which survives a delete and a re-insert of the
 * text of [element].
 *
 * A plain smart pointer is not enough for the in-place rename. The template of
 * [com.intellij.refactoring.rename.api.RenameTarget] deletes the text of every usage in the file,
 * the declaration included, so a smart pointer to a name identifier stays dead from then on. The
 * template puts the old text back before it starts the real rename, so a search by the original
 * range finds the identifier again. The classic
 * [com.intellij.refactoring.rename.inplace.InplaceRefactoring] re-finds its own element by range for
 * the same reason.
 *
 * The fallback keeps the range of the file it started in, so it answers only while that file holds
 * the original text. A rename writes the new text after it dereferences the target, and any other
 * edit invalidates the smart pointer of the file, so no other caller can reach the fallback.
 *
 * @param element the name identifier of the declaration.
 * @param symbol builds the symbol from the restored [element].
 */
internal fun <E : PsiElement, S : Any> gdPsiSymbolPointer(element: E, symbol: (E) -> S): Pointer<S> {
    val elementPointer = element.createSmartPointer()
    val filePointer = element.containingFile?.createSmartPointer()
    val range = element.textRange

    @Suppress("UNCHECKED_CAST")
    val elementClass = element.javaClass as Class<E>

    return Pointer {
        val restored = elementPointer.element
                       ?: filePointer?.element?.let {
                           PsiTreeUtil.findElementOfClassAtRange(it, range.startOffset, range.endOffset, elementClass)
                       }
        restored?.let(symbol)
    }
}
