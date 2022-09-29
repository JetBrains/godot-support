package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdSignalDeclTl

object GdSignalDeclIndex : StringStubIndexExtensionExt<GdSignalDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdSignalDeclTl> = Indices.SIGNAL_DECL;

    override fun getVersion(): Int = Indices.VERSION;

}
