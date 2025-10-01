package tscn.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnConnectionHeader

class TscnConnectionIndex : StringStubIndexExtensionExt<TscnConnectionHeader>() {

    companion object {
        val INSTANCE = TscnConnectionIndex()
    }

    override fun getKey(): StubIndexKey<String, TscnConnectionHeader> = TscnIndices.CONNECTION_INDEX

    override fun getVersion(): Int {
        return TscnIndices.VERSION
    }

}
