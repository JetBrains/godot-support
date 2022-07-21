package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdEnumDeclTl

interface GdEnumDeclStub : StubElement<GdEnumDeclTl> {

    fun name(): String?;
    fun values(): HashMap<String, Int>;

}
