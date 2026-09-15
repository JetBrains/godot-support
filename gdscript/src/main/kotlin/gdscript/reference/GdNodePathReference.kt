package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.psi.GdNodePath
import gdscript.psi.utils.GdNodeUtil

/**
 * `$Node` / `%UniqueNode` reference to the node it names in the `.tscn` scene attached to the
 * containing script. The resolve walk is [GdNodeUtil.findNode], already used by completion, type
 * inference and [gdscript.annotator.GdResourceTypeAnnotator] to check the path exists.
 */
class GdNodePathReference(element: GdNodePath) : PsiReferenceBase<GdNodePath>(element, TextRange(0, element.textLength)) {

    override fun resolve(): PsiElement? = GdNodeUtil.findNode(element)?.element

    override fun getVariants(): Array<Any> = emptyArray()

}
