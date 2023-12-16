package gdscript.index.impl

import com.intellij.openapi.project.Project
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

    fun userFiles(project: Project): Collection<GdStringVal> {
        return this.getAllValues(project).filter {
            it.text.startsWith("\"user://")
        }
    }

    fun resourceFiles(project: Project, filterBy: String? = null): Collection<GdStringVal> {
        return this.getAllValues(project).filter {
            val name = it.text.trim('"')

            name.startsWith("res://")
                && (filterBy == null || name == filterBy)
        }
    }

}
