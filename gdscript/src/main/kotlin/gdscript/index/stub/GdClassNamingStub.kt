package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNaming

interface GdClassNamingStub : StubElement<GdClassNaming> {
    fun name(): String;
}
