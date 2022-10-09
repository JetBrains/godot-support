extends Resource
#brief Base class for audio streams.
#desc Base class for audio streams. Audio streams are used for sound effects and music playback, and support WAV (via [AudioStreamWAV]) and Ogg (via [AudioStreamOggVorbis]) file formats.
class_name AudioStream




func _get_beat_count() -> int:
	pass;

func _get_bpm() -> float:
	pass;

func _get_length() -> float:
	pass;

func _get_stream_name() -> String:
	pass;

func _instantiate_playback() -> AudioStreamPlayback:
	pass;

func _is_monophonic() -> bool:
	pass;

#desc Returns the length of the audio stream in seconds.
func get_length() -> float:
	pass;

#desc Returns an AudioStreamPlayback. Useful for when you want to extend [method _instantiate_playback] but call [method instantiate_playback] from an internally held AudioStream subresource. An example of this can be found in the source files for [code]AudioStreamRandomPitch::instantiate_playback[/code].
func instantiate_playback() -> AudioStreamPlayback:
	pass;

#desc Returns true if this audio stream only supports monophonic playback, or false if the audio stream supports polyphony.
func is_monophonic() -> bool:
	pass;


