package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdMethodDeclTl

interface GdMethodDeclStub : StubElement<GdMethodDeclTl> {
    fun isStatic(): Boolean;
    fun isVariadic(): Boolean;
    fun name(): String;
    fun returnType(): String;
    fun parameters(): LinkedHashMap<String, String?>;
    fun isConstructor(): Boolean;
}
