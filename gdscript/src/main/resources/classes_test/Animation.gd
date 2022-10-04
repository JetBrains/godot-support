#brief Contains data used to animate everything in the engine.
#desc An Animation resource contains data used to animate everything in the engine. Animations are divided into tracks, and each track must be linked to a node. The state of that node can be changed through time, by adding timed keys (events) to the track.
#desc [codeblocks]
#desc [gdscript]
#desc # This creates an animation that makes the node "Enemy" move to the right by
#desc # 100 pixels in 0.5 seconds.
#desc var animation = Animation.new()
#desc var track_index = animation.add_track(Animation.TYPE_VALUE)
#desc animation.track_set_path(track_index, "Enemy:position:x")
#desc animation.track_insert_key(track_index, 0.0, 0)
#desc animation.track_insert_key(track_index, 0.5, 100)
#desc [/gdscript]
#desc [csharp]
#desc // This creates an animation that makes the node "Enemy" move to the right by
#desc // 100 pixels in 0.5 seconds.
#desc var animation = new Animation();
#desc int trackIndex = animation.AddTrack(Animation.TrackType.Value);
#desc animation.TrackSetPath(trackIndex, "Enemy:position:x");
#desc animation.TrackInsertKey(trackIndex, 0.0f, 0);
#desc animation.TrackInsertKey(trackIndex, 0.5f, 100);
#desc [/csharp]
#desc [/codeblocks]
#desc Animations are just data containers, and must be added to nodes such as an [AnimationPlayer] to be played back. Animation tracks have different types, each with its own set of dedicated methods. Check [enum TrackType] to see available types.
class_name Animation

#desc Value tracks set values in node properties, but only those which can be Interpolated.
const TYPE_VALUE = 0;

const TYPE_POSITION_3D = 1;

const TYPE_ROTATION_3D = 2;

const TYPE_SCALE_3D = 3;

const TYPE_BLEND_SHAPE = 4;

#desc Method tracks call functions with given arguments per key.
const TYPE_METHOD = 5;

#desc Bezier tracks are used to interpolate a value using custom curves. They can also be used to animate sub-properties of vectors and colors (e.g. alpha value of a [Color]).
const TYPE_BEZIER = 6;

#desc Audio tracks are used to play an audio stream with either type of [AudioStreamPlayer]. The stream can be trimmed and previewed in the animation.
const TYPE_AUDIO = 7;

#desc Animation tracks play animations in other [AnimationPlayer] nodes.
const TYPE_ANIMATION = 8;

#desc No interpolation (nearest value).
const INTERPOLATION_NEAREST = 0;

#desc Linear interpolation.
const INTERPOLATION_LINEAR = 1;

#desc Cubic interpolation.
const INTERPOLATION_CUBIC = 2;

#desc Linear interpolation with shortest path rotation.
#desc [b]Note:[/b] The result value is always normalized and may not match the key value.
const INTERPOLATION_LINEAR_ANGLE = 3;

#desc Cubic interpolation with shortest path rotation.
#desc [b]Note:[/b] The result value is always normalized and may not match the key value.
const INTERPOLATION_CUBIC_ANGLE = 4;

#desc Update between keyframes.
const UPDATE_CONTINUOUS = 0;

#desc Update at the keyframes and hold the value.
const UPDATE_DISCRETE = 1;

#desc Update at the keyframes.
const UPDATE_TRIGGER = 2;

#desc Same as linear interpolation, but also interpolates from the current value (i.e. dynamically at runtime) if the first key isn't at 0 seconds.
const UPDATE_CAPTURE = 3;

#desc At both ends of the animation, the animation will stop playing.
const LOOP_NONE = 0;

#desc At both ends of the animation, the animation will be repeated without changing the playback direction.
const LOOP_LINEAR = 1;

#desc Repeats playback and reverse playback at both ends of the animation.
const LOOP_PINGPONG = 2;


#desc The total length of the animation (in seconds).
#desc [b]Note:[/b] Length is not delimited by the last key, as this one may be before or after the end to ensure correct interpolation and looping.
var length: float;

#desc Determines the behavior of both ends of the animation timeline during animation playback. This is used for correct interpolation of animation cycles, and for hinting the player that it must restart the animation.
var loop_mode: int;

#desc The animation step value.
var step: float;



#desc Adds a track to the Animation.
func add_track(type: int, at_position: int) -> int:
	pass;

#desc Returns the animation name at the key identified by [param key_idx]. The [param track_idx] must be the index of an Animation Track.
func animation_track_get_key_animation(track_idx: int, key_idx: int) -> StringName:
	pass;

#desc Inserts a key with value [param animation] at the given [param time] (in seconds). The [param track_idx] must be the index of an Animation Track.
func animation_track_insert_key(track_idx: int, time: float, animation: StringName) -> int:
	pass;

#desc Sets the key identified by [param key_idx] to value [param animation]. The [param track_idx] must be the index of an Animation Track.
func animation_track_set_key_animation(track_idx: int, key_idx: int, animation: StringName) -> void:
	pass;

