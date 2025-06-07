package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdSignalDeclTl

class GdSignalDeclIndex : StringStubIndexExtensionExt<GdSignalDeclTl>() {

    companion object {
        val INSTANCE = GdSignalDeclIndex()
    }

    override fun getKey(): StubIndexKey<String, GdSignalDeclTl> = Indices.SIGNAL_DECL

    override fun getVersion(): Int = Indices.VERSION

}
