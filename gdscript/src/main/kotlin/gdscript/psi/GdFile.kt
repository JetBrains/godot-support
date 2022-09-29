package gdscript.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import gdscript.GdFileType
import gdscript.GdLanguage

class GdFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GdLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return GdFileType.INSTANCE
    }

    override fun toString(): String {
        return "GdScript File"
    }
}
