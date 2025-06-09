package gdscript.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage

open class GdFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GdLanguage) {
    override fun getFileType(): FileType = GdFileType

    override fun toString(): String {
        return "GdScript File"
    }
}
