package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdClassVarDeclTl

class GdClassVarDeclIndex : StringStubIndexExtensionExt<GdClassVarDeclTl>() {

    companion object {
        val INSTANCE = GdClassVarDeclIndex()
    }

    override fun getKey(): StubIndexKey<String, GdClassVarDeclTl> = Indices.CLASS_VAR

    override fun getVersion(): Int = Indices.VERSION

}
