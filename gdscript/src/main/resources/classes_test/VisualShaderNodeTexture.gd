extends VisualShaderNode
class_name VisualShaderNodeTexture

const SOURCE_TEXTURE = 0;
const SOURCE_SCREEN = 1;
const SOURCE_2D_TEXTURE = 2;
const SOURCE_2D_NORMAL = 3;
const SOURCE_DEPTH = 4;
const SOURCE_PORT = 5;
const TYPE_DATA = 0;
const TYPE_COLOR = 1;
const TYPE_NORMAL_MAP = 2;

var source: int setget set_source, get_source;
var texture: Texture2D setget set_texture, get_texture;
var texture_type: int setget set_texture_type, get_texture_type;

