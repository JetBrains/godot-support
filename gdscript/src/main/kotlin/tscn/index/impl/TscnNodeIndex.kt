package tscn.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import tscn.index.TscnIndices
import tscn.psi.TscnNodeHeader

object TscnNodeIndex : StringStubIndexExtension<TscnNodeHeader>() {

    override fun getKey(): StubIndexKey<String, TscnNodeHeader> = TscnIndices.NODE_INDEX;

    override fun getVersion(): Int {
        return TscnIndices.VERSION;
    }

}
