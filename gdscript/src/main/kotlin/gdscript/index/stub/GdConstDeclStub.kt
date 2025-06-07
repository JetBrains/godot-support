package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdConstDeclTl
import gdscript.psi.types.GdDocumented

interface GdConstDeclStub : StubElement<GdConstDeclTl>, GdDocumented {
    fun name(): String;
}
