#brief Audio effect used for recording the sound from an audio bus.
#desc Allows the user to record the sound from an audio bus. This can include all audio output by Godot when used on the "Master" audio bus.
#desc Can be used (with an [AudioStreamMicrophone]) to record from a microphone.
#desc It sets and gets the format in which the audio file will be recorded (8-bit, 16-bit, or compressed). It checks whether or not the recording is active, and if it is, records the sound. It then returns the recorded sample.
class_name AudioEffectRecord


#desc Specifies the format in which the sample will be recorded. See [enum AudioStreamWAV.Format] for available formats.
var format: int;



#desc Returns the recorded sample.
func get_recording() -> AudioStreamWAV:
	pass;

#desc Returns whether the recording is active or not.
func is_recording_active() -> bool:
	pass;

#desc If [code]true[/code], the sound will be recorded. Note that restarting the recording will remove the previously recorded sample.
func set_recording_active(record: bool) -> void:
	pass;


