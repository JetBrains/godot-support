extends AudioEffect
#brief Adds a reverberation audio effect to an Audio bus.
#desc Simulates the sound of acoustic environments such as rooms, concert halls, caverns, or an open spaces.
class_name AudioEffectReverb


#desc Defines how reflective the imaginary room's walls are. Value can range from 0 to 1.
var damping: float;

#desc Output percent of original sound. At 0, only modified sound is outputted. Value can range from 0 to 1.
var dry: float;

#desc High-pass filter passes signals with a frequency higher than a certain cutoff frequency and attenuates signals with frequencies lower than the cutoff frequency. Value can range from 0 to 1.
var hipass: float;

#desc Output percent of predelay. Value can range from 0 to 1.
var predelay_feedback: float;

#desc Time between the original signal and the early reflections of the reverb signal, in milliseconds.
var predelay_msec: float;

#desc Dimensions of simulated room. Bigger means more echoes. Value can range from 0 to 1.
var room_size: float;

#desc Widens or narrows the stereo image of the reverb tail. 1 means fully widens. Value can range from 0 to 1.
var spread: float;

#desc Output percent of modified sound. At 0, only original sound is outputted. Value can range from 0 to 1.
var wet: float;




