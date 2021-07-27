package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdClassNameNm
import gdscript.psi.GdFile
import gdscript.psi.impl.GdClassNamingElementType

object PsiGdClassNamingUtil {

    fun listClassNameNm(project: Project): List<GdClassNameNm> {
        return mapFiles(PsiGdFileUtil.gdFiles(project));
    }

    private fun mapFiles(files: Collection<GdFile>): List<GdClassNameNm> {
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
