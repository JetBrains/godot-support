package gdscript

import GdScriptPluginIcons
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.util.IconLoader
import org.jetbrains.annotations.ApiStatus
import javax.swing.Icon

object GdIcon {
    private var editorIcons = HashMap<String, Icon>()

    @ApiStatus.Obsolete // todo: find a way to get rid of this completely and also all unused Godot editor icons
    fun getEditorIcon(className: String): Icon {
        val icon = editorIcons[className]
        if (icon == null) {
            try {
                val loaded = IconLoader.getIcon(
                    String.format("icons/godot_editor/%s.svg", className),
                    GdIcon::class.java
                )
                if (loaded.iconHeight > 1) {
                    //loaded = IconUtil.toSize(loaded, 16, 16)
                    // todo: determine not needed icons and remove them
                    // rework icons to be required size without scaling
                    editorIcons[className] = loaded
                } else {
                    editorIcons[className] = GdScriptPluginIcons.Icons.BackupIcon
                }
            } catch (e: Exception) {
                thisLogger().error("Unable to load editor icon for $className. Using default one.", e)
                editorIcons[className] = GdScriptPluginIcons.Icons.BackupIcon
            }
        }

        return editorIcons[className]!!
    }
}
