extends AudioEffect
#brief Adds a pitch-shifting audio effect to an audio bus.
#brief Raises or lowers the pitch of original sound.
#desc Allows modulation of pitch independently of tempo. All frequencies can be increased/decreased with minimal effect on transients.
class_name AudioEffectPitchShift

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


#desc The size of the [url=https://en.wikipedia.org/wiki/Fast_Fourier_transform]Fast Fourier transform[/url] buffer. Higher values smooth out the effect over time, but have greater latency. The effects of this higher latency are especially noticeable on sounds that have sudden amplitude changes.
var fft_size: int;

#desc The oversampling factor to use. Higher values result in better quality, but are more demanding on the CPU and may cause audio cracking if the CPU can't keep up.
var oversampling: int;

#desc The pitch scale to use. [code]1.0[/code] is the default pitch and plays sounds unaltered. [member pitch_scale] can range from [code]0.0[/code] (infinitely low pitch, inaudible) to [code]16[/code] (16 times higher than the initial pitch).
var pitch_scale: float;




