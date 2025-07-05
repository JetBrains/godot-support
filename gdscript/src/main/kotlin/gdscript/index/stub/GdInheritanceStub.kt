package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdInheritance

interface GdInheritanceStub : StubElement<GdInheritance> {
    fun inheritancePath(): String;
}
