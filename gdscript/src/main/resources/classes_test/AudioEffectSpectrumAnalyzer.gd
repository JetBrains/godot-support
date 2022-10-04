#brief Audio effect that can be used for real-time audio visualizations.
#desc This audio effect does not affect sound output, but can be used for real-time audio visualizations.
#desc See also [AudioStreamGenerator] for procedurally generating sounds.
class_name AudioEffectSpectrumAnalyzer

#desc Use a buffer of 256 samples for the Fast Fourier transform. Lowest latency, but least stable over time.
const FFT_SIZE_256 = 0;

#desc Use a buffer of 512 samples for the Fast Fourier transform. Low latency, but less stable over time.
const FFT_SIZE_512 = 1;

#desc Use a buffer of 1024 samples for the Fast Fourier transform. This is a compromise between latency and stability over time.
const FFT_SIZE_1024 = 2;

#desc Use a buffer of 2048 samples for the Fast Fourier transform. High latency, but stable over time.
const FFT_SIZE_2048 = 3;

#desc Use a buffer of 4096 samples for the Fast Fourier transform. Highest latency, but most stable over time.
const FFT_SIZE_4096 = 4;

#desc Represents the size of the [enum FFTSize] enum.
const FFT_SIZE_MAX = 5;


#desc The length of the buffer to keep (in seconds). Higher values keep data around for longer, but require more memory.
var buffer_length: float;

#desc The size of the [url=https://en.wikipedia.org/wiki/Fast_Fourier_transform]Fast Fourier transform[/url] buffer. Higher values smooth out the spectrum analysis over time, but have greater latency. The effects of this higher latency are especially noticeable with sudden amplitude changes.
var fft_size: int;

var tap_back_pos: float;




