package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdEnumDeclTl

class GdEnumDeclIndex : StringStubIndexExtensionExt<GdEnumDeclTl>() {

    companion object {
        val INSTANCE = GdEnumDeclIndex()
    }

    override fun getKey(): StubIndexKey<String, GdEnumDeclTl> = Indices.ENUM

    override fun getVersion(): Int = Indices.VERSION

}
