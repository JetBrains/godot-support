package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdClassVarDeclTl

object GdClassVarDeclIndex : StringStubIndexExtension<GdClassVarDeclTl>() {

    override fun getKey(): StubIndexKey<String, GdClassVarDeclTl> = Indices.CLASS_VAR;

    override fun getVersion(): Int = Indices.VERSION;

}
