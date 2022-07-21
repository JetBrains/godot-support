package gdscript.completion.util

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IconUtil
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdClassNaming
import java.io.File

object GdClassNameCompletionUtil {

    fun toLookup(className: GdClassNaming): LookupElement {
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
