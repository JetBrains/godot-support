package gdscript.index.impl

import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import gdscript.index.Indices
import gdscript.psi.GdInheritance

object GdInheritanceIndex : StringStubIndexExtensionExt<GdInheritance>() {

    override fun getKey(): StubIndexKey<String, GdInheritance> = Indices.INHERITANCE;

    override fun getVersion(): Int = Indices.VERSION;

}
