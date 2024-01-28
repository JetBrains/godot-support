package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.util.IconLoader

class GodotIcons {
    class Icons {
        companion object {
            val GodotLogo = IconLoader.getIcon("resharper/GodotLogo/Godot.svg", GodotIcons::class.java)
            val GodotLogoDisconnected = IconLoader.getIcon("/Icons/GodotDisconnected.svg", GodotIcons::class.java)
        }
    }

    class Actions {
        companion object {
            @JvmField val StartGodotEditorActionIcon = Icons.GodotLogo
        }
    }

    class RunConfigurations {
        companion object {
            val StartAndDebug = Icons.GodotLogo
        }
    }
}