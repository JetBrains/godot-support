extends AudioEffect
class_name AudioEffectDelay


var dry: float setget set_dry, get_dry;
var feedback/active: bool setget set_feedback_active, is_feedback_active;
var feedback/delay_ms: float setget set_feedback_delay_ms, get_feedback_delay_ms;
var feedback/level_db: float setget set_feedback_level_db, get_feedback_level_db;
var feedback/lowpass: float setget set_feedback_lowpass, get_feedback_lowpass;
var tap1/active: bool setget set_tap1_active, is_tap1_active;
var tap1/delay_ms: float setget set_tap1_delay_ms, get_tap1_delay_ms;
var tap1/level_db: float setget set_tap1_level_db, get_tap1_level_db;
var tap1/pan: float setget set_tap1_pan, get_tap1_pan;
var tap2/active: bool setget set_tap2_active, is_tap2_active;
var tap2/delay_ms: float setget set_tap2_delay_ms, get_tap2_delay_ms;
var tap2/level_db: float setget set_tap2_level_db, get_tap2_level_db;
var tap2/pan: float setget set_tap2_pan, get_tap2_pan;

