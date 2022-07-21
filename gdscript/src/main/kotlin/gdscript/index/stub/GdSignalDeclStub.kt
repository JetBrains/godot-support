package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdSignalDeclTl

interface GdSignalDeclStub : StubElement<GdSignalDeclTl> {

    fun name(): String;
    fun parameters(): Array<String>;

}
