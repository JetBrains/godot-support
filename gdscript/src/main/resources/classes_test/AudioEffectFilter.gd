extends AudioEffect
class_name AudioEffectFilter
const FILTER_6DB = 0;
const FILTER_12DB = 1;
const FILTER_18DB = 2;
const FILTER_24DB = 3;

var cutoff_hz: float;
var db: int;
var gain: float;
var resonance: float;

