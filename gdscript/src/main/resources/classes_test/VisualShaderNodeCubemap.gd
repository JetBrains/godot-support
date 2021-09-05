extends VisualShaderNode
class_name VisualShaderNodeCubemap

const SOURCE_TEXTURE = 0;
const SOURCE_PORT = 1;
const TYPE_DATA = 0;
const TYPE_COLOR = 1;
const TYPE_NORMAL_MAP = 2;

var cube_map: Cubemap setget set_cube_map, get_cube_map;
var source: int setget set_source, get_source;
var texture_type: int setget set_texture_type, get_texture_type;

