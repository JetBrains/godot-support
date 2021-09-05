extends VisualShaderNode
class_name VisualShaderNodeBillboard

const BILLBOARD_TYPE_DISABLED = 0;
const BILLBOARD_TYPE_ENABLED = 1;
const BILLBOARD_TYPE_FIXED_Y = 2;
const BILLBOARD_TYPE_PARTICLES = 3;
const BILLBOARD_TYPE_MAX = 4;

var billboard_type: int setget set_billboard_type, get_billboard_type;
var keep_scale: bool setget set_keep_scale_enabled, is_keep_scale_enabled;

