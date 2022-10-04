extends Node
#brief Player of [Animation] resources.
#desc An animation player is used for general-purpose playback of [Animation] resources. It contains a dictionary of [AnimationLibrary] resources and custom blend times between animation transitions.
#desc Some methods and properties use a single key to reference an animation directly. These keys are formatted as the key for the library, followed by a forward slash, then the key for the animation within the library, for example [code]"movement/run"[/code]. If the library's key is an empty string (known as the default library), the forward slash is omitted, being the same key used by the library.
#desc [AnimationPlayer] is more suited than [Tween] for animations where you know the final values in advance. For example, fading a screen in and out is more easily done with an [AnimationPlayer] node thanks to the animation tools provided by the editor. That particular example can also be implemented with a [Tween], but it requires doing everything by code.
#desc Updating the target properties of animations occurs at process time.
class_name AnimationPlayer

#desc Process animation during the physics process. This is especially useful when animating physics bodies.
const ANIMATION_PROCESS_PHYSICS = 0;

#desc Process animation during the idle process.
const ANIMATION_PROCESS_IDLE = 1;

#desc Do not process animation. Use [method advance] to process the animation manually.
const ANIMATION_PROCESS_MANUAL = 2;

#desc Batch method calls during the animation process, then do the calls after events are processed. This avoids bugs involving deleting nodes or modifying the AnimationPlayer while playing.
const ANIMATION_METHOD_CALL_DEFERRED = 0;

#desc Make method calls immediately when reached in the animation.
const ANIMATION_METHOD_CALL_IMMEDIATE = 1;


#desc If playing, the the current animation's key, otherwise, the animation last played. When set, this changes the animation, but will not play it unless already playing. See also [member current_animation].
var assigned_animation: String;

#desc The key of the animation to play when the scene loads.
var autoplay: String;

#desc The key of the currently playing animation. If no animation is playing, the property's value is an empty string. Changing this value does not restart the animation. See [method play] for more information on playing animations.
#desc [b]Note:[/b] while this property appears in the inspector, it's not meant to be edited, and it's not saved in the scene. This property is mainly used to get the currently playing animation, and internally for animation playback tracks. For more information, see [Animation].
var current_animation: String;

#desc The length (in seconds) of the currently playing animation.
var current_animation_length: float;

#desc The position (in seconds) of the currently playing animation.
var current_animation_position: float;

#desc The call mode to use for Call Method tracks.
var method_call_mode: int;

#desc If [code]true[/code] and the engine is running in Movie Maker mode (see [MovieWriter]), exits the engine with [method SceneTree.quit] as soon as an animation is done playing in this [AnimationPlayer]. A message is printed when the engine quits for this reason.
#desc [b]Note:[/b] This obeys the same logic as the [signal animation_finished] signal, so it will not quit the engine if the animation is set to be looping.
var movie_quit_on_finish: bool;

#desc If [code]true[/code], updates animations in response to process-related notifications.
var playback_active: bool;

#desc The default time in which to blend animations. Ranges from 0 to 4096 with 0.01 precision.
var playback_default_blend_time: float;

#desc The process notification in which to update animations.
var playback_process_mode: int;

#desc The speed scaling ratio. For instance, if this value is 1, then the animation plays at normal speed. If it's 0.5, then it plays at half speed. If it's 2, then it plays at double speed.
var playback_speed: float;

#desc This is used by the editor. If set to [code]true[/code], the scene will be saved with the effects of the reset animation (the animation with the key [code]"RESET"[/code]) applied as if it had been seeked to time 0, with the editor keeping the values that the scene had before saving.
#desc This makes it more convenient to preview and edit animations in the editor, as changes to the scene will not be saved as long as they are set in the reset animation.
var reset_on_save: bool;

#desc The node from which node path references will travel.
var root_node: NodePath;



#desc Adds [param library] to the animation player, under the key [param name].
func add_animation_library(name: StringName, library: AnimationLibrary) -> int:
	pass;

#desc Shifts position in the animation timeline and immediately updates the animation. [param delta] is the time in seconds to shift. Events between the current frame and [param delta] are handled.
func advance(delta: float) -> void:
	pass;

#desc Returns the key of the animation which is queued to play after the [param anim_from] animation.
func animation_get_next(anim_from: StringName) -> StringName:
	pass;

#desc Triggers the [param anim_to] animation when the [param anim_from] animation completes.
func animation_set_next(anim_from: StringName, anim_to: StringName) -> void:
	pass;

