package gdscript.psi.utils

import com.intellij.psi.search.GlobalSearchScope
import gdscript.index.impl.GdSignalDeclIndex
import gdscript.psi.GdCallEx
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdSignalDeclTl

object PsiGdSignalUtil {

    fun getParameters(element: GdSignalDeclTl): LinkedHashMap<String, String?> {
        val stub = element.stub
        if (stub !== null) {
            return stub.parameters()
        }

        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

    fun getDeclaration(element: GdCallEx): GdSignalDeclTl? {
        val root = element.prevSibling?.prevSibling ?: return null
        if (root !is GdLiteralEx) return null

        val signalName = root.text
        val file = PsiGdExprUtil.getAttrOrCallParentFile(root) ?: element.containingFile

        return GdSignalDeclIndex.INSTANCE
            .get(signalName, element.project, GlobalSearchScope.fileScope(file))
            .firstOrNull()
    }

}
