package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdEnumDeclTl

object GdEnumDeclIndex : StringStubIndexExtension<GdEnumDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdEnumDeclTl> = Indices.ENUM;

}
