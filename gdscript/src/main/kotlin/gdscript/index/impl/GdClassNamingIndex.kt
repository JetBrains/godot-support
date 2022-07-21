package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdClassNaming

object GdClassNamingIndex : StringStubIndexExtension<GdClassNaming>() {

    override fun getKey(): StubIndexKey<String, GdClassNaming> = Indices.CLASS_NAMING;

}
