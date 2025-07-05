package project.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import project.ProjectFileType
import project.ProjectLanguage

class ProjectFile : PsiFileBase {

    constructor(viewProvider: FileViewProvider) : super(viewProvider, ProjectLanguage)

    override fun getFileType(): FileType = ProjectFileType;

    override fun toString(): String = "Godot's project file"

}
