extends AudioEffect
class_name AudioEffectPitchShift

const FFT_SIZE_256 = 0;
const FFT_SIZE_512 = 1;
const FFT_SIZE_1024 = 2;
const FFT_SIZE_2048 = 3;
const FFT_SIZE_4096 = 4;
const FFT_SIZE_MAX = 5;

var fft_size: int setget set_fft_size, get_fft_size;
var oversampling: int setget set_oversampling, get_oversampling;
var pitch_scale: float setget set_pitch_scale, get_pitch_scale;