#desc [AnimationPlayer] caches animated nodes. It may not notice if a node disappears; [method clear_caches] forces it to update the cache again.
func clear_caches() -> void:
	pass;

#desc Clears all queued, unplayed animations.
func clear_queue() -> void:
	pass;

#desc Returns the key of [param animation] or an empty [StringName] if not found.
func find_animation(animation: Animation) -> StringName:
	pass;

#desc Returns the key for the [AnimationLibrary] that contains [param animation] or an empty [StringName] if not found.
func find_animation_library(animation: Animation) -> StringName:
	pass;

#desc Returns the [Animation] with the key [param name]. If the animation does not exist, [code]null[/code] is returned and an error is logged.
func get_animation(name: StringName) -> Animation:
	pass;

#desc Returns the first [AnimationLibrary] with key [param name] or [code]null[/code] if not found.
func get_animation_library(name: StringName) -> AnimationLibrary:
	pass;

#desc Returns the list of stored library keys.
func get_animation_library_list() -> StringName[]:
	pass;

#desc Returns the list of stored animation keys.
func get_animation_list() -> PackedStringArray:
	pass;

#desc Gets the blend time (in seconds) between two animations, referenced by their keys.
func get_blend_time(anim_from: StringName, anim_to: StringName) -> float:
	pass;

#desc Gets the actual playing speed of current animation or 0 if not playing. This speed is the [member playback_speed] property multiplied by [code]custom_speed[/code] argument specified when calling the [method play] method.
func get_playing_speed() -> float:
	pass;

#desc Returns a list of the animation keys that are currently queued to play.
func get_queue() -> PackedStringArray:
	pass;

#desc Returns [code]true[/code] if the [AnimationPlayer] stores an [Animation] with key [param name].
func has_animation(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if the [AnimationPlayer] stores an [AnimationLibrary] with key [param name].
func has_animation_library(name: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if playing an animation.
func is_playing() -> bool:
	pass;

#desc Plays the animation with key [param name]. Custom blend times and speed can be set. If [param custom_speed] is negative and [param from_end] is [code]true[/code], the animation will play backwards (which is equivalent to calling [method play_backwards]).
#desc The [AnimationPlayer] keeps track of its current or last played animation with [member assigned_animation]. If this method is called with that same animation [param name], or with no [param name] parameter, the assigned animation will resume playing if it was paused, or restart if it was stopped (see [method stop] for both pause and stop). If the animation was already playing, it will keep playing.
#desc [b]Note:[/b] The animation will be updated the next time the [AnimationPlayer] is processed. If other variables are updated at the same time this is called, they may be updated too early. To perform the update immediately, call [code]advance(0)[/code].
func play(name: StringName, custom_blend: float, custom_speed: float, from_end: bool) -> void:
	pass;

#desc Plays the animation with key [param name] in reverse.
#desc This method is a shorthand for [method play] with [code]custom_speed = -1.0[/code] and [code]from_end = true[/code], so see its description for more information.
func play_backwards(name: StringName, custom_blend: float) -> void:
	pass;

#desc Queues an animation for playback once the current one is done.
#desc [b]Note:[/b] If a looped animation is currently playing, the queued animation will never play unless the looped animation is stopped somehow.
func queue(name: StringName) -> void:
	pass;

#desc Removes the [AnimationLibrary] associated with the key [param name].
func remove_animation_library(name: StringName) -> void:
	pass;

#desc Moves the [AnimationLibrary] associated with the key [param name] to the key [param newname].
func rename_animation_library(name: StringName, newname: StringName) -> void:
	pass;

#desc Seeks the animation to the [param seconds] point in time (in seconds). If [param update] is [code]true[/code], the animation updates too, otherwise it updates at process time. Events between the current frame and [param seconds] are skipped.
#desc [b]Note:[/b] Seeking to the end of the animation doesn't emit [signal animation_finished]. If you want to skip animation and emit the signal, use [method advance].
func seek(seconds: float, update: bool) -> void:
	pass;

#desc Specifies a blend time (in seconds) between two animations, referenced by their keys.
func set_blend_time(anim_from: StringName, anim_to: StringName, sec: float) -> void:
	pass;

#desc Stops or pauses the currently playing animation. If [param reset] is [code]true[/code], the animation position is reset to [code]0[/code] and the playback speed is reset to [code]1.0[/code].
#desc If [param reset] is [code]false[/code], the [member current_animation_position] will be kept and calling [method play] or [method play_backwards] without arguments or with the same animation name as [member assigned_animation] will resume the animation.
func stop(reset: bool) -> void:
	pass;


