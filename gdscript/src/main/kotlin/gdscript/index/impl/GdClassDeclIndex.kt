package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdClassDeclTl

object GdClassDeclIndex : StringStubIndexExtensionExt<GdClassDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdClassDeclTl> = Indices.CLASS_DECL;

    override fun getVersion(): Int = Indices.VERSION;

}
