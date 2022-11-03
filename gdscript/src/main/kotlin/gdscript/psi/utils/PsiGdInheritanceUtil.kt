package gdscript.psi.utils

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.elementType
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.*

object PsiGdInheritanceUtil {

    fun isClassName(inheritance: GdInheritanceIdNm): Boolean {
        return inheritance.firstChild.elementType == GdTypes.IDENTIFIER;
    }

    fun getPsiFile(inheritance: GdInheritanceIdNm): PsiFile? {
        val key = inheritance.text.trim('"');
        if (key.startsWith("res://")) {
            return null;
            val virtual = GdFileResIndex.getFiles(key, inheritance.project).first();

            return PsiManager.getInstance(inheritance.project).findFile(virtual);
        }

        return GdClassNamingIndex.getGlobally(inheritance).firstOrNull()?.containingFile;
    }

}
