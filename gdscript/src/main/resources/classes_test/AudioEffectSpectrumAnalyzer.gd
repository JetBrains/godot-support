extends AudioEffect
class_name AudioEffectSpectrumAnalyzer

const FFT_SIZE_256 = 0;
const FFT_SIZE_512 = 1;
const FFT_SIZE_1024 = 2;
const FFT_SIZE_2048 = 3;
const FFT_SIZE_4096 = 4;
const FFT_SIZE_MAX = 5;

var buffer_length: float setget set_buffer_length, get_buffer_length;
var fft_size: int setget set_fft_size, get_fft_size;
var tap_back_pos: float setget set_tap_back_pos, get_tap_back_pos;

