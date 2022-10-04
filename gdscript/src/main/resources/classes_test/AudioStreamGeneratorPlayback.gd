#brief Plays back audio generated using [AudioStreamGenerator].
#desc This class is meant to be used with [AudioStreamGenerator] to play back the generated audio in real-time.
class_name AudioStreamGeneratorPlayback




#desc Returns [code]true[/code] if a buffer of the size [param amount] can be pushed to the audio sample data buffer without overflowing it, [code]false[/code] otherwise.
func can_push_buffer() -> bool:
	pass;

#desc Clears the audio sample data buffer.
func clear_buffer() -> void:
	pass;

#desc Returns the number of audio data frames left to play. If this returned number reaches [code]0[/code], the audio will stop playing until frames are added again. Therefore, make sure your script can always generate and push new audio frames fast enough to avoid audio cracking.
func get_frames_available() -> int:
	pass;

func get_skips() -> int:
	pass;

#desc Pushes several audio data frames to the buffer. This is usually more efficient than [method push_frame] in C# and compiled languages via GDExtension, but [method push_buffer] may be [i]less[/i] efficient in GDScript.
func push_buffer() -> bool:
	pass;

#desc Pushes a single audio data frame to the buffer. This is usually less efficient than [method push_buffer] in C# and compiled languages via GDExtension, but [method push_frame] may be [i]more[/i] efficient in GDScript.
func push_frame() -> bool:
	pass;


