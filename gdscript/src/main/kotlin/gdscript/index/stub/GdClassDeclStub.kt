package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassDeclTl

interface GdClassDeclStub : StubElement<GdClassDeclTl> {
    fun name(): String;
    fun parent(): String;
}
