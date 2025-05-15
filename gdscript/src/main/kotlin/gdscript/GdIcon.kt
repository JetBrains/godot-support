package gdscript

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object GdIcon {
    private var editorIcons = HashMap<String, Icon>()

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
                    thisLogger().info("Icon $className requested.")
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
