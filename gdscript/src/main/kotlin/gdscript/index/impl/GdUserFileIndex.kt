package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdStringVal

object GdUserFileIndex : StringStubIndexExtensionExt<GdStringVal>() {

    override fun getKey(): StubIndexKey<String, GdStringVal> = Indices.USER_FILES;

    override fun getVersion(): Int = Indices.VERSION;

}
