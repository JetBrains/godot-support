package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdConstDeclTl

object GdConstDeclIndex : StringStubIndexExtension<GdConstDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdConstDeclTl> = Indices.CONST_DECL;

}
