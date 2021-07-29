package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl

interface GdMethodDeclStub : StubElement<GdMethodDeclTl> {
    fun name(): String;
}
