package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdConstDeclTl

interface GdConstDeclStub : StubElement<GdConstDeclTl> {
    fun name(): String;
}
