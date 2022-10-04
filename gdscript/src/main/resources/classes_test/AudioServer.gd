#brief Server interface for low-level audio access.
#desc [AudioServer] is a low-level server interface for audio access. It is in charge of creating sample data (playable audio) as well as its playback via a voice interface.
class_name AudioServer

#desc Two or fewer speakers were detected.
const SPEAKER_MODE_STEREO = 0;

#desc A 3.1 channel surround setup was detected.
const SPEAKER_SURROUND_31 = 1;

#desc A 5.1 channel surround setup was detected.
const SPEAKER_SURROUND_51 = 2;

#desc A 7.1 channel surround setup was detected.
const SPEAKER_SURROUND_71 = 3;


#desc Number of available audio buses.
var bus_count: int;

#desc Name of the current device for audio input (see [method capture_get_device_list]). On systems with multiple audio inputs (such as analog, USB and HDMI audio), this can be used to select the audio input device. The value [code]"Default"[/code] will record audio on the system-wide default audio input. If an invalid device name is set, the value will be reverted back to [code]"Default"[/code].
#desc [b]Note:[/b] [member ProjectSettings.audio/driver/enable_input] must be [code]true[/code] for audio input to work. See also that setting's description for caveats related to permissions and operating system privacy settings.
var capture_device: String;

#desc Name of the current device for audio output (see [method get_device_list]). On systems with multiple audio outputs (such as analog, USB and HDMI audio), this can be used to select the audio output device. The value [code]"Default"[/code] will play audio on the system-wide default audio output. If an invalid device name is set, the value will be reverted back to [code]"Default"[/code].
var device: String;

#desc Scales the rate at which audio is played (i.e. setting it to [code]0.5[/code] will make the audio be played at half its speed).
var playback_speed_scale: float;



#desc Adds a bus at [param at_position].
func add_bus() -> void:
	pass;

#desc Adds an [AudioEffect] effect to the bus [param bus_idx] at [param at_position].
func add_bus_effect(bus_idx: int, effect: AudioEffect, at_position: int) -> void:
	pass;

#desc Returns the names of all audio input devices detected on the system.
#desc [b]Note:[/b] [member ProjectSettings.audio/driver/enable_input] must be [code]true[/code] for audio input to work. See also that setting's description for caveats related to permissions and operating system privacy settings.
func capture_get_device_list() -> PackedStringArray:
	pass;

#desc Generates an [AudioBusLayout] using the available buses and effects.
func generate_bus_layout() -> AudioBusLayout:
	pass;

#desc Returns the number of channels of the bus at index [param bus_idx].
func get_bus_channels() -> int:
	pass;

#desc Returns the [AudioEffect] at position [param effect_idx] in bus [param bus_idx].
func get_bus_effect(bus_idx: int, effect_idx: int) -> AudioEffect:
	pass;

#desc Returns the number of effects on the bus at [param bus_idx].
func get_bus_effect_count() -> int:
	pass;

#desc Returns the [AudioEffectInstance] assigned to the given bus and effect indices (and optionally channel).
func get_bus_effect_instance(bus_idx: int, effect_idx: int, channel: int) -> AudioEffectInstance:
	pass;

#desc Returns the index of the bus with the name [param bus_name].
func get_bus_index() -> int:
	pass;

#desc Returns the name of the bus with the index [param bus_idx].
func get_bus_name() -> String:
	pass;

#desc Returns the peak volume of the left speaker at bus index [param bus_idx] and channel index [param channel].
func get_bus_peak_volume_left_db(bus_idx: int, channel: int) -> float:
	pass;

#desc Returns the peak volume of the right speaker at bus index [param bus_idx] and channel index [param channel].
func get_bus_peak_volume_right_db(bus_idx: int, channel: int) -> float:
	pass;

#desc Returns the name of the bus that the bus at index [param bus_idx] sends to.
func get_bus_send() -> StringName:
	pass;

#desc Returns the volume of the bus at index [param bus_idx] in dB.
func get_bus_volume_db() -> float:
	pass;

#desc Returns the names of all audio devices detected on the system.
func get_device_list() -> PackedStringArray:
	pass;

#desc Returns the sample rate at the output of the [AudioServer].
func get_mix_rate() -> float:
	pass;

#desc Returns the audio driver's output latency.
func get_output_latency() -> float:
	pass;

#desc Returns the speaker configuration.
func get_speaker_mode() -> int:
	pass;

#desc Returns the relative time since the last mix occurred.
func get_time_since_last_mix() -> float:
	pass;

#desc Returns the relative time until the next mix occurs.
func get_time_to_next_mix() -> float:
	pass;

#desc If [code]true[/code], the bus at index [param bus_idx] is bypassing effects.
func is_bus_bypassing_effects() -> bool:
	pass;

#desc If [code]true[/code], the effect at index [param effect_idx] on the bus at index [param bus_idx] is enabled.
func is_bus_effect_enabled(bus_idx: int, effect_idx: int) -> bool:
	pass;

#desc If [code]true[/code], the bus at index [param bus_idx] is muted.
func is_bus_mute() -> bool:
	pass;

#desc If [code]true[/code], the bus at index [param bus_idx] is in solo mode.
func is_bus_solo() -> bool:
	pass;

#desc Locks the audio driver's main loop.
#desc [b]Note:[/b] Remember to unlock it afterwards.
func lock() -> void:
	pass;

#desc Moves the bus from index [param index] to index [param to_index].
func move_bus(index: int, to_index: int) -> void:
	pass;

#desc Removes the bus at index [param index].
func remove_bus() -> void:
	pass;

#desc Removes the effect at index [param effect_idx] from the bus at index [param bus_idx].
func remove_bus_effect(bus_idx: int, effect_idx: int) -> void:
	pass;

#desc If [code]true[/code], the bus at index [param bus_idx] is bypassing effects.
func set_bus_bypass_effects(bus_idx: int, enable: bool) -> void:
	pass;

#desc If [code]true[/code], the effect at index [param effect_idx] on the bus at index [param bus_idx] is enabled.
func set_bus_effect_enabled(bus_idx: int, effect_idx: int, enabled: bool) -> void:
	pass;

#desc Overwrites the currently used [AudioBusLayout].
func set_bus_layout() -> void:
	pass;

#desc If [code]true[/code], the bus at index [param bus_idx] is muted.
func set_bus_mute(bus_idx: int, enable: bool) -> void:
	pass;

#desc Sets the name of the bus at index [param bus_idx] to [param name].
func set_bus_name(bus_idx: int, name: String) -> void:
	pass;

#desc Connects the output of the bus at [param bus_idx] to the bus named [param send].
func set_bus_send(bus_idx: int, send: StringName) -> void:
	pass;

#desc If [code]true[/code], the bus at index [param bus_idx] is in solo mode.
func set_bus_solo(bus_idx: int, enable: bool) -> void:
	pass;

#desc Sets the volume of the bus at index [param bus_idx] to [param volume_db].
func set_bus_volume_db(bus_idx: int, volume_db: float) -> void:
	pass;

func set_enable_tagging_used_audio_streams() -> void:
	pass;

#desc Swaps the position of two effects in bus [param bus_idx].
func swap_bus_effects(bus_idx: int, effect_idx: int, by_effect_idx: int) -> void:
	pass;

#desc Unlocks the audio driver's main loop. (After locking it, you should always unlock it.)
func unlock() -> void:
	pass;


