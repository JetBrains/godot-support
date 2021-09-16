extends AudioEffect
class_name AudioEffectRecord

var format: int;

func get_recording() -> AudioStreamSample:
    pass;
func is_recording_active() -> bool:
    pass;
func set_recording_active(record: bool) -> void:
    pass;
