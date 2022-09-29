package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdEnumDeclTl

object GdEnumDeclIndex : StringStubIndexExtensionExt<GdEnumDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdEnumDeclTl> = Indices.ENUM;

    override fun getVersion(): Int = Indices.VERSION;

}