#desc Returns the end offset of the key identified by [param key_idx]. The [param track_idx] must be the index of an Audio Track.
#desc End offset is the number of seconds cut off at the ending of the audio stream.
func audio_track_get_key_end_offset(track_idx: int, key_idx: int) -> float:
	pass;

#desc Returns the start offset of the key identified by [param key_idx]. The [param track_idx] must be the index of an Audio Track.
#desc Start offset is the number of seconds cut off at the beginning of the audio stream.
func audio_track_get_key_start_offset(track_idx: int, key_idx: int) -> float:
	pass;

#desc Returns the audio stream of the key identified by [param key_idx]. The [param track_idx] must be the index of an Audio Track.
func audio_track_get_key_stream(track_idx: int, key_idx: int) -> Resource:
	pass;

#desc Inserts an Audio Track key at the given [param time] in seconds. The [param track_idx] must be the index of an Audio Track.
#desc [param stream] is the [AudioStream] resource to play. [param start_offset] is the number of seconds cut off at the beginning of the audio stream, while [param end_offset] is at the ending.
func audio_track_insert_key(track_idx: int, time: float, stream: Resource, start_offset: float, end_offset: float) -> int:
	pass;

#desc Sets the end offset of the key identified by [param key_idx] to value [param offset]. The [param track_idx] must be the index of an Audio Track.
func audio_track_set_key_end_offset(track_idx: int, key_idx: int, offset: float) -> void:
	pass;

#desc Sets the start offset of the key identified by [param key_idx] to value [param offset]. The [param track_idx] must be the index of an Audio Track.
func audio_track_set_key_start_offset(track_idx: int, key_idx: int, offset: float) -> void:
	pass;

#desc Sets the stream of the key identified by [param key_idx] to value [param stream]. The [param track_idx] must be the index of an Audio Track.
func audio_track_set_key_stream(track_idx: int, key_idx: int, stream: Resource) -> void:
	pass;

#desc Returns the in handle of the key identified by [param key_idx]. The [param track_idx] must be the index of a Bezier Track.
func bezier_track_get_key_in_handle(track_idx: int, key_idx: int) -> Vector2:
	pass;

#desc Returns the out handle of the key identified by [param key_idx]. The [param track_idx] must be the index of a Bezier Track.
func bezier_track_get_key_out_handle(track_idx: int, key_idx: int) -> Vector2:
	pass;

#desc Returns the value of the key identified by [param key_idx]. The [param track_idx] must be the index of a Bezier Track.
func bezier_track_get_key_value(track_idx: int, key_idx: int) -> float:
	pass;

#desc Inserts a Bezier Track key at the given [param time] in seconds. The [param track_idx] must be the index of a Bezier Track.
#desc [param in_handle] is the left-side weight of the added Bezier curve point, [param out_handle] is the right-side one, while [param value] is the actual value at this point.
func bezier_track_insert_key(track_idx: int, time: float, value: float, in_handle: Vector2, out_handle: Vector2) -> int:
	pass;

#desc Returns the interpolated value at the given [param time] (in seconds). The [param track_idx] must be the index of a Bezier Track.
func bezier_track_interpolate(track_idx: int, time: float) -> float:
	pass;

#desc Sets the in handle of the key identified by [param key_idx] to value [param in_handle]. The [param track_idx] must be the index of a Bezier Track.
func bezier_track_set_key_in_handle(track_idx: int, key_idx: int, in_handle: Vector2, balanced_value_time_ratio: float) -> void:
	pass;

#desc Sets the out handle of the key identified by [param key_idx] to value [param out_handle]. The [param track_idx] must be the index of a Bezier Track.
func bezier_track_set_key_out_handle(track_idx: int, key_idx: int, out_handle: Vector2, balanced_value_time_ratio: float) -> void:
	pass;

#desc Sets the value of the key identified by [param key_idx] to the given value. The [param track_idx] must be the index of a Bezier Track.
func bezier_track_set_key_value(track_idx: int, key_idx: int, value: float) -> void:
	pass;

func blend_shape_track_insert_key(track_idx: int, time: float, amount: float) -> int:
	pass;

#desc Clear the animation (clear all tracks and reset all).
func clear() -> void:
	pass;

func compress(page_size: int, fps: int, split_tolerance: float) -> void:
	pass;

#desc Adds a new track that is a copy of the given track from [param to_animation].
func copy_track(track_idx: int, to_animation: Animation) -> void:
	pass;

#desc Returns the index of the specified track. If the track is not found, return -1.
func find_track(path: NodePath, type: int) -> int:
	pass;

#desc Returns the amount of tracks in the animation.
func get_track_count() -> int:
	pass;

#desc Returns all the key indices of a method track, given a position and delta time.
func method_track_get_key_indices(track_idx: int, time_sec: float, delta: float) -> PackedInt32Array:
	pass;

#desc Returns the method name of a method track.
func method_track_get_name(track_idx: int, key_idx: int) -> StringName:
	pass;

