package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdMethodDeclTl

object GdMethodDeclIndex : StringStubIndexExtensionExt<GdMethodDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdMethodDeclTl> = Indices.METHOD_DECL;

    override fun getVersion(): Int = Indices.VERSION;

}
