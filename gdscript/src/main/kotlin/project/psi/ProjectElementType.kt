package project.psi

import com.intellij.psi.tree.IElementType
import project.ProjectLanguage

class ProjectElementType : IElementType {

    constructor(debugName: String) : super(debugName, ProjectLanguage);

}
