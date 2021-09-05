extends AudioStreamPlaybackResampled
class_name AudioStreamGeneratorPlayback



func can_push_buffer(amount: int) -> bool:
    pass;

func clear_buffer() -> void:
    pass;

func get_frames_available() -> int:
    pass;

func get_skips() -> int:
    pass;

func push_buffer(frames: PackedVector2Array) -> bool:
    pass;

func push_frame(frame: Vector2) -> bool:
    pass;

