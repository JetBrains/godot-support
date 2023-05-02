package gdscript.library

import com.intellij.openapi.roots.libraries.LibraryKindRegistry
import com.intellij.openapi.roots.libraries.PersistentLibraryKind

class GdLibraryKind : PersistentLibraryKind<GdLibraryProperties>(KIND_ID) {

    companion object {
        const val KIND_ID = "GdLibraryKind"

        fun getOrCreate(): GdLibraryKind {
            val kind = LibraryKindRegistry.getInstance().findKindById(KIND_ID)
            if (kind is GdLibraryKind) {
                return kind
            }

            if (kind != null) {
                throw Exception("Trying to create GdLibraryKind for known kind $kind");
            }

            return GdLibraryKind()
        }
    }

    override fun createDefaultProperties(): GdLibraryProperties {
        return GdLibraryProperties()
    }

}
