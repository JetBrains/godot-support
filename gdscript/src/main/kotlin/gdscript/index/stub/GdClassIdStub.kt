package gdscript.index.stub

import com.intellij.psi.stubs.StubElement
import gdscript.psi.GdClassNameNmi

interface GdClassIdStub : StubElement<GdClassNameNmi> {
    fun name(): String
    fun parent(): String
}
