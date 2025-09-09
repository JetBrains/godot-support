package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.types.GdDocumented

interface GdEnumDeclStub : StubElement<GdEnumDeclTl>, GdDocumented {

    fun name(): String
    fun values(): LinkedHashMap<String, Long>

}
