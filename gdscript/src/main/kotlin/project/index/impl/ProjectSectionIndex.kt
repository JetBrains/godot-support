package project.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import project.index.ProjectIndices
import project.psi.ProjectSection

object ProjectSectionIndex : StringStubIndexExtensionExt<ProjectSection>() {

    val INPUT_SECTION = "[input]"
    val AUTOLOAD_SECTION = "[autoload]"

    override fun getKey(): StubIndexKey<String, ProjectSection> = ProjectIndices.SECTION_INDEX;

    override fun getVersion(): Int {
        return ProjectIndices.VERSION;
    }

}
