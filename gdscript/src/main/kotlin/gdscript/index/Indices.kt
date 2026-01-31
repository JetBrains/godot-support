package gdscript.index

import com.intellij.psi.stubs.StubIndexKey
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdInheritance
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl

object Indices {

    val VERSION = 18

    val CLASS_NAMING = StubIndexKey.createIndexKey<String, GdClassNaming>("gdscript.classNaming")
    val CLASS_DECL = StubIndexKey.createIndexKey<String, GdClassDeclTl>("gdscript.classDecl")
    val CLASS_NAME_ID = StubIndexKey.createIndexKey<String, GdClassNameNmi>("gdscript.classId")
    val INHERITANCE = StubIndexKey.createIndexKey<String, GdInheritance>("gdscript.inheritance")
    val CONST_DECL = StubIndexKey.createIndexKey<String, GdConstDeclTl>("gdscript.constDecl")
    val CLASS_VAR = StubIndexKey.createIndexKey<String, GdClassVarDeclTl>("gdscript.classVarDecl")
    val METHOD_DECL = StubIndexKey.createIndexKey<String, GdMethodDeclTl>("gdscript.methodDecl")
    val SIGNAL_DECL = StubIndexKey.createIndexKey<String, GdSignalDeclTl>("gdscript.signal")
    val ENUM = StubIndexKey.createIndexKey<String, GdEnumDeclTl>("gdscript.enum")

}
