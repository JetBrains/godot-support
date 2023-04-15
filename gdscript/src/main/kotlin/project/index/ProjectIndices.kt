package project.index

import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.indexing.ID
import project.psi.ProjectData
import project.psi.ProjectSection

object ProjectIndices {

    val VERSION = 2

    val SECTION_INDEX = StubIndexKey.createIndexKey<String, ProjectSection>("gdproject.section")
    val DATA_INDEX = StubIndexKey.createIndexKey<String, ProjectData>("gdproject.data")

    /* File Index */
    val PROJECT_ROOT_INDEX = ID.create<String, Void>("gdproject.project")

}
