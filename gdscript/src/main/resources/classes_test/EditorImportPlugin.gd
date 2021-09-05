extends ResourceImporter
class_name EditorImportPlugin



func _get_import_options(preset: int) -> Array:
    pass;

func _get_import_order() -> int:
    pass;

func _get_importer_name() -> String:
    pass;

func _get_option_visibility(option: String, options: Dictionary) -> bool:
    pass;

func _get_preset_count() -> int:
    pass;

func _get_preset_name(preset: int) -> String:
    pass;

func _get_priority() -> float:
    pass;

func _get_recognized_extensions() -> Array:
    pass;

func _get_resource_type() -> String:
    pass;

func _get_save_extension() -> String:
    pass;

func _get_visible_name() -> String:
    pass;

func _import(source_file: String, save_path: String, options: Dictionary, platform_variants: Array, gen_files: Array) -> int:
    pass;