#desc Returns the arguments values to be called on a method track for a given key in a given track.
func method_track_get_params(track_idx: int, key_idx: int) -> Array:
	pass;

func position_track_insert_key(track_idx: int, time: float, position: Vector3) -> int:
	pass;

#desc Removes a track by specifying the track index.
func remove_track() -> void:
	pass;

func rotation_track_insert_key(track_idx: int, time: float, rotation: Quaternion) -> int:
	pass;

func scale_track_insert_key(track_idx: int, time: float, scale: Vector3) -> int:
	pass;

#desc Finds the key index by time in a given track. Optionally, only find it if the exact time is given.
func track_find_key(track_idx: int, time: float, exact: bool) -> int:
	pass;

#desc Returns [code]true[/code] if the track at [param track_idx] wraps the interpolation loop. New tracks wrap the interpolation loop by default.
func track_get_interpolation_loop_wrap() -> bool:
	pass;

#desc Returns the interpolation type of a given track.
func track_get_interpolation_type() -> int:
	pass;

#desc Returns the number of keys in a given track.
func track_get_key_count() -> int:
	pass;

#desc Returns the time at which the key is located.
func track_get_key_time(track_idx: int, key_idx: int) -> float:
	pass;

#desc Returns the transition curve (easing) for a specific key (see the built-in math function [method @GlobalScope.ease]).
func track_get_key_transition(track_idx: int, key_idx: int) -> float:
	pass;

#desc Returns the value of a given key in a given track.
func track_get_key_value(track_idx: int, key_idx: int) -> Variant:
	pass;

#desc Gets the path of a track. For more information on the path format, see [method track_set_path].
func track_get_path() -> NodePath:
	pass;

#desc Gets the type of a track.
func track_get_type() -> int:
	pass;

#desc Inserts a generic key in a given track. Returns the key index.
func track_insert_key(track_idx: int, time: float, key: Variant, transition: float) -> int:
	pass;

func track_is_compressed() -> bool:
	pass;

#desc Returns [code]true[/code] if the track at index [param track_idx] is enabled.
func track_is_enabled() -> bool:
	pass;

#desc Returns [code]true[/code] if the given track is imported. Else, return [code]false[/code].
func track_is_imported() -> bool:
	pass;

#desc Moves a track down.
func track_move_down() -> void:
	pass;

#desc Changes the index position of track [param track_idx] to the one defined in [param to_idx].
func track_move_to(track_idx: int, to_idx: int) -> void:
	pass;

#desc Moves a track up.
func track_move_up() -> void:
	pass;

#desc Removes a key by index in a given track.
func track_remove_key(track_idx: int, key_idx: int) -> void:
	pass;

#desc Removes a key at [param time] in a given track.
func track_remove_key_at_time(track_idx: int, time: float) -> void:
	pass;

#desc Enables/disables the given track. Tracks are enabled by default.
func track_set_enabled(track_idx: int, enabled: bool) -> void:
	pass;

#desc Sets the given track as imported or not.
func track_set_imported(track_idx: int, imported: bool) -> void:
	pass;

#desc If [code]true[/code], the track at [param track_idx] wraps the interpolation loop.
func track_set_interpolation_loop_wrap(track_idx: int, interpolation: bool) -> void:
	pass;

#desc Sets the interpolation type of a given track.
func track_set_interpolation_type(track_idx: int, interpolation: int) -> void:
	pass;

#desc Sets the time of an existing key.
func track_set_key_time(track_idx: int, key_idx: int, time: float) -> void:
	pass;

#desc Sets the transition curve (easing) for a specific key (see the built-in math function [method @GlobalScope.ease]).
func track_set_key_transition(track_idx: int, key_idx: int, transition: float) -> void:
	pass;

#desc Sets the value of an existing key.
func track_set_key_value(track_idx: int, key: int, value: Variant) -> void:
	pass;

#desc Sets the path of a track. Paths must be valid scene-tree paths to a node and must be specified starting from the parent node of the node that will reproduce the animation. Tracks that control properties or bones must append their name after the path, separated by [code]":"[/code].
#desc For example, [code]"character/skeleton:ankle"[/code] or [code]"character/mesh:transform/local"[/code].
func track_set_path(track_idx: int, path: NodePath) -> void:
	pass;

#desc Swaps the track [param track_idx]'s index position with the track [param with_idx].
func track_swap(track_idx: int, with_idx: int) -> void:
	pass;

#desc Returns all the key indices of a value track, given a position and delta time.
func value_track_get_key_indices(track_idx: int, time_sec: float, delta: float) -> PackedInt32Array:
	pass;

#desc Returns the update mode of a value track.
func value_track_get_update_mode() -> int:
	pass;

#desc Returns the interpolated value at the given time (in seconds). The [param track_idx] must be the index of a value track.
func value_track_interpolate(track_idx: int, time_sec: float) -> Variant:
	pass;

#desc Sets the update mode (see [enum UpdateMode]) of a value track.
func value_track_set_update_mode(track_idx: int, mode: int) -> void:
	pass;


