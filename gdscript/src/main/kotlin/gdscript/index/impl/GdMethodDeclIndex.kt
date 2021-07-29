package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdMethodDeclTl

object GdMethodDeclIndex : StringStubIndexExtension<GdMethodDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdMethodDeclTl> = Indices.METHOD_DECL;

}
