package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.types.GdDocumented

interface GdMethodDeclStub : StubElement<GdMethodDeclTl>, GdDocumented {
    fun isStatic(): Boolean;
    fun isVariadic(): Boolean;
    fun name(): String;
    fun returnType(): String;
    fun parameters(): LinkedHashMap<String, String?>;
    fun isConstructor(): Boolean;
}
