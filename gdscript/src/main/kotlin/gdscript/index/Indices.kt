package gdscript.index

import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.indexing.ID
import gdscript.psi.*

object Indices {

    val VERSION = 6

    val CLASS_NAMING = StubIndexKey.createIndexKey<String, GdClassNaming>("gdscript.classNaming")
    val CLASS_DECL = StubIndexKey.createIndexKey<String, GdClassDeclTl>("gdscript.classDecl")
    val CLASS_NAME_ID = StubIndexKey.createIndexKey<String, GdClassNameNmi>("gdscript.classId")
    val INHERITANCE = StubIndexKey.createIndexKey<String, GdInheritance>("gdscript.inheritance")
    val CONST_DECL = StubIndexKey.createIndexKey<String, GdConstDeclTl>("gdscript.constDecl")
    val CLASS_VAR = StubIndexKey.createIndexKey<String, GdClassVarDeclTl>("gdscript.classVarDecl")
    val METHOD_DECL = StubIndexKey.createIndexKey<String, GdMethodDeclTl>("gdscript.methodDecl")
    val SIGNAL_DECL = StubIndexKey.createIndexKey<String, GdSignalDeclTl>("gdscript.signal")
    val ENUM = StubIndexKey.createIndexKey<String, GdEnumDeclTl>("gdscript.enum")
    val USER_FILES = StubIndexKey.createIndexKey<String, GdStringVal>("gdscript.userResource")

    val FILE_RES = ID.create<String, Void>("gdscript.fileResource")

}
