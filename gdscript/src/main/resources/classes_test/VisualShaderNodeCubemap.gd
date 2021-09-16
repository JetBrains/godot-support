extends VisualShaderNode
class_name VisualShaderNodeCubemap
const SOURCE_TEXTURE = 0;
const SOURCE_PORT = 1;
const TYPE_DATA = 0;
const TYPE_COLOR = 1;
const TYPE_NORMAL_MAP = 2;

var cube_map: Cubemap;
var source: int;
var texture_type: int;

