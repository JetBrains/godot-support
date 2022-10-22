package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import gdscript.completion.GdLookup
import gdscript.psi.utils.PsiGdResourceUtil

object GdFileCompletionUtil {

    fun listFileResources(project: Project, onlyGdFiles: Boolean = false, withQuotes: Boolean = false): Array<LookupElement> {
        return PsiGdResourceUtil.listResourceFiles(project, onlyGdFiles).map {
            var str = PsiGdResourceUtil.resourcePath(it);
            if (withQuotes) {
                str = "\"$str\"";
            }

            GdLookup.create(
                str,
                priority = GdLookup.USER_DEFINED,
            )
        }.toTypedArray()
    }

}
