extends AudioEffect
class_name AudioEffectCapture


var buffer_length: float setget set_buffer_length, get_buffer_length;

func can_get_buffer(frames: int) -> bool:
    pass;

func clear_buffer() -> void:
    pass;

func get_buffer(frames: int) -> PackedVector2Array:
    pass;

func get_buffer_length_frames() -> int:
    pass;

func get_discarded_frames() -> int:
    pass;

func get_frames_available() -> int:
    pass;

func get_pushed_frames() -> int:
    pass;

