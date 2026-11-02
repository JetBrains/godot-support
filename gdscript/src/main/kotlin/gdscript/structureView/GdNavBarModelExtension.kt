package gdscript.structureView

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.lang.Language
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import javax.swing.Icon

/**
 * Shows the GDScript members from [GdStructureViewModel] in the navigation bar.
 */
class GdNavBarModelExtension : StructureAwareNavBarModelExtension() {
    override val language: Language = GdLanguage

    override fun getPresentableText(`object`: Any?): String? {
        val element = member(`object`) ?: return null
        return GdPresentationUtil.presentationOf(element)?.presentableText
    }

    override fun getIcon(`object`: Any?): Icon? {
        val element = member(`object`) ?: return null
        return GdPresentationUtil.presentationOf(element)?.getIcon(false)
    }

    private fun member(`object`: Any?): NavigatablePsiElement? {
        val element = `object` as? NavigatablePsiElement ?: return null
        if (element is PsiFile || element.language != language) return null
        return element
    }
}
