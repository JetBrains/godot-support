extends VisualShaderNode
class_name VisualShaderNodeParticleEmit

const EMIT_FLAG_POSITION = 1;
const EMIT_FLAG_ROT_SCALE = 2;
const EMIT_FLAG_VELOCITY = 4;
const EMIT_FLAG_COLOR = 8;
const EMIT_FLAG_CUSTOM = 16;

var flags: int setget set_flags, get_flags;

