extends RefCounted
class_name AudioEffectInstance




func _process(src_buffer: const void*, dst_buffer: AudioFrame*, frame_count: int) -> void:
	pass;

func _process_silence() -> bool:
	pass;


