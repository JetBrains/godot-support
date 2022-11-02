package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.model.GdNodeHolder
import gdscript.psi.GdNodePath
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnNodeHeader

/**
 * Node utils for available nodes from given script
 */
object GdNodeUtil {

    /**
     * Returns corresponding node for given NodePath element
     */
    fun findNode(element: GdNodePath): GdNodeHolder? {
        val nodes = listNodes(element);
        val path = element.text;

        return nodes.find { it.relativePath == path || it.uniqueId == path };
    }

    /**
     * List all available nodes for given file with parsed relative paths
     */
    fun listNodes(element: PsiElement): Array<GdNodeHolder> {
        val resource = PsiGdResourceUtil.resourcePath(element.containingFile.originalFile.virtualFile);
        val script = TscnResourceIndex.getGlobally(resource, element).firstOrNull() ?: return emptyArray();
        val nodes = PsiTreeUtil.findChildrenOfType(script.containingFile, TscnNodeHeader::class.java);

        val scriptPath = nodes.find { it.scriptResource == resource }?.nodePath?.split("/") ?: return emptyArray();

        return nodes.map {
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

            var uniqueId: String? = null;
            if (it.isUniqueNameOwner) {
                uniqueId = "%${it.name}";
                tail = relativePath;
            } else {
                relativePath = "$$relativePath";
            }

            GdNodeHolder(it, relativePath, uniqueId, tail, "$$hint")
        }.toTypedArray();
    }

}
