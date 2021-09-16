extends AudioEffect
class_name AudioEffectDistortion
const MODE_CLIP = 0;
const MODE_ATAN = 1;
const MODE_LOFI = 2;
const MODE_OVERDRIVE = 3;
const MODE_WAVESHAPE = 4;

var drive: float;
var keep_hf_hz: float;
var mode: int;
var post_gain: float;
var pre_gain: float;

