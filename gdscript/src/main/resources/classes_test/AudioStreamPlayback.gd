#brief Meta class for playing back audio.
#desc Can play, loop, pause a scroll through audio. See [AudioStream] and [AudioStreamOggVorbis] for usage.
class_name AudioStreamPlayback




virtual const func _get_loop_count() -> int:
	pass;

virtual const func _get_playback_position() -> float:
	pass;

virtual const func _is_playing() -> bool:
	pass;

virtual func _mix(buffer: AudioFrame*, rate_scale: float, frames: int) -> int:
	pass;

virtual func _seek() -> void:
	pass;

virtual func _start() -> void:
	pass;

virtual func _stop() -> void:
	pass;

virtual func _tag_used_streams() -> void:
	pass;


