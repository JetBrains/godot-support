extends AudioEffect
class_name AudioEffectSpectrumAnalyzer
const FFT_SIZE_256 = 0;
const FFT_SIZE_512 = 1;
const FFT_SIZE_1024 = 2;
const FFT_SIZE_2048 = 3;
const FFT_SIZE_4096 = 4;
const FFT_SIZE_MAX = 5;

var buffer_length: float;
var fft_size: int;
var tap_back_pos: float;

