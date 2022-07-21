package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.GdLookup
import gdscript.psi.utils.PsiGdFileUtil
import tscn.index.impl.TscnScriptIndex
import tscn.psi.TscnNodeHeader

object GdResourceCompletionUtil {

    fun resources(element: PsiElement, result: CompletionResultSet) {
        val filename = PsiGdFileUtil.filepath(element);
        val script = TscnScriptIndex.get(filename, element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()
            ?: return;

        val nodes = PsiTreeUtil.getStubChildrenOfTypeAsList(script.containingFile, TscnNodeHeader::class.java);

        nodes.forEach {
            if (it.parentPath.isNotEmpty()) {
                val nodePath = it.nodePath;
                result
                    .addElement(
                        GdLookup.create(
                            "$$nodePath",
                            color = GdLookup.RESOURCE_COLOR,
                            priority = GdLookup.USER_DEFINED,
                            typed = it.type,
                        )
                    );
            }
        }
    }

}
