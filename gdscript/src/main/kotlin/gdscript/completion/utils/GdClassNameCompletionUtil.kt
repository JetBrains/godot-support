package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdClassNaming

object GdClassNameCompletionUtil {

    fun toLookup(className: GdClassNaming): LookupElement {
        // TODO
        /*val annotations = PsiTreeUtil.getChildrenOfType(className.parent, GdAnnotationTl::class.java);
        val iconResource = annotations?.find {
            it.annotation.text.startsWith("@icon")
        }
            ?.literalExList
            ?.firstOrNull()
            ?.text
            ?.trim('"')
            ?.removePrefix("res://")

        var icon = GdIcon.getEditorIcon(className.classname);
        if (iconResource != null) {
            val path = "${className.project.basePath}/${iconResource}";
            icon = IconLoader.getIcon(path, GdClassNameCompletionUtil::class.java);
        };*/

        return GdLookup.create(
            className.classname,
            priority = GdLookup.USER_DEFINED,
            icon = GdIcon.getEditorIcon(className.classname),
        )
    }

}
