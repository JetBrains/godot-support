import com.intellij.openapi.util.IconLoader
import gdscript.GdIcon.getEditorIcon

// IJ will automatically add `_dark` to the basename of the SVG file if in Dark theme.

class GdScriptPluginIcons {
    class Icons {
        companion object {
            val GodotLogo = IconLoader.getIcon("icons/godot_editor/Godot.svg", GdScriptPluginIcons::class.java)
            val BackupIcon = IconLoader.getIcon("icons/godot_editor/Object.svg", GdScriptPluginIcons::class.java)
            val GodotFile = IconLoader.getIcon("icons/godot_editor/GodotFile.svg", GdScriptPluginIcons::class.java)
            val GodotConfigFile = IconLoader.getIcon("icons/godot_editor/FileDialog.svg", GdScriptPluginIcons::class.java)
            val GodotProjectFile = IconLoader.getIcon("icons/godot_editor/FileDialog.svg", GdScriptPluginIcons::class.java)
            val GDScript = IconLoader.getIcon("icons/godot_editor/GDScript.svg", GdScriptPluginIcons::class.java)
        }
    }

    class GDScriptIcons{
        companion object{
            val METHOD_MARKER = IconLoader.getIcon("icons/godot_editor/MemberMethod.svg", GdScriptPluginIcons::class.java)
            val VAR_MARKER = IconLoader.getIcon("icons/godot_editor/MemberProperty.svg", GdScriptPluginIcons::class.java)
            val CONST_MARKER = IconLoader.getIcon("icons/godot_editor/MemberConstant.svg", GdScriptPluginIcons::class.java)
            val ENUM_MARKER = IconLoader.getIcon("icons/godot_editor/Enum.svg", GdScriptPluginIcons::class.java)
            val SIGNAL_MARKER = IconLoader.getIcon("icons/godot_editor/MemberSignal.svg", GdScriptPluginIcons::class.java)
            val OBJECT = IconLoader.getIcon("icons/godot_editor/Object.svg", GdScriptPluginIcons::class.java)
            val SIGNAL = IconLoader.getIcon("icons/godot_editor/Signal.svg", GdScriptPluginIcons::class.java)
            val SLOT = IconLoader.getIcon("icons/godot_editor/Slot.svg", GdScriptPluginIcons::class.java)
            val LINK = IconLoader.getIcon("icons/godot_editor/LinkButton.svg", GdScriptPluginIcons::class.java)
            val ERROR = IconLoader.getIcon("icons/godot_editor/StatusError.svg", GdScriptPluginIcons::class.java)
            val RESOURCE = IconLoader.getIcon("icons/godot_editor/Mesh.svg", GdScriptPluginIcons::class.java)
            val STRING = IconLoader.getIcon("icons/godot_editor/String.svg", GdScriptPluginIcons::class.java)
            val OVERRIDE = IconLoader.getIcon("icons/godot_editor/MethodOverride.svg", GdScriptPluginIcons::class.java)
            val ANIMATION = IconLoader.getIcon("icons/godot_editor/Animation.svg", GdScriptPluginIcons::class.java)
            val NODE = IconLoader.getIcon("icons/godot_editor/Node.svg", GdScriptPluginIcons::class.java)
        }
    }

    class TscnIcons {
        companion object {
            val FILE = IconLoader.getIcon("icons/tscnFile.svg", GdScriptPluginIcons::class.java)
            val InstanceOptions = IconLoader.getIcon("icons/godot_editor/InstanceOptions.svg", GdScriptPluginIcons::class.java)
            val Script = IconLoader.getIcon("icons/godot_editor/Script.svg", GdScriptPluginIcons::class.java)
            val SceneUniqueName = IconLoader.getIcon("icons/godot_editor/SceneUniqueName.svg", GdScriptPluginIcons::class.java)
            val GuiVisibilityXray = IconLoader.getIcon("icons/godot_editor/GuiVisibilityXray.svg", GdScriptPluginIcons::class.java)
            val GuiVisibilityVisible = IconLoader.getIcon("icons/godot_editor/GuiVisibilityVisible.svg", GdScriptPluginIcons::class.java)
            val GuiVisibilityVisibleDark = IconLoader.getIcon("icons/godot_editor/GuiVisibilityVisibleDark.svg", GdScriptPluginIcons::class.java)
            val GuiVisibilityHidden = IconLoader.getIcon("icons/godot_editor/GuiVisibilityHidden.svg", GdScriptPluginIcons::class.java)
            val GuiVisibilityHiddenDark = IconLoader.getIcon("icons/godot_editor/GuiVisibilityHiddenDark.svg", GdScriptPluginIcons::class.java)
            val TOOL_WINDOW_ICON = FILE
        }
    }
}