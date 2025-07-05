package config.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import config.index.GdConfigIndices
import config.psi.ConfigOperation

class GdConfigOperationDataIndex : StringStubIndexExtensionExt<ConfigOperation>() {

    companion object {
        val INSTANCE = GdConfigOperationDataIndex()
    }

    override fun getKey(): StubIndexKey<String, ConfigOperation> {
        return GdConfigIndices.OPERATION_INDEX
    }

    override fun getVersion(): Int {
        return GdConfigIndices.VERSION
    }

}
