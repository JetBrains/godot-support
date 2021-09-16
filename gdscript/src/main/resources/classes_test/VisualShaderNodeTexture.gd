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

var source: int;
var texture: Texture2D;
var texture_type: int;

