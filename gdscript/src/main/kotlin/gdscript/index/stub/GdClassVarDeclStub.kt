package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.types.GdDocumented

interface GdClassVarDeclStub : StubElement<GdClassVarDeclTl>, GdDocumented {
    fun name(): String
    fun isStatic(): Boolean
}
