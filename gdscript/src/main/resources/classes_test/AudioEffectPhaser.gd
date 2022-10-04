#brief Adds a phaser audio effect to an audio bus.
#brief Combines the original signal with a copy that is slightly out of phase with the original.
#desc Combines phase-shifted signals with the original signal. The movement of the phase-shifted signals is controlled using a low-frequency oscillator.
class_name AudioEffectPhaser


#desc Governs how high the filter frequencies sweep. Low value will primarily affect bass frequencies. High value can sweep high into the treble. Value can range from 0.1 to 4.
var depth: float;

#desc Output percent of modified sound. Value can range from 0.1 to 0.9.
var feedback: float;

#desc Determines the maximum frequency affected by the LFO modulations, in Hz. Value can range from 10 to 10000.
var range_max_hz: float;

#desc Determines the minimum frequency affected by the LFO modulations, in Hz. Value can range from 10 to 10000.
var range_min_hz: float;

#desc Adjusts the rate in Hz at which the effect sweeps up and down across the frequency range.
var rate_hz: float;




