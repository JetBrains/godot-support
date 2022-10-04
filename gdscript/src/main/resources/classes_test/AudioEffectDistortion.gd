extends AudioEffect
#brief Adds a distortion audio effect to an Audio bus.
#brief Modify the sound to make it distorted.
#desc Different types are available: clip, tan, lo-fi (bit crushing), overdrive, or waveshape.
#desc By distorting the waveform the frequency content change, which will often make the sound "crunchy" or "abrasive". For games, it can simulate sound coming from some saturated device or speaker very efficiently.
class_name AudioEffectDistortion

#desc Digital distortion effect which cuts off peaks at the top and bottom of the waveform.
const MODE_CLIP = 0;

const MODE_ATAN = 1;

#desc Low-resolution digital distortion effect. You can use it to emulate the sound of early digital audio devices.
const MODE_LOFI = 2;

#desc Emulates the warm distortion produced by a field effect transistor, which is commonly used in solid-state musical instrument amplifiers.
const MODE_OVERDRIVE = 3;

#desc Waveshaper distortions are used mainly by electronic musicians to achieve an extra-abrasive sound.
const MODE_WAVESHAPE = 4;


#desc Distortion power. Value can range from 0 to 1.
var drive: float;

#desc High-pass filter, in Hz. Frequencies higher than this value will not be affected by the distortion. Value can range from 1 to 20000.
var keep_hf_hz: float;

#desc Distortion type.
var mode: int;

#desc Increases or decreases the volume after the effect. Value can range from -80 to 24.
var post_gain: float;

#desc Increases or decreases the volume before the effect. Value can range from -60 to 60.
var pre_gain: float;




