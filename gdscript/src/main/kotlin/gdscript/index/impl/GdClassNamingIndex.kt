package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdClassNaming

class GdClassNamingIndex : StringStubIndexExtensionExt<GdClassNaming>() {

    companion object {
        val INSTANCE = GdClassNamingIndex()
    }

    override fun getKey(): StubIndexKey<String, GdClassNaming> = Indices.CLASS_NAMING

    override fun getVersion(): Int = Indices.VERSION

}
