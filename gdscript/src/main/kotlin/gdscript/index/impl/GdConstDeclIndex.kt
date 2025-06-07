package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdConstDeclTl

class GdConstDeclIndex : StringStubIndexExtensionExt<GdConstDeclTl>() {

    companion object {
        val INSTANCE = GdConstDeclIndex()
    }

    override fun getKey(): StubIndexKey<String, GdConstDeclTl> = Indices.CONST_DECL

    override fun getVersion(): Int = Indices.VERSION

}
