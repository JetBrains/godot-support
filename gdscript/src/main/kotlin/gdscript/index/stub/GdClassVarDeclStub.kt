package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassVarDeclTl

interface GdClassVarDeclStub : StubElement<GdClassVarDeclTl> {
    fun name(): String;
}
