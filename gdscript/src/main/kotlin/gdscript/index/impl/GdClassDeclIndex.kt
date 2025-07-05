package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdClassDeclTl

class GdClassDeclIndex : StringStubIndexExtensionExt<GdClassDeclTl>() {

    companion object {
        val INSTANCE = GdClassDeclIndex()
    }

    override fun getKey(): StubIndexKey<String, GdClassDeclTl> = Indices.CLASS_DECL

    override fun getVersion(): Int = Indices.VERSION

}
