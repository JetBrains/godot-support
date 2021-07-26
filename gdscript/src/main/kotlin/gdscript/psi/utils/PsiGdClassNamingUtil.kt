package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdClassNameNm
import gdscript.psi.impl.GdClassNamingElementType

object PsiGdClassNamingUtil {
    // TODO
    fun listClassNameNm(project: Project): List<GdClassNameNm> {
        val files = PsiGdFileUtil.gdFiles(project);

        return files.map {
            val stub = it.getGreenStub();
            if (stub !== null) {
                val naming = stub.findChildStubByType(GdClassNamingElementType)
                naming?.psi?.classNameNm
            } else {
                PsiTreeUtil.findChildOfType(it, GdClassNameNm::class.java)
            }
        }.filterNotNull();
    }

}
