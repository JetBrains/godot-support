class_name AudioStreamPlaybackResampled




virtual const func _get_stream_sampling_rate() -> float:
	pass;

virtual func _mix_resampled(dst_buffer: AudioFrame*, frame_count: int) -> int:
	pass;

func begin_resample() -> void:
	pass;


