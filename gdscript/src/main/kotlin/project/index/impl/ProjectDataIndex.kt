package project.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import project.index.ProjectIndices
import project.psi.ProjectData

object ProjectDataIndex : StringStubIndexExtensionExt<ProjectData>() {

    override fun getKey(): StubIndexKey<String, ProjectData> = ProjectIndices.DATA_INDEX;

    override fun getVersion(): Int {
        return ProjectIndices.VERSION;
    }

}
