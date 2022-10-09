extends AudioEffect
#brief Adds a chorus audio effect.
#desc Adds a chorus audio effect. The effect applies a filter with voices to duplicate the audio source and manipulate it through the filter.
class_name AudioEffectChorus


#desc The effect's raw signal.
var dry: float;

#desc The voice's cutoff frequency.
var voice/1/cutoff_hz: float;

#desc The voice's signal delay.
var voice/1/delay_ms: float;

#desc The voice filter's depth.
var voice/1/depth_ms: float;

#desc The voice's volume.
var voice/1/level_db: float;

#desc The voice's pan level.
var voice/1/pan: float;

#desc The voice's filter rate.
var voice/1/rate_hz: float;

#desc The voice's cutoff frequency.
var voice/2/cutoff_hz: float;

#desc The voice's signal delay.
var voice/2/delay_ms: float;

#desc The voice filter's depth.
var voice/2/depth_ms: float;

#desc The voice's volume.
var voice/2/level_db: float;

#desc The voice's pan level.
var voice/2/pan: float;

#desc The voice's filter rate.
var voice/2/rate_hz: float;

#desc The voice's cutoff frequency.
var voice/3/cutoff_hz: float;

#desc The voice's signal delay.
var voice/3/delay_ms: float;

#desc The voice filter's depth.
var voice/3/depth_ms: float;

#desc The voice's volume.
var voice/3/level_db: float;

#desc The voice's pan level.
var voice/3/pan: float;

#desc The voice's filter rate.
var voice/3/rate_hz: float;

#desc The voice's cutoff frequency.
var voice/4/cutoff_hz: float;

#desc The voice's signal delay.
var voice/4/delay_ms: float;

#desc The voice filter's depth.
var voice/4/depth_ms: float;

#desc The voice's volume.
var voice/4/level_db: float;

#desc The voice's pan level.
var voice/4/pan: float;

#desc The voice's filter rate.
var voice/4/rate_hz: float;

#desc The number of voices in the effect.
var voice_count: int;

#desc The effect's processed signal.
var wet: float;



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


