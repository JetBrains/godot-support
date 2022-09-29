package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdClassNaming

object GdClassNamingIndex : StringStubIndexExtensionExt<GdClassNaming>() {

    override fun getKey(): StubIndexKey<String, GdClassNaming> = Indices.CLASS_NAMING;

    override fun getVersion(): Int = Indices.VERSION;

}
