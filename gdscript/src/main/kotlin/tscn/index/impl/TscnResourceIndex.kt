package tscn.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnResourceHeader

object TscnResourceIndex : StringStubIndexExtensionExt<TscnResourceHeader>() {

    override fun getKey(): StubIndexKey<String, TscnResourceHeader> = TscnIndices.RESOURCE_INDEX;

    override fun getVersion(): Int {
        return TscnIndices.VERSION;
    }

}
