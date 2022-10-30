package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.utils.StringUtils.camelToSnakeCase
import tscn.index.impl.TscnScriptIndex
import tscn.psi.TscnNodeHeader

object GdResourceCompletionUtil {

    fun resources(element: PsiElement, result: CompletionResultSet) {
        this.resourceList(element).forEach {
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

    // TODO ii tady se musí řešit relativní cesta !!
    fun fullVarResources(element: PsiElement, result: CompletionResultSet) {
        this.resourceList(element).forEach {
            if (it.parentPath.isNotEmpty()) {
                var nodePath = it.nodePath;
                if (it.isUniqueNameOwner) {
                    nodePath = "\"%${it.name}\"";
                }

                result
                    .addElement(
                        GdLookup.create(
                            "@onready var ${it.name.camelToSnakeCase()}: ${it.type} = $$nodePath", // +;
                            color = GdLookup.RESOURCE_COLOR,
                            priority = GdLookup.USER_DEFINED,
                            typed = it.type,
                            presentable = "$${it.nodePath}",
                        )
                    );
            }
        }
    }

    private fun resourceList(element: PsiElement): List<TscnNodeHeader> {
        val filename = PsiGdFileUtil.filepath(element);
        val script = TscnScriptIndex.get("${GdKeywords.RESOURCE_PREFIX}$filename", element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()
            ?: return emptyList();

        return PsiTreeUtil.getStubChildrenOfTypeAsList(script.containingFile, TscnNodeHeader::class.java);
    }

}
