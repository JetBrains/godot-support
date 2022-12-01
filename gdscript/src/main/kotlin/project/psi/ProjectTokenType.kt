package project.psi

import com.intellij.psi.tree.IElementType
import project.ProjectLanguage

class ProjectTokenType : IElementType {

    constructor(debugName: String) : super(debugName, ProjectLanguage);

    override fun toString(): String = "ProjectTokenType.${super.toString()}"

}