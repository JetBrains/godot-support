package project.index

import com.intellij.psi.stubs.StubIndexKey
import project.psi.ProjectData
import project.psi.ProjectSection

object ProjectIndices {

    val VERSION = 1;

    val SECTION_INDEX = StubIndexKey.createIndexKey<String, ProjectSection>("project.section")
    val DATA_INDEX = StubIndexKey.createIndexKey<String, ProjectData>("project.data")

}
