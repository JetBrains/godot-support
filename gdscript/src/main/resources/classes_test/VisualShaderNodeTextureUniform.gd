extends VisualShaderNodeUniform
class_name VisualShaderNodeTextureUniform

const TYPE_DATA = 0;
const TYPE_COLOR = 1;
const TYPE_NORMAL_MAP = 2;
const TYPE_ANISO = 3;
const COLOR_DEFAULT_WHITE = 0;
const COLOR_DEFAULT_BLACK = 1;

var color_default: int setget set_color_default, get_color_default;
var texture_type: int setget set_texture_type, get_texture_type;

