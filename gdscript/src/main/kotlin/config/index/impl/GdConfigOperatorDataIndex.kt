package config.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import config.index.GdConfigIndices
import config.psi.ConfigOperator

class GdConfigOperatorDataIndex : StringStubIndexExtensionExt<ConfigOperator>() {

    companion object {
        val INSTANCE = GdConfigOperatorDataIndex()
    }

    override fun getKey(): StubIndexKey<String, ConfigOperator> {
        return GdConfigIndices.OPERATOR_INDEX
    }

    override fun getVersion(): Int {
        return GdConfigIndices.VERSION
    }

}
