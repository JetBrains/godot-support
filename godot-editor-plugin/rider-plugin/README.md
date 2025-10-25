Provide a GodotEditor plugin to simplify setting external editor to JetBrains Rider

Which may help with setting up Godot - Rider env.
Available at https://godotengine.org/asset-library/asset

Features:

Quick toggle to switch external editor on-off

Set the recommended Godot setting for optimal integration with Rider as your external editor. To access these settings, make sure that the Advanced Settings switch is enabled at the top of the Editor Settings dialog.

 - Editor Settings | Text Editor | Behavior | Auto Reload Scripts on External Change
 - Editor Settings | Interface | Editor | Save on Focus Loss
 - Editor Settings | Interface | Editor | Import Resources When Unfocused

Auto-find and set path to Godot as external editor

Development

- rider-plugin.sln
	- Solution that groups the C++ projects and lists all Configuration|Platform pairs shown in the Solution Configuration selector.

- gdext.vcxproj
	- GDExtension C++ project. Rider reads compiler options/defines/includes and also calls scons to build the extension library (.dll/.so/.dylib).
