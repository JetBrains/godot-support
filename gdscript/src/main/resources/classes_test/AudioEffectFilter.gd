extends AudioEffect
class_name AudioEffectFilter

const FILTER_6DB = 0;
const FILTER_12DB = 1;
const FILTER_18DB = 2;
const FILTER_24DB = 3;

var cutoff_hz: float setget set_cutoff, get_cutoff;
var db: int setget set_db, get_db;
var gain: float setget set_gain, get_gain;
var resonance: float setget set_resonance, get_resonance;

