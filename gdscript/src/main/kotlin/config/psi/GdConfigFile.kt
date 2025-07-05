package config.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import config.GdConfigFileType
import config.GdConfigLanguage

class GdConfigFile : PsiFileBase {

    constructor(viewProvider: FileViewProvider) : super(viewProvider, GdConfigLanguage)

    override fun getFileType(): FileType = GdConfigFileType

    override fun toString(): String = "GdConfig file"

}
