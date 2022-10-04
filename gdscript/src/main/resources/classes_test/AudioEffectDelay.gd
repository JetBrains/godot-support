#brief Adds a delay audio effect to an audio bus. Plays input signal back after a period of time.
#brief Two tap delay and feedback options.
#desc Plays input signal back after a period of time. The delayed signal may be played back multiple times to create the sound of a repeating, decaying echo. Delay effects range from a subtle echo effect to a pronounced blending of previous sounds with new sounds.
class_name AudioEffectDelay


#desc Output percent of original sound. At 0, only delayed sounds are output. Value can range from 0 to 1.
var dry: float;

#desc If [code]true[/code], feedback is enabled.
var feedback_active: bool;

#desc Feedback delay time in milliseconds.
var feedback_delay_ms: float;

#desc Sound level for [code]tap1[/code].
var feedback_level_db: float;

#desc Low-pass filter for feedback, in Hz. Frequencies below this value are filtered out of the source signal.
var feedback_lowpass: float;

#desc If [code]true[/code], [code]tap1[/code] will be enabled.
var tap1_active: bool;

#desc [code]tap1[/code] delay time in milliseconds.
var tap1_delay_ms: float;

#desc Sound level for [code]tap1[/code].
var tap1_level_db: float;

#desc Pan position for [code]tap1[/code]. Value can range from -1 (fully left) to 1 (fully right).
var tap1_pan: float;

#desc If [code]true[/code], [code]tap2[/code] will be enabled.
var tap2_active: bool;

#desc [b]Tap2[/b] delay time in milliseconds.
var tap2_delay_ms: float;

#desc Sound level for [code]tap2[/code].
var tap2_level_db: float;

#desc Pan position for [code]tap2[/code]. Value can range from -1 (fully left) to 1 (fully right).
var tap2_pan: float;




