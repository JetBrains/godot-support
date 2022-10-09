extends MainLoop
#brief Manages the game loop via a hierarchy of nodes.
#desc As one of the most important classes, the [SceneTree] manages the hierarchy of nodes in a scene as well as scenes themselves. Nodes can be added, retrieved and removed. The whole scene tree (and thus the current scene) can be paused. Scenes can be loaded, switched and reloaded.
#desc You can also use the [SceneTree] to organize your nodes into groups: every node can be assigned as many groups as you want to create, e.g. an "enemy" group. You can then iterate these groups or even call methods and set properties on all the group's members at once.
#desc [SceneTree] is the default [MainLoop] implementation used by scenes, and is thus in charge of the game loop.
class_name SceneTree

#desc Call a group with no flags (default).
const GROUP_CALL_DEFAULT = 0;

#desc Call a group in reverse scene order.
const GROUP_CALL_REVERSE = 1;

#desc Call a group with a one-frame delay (idle frame, not physics).
const GROUP_CALL_DEFERRED = 2;

#desc Call a group only once even if the call is executed many times.
const GROUP_CALL_UNIQUE = 4;


#desc If [code]true[/code], the application automatically accepts quitting.
#desc For mobile platforms, see [member quit_on_go_back].
var auto_accept_quit: bool;

#desc The current scene.
var current_scene: Node;

#desc If [code]true[/code], collision shapes will be visible when running the game from the editor for debugging purposes.
#desc [b]Note:[/b] This property is not designed to be changed at run-time. Changing the value of [member debug_collisions_hint] while the project is running will not have the desired effect.
var debug_collisions_hint: bool;

#desc If [code]true[/code], navigation polygons will be visible when running the game from the editor for debugging purposes.
#desc [b]Note:[/b] This property is not designed to be changed at run-time. Changing the value of [member debug_navigation_hint] while the project is running will not have the desired effect.
var debug_navigation_hint: bool;

#desc If [code]true[/code], curves from [Path2D] and [Path3D] nodes will be visible when running the game from the editor for debugging purposes.
#desc [b]Note:[/b] This property is not designed to be changed at run-time. Changing the value of [member debug_paths_hint] while the project is running will not have the desired effect.
var debug_paths_hint: bool;

#desc The root of the edited scene.
var edited_scene_root: Node;

#desc If [code]true[/code] (default value), enables automatic polling of the [MultiplayerAPI] for this SceneTree during [signal process_frame].
#desc If [code]false[/code], you need to manually call [method MultiplayerAPI.poll] to process network packets and deliver RPCs. This allows running RPCs in a different loop (e.g. physics, thread, specific time step) and for manual [Mutex] protection when accessing the [MultiplayerAPI] from threads.
var multiplayer_poll: bool;

#desc If [code]true[/code], the [SceneTree] is paused. Doing so will have the following behavior:
#desc - 2D and 3D physics will be stopped. This includes signals and collision detection.
#desc - [method Node._process], [method Node._physics_process] and [method Node._input] will not be called anymore in nodes.
var paused: bool;

#desc If [code]true[/code], the application quits automatically on going back (e.g. on Android).
#desc To handle 'Go Back' button when this option is disabled, use [constant DisplayServer.WINDOW_EVENT_GO_BACK_REQUEST].
var quit_on_go_back: bool;

#desc The [SceneTree]'s root [Window].
var root: Window;



#desc Calls [param method] on each member of the given group. You can pass arguments to [param method] by specifying them at the end of the method call. If a node doesn't have the given method or the argument list does not match (either in count or in types), it will be skipped.
#desc [b]Note:[/b] [method call_group] will call methods immediately on all members at once, which can cause stuttering if an expensive method is called on lots of members. To wait for one frame after [method call_group] was called, use [method call_group_flags] with the [constant GROUP_CALL_DEFERRED] flag.
vararg func call_group(group: StringName, method: StringName) -> void:
	pass;

#desc Calls [param method] on each member of the given group, respecting the given [enum GroupCallFlags]. You can pass arguments to [param method] by specifying them at the end of the method call. If a node doesn't have the given method or the argument list does not match (either in count or in types), it will be skipped.
#desc [codeblock]
#desc # Call the method in a deferred manner and in reverse order.
#desc get_tree().call_group_flags(SceneTree.GROUP_CALL_DEFERRED | SceneTree.GROUP_CALL_REVERSE)
#desc [/codeblock]
#desc [b]Note:[/b] Group call flags are used to control the method calling behavior. By default, methods will be called immediately in a way similar to [method call_group]. However, if the [constant GROUP_CALL_DEFERRED] flag is present in the [param flags] argument, methods will be called with a one-frame delay in a way similar to [method Object.set_deferred].
vararg func call_group_flags(flags: int, group: StringName, method: StringName) -> void:
	pass;

#desc Changes the running scene to the one at the given [param path], after loading it into a [PackedScene] and creating a new instance.
#desc Returns [constant OK] on success, [constant ERR_CANT_OPEN] if the [param path] cannot be loaded into a [PackedScene], or [constant ERR_CANT_CREATE] if that scene cannot be instantiated.
#desc [b]Note:[/b] The scene change is deferred, which means that the new scene node is added on the next idle frame. You won't be able to access it immediately after the [method change_scene_to_file] call.
func change_scene_to_file(path: String) -> int:
	pass;

#desc Changes the running scene to a new instance of the given [PackedScene].
#desc Returns [constant OK] on success or [constant ERR_CANT_CREATE] if the scene cannot be instantiated.
#desc [b]Note:[/b] The scene change is deferred, which means that the new scene node is added on the next idle frame. You won't be able to access it immediately after the [method change_scene_to_packed] call.
func change_scene_to_packed(packed_scene: PackedScene) -> int:
	pass;

