package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdSignalDeclTl

object GdSignalDeclIndex : StringStubIndexExtension<GdSignalDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdSignalDeclTl> = Indices.SIGNAL_DECL;

    override fun getVersion(): Int = Indices.VERSION;

}
