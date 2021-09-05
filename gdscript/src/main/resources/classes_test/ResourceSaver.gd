extends Object
class_name ResourceSaver

const FLAG_RELATIVE_PATHS = 1;
const FLAG_BUNDLE_RESOURCES = 2;
const FLAG_CHANGE_PATH = 4;
const FLAG_OMIT_EDITOR_PROPERTIES = 8;
const FLAG_SAVE_BIG_ENDIAN = 16;
const FLAG_COMPRESS = 32;
const FLAG_REPLACE_SUBRESOURCE_PATHS = 64;


func get_recognized_extensions(type: Resource) -> PackedStringArray:
    pass;

func save(path: String, resource: Resource, flags: int) -> int:
    pass;

