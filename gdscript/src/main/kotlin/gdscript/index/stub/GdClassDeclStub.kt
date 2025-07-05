package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassDeclTl
import gdscript.psi.types.GdDocumented

interface GdClassDeclStub : StubElement<GdClassDeclTl>, GdDocumented {
    fun name(): String
    fun parent(): String
}
