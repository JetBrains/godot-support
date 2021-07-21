package gdscript.index.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import gdscript.index.Indices
import gdscript.psi.GdClassNaming

object GdClassNamingIndex : StringStubIndexExtension<GdClassNaming>() {
    override fun getKey(): StubIndexKey<String, GdClassNaming> = Indices.CLASS_NAMING_INDEX;

    // TODO simplicity
//    override fun get(key: String, project: Project, scope: GlobalSearchScope): MutableCollection<GdClassNaming> {
//        return super.get(key, project, scope)
//    }
}