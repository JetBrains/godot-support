package gdscript.library

import com.intellij.openapi.roots.libraries.LibraryProperties
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.annotations.Tag

@State(name = "GdLibraryProperties", storages = [Storage("GdLibraryProperties.xml")])
class GdLibraryProperties : LibraryProperties<GdLibrary>() {

    @Tag("config")
    var config: GdLibrary = GdLibrary()

    override fun equals(other: Any?): Boolean {
        if (other !is GdLibraryProperties) return false
        val otherConfig = other.config
        val myConfig = config

        return myConfig.path == otherConfig.path
                && myConfig.version == otherConfig.version
    }

    override fun hashCode(): Int {
        return config.hashCode()
    }

    override fun getState(): GdLibrary {
        return config
    }

    override fun loadState(state: GdLibrary) {
        config = state
    }

}
