#brief Plays positional sound in 3D space.
#desc Plays a sound effect with directed sound effects, dampens with distance if needed, generates effect of hearable position in space. For greater realism, a low-pass filter is automatically applied to distant sounds. This can be disabled by setting [member attenuation_filter_cutoff_hz] to [code]20500[/code].
#desc By default, audio is heard from the camera position. This can be changed by adding an [AudioListener3D] node to the scene and enabling it by calling [method AudioListener3D.make_current] on it.
#desc See also [AudioStreamPlayer] to play a sound non-positionally.
#desc [b]Note:[/b] Hiding an [AudioStreamPlayer3D] node does not disable its audio output. To temporarily disable an [AudioStreamPlayer3D]'s audio output, set [member unit_db] to a very low value like [code]-100[/code] (which isn't audible to human hearing).
class_name AudioStreamPlayer3D

#desc Linear dampening of loudness according to distance.
const ATTENUATION_INVERSE_DISTANCE = 0;

#desc Squared dampening of loudness according to distance.
const ATTENUATION_INVERSE_SQUARE_DISTANCE = 1;

#desc Logarithmic dampening of loudness according to distance.
const ATTENUATION_LOGARITHMIC = 2;

#desc No dampening of loudness according to distance. The sound will still be heard positionally, unlike an [AudioStreamPlayer]. [constant ATTENUATION_DISABLED] can be combined with a [member max_distance] value greater than [code]0.0[/code] to achieve linear attenuation clamped to a sphere of a defined size.
const ATTENUATION_DISABLED = 3;

#desc Disables doppler tracking.
const DOPPLER_TRACKING_DISABLED = 0;

#desc Executes doppler tracking in idle step.
const DOPPLER_TRACKING_IDLE_STEP = 1;

#desc Executes doppler tracking in physics step.
const DOPPLER_TRACKING_PHYSICS_STEP = 2;


#desc Determines which [Area3D] layers affect the sound for reverb and audio bus effects. Areas can be used to redirect [AudioStream]s so that they play in a certain audio bus. An example of how you might use this is making a "water" area so that sounds played in the water are redirected through an audio bus to make them sound like they are being played underwater.
var area_mask: int;

#desc Dampens audio using a low-pass filter above this frequency, in Hz. To disable the dampening effect entirely, set this to [code]20500[/code] as this frequency is above the human hearing limit.
var attenuation_filter_cutoff_hz: float;

#desc Amount how much the filter affects the loudness, in decibels.
var attenuation_filter_db: float;

#desc Decides if audio should get quieter with distance linearly, quadratically, logarithmically, or not be affected by distance, effectively disabling attenuation.
var attenuation_model: int;

#desc If [code]true[/code], audio plays when the AudioStreamPlayer3D node is added to scene tree.
var autoplay: bool;

#desc The bus on which this audio is playing.
#desc [b]Note:[/b] When setting this property, keep in mind that no validation is performed to see if the given name matches an existing bus. This is because audio bus layouts might be loaded after this property is set. If this given name can't be resolved at runtime, it will fall back to [code]"Master"[/code].
var bus: StringName;

#desc Decides in which step the Doppler effect should be calculated.
var doppler_tracking: int;

#desc The angle in which the audio reaches cameras undampened.
var emission_angle_degrees: float;

#desc If [code]true[/code], the audio should be dampened according to the direction of the sound.
var emission_angle_enabled: bool;

#desc Dampens audio if camera is outside of [member emission_angle_degrees] and [member emission_angle_enabled] is set by this factor, in decibels.
var emission_angle_filter_attenuation_db: float;

#desc Sets the absolute maximum of the soundlevel, in decibels.
var max_db: float;

#desc The distance past which the sound can no longer be heard at all. Only has an effect if set to a value greater than [code]0.0[/code]. [member max_distance] works in tandem with [member unit_size]. However, unlike [member unit_size] whose behavior depends on the [member attenuation_model], [member max_distance] always works in a linear fashion. This can be used to prevent the [AudioStreamPlayer3D] from requiring audio mixing when the listener is far away, which saves CPU resources.
var max_distance: float;

#desc The maximum number of sounds this node can play at the same time. Playing additional sounds after this value is reached will cut off the oldest sounds.
var max_polyphony: int;

#desc Scales the panning strength for this node by multiplying the base [member ProjectSettings.audio/general/3d_panning_strength] with this factor. Higher values will pan audio from left to right more dramatically than lower values.
var panning_strength: float;

#desc The pitch and the tempo of the audio, as a multiplier of the audio sample's sample rate.
var pitch_scale: float;

#desc If [code]true[/code], audio is playing.
var playing: bool;

#desc The [AudioStream] resource to be played.
var stream: AudioStream;

#desc If [code]true[/code], the playback is paused. You can resume it by setting [member stream_paused] to [code]false[/code].
var stream_paused: bool;

#desc The base sound level unaffected by dampening, in decibels.
var unit_db: float;

#desc The factor for the attenuation effect. Higher values make the sound audible over a larger distance.
var unit_size: float;



#desc Returns the position in the [AudioStream].
func get_playback_position() -> float:
	pass;

#desc Returns the [AudioStreamPlayback] object associated with this [AudioStreamPlayer3D].
func get_stream_playback() -> AudioStreamPlayback:
	pass;

#desc Plays the audio from the given position [param from_position], in seconds.
func play() -> void:
	pass;

#desc Sets the position from which audio will be played, in seconds.
func seek() -> void:
	pass;

#desc Stops the audio.
func stop() -> void:
	pass;


