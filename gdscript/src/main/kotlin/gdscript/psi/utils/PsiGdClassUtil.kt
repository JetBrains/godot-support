package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdClassNameNm
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.psi.impl.GdClassNamingElementType

object PsiGdClassUtil {

    fun listClassNaming(project: Project): List<GdClassNaming> {
        return mapFiles(PsiGdFileUtil.gdFiles(project));
    }

    fun getClassName(element: PsiElement): String? {
        return PsiTreeUtil
            .getChildOfType(element.containingFile, GdClassNaming::class.java)
            ?.classname;
    }

    private fun mapFiles(files: Collection<GdFile>): List<GdClassNaming> {
        return files.map {
            val stub = it.getGreenStub();
            if (stub !== null) {
                val naming = stub.findChildStubByType(GdClassNamingElementType)
                naming?.psi
            } else {
                PsiTreeUtil.findChildOfType(it, GdClassNaming::class.java)
            }
        }.filterNotNull();
    }

}
