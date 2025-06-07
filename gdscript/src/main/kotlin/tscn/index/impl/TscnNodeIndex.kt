package tscn.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnNodeHeader

class TscnNodeIndex : StringStubIndexExtensionExt<TscnNodeHeader>() {

    companion object {
        val INSTANCE = TscnNodeIndex()
    }

    override fun getKey(): StubIndexKey<String, TscnNodeHeader> = TscnIndices.NODE_INDEX;

    override fun getVersion(): Int {
        return TscnIndices.VERSION
    }

}
