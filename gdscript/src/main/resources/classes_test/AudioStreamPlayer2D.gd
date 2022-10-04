extends Node2D
#brief Plays positional sound in 2D space.
#desc Plays audio that dampens with distance from a given position.
#desc By default, audio is heard from the screen center. This can be changed by adding an [AudioListener2D] node to the scene and enabling it by calling [method AudioListener2D.make_current] on it.
#desc See also [AudioStreamPlayer] to play a sound non-positionally.
#desc [b]Note:[/b] Hiding an [AudioStreamPlayer2D] node does not disable its audio output. To temporarily disable an [AudioStreamPlayer2D]'s audio output, set [member volume_db] to a very low value like [code]-100[/code] (which isn't audible to human hearing).
class_name AudioStreamPlayer2D


#desc Determines which [Area2D] layers affect the sound for reverb and audio bus effects. Areas can be used to redirect [AudioStream]s so that they play in a certain audio bus. An example of how you might use this is making a "water" area so that sounds played in the water are redirected through an audio bus to make them sound like they are being played underwater.
var area_mask: int;

#desc Dampens audio over distance with this as an exponent.
var attenuation: float;

#desc If [code]true[/code], audio plays when added to scene tree.
var autoplay: bool;

#desc Bus on which this audio is playing.
#desc [b]Note:[/b] When setting this property, keep in mind that no validation is performed to see if the given name matches an existing bus. This is because audio bus layouts might be loaded after this property is set. If this given name can't be resolved at runtime, it will fall back to [code]"Master"[/code].
var bus: StringName;

#desc Maximum distance from which audio is still hearable.
var max_distance: float;

#desc The maximum number of sounds this node can play at the same time. Playing additional sounds after this value is reached will cut off the oldest sounds.
var max_polyphony: int;

#desc Scales the panning strength for this node by multiplying the base [member ProjectSettings.audio/general/2d_panning_strength] with this factor. Higher values will pan audio from left to right more dramatically than lower values.
var panning_strength: float;

#desc The pitch and the tempo of the audio, as a multiplier of the audio sample's sample rate.
var pitch_scale: float;

#desc If [code]true[/code], audio is playing.
var playing: bool;

#desc The [AudioStream] object to be played.
var stream: AudioStream;

#desc If [code]true[/code], the playback is paused. You can resume it by setting [code]stream_paused[/code] to [code]false[/code].
var stream_paused: bool;

#desc Base volume without dampening.
var volume_db: float;



#desc Returns the position in the [AudioStream].
func get_playback_position() -> float:
	pass;

#desc Returns the [AudioStreamPlayback] object associated with this [AudioStreamPlayer2D].
func get_stream_playback() -> AudioStreamPlayback:
	pass;

#desc Plays the audio from the given position [param from_position], in seconds.
func play(from_position: float) -> void:
	pass;

#desc Sets the position from which audio will be played, in seconds.
func seek(to_position: float) -> void:
	pass;

#desc Stops the audio.
func stop() -> void:
	pass;


