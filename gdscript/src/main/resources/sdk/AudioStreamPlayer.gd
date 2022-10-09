extends Node
#brief Plays back audio non-positionally.
#desc Plays an audio stream non-positionally.
#desc To play audio positionally, use [AudioStreamPlayer2D] or [AudioStreamPlayer3D] instead of [AudioStreamPlayer].
class_name AudioStreamPlayer

#desc The audio will be played only on the first channel.
const MIX_TARGET_STEREO = 0;

#desc The audio will be played on all surround channels.
const MIX_TARGET_SURROUND = 1;

#desc The audio will be played on the second channel, which is usually the center.
const MIX_TARGET_CENTER = 2;


#desc If [code]true[/code], audio plays when added to scene tree.
var autoplay: bool;

#desc Bus on which this audio is playing.
#desc [b]Note:[/b] When setting this property, keep in mind that no validation is performed to see if the given name matches an existing bus. This is because audio bus layouts might be loaded after this property is set. If this given name can't be resolved at runtime, it will fall back to [code]"Master"[/code].
var bus: StringName;

#desc The maximum number of sounds this node can play at the same time. Playing additional sounds after this value is reached will cut off the oldest sounds.
var max_polyphony: int;

#desc If the audio configuration has more than two speakers, this sets the target channels. See [enum MixTarget] constants.
var mix_target: int;

#desc The pitch and the tempo of the audio, as a multiplier of the audio sample's sample rate.
var pitch_scale: float;

#desc If [code]true[/code], audio is playing.
var playing: bool;

#desc The [AudioStream] object to be played.
var stream: AudioStream;

#desc If [code]true[/code], the playback is paused. You can resume it by setting [code]stream_paused[/code] to [code]false[/code].
var stream_paused: bool;

#desc Volume of sound, in dB.
var volume_db: float;



#desc Returns the position in the [AudioStream] in seconds.
func get_playback_position() -> float:
	pass;

#desc Returns the [AudioStreamPlayback] object associated with this [AudioStreamPlayer].
func get_stream_playback() -> AudioStreamPlayback:
	pass;

#desc Plays the audio from the given [param from_position], in seconds.
func play(from_position: float) -> void:
	pass;

#desc Sets the position from which audio will be played, in seconds.
func seek(to_position: float) -> void:
	pass;

#desc Stops the audio.
func stop() -> void:
	pass;


