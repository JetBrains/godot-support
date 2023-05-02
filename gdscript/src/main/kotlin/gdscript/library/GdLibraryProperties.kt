package gdscript.library

import com.intellij.openapi.roots.libraries.LibraryProperties

class GdLibraryProperties : LibraryProperties<GdLibrary>() {
    var config: GdLibrary? = null

    override fun equals(other: Any?): Boolean {
        if (other !is GdLibraryProperties) return false
        val otherConfig = other.config
        val myConfig = config

        if (myConfig == null && otherConfig == null) return true
        if (myConfig == null || otherConfig == null) return false

        return myConfig.path == otherConfig.path
                && myConfig.version == otherConfig.version
    }

    override fun hashCode(): Int {
        return config.hashCode()
    }

    override fun getState(): GdLibrary? {
        return config
    }

    override fun loadState(state: GdLibrary) {
        config = state
    }

}
