extends AudioEffect
class_name AudioEffectDistortion

const MODE_CLIP = 0;
const MODE_ATAN = 1;
const MODE_LOFI = 2;
const MODE_OVERDRIVE = 3;
const MODE_WAVESHAPE = 4;

var drive: float setget set_drive, get_drive;
var keep_hf_hz: float setget set_keep_hf_hz, get_keep_hf_hz;
var mode: int setget set_mode, get_mode;
var post_gain: float setget set_post_gain, get_post_gain;
var pre_gain: float setget set_pre_gain, get_pre_gain;

