package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.types.GdDocumented

interface GdSignalDeclStub : StubElement<GdSignalDeclTl>, GdDocumented {

    fun name(): String
    fun parameters(): LinkedHashMap<String, String?>

}
