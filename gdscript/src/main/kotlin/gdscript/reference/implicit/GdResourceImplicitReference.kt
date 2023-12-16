package gdscript.reference.implicit

import com.intellij.model.Symbol
import com.intellij.model.psi.ImplicitReferenceProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdInheritance
import gdscript.psi.GdStringVal
import gdscript.utils.VirtualFileUtil.getPsiFile

class GdResourceImplicitReference : ImplicitReferenceProvider {

    override fun resolveAsReference(element: PsiElement): MutableCollection<out Symbol> {
        val result = mutableListOf<Symbol>()
        if (element !is GdStringVal) return result
        val name = element.text.trim('"')
        if (!name.startsWith("res://")) return result

        GdFileResIndex.INSTANCE.getFiles(name, element)
            ?.firstOrNull()
            ?.getPsiFile(element.project)
            ?.run {
                PsiTreeUtil.getStubChildOfType(this, GdInheritance::class.java)?.run {
                    result.add(this.symbol)
                }
            }

        return result
    }

}
