extends RefCounted
#brief A script that is executed when exporting the project.
#desc [EditorExportPlugin]s are automatically invoked whenever the user exports the project. Their most common use is to determine what files are being included in the exported project. For each plugin, [method _export_begin] is called at the beginning of the export process and then [method _export_file] is called for each exported file.
#desc To use [EditorExportPlugin], register it using the [method EditorPlugin.add_export_plugin] method first.
class_name EditorExportPlugin




#desc Return true if this plugin will customize resources based on the platform and features used.
func _begin_customize_resources(platform: EditorExportPlatform, features: PackedStringArray) -> bool:
	pass;

#desc Return true if this plugin will customize scenes based on the platform and features used.
func _begin_customize_scenes(platform: EditorExportPlatform, features: PackedStringArray) -> bool:
	pass;

#desc Customize a resource. If changes are made to it, return the same or a new resource. Otherwise, return [code]null[/code].
#desc The [i]path[/i] argument is only used when customizing an actual file, otherwise this means that this resource is part of another one and it will be empty.
func _customize_resource(resource: Resource, path: String) -> Resource:
	pass;

#desc Customize a scene. If changes are made to it, return the same or a new scene. Otherwise, return [code]null[/code]. If a new scene is returned, it is up to you to dispose of the old one.
func _customize_scene(scene: Node, path: String) -> Node:
	pass;

#desc This is called when the customization process for resources ends.
func _end_customize_resources() -> void:
	pass;

#desc This is called when the customization process for scenes ends.
func _end_customize_scenes() -> void:
	pass;

#desc Virtual method to be overridden by the user. It is called when the export starts and provides all information about the export. [param features] is the list of features for the export, [param is_debug] is [code]true[/code] for debug builds, [param path] is the target path for the exported project. [param flags] is only used when running a runnable profile, e.g. when using native run on Android.
func _export_begin(features: PackedStringArray, is_debug: bool, path: String, flags: int) -> void:
	pass;

#desc Virtual method to be overridden by the user. Called when the export is finished.
func _export_end() -> void:
	pass;

#desc Virtual method to be overridden by the user. Called for each exported file, providing arguments that can be used to identify the file. [param path] is the path of the file, [param type] is the [Resource] represented by the file (e.g. [PackedScene]) and [param features] is the list of features for the export.
#desc Calling [method skip] inside this callback will make the file not included in the export.
func _export_file(path: String, type: String, features: PackedStringArray) -> void:
	pass;

#desc Return a hash based on the configuration passed (for both scenes and resources). This helps keep separate caches for separate export configurations.
func _get_customization_configuration_hash() -> int:
	pass;

#desc Return the name identifier of this plugin (for future identification by the exporter).
func _get_name() -> String:
	pass;

#desc Adds a custom file to be exported. [param path] is the virtual path that can be used to load the file, [param file] is the binary data of the file. If [param remap] is [code]true[/code], file will not be exported, but instead remapped to the given [param path].
func add_file(path: String, file: PackedByteArray, remap: bool) -> void:
	pass;

#desc Adds an iOS bundle file from the given [param path] to the exported project.
func add_ios_bundle_file(path: String) -> void:
	pass;

#desc Adds a C++ code to the iOS export. The final code is created from the code appended by each active export plugin.
func add_ios_cpp_code(code: String) -> void:
	pass;

#desc Adds a dynamic library (*.dylib, *.framework) to Linking Phase in iOS's Xcode project and embeds it into resulting binary.
#desc [b]Note:[/b] For static libraries (*.a) works in same way as [code]add_ios_framework[/code].
#desc This method should not be used for System libraries as they are already present on the device.
func add_ios_embedded_framework(path: String) -> void:
	pass;

#desc Adds a static library (*.a) or dynamic library (*.dylib, *.framework) to Linking Phase in iOS's Xcode project.
func add_ios_framework(path: String) -> void:
	pass;

#desc Adds linker flags for the iOS export.
func add_ios_linker_flags(flags: String) -> void:
	pass;

#desc Adds content for iOS Property List files.
func add_ios_plist_content(plist_content: String) -> void:
	pass;

#desc Adds a static lib from the given [param path] to the iOS project.
func add_ios_project_static_lib(path: String) -> void:
	pass;

#desc Adds file or directory matching [param path] to [code]PlugIns[/code] directory of macOS app bundle.
#desc [b]Note:[/b] This is useful only for macOS exports.
func add_macos_plugin_file(path: String) -> void:
	pass;

#desc Adds a shared object or a directory containing only shared objects with the given [param tags] and destination [param path].
#desc [b]Note:[/b] In case of macOS exports, those shared objects will be added to [code]Frameworks[/code] directory of app bundle.
#desc In case of a directory code-sign will error if you place non code object in directory.
func add_shared_object(path: String, tags: PackedStringArray, target: String) -> void:
	pass;

#desc To be called inside [method _export_file]. Skips the current file, so it's not included in the export.
func skip() -> void:
	pass;


