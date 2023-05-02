package gdscript.library

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.LibraryType
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration
import com.intellij.openapi.roots.libraries.ui.LibraryEditorComponent
import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.JComponent

// TODO bude to potřeba?
class GdLibraryType : LibraryType<GdLibraryProperties> {

    constructor(kind: GdLibraryKind): super(kind)

    override fun getCreateActionName(): String? {
        return null
    }

    override fun createNewLibrary(
        parentComponent: JComponent,
        contextDirectory: VirtualFile?,
        project: Project,
    ): NewLibraryConfiguration? {
        return null
    }

    override fun createPropertiesEditor(editorComponent: LibraryEditorComponent<GdLibraryProperties>): LibraryPropertiesEditor? {
        return null
    }

}
