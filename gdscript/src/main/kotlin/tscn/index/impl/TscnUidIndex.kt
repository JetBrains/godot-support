package tscn.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnResourceHeader

class TscnUidIndex : StringStubIndexExtensionExt<TscnResourceHeader>() {

    companion object {
        val INSTANCE = TscnUidIndex()
    }

    override fun getKey(): StubIndexKey<String, TscnResourceHeader> = TscnIndices.UID_INDEX

    override fun getVersion(): Int {
        return TscnIndices.VERSION
    }

}
