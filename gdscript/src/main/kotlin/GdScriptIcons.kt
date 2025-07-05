import com.intellij.rider.plugins.godot.gdscript.icons.RiderPluginsGodotGdscriptIcons

// IJ will automatically add `_dark` to the basename of the SVG file if in Dark theme.

class GdScriptPluginIcons {
    class Icons {
        companion object {
            val GodotLogo = RiderPluginsGodotGdscriptIcons.GodotEditor.Godot
            val BackupIcon = RiderPluginsGodotGdscriptIcons.GodotEditor.Object
            val GodotFile = RiderPluginsGodotGdscriptIcons.GodotEditor.GodotFile
            val GodotConfigFile = RiderPluginsGodotGdscriptIcons.GodotEditor.FileDialog
            val GodotProjectFile = RiderPluginsGodotGdscriptIcons.GodotEditor.FileDialog
            @JvmField val GDScript = RiderPluginsGodotGdscriptIcons.GodotEditor.GDScript
        }
    }

    class GDScriptIcons{
        companion object{
            val METHOD_MARKER = RiderPluginsGodotGdscriptIcons.GodotEditor.MemberMethod
            val VAR_MARKER = RiderPluginsGodotGdscriptIcons.GodotEditor.MemberProperty
            val CONST_MARKER = RiderPluginsGodotGdscriptIcons.GodotEditor.MemberConstant
            val ENUM_MARKER = RiderPluginsGodotGdscriptIcons.GodotEditor.Enum
            val SIGNAL_MARKER = RiderPluginsGodotGdscriptIcons.GodotEditor.MemberSignal
            val OBJECT = RiderPluginsGodotGdscriptIcons.GodotEditor.Object
            val SIGNAL = RiderPluginsGodotGdscriptIcons.GodotEditor.Signal
            val SLOT = RiderPluginsGodotGdscriptIcons.GodotEditor.Slot
            val LINK = RiderPluginsGodotGdscriptIcons.GodotEditor.LinkButton
            val ERROR = RiderPluginsGodotGdscriptIcons.GodotEditor.StatusError
            val RESOURCE = RiderPluginsGodotGdscriptIcons.GodotEditor.Mesh
            val STRING = RiderPluginsGodotGdscriptIcons.GodotEditor.String
            val OVERRIDE = RiderPluginsGodotGdscriptIcons.GodotEditor.MethodOverride
            val ANIMATION = RiderPluginsGodotGdscriptIcons.GodotEditor.Animation
            val NODE = RiderPluginsGodotGdscriptIcons.GodotEditor.Node
        }
    }

    class TscnIcons {
        companion object {
            val FILE = RiderPluginsGodotGdscriptIcons.TscnFile
            val InstanceOptions = RiderPluginsGodotGdscriptIcons.GodotEditor.InstanceOptions
            val Script = RiderPluginsGodotGdscriptIcons.GodotEditor.Script
            val SceneUniqueName = RiderPluginsGodotGdscriptIcons.GodotEditor.SceneUniqueName
            val GuiVisibilityXray = RiderPluginsGodotGdscriptIcons.GodotEditor.GuiVisibilityXray
            val GuiVisibilityVisible = RiderPluginsGodotGdscriptIcons.GodotEditor.GuiVisibilityVisible
            val GuiVisibilityVisibleDark = RiderPluginsGodotGdscriptIcons.GodotEditor.GuiVisibilityVisibleDark
            val GuiVisibilityHidden = RiderPluginsGodotGdscriptIcons.GodotEditor.GuiVisibilityHidden
            val GuiVisibilityHiddenDark = RiderPluginsGodotGdscriptIcons.GodotEditor.GuiVisibilityHiddenDark
            val TOOL_WINDOW_ICON = RiderPluginsGodotGdscriptIcons.TscnFile
        }
    }
}