extends Texture2D
class_name CurveTexture

const TEXTURE_MODE_RGB = 0;
const TEXTURE_MODE_RED = 1;

var curve: Curve setget set_curve, get_curve;
var texture_mode: int setget set_texture_mode, get_texture_mode;
var width: int setget set_width, get_width;

