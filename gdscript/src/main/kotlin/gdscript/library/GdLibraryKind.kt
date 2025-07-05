package gdscript.library

import com.intellij.openapi.roots.libraries.PersistentLibraryKind

const val ID = "GdScript"
object GdLibraryKind : PersistentLibraryKind<GdLibraryProperties>(ID) {

    override fun createDefaultProperties(): GdLibraryProperties {
        return GdLibraryProperties()
    }

}
