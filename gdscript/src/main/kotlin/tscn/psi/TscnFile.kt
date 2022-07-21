package tscn.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import tscn.TscnFileType
import tscn.TscnLanguage

class TscnFile : PsiFileBase {

    constructor(viewProvider: FileViewProvider) : super(viewProvider, TscnLanguage.INSTANCE)

    override fun getFileType(): FileType = TscnFileType.INSTANCE;

    override fun toString(): String = "GodotScene file"

}
