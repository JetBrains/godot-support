extends RefCounted
class_name EditorSceneImporter
const IMPORT_SCENE = 1;
const IMPORT_ANIMATION = 2;
const IMPORT_FAIL_ON_MISSING_DEPENDENCIES = 4;
const IMPORT_GENERATE_TANGENT_ARRAYS = 8;
const IMPORT_USE_NAMED_SKIN_BINDS = 16;


func _get_extensions() -> Array:
    pass;
func _get_import_flags() -> int:
    pass;
func _import_animation(path: String, flags: int, bake_fps: int) -> Animation:
    pass;
func _import_scene(path: String, flags: int, bake_fps: int) -> Node:
    pass;
func import_animation_from_other_importer(path: String, flags: int, bake_fps: int) -> Animation:
    pass;
func import_scene_from_other_importer(path: String, flags: int, bake_fps: int) -> Node:
    pass;
