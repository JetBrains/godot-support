package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.GdLookup
import gdscript.completion.handler.GdReplaceInsertHandler
import gdscript.psi.utils.PsiGdResourceUtil
import gdscript.utils.StringUtils.camelToSnakeCase
import tscn.index.impl.TscnResourceIndex
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
        val list = this.resourceList(element);
        val parsed = this.resourceList(element).map {
            val absolutePath = "$${it.nodePath}";
            var localPath = absolutePath;
            if (it.isUniqueNameOwner) {
                localPath = "%${it.name}";
            }

            val name = absolutePath;
            val presentable = it.nodePath;
            val script = it.scriptResource;

            GdLookup.create(
//              "@onready var ${it.name.camelToSnakeCase()}: ${it.type} = $$nodePath", // +;
                localPath,
                color = GdLookup.RESOURCE_COLOR,
                priority = GdLookup.USER_DEFINED,
                typed = it.type,
                presentable = absolutePath,
                )
        }
        parsed.forEach {
            result.addElement(it);
        }
    }

    @Deprecated("moved")
    fun listVarResources(element: PsiElement): MutableList<LookupElement> {
        val results = mutableListOf<LookupElement>();
        val resource = PsiGdResourceUtil.resourcePath(element.containingFile.originalFile.virtualFile);
        val script = TscnResourceIndex.getGlobally(resource, element).firstOrNull() ?: return results;
        val nodes = PsiTreeUtil.findChildrenOfType(script.containingFile, TscnNodeHeader::class.java);

        val scriptPath = nodes.find { it.scriptResource == resource }?.nodePath?.split("/") ?: return results;

        nodes.forEach {
            val nodePath = it.nodePath;
            val path = nodePath.split("/");

            var relativePath = if (path.size == scriptPath.size) {
                "."
            } else if (path.size > scriptPath.size) {
                path.subList(scriptPath.size, path.size).joinToString("/")
            } else {
                List(scriptPath.size - path.size) { ".." }.joinToString("/")
            }

            if (relativePath.contains(".")) {
                relativePath = "\"$relativePath\"";
            }

            var hint = nodePath.removePrefix("../");
            var tail: String? = null;
            if (hint == "..") {
                hint = it.name;
                tail = "(root)";
            }

            if (it.isUniqueNameOwner) {
                relativePath = "%${it.name}";
                tail = relativePath;
            } else {
                relativePath = "$$relativePath";
            }

            results.add(
                GdLookup.create(
                    "$$hint",
                    color = GdLookup.RESOURCE_COLOR,
                    priority = GdLookup.USER_DEFINED,
                    typed = it.type,
                    tail = tail,
                    handler = GdReplaceInsertHandler("@onready var ${it.name.camelToSnakeCase()}: ${it.type} = $relativePath"), // +;
                )
            )
        }

        return results;
    }

    private fun resourceList(element: PsiElement): Array<TscnNodeHeader> {
        val resource = PsiGdResourceUtil.resourcePath(element.containingFile.originalFile.virtualFile);
        val script = TscnResourceIndex.getGlobally(resource, element).firstOrNull() ?: return emptyArray();

        return PsiTreeUtil.findChildrenOfType(script.containingFile, TscnNodeHeader::class.java).toTypedArray();
    }

}
