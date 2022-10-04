#brief Adds a filter to the audio bus.
#desc Allows frequencies other than the [member cutoff_hz] to pass.
class_name AudioEffectFilter

const FILTER_6DB = 0;

const FILTER_12DB = 1;

const FILTER_18DB = 2;

const FILTER_24DB = 3;


#desc Threshold frequency for the filter, in Hz.
var cutoff_hz: float;

var db: int;

#desc Gain amount of the frequencies after the filter.
var gain: float;

#desc Amount of boost in the frequency range near the cutoff frequency.
var resonance: float;




