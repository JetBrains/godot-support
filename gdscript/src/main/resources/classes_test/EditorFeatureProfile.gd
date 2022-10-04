#brief An editor feature profile which can be used to disable specific features.
#desc An editor feature profile can be used to disable specific features of the Godot editor. When disabled, the features won't appear in the editor, which makes the editor less cluttered. This is useful in education settings to reduce confusion or when working in a team. For example, artists and level designers could use a feature profile that disables the script editor to avoid accidentally making changes to files they aren't supposed to edit.
#desc To manage editor feature profiles visually, use [b]Editor > Manage Feature Profiles...[/b] at the top of the editor window.
class_name EditorFeatureProfile

#desc The 3D editor. If this feature is disabled, the 3D editor won't display but 3D nodes will still display in the Create New Node dialog.
const FEATURE_3D = 0;

#desc The Script tab, which contains the script editor and class reference browser. If this feature is disabled, the Script tab won't display.
const FEATURE_SCRIPT = 1;

#desc The AssetLib tab. If this feature is disabled, the AssetLib tab won't display.
const FEATURE_ASSET_LIB = 2;

#desc Scene tree editing. If this feature is disabled, the Scene tree dock will still be visible but will be read-only.
const FEATURE_SCENE_TREE = 3;

#desc The Node dock. If this feature is disabled, signals and groups won't be visible and modifiable from the editor.
const FEATURE_NODE_DOCK = 4;

#desc The FileSystem dock. If this feature is disabled, the FileSystem dock won't be visible.
const FEATURE_FILESYSTEM_DOCK = 5;

#desc The Import dock. If this feature is disabled, the Import dock won't be visible.
const FEATURE_IMPORT_DOCK = 6;

#desc Represents the size of the [enum Feature] enum.
const FEATURE_MAX = 7;




#desc Returns the specified [param feature]'s human-readable name.
func get_feature_name(feature: int) -> String:
	pass;

#desc Returns [code]true[/code] if the class specified by [param class_name] is disabled. When disabled, the class won't appear in the Create New Node dialog.
func is_class_disabled(class_name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if editing for the class specified by [param class_name] is disabled. When disabled, the class will still appear in the Create New Node dialog but the inspector will be read-only when selecting a node that extends the class.
func is_class_editor_disabled(class_name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if [param property] is disabled in the class specified by [param class_name]. When a property is disabled, it won't appear in the inspector when selecting a node that extends the class specified by [param class_name].
func is_class_property_disabled(class_name: StringName, property: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the [param feature] is disabled. When a feature is disabled, it will disappear from the editor entirely.
func is_feature_disabled(feature: int) -> bool:
	pass;

#desc Loads an editor feature profile from a file. The file must follow the JSON format obtained by using the feature profile manager's [b]Export[/b] button or the [method save_to_file] method.
func load_from_file(path: String) -> int:
	pass;

#desc Saves the editor feature profile to a file in JSON format. It can then be imported using the feature profile manager's [b]Import[/b] button or the [method load_from_file] method.
func save_to_file(path: String) -> int:
	pass;

#desc If [param disable] is [code]true[/code], disables the class specified by [param class_name]. When disabled, the class won't appear in the Create New Node dialog.
func set_disable_class(class_name: StringName, disable: bool) -> void:
	pass;

#desc If [param disable] is [code]true[/code], disables editing for the class specified by [param class_name]. When disabled, the class will still appear in the Create New Node dialog but the inspector will be read-only when selecting a node that extends the class.
func set_disable_class_editor(class_name: StringName, disable: bool) -> void:
	pass;

#desc If [param disable] is [code]true[/code], disables editing for [param property] in the class specified by [param class_name]. When a property is disabled, it won't appear in the inspector when selecting a node that extends the class specified by [param class_name].
func set_disable_class_property(class_name: StringName, property: StringName, disable: bool) -> void:
	pass;

#desc If [param disable] is [code]true[/code], disables the editor feature specified in [param feature]. When a feature is disabled, it will disappear from the editor entirely.
func set_disable_feature(feature: int, disable: bool) -> void:
	pass;


