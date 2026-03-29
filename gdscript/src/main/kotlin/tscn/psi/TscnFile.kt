package tscn.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.jetbrains.rider.godot.community.tscn.TscnFileType
import com.jetbrains.rider.godot.community.tscn.TscnLanguage

class TscnFile : PsiFileBase {

    constructor(viewProvider: FileViewProvider) : super(viewProvider, TscnLanguage)

    override fun getFileType(): FileType = TscnFileType

    override fun toString(): String = "GodotScene file"

}
