GdExtension part of the GodotEditor plugin for JetBrains Rider integration

Features:

Finds Rider installations in the system

Development

- rider-plugin.sln
	- Solution that groups the C++ projects and lists all Configuration|Platform pairs shown in the Solution Configuration selector.

- gdext.vcxproj
	- GDExtension C++ project. Rider reads compiler options/defines/includes and also calls scons to build the extension library (.dll/.so/.dylib).
