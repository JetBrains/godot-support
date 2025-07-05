package gdscript.utils

import com.intellij.openapi.editor.EditorSettings
import com.intellij.openapi.project.Project

object EditorSettingsUtil {

    fun EditorSettings.normalIndent(project: Project): String {
        return if (this.isUseTabCharacter(project)) "\t"
        else " ".repeat(this.getTabSize(project))
    }

}
