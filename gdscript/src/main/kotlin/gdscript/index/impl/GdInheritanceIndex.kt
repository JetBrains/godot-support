package gdscript.index.impl

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdInheritance

object GdInheritanceIndex : StringStubIndexExtension<GdInheritance>() {
    override fun getKey(): StubIndexKey<String, GdInheritance> = Indices.INHERITANCE_INDEX;

    // TODO simplicity
//    override fun get(key: String, project: Project, scope: GlobalSearchScope): MutableCollection<GdClassNaming> {
//        return super.get(key, project, scope)
//    }
}
