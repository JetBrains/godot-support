package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdStringVal

interface GdStringValStub : StubElement<GdStringVal> {
    fun name(): String;
}
