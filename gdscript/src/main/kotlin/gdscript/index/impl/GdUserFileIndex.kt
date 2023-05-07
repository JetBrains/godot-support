package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdStringVal

class GdUserFileIndex : StringStubIndexExtensionExt<GdStringVal>() {

    companion object {
        val INSTANCE = GdUserFileIndex()
    }

    override fun getKey(): StubIndexKey<String, GdStringVal> = Indices.USER_FILES

    override fun getVersion(): Int = Indices.VERSION

}
