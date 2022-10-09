extends RefCounted
#brief Meta class for playing back audio.
#desc Can play, loop, pause a scroll through audio. See [AudioStream] and [AudioStreamOggVorbis] for usage.
class_name AudioStreamPlayback




func _get_loop_count() -> int:
	pass;

func _get_playback_position() -> float:
	pass;

func _is_playing() -> bool:
	pass;

func _mix(buffer: AudioFrame*, rate_scale: float, frames: int) -> int:
	pass;

func _seek(position: float) -> void:
	pass;

func _start(from_pos: float) -> void:
	pass;

func _stop() -> void:
	pass;

func _tag_used_streams() -> void:
	pass;


