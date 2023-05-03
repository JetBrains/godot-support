package gdscript.library

import com.intellij.openapi.roots.libraries.PersistentLibraryKind

object GdLibraryKind : PersistentLibraryKind<GdLibraryProperties>("GdScript") {

    const val ID = "GdScript"

    override fun createDefaultProperties(): GdLibraryProperties {
        return GdLibraryProperties()
    }

}