#desc Returns a [SceneTreeTimer] which will [signal SceneTreeTimer.timeout] after the given time in seconds elapsed in this [SceneTree].
#desc If [code]process_always[/code] is set to [code]false[/code], pausing the [SceneTree] will also pause the timer.
#desc If [code]process_in_physics[/code] is set to [code]true[/code], will update the [SceneTreeTimer] during the physics frame instead of the process frame (fixed framerate processing).
#desc If [code]ignore_time_scale[/code] is set to [code]true[/code], will ignore [member Engine.time_scale] and update the [SceneTreeTimer] with the actual frame delta.
#desc Commonly used to create a one-shot delay timer as in the following example:
#desc [codeblocks]
#desc [gdscript]
#desc func some_function():
#desc print("start")
#desc await get_tree().create_timer(1.0).timeout
#desc print("end")
#desc [/gdscript]
#desc [csharp]
#desc public async void SomeFunction()
#desc {
#desc GD.Print("start");
#desc await ToSignal(GetTree().CreateTimer(1.0f), "timeout");
#desc GD.Print("end");
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc The timer will be automatically freed after its time elapses.
func create_timer(time_sec: float, process_always: bool, process_in_physics: bool, ignore_time_scale: bool) -> SceneTreeTimer:
	pass;

#desc Creates and returns a new [Tween].
func create_tween() -> Tween:
	pass;

#desc Returns the first node in the specified group, or [code]null[/code] if the group is empty or does not exist.
func get_first_node_in_group(group: StringName) -> Node:
	pass;

#desc Returns the current frame number, i.e. the total frame count since the application started.
func get_frame() -> int:
	pass;

#desc Return the [MultiplayerAPI] configured for the given path, or the default one if [param for_path] is empty.
func get_multiplayer(for_path: NodePath) -> MultiplayerAPI:
	pass;

#desc Returns the number of nodes in this [SceneTree].
func get_node_count() -> int:
	pass;

#desc Returns a list of all nodes assigned to the given group.
func get_nodes_in_group(group: StringName) -> Array[Node]:
	pass;

#desc Returns an array of currently existing [Tween]s in the [SceneTree] (both running and paused).
func get_processed_tweens() -> Array[Tween]:
	pass;

#desc Returns [code]true[/code] if the given group exists.
func has_group(name: StringName) -> bool:
	pass;

#desc Sends the given notification to all members of the [param group].
#desc [b]Note:[/b] [method notify_group] will immediately notify all members at once, which can cause stuttering if an expensive method is called as a result of sending the notification lots of members. To wait for one frame, use [method notify_group_flags] with the [constant GROUP_CALL_DEFERRED] flag.
func notify_group(group: StringName, notification: int) -> void:
	pass;

#desc Sends the given notification to all members of the [param group], respecting the given [enum GroupCallFlags].
#desc [b]Note:[/b] Group call flags are used to control the notification sending behavior. By default, notifications will be sent immediately in a way similar to [method notify_group]. However, if the [constant GROUP_CALL_DEFERRED] flag is present in the [param call_flags] argument, notifications will be sent with a one-frame delay in a way similar to using [code]Object.call_deferred("notification", ...)[/code].
func notify_group_flags(call_flags: int, group: StringName, notification: int) -> void:
	pass;

#desc Queues the given object for deletion, delaying the call to [method Object.free] to after the current frame.
func queue_delete(obj: Object) -> void:
	pass;

#desc Quits the application at the end of the current iteration. Argument [param exit_code] can optionally be given (defaulting to 0) to customize the exit status code.
#desc By convention, an exit code of [code]0[/code] indicates success whereas a non-zero exit code indicates an error.
#desc For portability reasons, the exit code should be set between 0 and 125 (inclusive).
#desc [b]Note:[/b] On iOS this method doesn't work. Instead, as recommended by the iOS Human Interface Guidelines, the user is expected to close apps via the Home button.
func quit(exit_code: int) -> void:
	pass;

#desc Reloads the currently active scene.
#desc Returns [constant OK] on success, [constant ERR_UNCONFIGURED] if no [member current_scene] was defined yet, [constant ERR_CANT_OPEN] if [member current_scene] cannot be loaded into a [PackedScene], or [constant ERR_CANT_CREATE] if the scene cannot be instantiated.
func reload_current_scene() -> int:
	pass;

#desc Sets the given [param property] to [param value] on all members of the given group.
#desc [b]Note:[/b] [method set_group] will set the property immediately on all members at once, which can cause stuttering if a property with an expensive setter is set on lots of members. To wait for one frame, use [method set_group_flags] with the [constant GROUP_CALL_DEFERRED] flag.
func set_group(group: StringName, property: String, value: Variant) -> void:
	pass;

#desc Sets the given [param property] to [param value] on all members of the given group, respecting the given [enum GroupCallFlags].
#desc [b]Note:[/b] Group call flags are used to control the property setting behavior. By default, properties will be set immediately in a way similar to [method set_group]. However, if the [constant GROUP_CALL_DEFERRED] flag is present in the [param call_flags] argument, properties will be set with a one-frame delay in a way similar to [method Object.call_deferred].
func set_group_flags(call_flags: int, group: StringName, property: String, value: Variant) -> void:
	pass;

#desc Sets a custom [MultiplayerAPI] with the given [param root_path] (controlling also the relative subpaths), or override the default one if [param root_path] is empty.
func set_multiplayer(multiplayer: MultiplayerAPI, root_path: NodePath) -> void:
	pass;


