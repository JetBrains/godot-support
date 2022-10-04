#brief Imports scenes from third-parties' 3D files.
#desc [EditorSceneFormatImporter] allows to define an importer script for a third-party 3D format.
#desc To use [EditorSceneFormatImporter], register it using the [method EditorPlugin.add_scene_format_importer_plugin] method first.
class_name EditorSceneFormatImporter

const IMPORT_SCENE = 1;

const IMPORT_ANIMATION = 2;

const IMPORT_FAIL_ON_MISSING_DEPENDENCIES = 4;

const IMPORT_GENERATE_TANGENT_ARRAYS = 8;

const IMPORT_USE_NAMED_SKIN_BINDS = 16;

const IMPORT_DISCARD_MESHES_AND_MATERIALS = 32;




virtual const func _get_extensions() -> PackedStringArray:
	pass;

virtual const func _get_import_flags() -> int:
	pass;

virtual func _get_import_options() -> void:
	pass;

virtual const func _get_option_visibility(path: String, for_animation: bool, option: String) -> Variant:
	pass;

virtual func _import_scene(path: String, flags: int, options: Dictionary, bake_fps: int) -> Object:
	pass;


