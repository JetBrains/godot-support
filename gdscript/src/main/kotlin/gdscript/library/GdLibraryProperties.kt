package gdscript.library

import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.roots.libraries.LibraryProperties
import com.intellij.util.xmlb.annotations.Attribute

@State(name = "GdLibraryProperties", storages = [Storage("GdLibraryProperties.xml")])
class GdLibraryProperties : LibraryProperties<GdLibraryProperties>() {

    @Attribute
    var path: String = ""

    @Attribute
    var version: String = ""

    @Attribute
    var date: String = ""

    override fun equals(other: Any?): Boolean {
        if (other !is GdLibraryProperties) return false

        return path == other.path
                && version == other.version
                && date == other.date
    }

    override fun hashCode(): Int {
        return this.hashCode()
    }

    override fun getState(): GdLibraryProperties {
        return this
    }

    override fun loadState(state: GdLibraryProperties) {
        path = state.path
        version = state.version
        date = state.date
    }

}
