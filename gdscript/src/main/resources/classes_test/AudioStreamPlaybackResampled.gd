extends AudioStreamPlayback
class_name AudioStreamPlaybackResampled




func _get_stream_sampling_rate() -> float:
	pass;

func _mix_resampled(dst_buffer: AudioFrame*, frame_count: int) -> int:
	pass;

func begin_resample() -> void:
	pass;


