package gdscript.index

import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.indexing.ID
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance

object Indices {

    val CLASS_NAMING_INDEX = StubIndexKey.createIndexKey<String, GdClassNaming>("gdscript.classNaming")
    val INHERITANCE_INDEX = StubIndexKey.createIndexKey<String, GdInheritance>("gdscript.inheritance")

    val FILE_NAMES = ID.create<String, Void>("gdscript.filename")
    val CLASS_NAMES = ID.create<String, Void>("gdscript.classname") // TODO remove
}