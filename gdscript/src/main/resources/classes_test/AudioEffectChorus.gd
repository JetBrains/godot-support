extends AudioEffect
class_name AudioEffectChorus


var dry: float setget set_dry, get_dry;
var voice/1/cutoff_hz: float setget set_voice_cutoff_hz, get_voice_cutoff_hz;
var voice/1/delay_ms: float setget set_voice_delay_ms, get_voice_delay_ms;
var voice/1/depth_ms: float setget set_voice_depth_ms, get_voice_depth_ms;
var voice/1/level_db: float setget set_voice_level_db, get_voice_level_db;
var voice/1/pan: float setget set_voice_pan, get_voice_pan;
var voice/1/rate_hz: float setget set_voice_rate_hz, get_voice_rate_hz;
var voice/2/cutoff_hz: float setget set_voice_cutoff_hz, get_voice_cutoff_hz;
var voice/2/delay_ms: float setget set_voice_delay_ms, get_voice_delay_ms;
var voice/2/depth_ms: float setget set_voice_depth_ms, get_voice_depth_ms;
var voice/2/level_db: float setget set_voice_level_db, get_voice_level_db;
var voice/2/pan: float setget set_voice_pan, get_voice_pan;
var voice/2/rate_hz: float setget set_voice_rate_hz, get_voice_rate_hz;
var voice/3/cutoff_hz: float setget set_voice_cutoff_hz, get_voice_cutoff_hz;
var voice/3/delay_ms: float setget set_voice_delay_ms, get_voice_delay_ms;
var voice/3/depth_ms: float setget set_voice_depth_ms, get_voice_depth_ms;
var voice/3/level_db: float setget set_voice_level_db, get_voice_level_db;
var voice/3/pan: float setget set_voice_pan, get_voice_pan;
var voice/3/rate_hz: float setget set_voice_rate_hz, get_voice_rate_hz;
var voice/4/cutoff_hz: float setget set_voice_cutoff_hz, get_voice_cutoff_hz;
var voice/4/delay_ms: float setget set_voice_delay_ms, get_voice_delay_ms;
var voice/4/depth_ms: float setget set_voice_depth_ms, get_voice_depth_ms;
var voice/4/level_db: float setget set_voice_level_db, get_voice_level_db;
var voice/4/pan: float setget set_voice_pan, get_voice_pan;
var voice/4/rate_hz: float setget set_voice_rate_hz, get_voice_rate_hz;
var voice_count: int setget set_voice_count, get_voice_count;
var wet: float setget set_wet, get_wet;

func get_voice_cutoff_hz(voice_idx: int) -> float:
    pass;

func get_voice_delay_ms(voice_idx: int) -> float:
    pass;

func get_voice_depth_ms(voice_idx: int) -> float:
    pass;

func get_voice_level_db(voice_idx: int) -> float:
    pass;

func get_voice_pan(voice_idx: int) -> float:
    pass;

func get_voice_rate_hz(voice_idx: int) -> float:
    pass;

func set_voice_cutoff_hz(voice_idx: int, cutoff_hz: float) -> void:
    pass;

func set_voice_delay_ms(voice_idx: int, delay_ms: float) -> void:
    pass;

func set_voice_depth_ms(voice_idx: int, depth_ms: float) -> void:
    pass;

func set_voice_level_db(voice_idx: int, level_db: float) -> void:
    pass;

func set_voice_pan(voice_idx: int, pan: float) -> void:
    pass;

func set_voice_rate_hz(voice_idx: int, rate_hz: float) -> void:
    pass;

