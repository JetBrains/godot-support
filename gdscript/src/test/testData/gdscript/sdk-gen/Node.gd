extends Object
class_name Node

## Base class for all scene objects.
##
## Nodes are Godot's building blocks. They can be assigned as the child of another node, resulting in a tree arrangement. A given node can contain any number of nodes as children with the requirement that all siblings (direct children of a node) should have unique names.
## A tree of nodes is called a [i]scene[/i]. Scenes can be saved to the disk and then instantiated into other scenes. This allows for very high flexibility in the architecture and data model of Godot projects.
## [b]Scene tree:[/b] The [SceneTree] contains the active tree of nodes. When a node is added to the scene tree, it receives the [constant NOTIFICATION_ENTER_TREE] notification and its [method _enter_tree] callback is triggered. Child nodes are always added [i]after[/i] their parent node, i.e. the [method _enter_tree] callback of a parent node will be triggered before its child's.
## Once all nodes have been added in the scene tree, they receive the [constant NOTIFICATION_READY] notification and their respective [method _ready] callbacks are triggered. For groups of nodes, the [method _ready] callback is called in reverse order, starting with the children and moving up to the parent nodes.
## This means that when adding a node to the scene tree, the following order will be used for the callbacks: [method _enter_tree] of the parent, [method _enter_tree] of the children, [method _ready] of the children and finally [method _ready] of the parent (recursively for the entire scene tree).
## [b]Processing:[/b] Nodes can override the "process" state, so that they receive a callback on each frame requesting them to process (do something). Normal processing (callback [method _process], toggled with [method set_process]) happens as fast as possible and is dependent on the frame rate, so the processing time [i]delta[/i] (in seconds) is passed as an argument. Physics processing (callback [method _physics_process], toggled with [method set_physics_process]) happens a fixed number of times per second (60 by default) and is useful for code related to the physics engine.
## Nodes can also process input events. When present, the [method _input] function will be called for each input that the program receives. In many cases, this can be overkill (unless used for simple projects), and the [method _unhandled_input] function might be preferred; it is called when the input event was not handled by anyone else (typically, GUI [Control] nodes), ensuring that the node only receives the events that were meant for it.
## To keep track of the scene hierarchy (especially when instantiating scenes into other scenes), an "owner" can be set for the node with the [member owner] property. This keeps track of who instantiated what. This is mostly useful when writing editors and tools, though.
## Finally, when a node is freed with [method Object.free] or [method queue_free], it will also free all its children.
## [b]Groups:[/b] Nodes can be added to as many groups as you want to be easy to manage, you could create groups like "enemies" or "collectables" for example, depending on your game. See [method add_to_group], [method is_in_group] and [method remove_from_group]. You can then retrieve all nodes in these groups, iterate them and even call methods on groups via the methods on [SceneTree].
## [b]Networking with nodes:[/b] After connecting to a server (or making one, see [ENetMultiplayerPeer]), it is possible to use the built-in RPC (remote procedure call) system to communicate over the network. By calling [method rpc] with a method name, it will be called locally and in all connected peers (peers = clients and the server that accepts connections). To identify which node receives the RPC call, Godot will use its [NodePath] (make sure node names are the same on all peers). Also, take a look at the high-level networking tutorial and corresponding demos.
## [b]Note:[/b] The [code]script[/code] property is part of the [Object] class, not [Node]. It isn't exposed like most properties but does have a setter and getter (see [method Object.set_script] and [method Object.get_script]).
##
## @tutorial(Nodes and scenes): https://docs.godotengine.org/en/stable/getting_started/step_by_step/nodes_and_scenes.html
## @tutorial(All Demos): https://github.com/godotengine/godot-demo-projects/


## Emitted when the child [param node] enters the [SceneTree], usually because this node entered the tree (see [signal tree_entered]), or [method add_child] has been called.
## This signal is emitted [i]after[/i] the child node's own [constant NOTIFICATION_ENTER_TREE] and [signal tree_entered].
signal child_entered_tree(node: Node)
## Emitted when the child [param node] is about to exit the [SceneTree], usually because this node is exiting the tree (see [signal tree_exiting]), or because the child [param node] is being removed or freed.
## When this signal is received, the child [param node] is still accessible inside the tree. This signal is emitted [i]after[/i] the child node's own [signal tree_exiting] and [constant NOTIFICATION_EXIT_TREE].
signal child_exiting_tree(node: Node)
## Emitted when the list of children is changed. This happens when child nodes are added, moved or removed.
signal child_order_changed
## Emitted when the node's editor description field changed.
signal editor_description_changed(node: Node)
## Emitted when an attribute of the node that is relevant to the editor is changed. Only emitted in the editor.
signal editor_state_changed
## Emitted when the node is considered ready, after [method _ready] is called.
signal ready
## Emitted when the node's [member name] is changed, if the node is inside the tree.
signal renamed
## Emitted when this node is being replaced by the [param node], see [method replace_by].
## This signal is emitted [i]after[/i] [param node] has been added as a child of the original parent node, but [i]before[/i] all original child nodes have been reparented to [param node].
signal replacing_by(node: Node)
## Emitted when the node enters the tree.
## This signal is emitted [i]after[/i] the related [constant NOTIFICATION_ENTER_TREE] notification.
signal tree_entered
## Emitted after the node exits the tree and is no longer active.
## This signal is emitted [i]after[/i] the related [constant NOTIFICATION_EXIT_TREE] notification.
signal tree_exited
## Emitted when the node is just about to exit the tree. The node is still valid. As such, this is the right place for de-initialization (or a "destructor", if you will).
## This signal is emitted [i]after[/i] the node's [method _exit_tree], and [i]before[/i] the related [constant NOTIFICATION_EXIT_TREE].
signal tree_exiting
## Notification received when the node enters a [SceneTree]. See [method _enter_tree].
## This notification is received [i]before[/i] the related [signal tree_entered] signal.
const NOTIFICATION_ENTER_TREE = 10;

## Notification received when the node is about to exit a [SceneTree]. See [method _exit_tree].
## This notification is received [i]after[/i] the related [signal tree_exiting] signal.
const NOTIFICATION_EXIT_TREE = 11;

const NOTIFICATION_MOVED_IN_PARENT = 12;

## Notification received when the node is ready. See [method _ready].
const NOTIFICATION_READY = 13;

## Notification received when the node is paused. See [member process_mode].
const NOTIFICATION_PAUSED = 14;

## Notification received when the node is unpaused. See [member process_mode].
const NOTIFICATION_UNPAUSED = 15;

## Notification received from the tree every physics frame when [method is_physics_processing] returns [code]true[/code]. See [method _physics_process].
const NOTIFICATION_PHYSICS_PROCESS = 16;

## Notification received from the tree every rendered frame when [method is_processing] returns [code]true[/code]. See [method _process].
const NOTIFICATION_PROCESS = 17;

## Notification received when the node is set as a child of another node (see [method add_child] and [method add_sibling]).
## [b]Note:[/b] This does [i]not[/i] mean that the node entered the [SceneTree].
const NOTIFICATION_PARENTED = 18;

## Notification received when the parent node calls [method remove_child] on this node.
## [b]Note:[/b] This does [i]not[/i] mean that the node exited the [SceneTree].
const NOTIFICATION_UNPARENTED = 19;

## Notification received [i]only[/i] by the newly instantiated scene root node, when [method PackedScene.instantiate] is completed.
const NOTIFICATION_SCENE_INSTANTIATED = 20;

## Notification received when a drag operation begins. All nodes receive this notification, not only the dragged one.
## Can be triggered either by dragging a [Control] that provides drag data (see [method Control._get_drag_data]) or using [method Control.force_drag].
## Use [method Viewport.gui_get_drag_data] to get the dragged data.
const NOTIFICATION_DRAG_BEGIN = 21;

## Notification received when a drag operation ends.
## Use [method Viewport.gui_is_drag_successful] to check if the drag succeeded.
const NOTIFICATION_DRAG_END = 22;

## Notification received when the node's [member name] or one of its ancestors' [member name] is changed. This notification is [i]not[/i] received when the node is removed from the [SceneTree].
const NOTIFICATION_PATH_RENAMED = 23;

## Notification received when the list of children is changed. This happens when child nodes are added, moved or removed.
const NOTIFICATION_CHILD_ORDER_CHANGED = 24;

## Notification received from the tree every rendered frame when [method is_processing_internal] returns [code]true[/code].
const NOTIFICATION_INTERNAL_PROCESS = 25;

## Notification received from the tree every physics frame when [method is_physics_processing_internal] returns [code]true[/code].
const NOTIFICATION_INTERNAL_PHYSICS_PROCESS = 26;

## Notification received when the node enters the tree, just before [constant NOTIFICATION_READY] may be received. Unlike the latter, it is sent every time the node enters tree, not just once.
const NOTIFICATION_POST_ENTER_TREE = 27;

## Notification received when the node is disabled. See [constant PROCESS_MODE_DISABLED].
const NOTIFICATION_DISABLED = 28;

## Notification received when the node is enabled again after being disabled. See [constant PROCESS_MODE_DISABLED].
const NOTIFICATION_ENABLED = 29;

## Notification received when [method reset_physics_interpolation] is called on the node or its ancestors.
const NOTIFICATION_RESET_PHYSICS_INTERPOLATION = 2001;

## Notification received right before the scene with the node is saved in the editor. This notification is only sent in the Godot editor and will not occur in exported projects.
const NOTIFICATION_EDITOR_PRE_SAVE = 9001;

## Notification received right after the scene with the node is saved in the editor. This notification is only sent in the Godot editor and will not occur in exported projects.
const NOTIFICATION_EDITOR_POST_SAVE = 9002;

## Notification received when the mouse enters the window.
## Implemented for embedded windows and on desktop and web platforms.
const NOTIFICATION_WM_MOUSE_ENTER = 1002;

## Notification received when the mouse leaves the window.
## Implemented for embedded windows and on desktop and web platforms.
const NOTIFICATION_WM_MOUSE_EXIT = 1003;

## Notification received from the OS when the node's [Window] ancestor is focused. This may be a change of focus between two windows of the same engine instance, or from the OS desktop or a third-party application to a window of the game (in which case [constant NOTIFICATION_APPLICATION_FOCUS_IN] is also received).
## A [Window] node receives this notification when it is focused.
const NOTIFICATION_WM_WINDOW_FOCUS_IN = 1004;

## Notification received from the OS when the node's [Window] ancestor is defocused. This may be a change of focus between two windows of the same engine instance, or from a window of the game to the OS desktop or a third-party application (in which case [constant NOTIFICATION_APPLICATION_FOCUS_OUT] is also received).
## A [Window] node receives this notification when it is defocused.
const NOTIFICATION_WM_WINDOW_FOCUS_OUT = 1005;

## Notification received from the OS when a close request is sent (e.g. closing the window with a "Close" button or [kbd]Alt + F4[/kbd]).
## Implemented on desktop platforms.
const NOTIFICATION_WM_CLOSE_REQUEST = 1006;

## Notification received from the OS when a go back request is sent (e.g. pressing the "Back" button on Android).
## Implemented only on Android.
const NOTIFICATION_WM_GO_BACK_REQUEST = 1007;

## Notification received when the window is resized.
## [b]Note:[/b] Only the resized [Window] node receives this notification, and it's not propagated to the child nodes.
const NOTIFICATION_WM_SIZE_CHANGED = 1008;

## Notification received from the OS when the screen's dots per inch (DPI) scale is changed. Only implemented on macOS.
const NOTIFICATION_WM_DPI_CHANGE = 1009;

## Notification received when the mouse cursor enters the [Viewport]'s visible area, that is not occluded behind other [Control]s or [Window]s, provided its [member Viewport.gui_disable_input] is [code]false[/code] and regardless if it's currently focused or not.
const NOTIFICATION_VP_MOUSE_ENTER = 1010;

## Notification received when the mouse cursor leaves the [Viewport]'s visible area, that is not occluded behind other [Control]s or [Window]s, provided its [member Viewport.gui_disable_input] is [code]false[/code] and regardless if it's currently focused or not.
const NOTIFICATION_VP_MOUSE_EXIT = 1011;

## Notification received when the window is moved.
const NOTIFICATION_WM_POSITION_CHANGED = 1012;

## Notification received from the OS when the application is exceeding its allocated memory.
## Implemented only on iOS.
const NOTIFICATION_OS_MEMORY_WARNING = 2009;

## Notification received when translations may have changed. Can be triggered by the user changing the locale, changing [member auto_translate_mode] or when the node enters the scene tree. Can be used to respond to language changes, for example to change the UI strings on the fly. Useful when working with the built-in translation support, like [method Object.tr].
## [b]Note:[/b] This notification is received alongside [constant NOTIFICATION_ENTER_TREE], so if you are instantiating a scene, the child nodes will not be initialized yet. You can use it to setup translations for this node, child nodes created from script, or if you want to access child nodes added in the editor, make sure the node is ready using [method is_node_ready].
## [codeblock]
## func _notification(what):
## if what == NOTIFICATION_TRANSLATION_CHANGED:
## if not is_node_ready():
## await ready # Wait until ready signal.
## $Label.text = atr("%d Bananas") % banana_counter
## [/codeblock]
const NOTIFICATION_TRANSLATION_CHANGED = 2010;

## Notification received from the OS when a request for "About" information is sent.
## Implemented only on macOS.
const NOTIFICATION_WM_ABOUT = 2011;

## Notification received from Godot's crash handler when the engine is about to crash.
## Implemented on desktop platforms, if the crash handler is enabled.
const NOTIFICATION_CRASH = 2012;

## Notification received from the OS when an update of the Input Method Engine occurs (e.g. change of IME cursor position or composition string).
## Implemented only on macOS.
const NOTIFICATION_OS_IME_UPDATE = 2013;

## Notification received from the OS when the application is resumed.
## Specific to the Android and iOS platforms.
const NOTIFICATION_APPLICATION_RESUMED = 2014;

## Notification received from the OS when the application is paused.
## Specific to the Android and iOS platforms.
## [b]Note:[/b] On iOS, you only have approximately 5 seconds to finish a task started by this signal. If you go over this allotment, iOS will kill the app instead of pausing it.
const NOTIFICATION_APPLICATION_PAUSED = 2015;

## Notification received from the OS when the application is focused, i.e. when changing the focus from the OS desktop or a thirdparty application to any open window of the Godot instance.
## Implemented on desktop and mobile platforms.
const NOTIFICATION_APPLICATION_FOCUS_IN = 2016;

## Notification received from the OS when the application is defocused, i.e. when changing the focus from any open window of the Godot instance to the OS desktop or a thirdparty application.
## Implemented on desktop and mobile platforms.
const NOTIFICATION_APPLICATION_FOCUS_OUT = 2017;

## Notification received when the [TextServer] is changed.
const NOTIFICATION_TEXT_SERVER_CHANGED = 2018;

## Inherits [member process_mode] from the node's parent. This is the default for any newly created node.
## Stops processing when [member SceneTree.paused] is [code]true[/code]. This is the inverse of [constant PROCESS_MODE_WHEN_PAUSED], and the default for the root node.
## Process [b]only[/b] when [member SceneTree.paused] is [code]true[/code]. This is the inverse of [constant PROCESS_MODE_PAUSABLE].
## Always process. Keeps processing, ignoring [member SceneTree.paused]. This is the inverse of [constant PROCESS_MODE_DISABLED].
## Never process. Completely disables processing, ignoring [member SceneTree.paused]. This is the inverse of [constant PROCESS_MODE_ALWAYS].
## Process this node based on the thread group mode of the first parent (or grandparent) node that has a thread group mode that is not inherit. See [member process_thread_group] for more information.
## Process this node (and child nodes set to inherit) on the main thread. See [member process_thread_group] for more information.
## Process this node (and child nodes set to inherit) on a sub-thread. See [member process_thread_group] for more information.
## Allows this node to process threaded messages created with [method call_deferred_thread_group] right before [method _process] is called.
## Allows this node to process threaded messages created with [method call_deferred_thread_group] right before [method _physics_process] is called.
## Allows this node to process threaded messages created with [method call_deferred_thread_group] right before either [method _process] or [method _physics_process] are called.
## Inherits [member physics_interpolation_mode] from the node's parent. This is the default for any newly created node.
## Enables physics interpolation for this node and for children set to [constant PHYSICS_INTERPOLATION_MODE_INHERIT]. This is the default for the root node.
## Disables physics interpolation for this node and for children set to [constant PHYSICS_INTERPOLATION_MODE_INHERIT].
## Duplicate the node's signal connections.
## Duplicate the node's groups.
## Duplicate the node's script (also overriding the duplicated children's scripts, if combined with [constant DUPLICATE_USE_INSTANTIATION]).
## Duplicate using [method PackedScene.instantiate]. If the node comes from a scene saved on disk, reuses [method PackedScene.instantiate] as the base for the duplicated node and its children.
## The node will not be internal.
## The node will be placed at the beginning of the parent's children, before any non-internal sibling.
## The node will be placed at the end of the parent's children, after any non-internal sibling.
## Inherits [member auto_translate_mode] from the node's parent. This is the default for any newly created node.
## Always automatically translate. This is the inverse of [constant AUTO_TRANSLATE_MODE_DISABLED], and the default for the root node.
## Never automatically translate. This is the inverse of [constant AUTO_TRANSLATE_MODE_ALWAYS].
## String parsing for POT generation will be skipped for this node and children that are set to [constant AUTO_TRANSLATE_MODE_INHERIT].

#enum ProcessMode
enum {
    PROCESS_MODE_INHERIT = 0,
    PROCESS_MODE_PAUSABLE = 1,
    PROCESS_MODE_WHEN_PAUSED = 2,
    PROCESS_MODE_ALWAYS = 3,
    PROCESS_MODE_DISABLED = 4,
}
#enum ProcessThreadGroup
enum {
    PROCESS_THREAD_GROUP_INHERIT = 0,
    PROCESS_THREAD_GROUP_MAIN_THREAD = 1,
    PROCESS_THREAD_GROUP_SUB_THREAD = 2,
}
#enum ProcessThreadMessages
enum {
    FLAG_PROCESS_THREAD_MESSAGES = 1,
    FLAG_PROCESS_THREAD_MESSAGES_PHYSICS = 2,
    FLAG_PROCESS_THREAD_MESSAGES_ALL = 3,
}
#enum PhysicsInterpolationMode
enum {
    PHYSICS_INTERPOLATION_MODE_INHERIT = 0,
    PHYSICS_INTERPOLATION_MODE_ON = 1,
    PHYSICS_INTERPOLATION_MODE_OFF = 2,
}
#enum DuplicateFlags
enum {
    DUPLICATE_SIGNALS = 1,
    DUPLICATE_GROUPS = 2,
    DUPLICATE_SCRIPTS = 4,
    DUPLICATE_USE_INSTANTIATION = 8,
}
#enum InternalMode
enum {
    INTERNAL_MODE_DISABLED = 0,
    INTERNAL_MODE_FRONT = 1,
    INTERNAL_MODE_BACK = 2,
}
#enum AutoTranslateMode
enum {
    AUTO_TRANSLATE_MODE_INHERIT = 0,
    AUTO_TRANSLATE_MODE_ALWAYS = 1,
    AUTO_TRANSLATE_MODE_DISABLED = 2,
}
#enum ProcessMode
enum ProcessMode {
    PROCESS_MODE_INHERIT = 0,
    PROCESS_MODE_PAUSABLE = 1,
    PROCESS_MODE_WHEN_PAUSED = 2,
    PROCESS_MODE_ALWAYS = 3,
    PROCESS_MODE_DISABLED = 4,
}
#enum ProcessThreadGroup
enum ProcessThreadGroup {
    PROCESS_THREAD_GROUP_INHERIT = 0,
    PROCESS_THREAD_GROUP_MAIN_THREAD = 1,
    PROCESS_THREAD_GROUP_SUB_THREAD = 2,
}
#enum ProcessThreadMessages
enum ProcessThreadMessages {
    FLAG_PROCESS_THREAD_MESSAGES = 1,
    FLAG_PROCESS_THREAD_MESSAGES_PHYSICS = 2,
    FLAG_PROCESS_THREAD_MESSAGES_ALL = 3,
}
#enum PhysicsInterpolationMode
enum PhysicsInterpolationMode {
    PHYSICS_INTERPOLATION_MODE_INHERIT = 0,
    PHYSICS_INTERPOLATION_MODE_ON = 1,
    PHYSICS_INTERPOLATION_MODE_OFF = 2,
}
#enum DuplicateFlags
enum DuplicateFlags {
    DUPLICATE_SIGNALS = 1,
    DUPLICATE_GROUPS = 2,
    DUPLICATE_SCRIPTS = 4,
    DUPLICATE_USE_INSTANTIATION = 8,
}
#enum InternalMode
enum InternalMode {
    INTERNAL_MODE_DISABLED = 0,
    INTERNAL_MODE_FRONT = 1,
    INTERNAL_MODE_BACK = 2,
}
#enum AutoTranslateMode
enum AutoTranslateMode {
    AUTO_TRANSLATE_MODE_INHERIT = 0,
    AUTO_TRANSLATE_MODE_ALWAYS = 1,
    AUTO_TRANSLATE_MODE_DISABLED = 2,
}
## Defines if any text should automatically change to its translated version depending on the current locale (for nodes such as [Label], [RichTextLabel], [Window], etc.). Also decides if the node's strings should be parsed for POT generation.
## [b]Note:[/b] For the root node, auto translate mode can also be set via [member ProjectSettings.internationalization/rendering/root_node_auto_translate].
var auto_translate_mode: int:
	get = get_auto_translate_mode, set = set_auto_translate_mode

## An optional description to the node. It will be displayed as a tooltip when hovering over the node in the editor's Scene dock.
var editor_description: String:
	get = get_editor_description, set = set_editor_description

## The [MultiplayerAPI] instance associated with this node. See [method SceneTree.get_multiplayer].
## [b]Note:[/b] Renaming the node, or moving it in the tree, will not move the [MultiplayerAPI] to the new path, you will have to update this manually.
var multiplayer: MultiplayerAPI:
	get = get_multiplayer

## The name of the node. This name must be unique among the siblings (other child nodes from the same parent). When set to an existing sibling's name, the node is automatically renamed.
## [b]Note:[/b] When changing the name, the following characters will be replaced with an underscore: ([code].[/code] [code]:[/code] [code]@[/code] [code]/[/code] [code]"[/code] [code]%[/code]). In particular, the [code]@[/code] character is reserved for auto-generated names. See also [method String.validate_node_name].
var name: StringName:
	get = get_name, set = set_name

## The owner of this node. The owner must be an ancestor of this node. When packing the owner node in a [PackedScene], all the nodes it owns are also saved with it. See also [member unique_name_in_owner].
## [b]Note:[/b] In the editor, nodes not owned by the scene root are usually not displayed in the Scene dock, and will [b]not[/b] be saved. To prevent this, remember to set the owner after calling [method add_child].
var owner: Node:
	get = get_owner, set = set_owner

## Allows enabling or disabling physics interpolation per node, offering a finer grain of control than turning physics interpolation on and off globally. See [member ProjectSettings.physics/common/physics_interpolation] and [member SceneTree.physics_interpolation] for the global setting.
## [b]Note:[/b] When teleporting a node to a distant position you should temporarily disable interpolation with [method Node.reset_physics_interpolation].
var physics_interpolation_mode: int:
	get = get_physics_interpolation_mode, set = set_physics_interpolation_mode

## The node's processing behavior (see [enum ProcessMode]). To check if the node can process in its current mode, use [method can_process].
var process_mode: int:
	get = get_process_mode, set = set_process_mode

## Similar to [member process_priority] but for [constant NOTIFICATION_PHYSICS_PROCESS], [method _physics_process], or [constant NOTIFICATION_INTERNAL_PHYSICS_PROCESS].
var process_physics_priority: int:
	get = get_physics_process_priority, set = set_physics_process_priority

## The node's execution order of the process callbacks ([method _process], [constant NOTIFICATION_PROCESS], and [constant NOTIFICATION_INTERNAL_PROCESS]). Nodes whose priority value is [i]lower[/i] call their process callbacks first, regardless of tree order.
var process_priority: int:
	get = get_process_priority, set = set_process_priority

## Set the process thread group for this node (basically, whether it receives [constant NOTIFICATION_PROCESS], [constant NOTIFICATION_PHYSICS_PROCESS], [method _process] or [method _physics_process] (and the internal versions) on the main thread or in a sub-thread.
## By default, the thread group is [constant PROCESS_THREAD_GROUP_INHERIT], which means that this node belongs to the same thread group as the parent node. The thread groups means that nodes in a specific thread group will process together, separate to other thread groups (depending on [member process_thread_group_order]). If the value is set is [constant PROCESS_THREAD_GROUP_SUB_THREAD], this thread group will occur on a sub thread (not the main thread), otherwise if set to [constant PROCESS_THREAD_GROUP_MAIN_THREAD] it will process on the main thread. If there is not a parent or grandparent node set to something other than inherit, the node will belong to the [i]default thread group[/i]. This default group will process on the main thread and its group order is 0.
## During processing in a sub-thread, accessing most functions in nodes outside the thread group is forbidden (and it will result in an error in debug mode). Use [method Object.call_deferred], [method call_thread_safe], [method call_deferred_thread_group] and the likes in order to communicate from the thread groups to the main thread (or to other thread groups).
## To better understand process thread groups, the idea is that any node set to any other value than [constant PROCESS_THREAD_GROUP_INHERIT] will include any child (and grandchild) nodes set to inherit into its process thread group. This means that the processing of all the nodes in the group will happen together, at the same time as the node including them.
var process_thread_group: int:
	get = get_process_thread_group, set = set_process_thread_group

## Change the process thread group order. Groups with a lesser order will process before groups with a greater order. This is useful when a large amount of nodes process in sub thread and, afterwards, another group wants to collect their result in the main thread, as an example.
var process_thread_group_order: int:
	get = get_process_thread_group_order, set = set_process_thread_group_order

## Set whether the current thread group will process messages (calls to [method call_deferred_thread_group] on threads), and whether it wants to receive them during regular process or physics process callbacks.
var process_thread_messages: int:
	get = get_process_thread_messages, set = set_process_thread_messages

## The original scene's file path, if the node has been instantiated from a [PackedScene] file. Only scene root nodes contains this.
var scene_file_path: String:
	get = get_scene_file_path, set = set_scene_file_path

## If [code]true[/code], the node can be accessed from any node sharing the same [member owner] or from the [member owner] itself, with special [code]%Name[/code] syntax in [method get_node].
## [b]Note:[/b] If another node with the same [member owner] shares the same [member name] as this node, the other node will no longer be accessible as unique.
var unique_name_in_owner: bool:
	get = is_unique_name_in_owner, set = set_unique_name_in_owner



## Called when the node enters the [SceneTree] (e.g. upon instantiating, scene changing, or after calling [method add_child] in a script). If the node has children, its [method _enter_tree] callback will be called first, and then that of the children.
## Corresponds to the [constant NOTIFICATION_ENTER_TREE] notification in [method Object._notification].
func _enter_tree() -> void:
	pass;

## Called when the node is about to leave the [SceneTree] (e.g. upon freeing, scene changing, or after calling [method remove_child] in a script). If the node has children, its [method _exit_tree] callback will be called last, after all its children have left the tree.
## Corresponds to the [constant NOTIFICATION_EXIT_TREE] notification in [method Object._notification] and signal [signal tree_exiting]. To get notified when the node has already left the active tree, connect to the [signal tree_exited].
func _exit_tree() -> void:
	pass;

## The elements in the array returned from this method are displayed as warnings in the Scene dock if the script that overrides it is a [code]tool[/code] script.
## Returning an empty array produces no warnings.
## Call [method update_configuration_warnings] when the warnings need to be updated for this node.
## [codeblock]
## @export var energy = 0:
## set(value):
## energy = value
## update_configuration_warnings()
## func _get_configuration_warnings():
## if energy < 0:
## return ["Energy must be 0 or greater."]
## else:
## return []
## [/codeblock]
func _get_configuration_warnings() -> PackedStringArray:
	pass;

## Called when there is an input event. The input event propagates up through the node tree until a node consumes it.
## It is only called if input processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_input].
## To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
## For gameplay input, [method _unhandled_input] and [method _unhandled_key_input] are usually a better fit as they allow the GUI to intercept the events first.
## [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
func _input(event: InputEvent) -> void:
	pass;

## Called during the physics processing step of the main loop. Physics processing means that the frame rate is synced to the physics, i.e. the [param delta] parameter will [i]generally[/i] be constant (see exceptions below). [param delta] is in seconds.
## It is only called if physics processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_physics_process].
## Processing happens in order of [member process_physics_priority], lower priority values are called first. Nodes with the same priority are processed in tree order, or top to bottom as seen in the editor (also known as pre-order traversal).
## Corresponds to the [constant NOTIFICATION_PHYSICS_PROCESS] notification in [method Object._notification].
## [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
## [b]Note:[/b] [param delta] will be larger than expected if running at a framerate lower than [member Engine.physics_ticks_per_second] / [member Engine.max_physics_steps_per_frame] FPS. This is done to avoid "spiral of death" scenarios where performance would plummet due to an ever-increasing number of physics steps per frame. This behavior affects both [method _process] and [method _physics_process]. As a result, avoid using [param delta] for time measurements in real-world seconds. Use the [Time] singleton's methods for this purpose instead, such as [method Time.get_ticks_usec].
func _physics_process(delta: float) -> void:
	pass;

## Called during the processing step of the main loop. Processing happens at every frame and as fast as possible, so the [param delta] time since the previous frame is not constant. [param delta] is in seconds.
## It is only called if processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process].
## Processing happens in order of [member process_priority], lower priority values are called first. Nodes with the same priority are processed in tree order, or top to bottom as seen in the editor (also known as pre-order traversal).
## Corresponds to the [constant NOTIFICATION_PROCESS] notification in [method Object._notification].
## [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
## [b]Note:[/b] [param delta] will be larger than expected if running at a framerate lower than [member Engine.physics_ticks_per_second] / [member Engine.max_physics_steps_per_frame] FPS. This is done to avoid "spiral of death" scenarios where performance would plummet due to an ever-increasing number of physics steps per frame. This behavior affects both [method _process] and [method _physics_process]. As a result, avoid using [param delta] for time measurements in real-world seconds. Use the [Time] singleton's methods for this purpose instead, such as [method Time.get_ticks_usec].
func _process(delta: float) -> void:
	pass;

## Called when the node is "ready", i.e. when both the node and its children have entered the scene tree. If the node has children, their [method _ready] callbacks get triggered first, and the parent node will receive the ready notification afterwards.
## Corresponds to the [constant NOTIFICATION_READY] notification in [method Object._notification]. See also the [code]@onready[/code] annotation for variables.
## Usually used for initialization. For even earlier initialization, [method Object._init] may be used. See also [method _enter_tree].
## [b]Note:[/b] This method may be called only once for each node. After removing a node from the scene tree and adding it again, [method _ready] will [b]not[/b] be called a second time. This can be bypassed by requesting another call with [method request_ready], which may be called anywhere before adding the node again.
func _ready() -> void:
	pass;

## Called when an [InputEventKey], [InputEventShortcut], or [InputEventJoypadButton] hasn't been consumed by [method _input] or any GUI [Control] item. It is called before [method _unhandled_key_input] and [method _unhandled_input]. The input event propagates up through the node tree until a node consumes it.
## It is only called if shortcut processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_shortcut_input].
## To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
## This method can be used to handle shortcuts. For generic GUI events, use [method _input] instead. Gameplay events should usually be handled with either [method _unhandled_input] or [method _unhandled_key_input].
## [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not orphan).
func _shortcut_input(event: InputEvent) -> void:
	pass;

## Called when an [InputEvent] hasn't been consumed by [method _input] or any GUI [Control] item. It is called after [method _shortcut_input] and after [method _unhandled_key_input]. The input event propagates up through the node tree until a node consumes it.
## It is only called if unhandled input processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_unhandled_input].
## To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
## For gameplay input, this method is usually a better fit than [method _input], as GUI events need a higher priority. For keyboard shortcuts, consider using [method _shortcut_input] instead, as it is called before this method. Finally, to handle keyboard events, consider using [method _unhandled_key_input] for performance reasons.
## [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
func _unhandled_input(event: InputEvent) -> void:
	pass;

## Called when an [InputEventKey] hasn't been consumed by [method _input] or any GUI [Control] item. It is called after [method _shortcut_input] but before [method _unhandled_input]. The input event propagates up through the node tree until a node consumes it.
## It is only called if unhandled key input processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_unhandled_key_input].
## To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
## This method can be used to handle Unicode character input with [kbd]Alt[/kbd], [kbd]Alt + Ctrl[/kbd], and [kbd]Alt + Shift[/kbd] modifiers, after shortcuts were handled.
## For gameplay input, this and [method _unhandled_input] are usually a better fit than [method _input], as GUI events should be handled first. This method also performs better than [method _unhandled_input], since unrelated events such as [InputEventMouseMotion] are automatically filtered. For shortcuts, consider using [method _shortcut_input] instead.
## [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
func _unhandled_key_input(event: InputEvent) -> void:
	pass;

## Adds a child [param node]. Nodes can have any number of children, but every child must have a unique name. Child nodes are automatically deleted when the parent node is deleted, so an entire scene can be removed by deleting its topmost node.
## If [param force_readable_name] is [code]true[/code], improves the readability of the added [param node]. If not named, the [param node] is renamed to its type, and if it shares [member name] with a sibling, a number is suffixed more appropriately. This operation is very slow. As such, it is recommended leaving this to [code]false[/code], which assigns a dummy name featuring [code]@[/code] in both situations.
## If [param internal] is different than [constant INTERNAL_MODE_DISABLED], the child will be added as internal node. These nodes are ignored by methods like [method get_children], unless their parameter [code]include_internal[/code] is [code]true[/code]. The intended usage is to hide the internal nodes from the user, so the user won't accidentally delete or modify them. Used by some GUI nodes, e.g. [ColorPicker]. See [enum InternalMode] for available modes.
## [b]Note:[/b] If [param node] already has a parent, this method will fail. Use [method remove_child] first to remove [param node] from its current parent. For example:
## [codeblocks]
## [gdscript]
## var child_node = get_child(0)
## if child_node.get_parent():
## child_node.get_parent().remove_child(child_node)
## add_child(child_node)
## [/gdscript]
## [csharp]
## Node childNode = GetChild(0);
## if (childNode.GetParent() != null)
## {
## childNode.GetParent().RemoveChild(childNode);
## }
## AddChild(childNode);
## [/csharp]
## [/codeblocks]
## If you need the child node to be added below a specific node in the list of children, use [method add_sibling] instead of this method.
## [b]Note:[/b] If you want a child to be persisted to a [PackedScene], you must set [member owner] in addition to calling [method add_child]. This is typically relevant for [url=$DOCS_URL/tutorials/plugins/running_code_in_the_editor.html]tool scripts[/url] and [url=$DOCS_URL/tutorials/plugins/editor/index.html]editor plugins[/url]. If [method add_child] is called without setting [member owner], the newly added [Node] will not be visible in the scene tree, though it will be visible in the 2D/3D view.
func add_child(node: Node, force_readable_name: bool = false, internal: int = 0) -> void:
	pass;

## Adds a [param sibling] node to this node's parent, and moves the added sibling right below this node.
## If [param force_readable_name] is [code]true[/code], improves the readability of the added [param sibling]. If not named, the [param sibling] is renamed to its type, and if it shares [member name] with a sibling, a number is suffixed more appropriately. This operation is very slow. As such, it is recommended leaving this to [code]false[/code], which assigns a dummy name featuring [code]@[/code] in both situations.
## Use [method add_child] instead of this method if you don't need the child node to be added below a specific node in the list of children.
## [b]Note:[/b] If this node is internal, the added sibling will be internal too (see [method add_child]'s [code]internal[/code] parameter).
func add_sibling(sibling: Node, force_readable_name: bool = false) -> void:
	pass;

## Adds the node to the [param group]. Groups can be helpful to organize a subset of nodes, for example [code]"enemies"[/code] or [code]"collectables"[/code]. See notes in the description, and the group methods in [SceneTree].
## If [param persistent] is [code]true[/code], the group will be stored when saved inside a [PackedScene]. All groups created and displayed in the Node dock are persistent.
## [b]Note:[/b] To improve performance, the order of group names is [i]not[/i] guaranteed and may vary between project runs. Therefore, do not rely on the group order.
## [b]Note:[/b] [SceneTree]'s group methods will [i]not[/i] work on this node if not inside the tree (see [method is_inside_tree]).
func add_to_group(group: StringName, persistent: bool = false) -> void:
	pass;

## Translates a [param message], using the translation catalogs configured in the Project Settings. Further [param context] can be specified to help with the translation. Note that most [Control] nodes automatically translate their strings, so this method is mostly useful for formatted strings or custom drawn text.
## This method works the same as [method Object.tr], with the addition of respecting the [member auto_translate_mode] state.
## If [method Object.can_translate_messages] is [code]false[/code], or no translation is available, this method returns the [param message] without changes. See [method Object.set_message_translation].
## For detailed examples, see [url=$DOCS_URL/tutorials/i18n/internationalizing_games.html]Internationalizing games[/url].
func atr(message: String, context: StringName = "") -> String:
	pass;

## Translates a [param message] or [param plural_message], using the translation catalogs configured in the Project Settings. Further [param context] can be specified to help with the translation.
## This method works the same as [method Object.tr_n], with the addition of respecting the [member auto_translate_mode] state.
## If [method Object.can_translate_messages] is [code]false[/code], or no translation is available, this method returns [param message] or [param plural_message], without changes. See [method Object.set_message_translation].
## The [param n] is the number, or amount, of the message's subject. It is used by the translation system to fetch the correct plural form for the current language.
## For detailed examples, see [url=$DOCS_URL/tutorials/i18n/localization_using_gettext.html]Localization using gettext[/url].
## [b]Note:[/b] Negative and [float] numbers may not properly apply to some countable subjects. It's recommended to handle these cases with [method atr].
func atr_n(message: String, plural_message: StringName, n: int, context: StringName = "") -> String:
	pass;

## This function is similar to [method Object.call_deferred] except that the call will take place when the node thread group is processed. If the node thread group processes in sub-threads, then the call will be done on that thread, right before [constant NOTIFICATION_PROCESS] or [constant NOTIFICATION_PHYSICS_PROCESS], the [method _process] or [method _physics_process] or their internal versions are called.
vararg func call_deferred_thread_group(method: StringName) -> Variant:
	pass;

## This function ensures that the calling of this function will succeed, no matter whether it's being done from a thread or not. If called from a thread that is not allowed to call the function, the call will become deferred. Otherwise, the call will go through directly.
vararg func call_thread_safe(method: StringName) -> Variant:
	pass;

## Returns [code]true[/code] if the node can receive processing notifications and input callbacks ([constant NOTIFICATION_PROCESS], [method _input], etc.) from the [SceneTree] and [Viewport]. The returned value depends on [member process_mode]:
## - If set to [constant PROCESS_MODE_PAUSABLE], returns [code]true[/code] when the game is processing, i.e. [member SceneTree.paused] is [code]false[/code];
## - If set to [constant PROCESS_MODE_WHEN_PAUSED], returns [code]true[/code] when the game is paused, i.e. [member SceneTree.paused] is [code]true[/code];
## - If set to [constant PROCESS_MODE_ALWAYS], always returns [code]true[/code];
## - If set to [constant PROCESS_MODE_DISABLED], always returns [code]false[/code];
## - If set to [constant PROCESS_MODE_INHERIT], use the parent node's [member process_mode] to determine the result.
## If the node is not inside the tree, returns [code]false[/code] no matter the value of [member process_mode].
func can_process() -> bool:
	pass;

## Creates a new [Tween] and binds it to this node.
## This is the equivalent of doing:
## [codeblocks]
## [gdscript]
## get_tree().create_tween().bind_node(self)
## [/gdscript]
## [csharp]
## GetTree().CreateTween().BindNode(this);
## [/csharp]
## [/codeblocks]
## The Tween will start automatically on the next process frame or physics frame (depending on [enum Tween.TweenProcessMode]). See [method Tween.bind_node] for more info on Tweens bound to nodes.
## [b]Note:[/b] The method can still be used when the node is not inside [SceneTree]. It can fail in an unlikely case of using a custom [MainLoop].
func create_tween() -> Tween:
	pass;

## Duplicates the node, returning a new node with all of its properties, signals, groups, and children copied from the original. The behavior can be tweaked through the [param flags] (see [enum DuplicateFlags]).
## [b]Note:[/b] For nodes with a [Script] attached, if [method Object._init] has been defined with required parameters, the duplicated node will not have a [Script].
func duplicate(flags: int = 15) -> Node:
	pass;

## Finds the first descendant of this node whose [member name] matches [param pattern], returning [code]null[/code] if no match is found. The matching is done against node names, [i]not[/i] their paths, through [method String.match]. As such, it is case-sensitive, [code]"*"[/code] matches zero or more characters, and [code]"?"[/code] matches any single character.
## If [param recursive] is [code]false[/code], only this node's direct children are checked. Nodes are checked in tree order, so this node's first direct child is checked first, then its own direct children, etc., before moving to the second direct child, and so on. Internal children are also included in the search (see [code]internal[/code] parameter in [method add_child]).
## If [param owned] is [code]true[/code], only descendants with a valid [member owner] node are checked.
## [b]Note:[/b] This method can be very slow. Consider storing a reference to the found node in a variable. Alternatively, use [method get_node] with unique names (see [member unique_name_in_owner]).
## [b]Note:[/b] To find all descendant nodes matching a pattern or a class type, see [method find_children].
func find_child(pattern: String, recursive: bool = true, owned: bool = true) -> Node:
	pass;

## Finds all descendants of this node whose names match [param pattern], returning an empty [Array] if no match is found. The matching is done against node names, [i]not[/i] their paths, through [method String.match]. As such, it is case-sensitive, [code]"*"[/code] matches zero or more characters, and [code]"?"[/code] matches any single character.
## If [param type] is not empty, only ancestors inheriting from [param type] are included (see [method Object.is_class]).
## If [param recursive] is [code]false[/code], only this node's direct children are checked. Nodes are checked in tree order, so this node's first direct child is checked first, then its own direct children, etc., before moving to the second direct child, and so on. Internal children are also included in the search (see [code]internal[/code] parameter in [method add_child]).
## If [param owned] is [code]true[/code], only descendants with a valid [member owner] node are checked.
## [b]Note:[/b] This method can be very slow. Consider storing references to the found nodes in a variable.
## [b]Note:[/b] To find a single descendant node matching a pattern, see [method find_child].
func find_children(pattern: String, type: String = "", recursive: bool = true, owned: bool = true) -> Array[Node]:
	pass;

## Finds the first ancestor of this node whose [member name] matches [param pattern], returning [code]null[/code] if no match is found. The matching is done through [method String.match]. As such, it is case-sensitive, [code]"*"[/code] matches zero or more characters, and [code]"?"[/code] matches any single character. See also [method find_child] and [method find_children].
## [b]Note:[/b] As this method walks upwards in the scene tree, it can be slow in large, deeply nested nodes. Consider storing a reference to the found node in a variable. Alternatively, use [method get_node] with unique names (see [member unique_name_in_owner]).
func find_parent(pattern: String) -> Node:
	pass;

## Fetches a child node by its index. Each child node has an index relative its siblings (see [method get_index]). The first child is at index 0. Negative values can also be used to start from the end of the list. This method can be used in combination with [method get_child_count] to iterate over this node's children. If no child exists at the given index, this method returns [code]null[/code] and an error is generated.
## If [param include_internal] is [code]false[/code], internal children are ignored (see [method add_child]'s [code]internal[/code] parameter).
## [codeblock]
## # Assuming the following are children of this node, in order:
## # First, Middle, Last.
## var a = get_child(0).name  # a is "First"
## var b = get_child(1).name  # b is "Middle"
## var b = get_child(2).name  # b is "Last"
## var c = get_child(-1).name # c is "Last"
## [/codeblock]
## [b]Note:[/b] To fetch a node by [NodePath], use [method get_node].
func get_child(idx: int, include_internal: bool = false) -> Node:
	pass;

## Returns the number of children of this node.
## If [param include_internal] is [code]false[/code], internal children are not counted (see [method add_child]'s [code]internal[/code] parameter).
func get_child_count(include_internal: bool = false) -> int:
	pass;

## Returns all children of this node inside an [Array].
## If [param include_internal] is [code]false[/code], excludes internal children from the returned array (see [method add_child]'s [code]internal[/code] parameter).
func get_children(include_internal: bool = false) -> Array[Node]:
	pass;

## Returns an [Array] of group names that the node has been added to.
## [b]Note:[/b] To improve performance, the order of group names is [i]not[/i] guaranteed and may vary between project runs. Therefore, do not rely on the group order.
## [b]Note:[/b] This method may also return some group names starting with an underscore ([code]_[/code]). These are internally used by the engine. To avoid conflicts, do not use custom groups starting with underscores. To exclude internal groups, see the following code snippet:
## [codeblocks]
## [gdscript]
## # Stores the node's non-internal groups only (as an array of StringNames).
## var non_internal_groups = []
## for group in get_groups():
## if not str(group).begins_with("_"):
## non_internal_groups.push_back(group)
## [/gdscript]
## [csharp]
## // Stores the node's non-internal groups only (as a List of StringNames).
## List<string> nonInternalGroups = new List<string>();
## foreach (string group in GetGroups())
## {
## if (!group.BeginsWith("_"))
## nonInternalGroups.Add(group);
## }
## [/csharp]
## [/codeblocks]
func get_groups() -> Array[StringName]:
	pass;

## Returns this node's order among its siblings. The first node's index is [code]0[/code]. See also [method get_child].
## If [param include_internal] is [code]false[/code], returns the index ignoring internal children. The first, non-internal child will have an index of [code]0[/code] (see [method add_child]'s [code]internal[/code] parameter).
func get_index(include_internal: bool = false) -> int:
	pass;

## Returns the [Window] that contains this node, or the last exclusive child in a chain of windows starting with the one that contains this node.
func get_last_exclusive_window() -> Window:
	pass;

## Returns the peer ID of the multiplayer authority for this node. See [method set_multiplayer_authority].
func get_multiplayer_authority() -> int:
	pass;

## Fetches a node. The [NodePath] can either be a relative path (from this node), or an absolute path (from the [member SceneTree.root]) to a node. If [param path] does not point to a valid node, generates an error and returns [code]null[/code]. Attempts to access methods on the return value will result in an [i]"Attempt to call <method> on a null instance."[/i] error.
## [b]Note:[/b] Fetching by absolute path only works when the node is inside the scene tree (see [method is_inside_tree]).
## [b]Example:[/b] Assume this method is called from the Character node, inside the following tree:
## [codeblock lang=text]
## ┖╴root
## ┠╴Character (you are here!)
## ┃  ┠╴Sword
## ┃  ┖╴Backpack
## ┃     ┖╴Dagger
## ┠╴MyGame
## ┖╴Swamp
## ┠╴Alligator
## ┠╴Mosquito
## ┖╴Goblin
## [/codeblock]
## The following calls will return a valid node:
## [codeblocks]
## [gdscript]
## get_node("Sword")
## get_node("Backpack/Dagger")
## get_node("../Swamp/Alligator")
## get_node("/root/MyGame")
## [/gdscript]
## [csharp]
## GetNode("Sword");
## GetNode("Backpack/Dagger");
## GetNode("../Swamp/Alligator");
## GetNode("/root/MyGame");
## [/csharp]
## [/codeblocks]
func get_node(path: NodePath) -> Node:
	pass;

## Fetches a node and its most nested resource as specified by the [NodePath]'s subname. Returns an [Array] of size [code]3[/code] where:
## - Element [code]0[/code] is the [Node], or [code]null[/code] if not found;
## - Element [code]1[/code] is the subname's last nested [Resource], or [code]null[/code] if not found;
## - Element [code]2[/code] is the remaining [NodePath], referring to an existing, non-[Resource] property (see [method Object.get_indexed]).
## [b]Example:[/b] Assume that the child's [member Sprite2D.texture] has been assigned a [AtlasTexture]:
## [codeblocks]
## [gdscript]
## var a = get_node_and_resource("Area2D/Sprite2D")
## print(a[0].name) # Prints Sprite2D
## print(a[1])      # Prints <null>
## print(a[2])      # Prints ^""
## var b = get_node_and_resource("Area2D/Sprite2D:texture:atlas")
## print(b[0].name)        # Prints Sprite2D
## print(b[1].get_class()) # Prints AtlasTexture
## print(b[2])             # Prints ^""
## var c = get_node_and_resource("Area2D/Sprite2D:texture:atlas:region")
## print(c[0].name)        # Prints Sprite2D
## print(c[1].get_class()) # Prints AtlasTexture
## print(c[2])             # Prints ^":region"
## [/gdscript]
## [csharp]
## var a = GetNodeAndResource(NodePath("Area2D/Sprite2D"));
## GD.Print(a[0].Name); // Prints Sprite2D
## GD.Print(a[1]);      // Prints <null>
## GD.Print(a[2]);      // Prints ^"
## var b = GetNodeAndResource(NodePath("Area2D/Sprite2D:texture:atlas"));
## GD.Print(b[0].name);        // Prints Sprite2D
## GD.Print(b[1].get_class()); // Prints AtlasTexture
## GD.Print(b[2]);             // Prints ^""
## var c = GetNodeAndResource(NodePath("Area2D/Sprite2D:texture:atlas:region"));
## GD.Print(c[0].name);        // Prints Sprite2D
## GD.Print(c[1].get_class()); // Prints AtlasTexture
## GD.Print(c[2]);             // Prints ^":region"
## [/csharp]
## [/codeblocks]
func get_node_and_resource(path: NodePath) -> Array:
	pass;

## Fetches a node by [NodePath]. Similar to [method get_node], but does not generate an error if [param path] does not point to a valid node.
func get_node_or_null(path: NodePath) -> Node:
	pass;

## Returns this node's parent node, or [code]null[/code] if the node doesn't have a parent.
func get_parent() -> Node:
	pass;

## Returns the node's absolute path, relative to the [member SceneTree.root]. If the node is not inside the scene tree, this method fails and returns an empty [NodePath].
func get_path() -> NodePath:
	pass;

## Returns the relative [NodePath] from this node to the specified [param node]. Both nodes must be in the same [SceneTree] or scene hierarchy, otherwise this method fails and returns an empty [NodePath].
## If [param use_unique_path] is [code]true[/code], returns the shortest path accounting for this node's unique name (see [member unique_name_in_owner]).
## [b]Note:[/b] If you get a relative path which starts from a unique node, the path may be longer than a normal relative path, due to the addition of the unique node's name.
func get_path_to(node: Node, use_unique_path: bool = false) -> NodePath:
	pass;

## Returns the time elapsed (in seconds) since the last physics callback. This value is identical to [method _physics_process]'s [code]delta[/code] parameter, and is often consistent at run-time, unless [member Engine.physics_ticks_per_second] is changed. See also [constant NOTIFICATION_PHYSICS_PROCESS].
## [b]Note:[/b] The returned value will be larger than expected if running at a framerate lower than [member Engine.physics_ticks_per_second] / [member Engine.max_physics_steps_per_frame] FPS. This is done to avoid "spiral of death" scenarios where performance would plummet due to an ever-increasing number of physics steps per frame. This behavior affects both [method _process] and [method _physics_process]. As a result, avoid using [code]delta[/code] for time measurements in real-world seconds. Use the [Time] singleton's methods for this purpose instead, such as [method Time.get_ticks_usec].
func get_physics_process_delta_time() -> float:
	pass;

## Returns the time elapsed (in seconds) since the last process callback. This value is identical to [method _process]'s [code]delta[/code] parameter, and may vary from frame to frame. See also [constant NOTIFICATION_PROCESS].
## [b]Note:[/b] The returned value will be larger than expected if running at a framerate lower than [member Engine.physics_ticks_per_second] / [member Engine.max_physics_steps_per_frame] FPS. This is done to avoid "spiral of death" scenarios where performance would plummet due to an ever-increasing number of physics steps per frame. This behavior affects both [method _process] and [method _physics_process]. As a result, avoid using [code]delta[/code] for time measurements in real-world seconds. Use the [Time] singleton's methods for this purpose instead, such as [method Time.get_ticks_usec].
func get_process_delta_time() -> float:
	pass;

## Returns a [Dictionary] mapping method names to their RPC configuration defined for this node using [method rpc_config].
func get_rpc_config() -> Variant:
	pass;

## Returns [code]true[/code] if this node is an instance load placeholder. See [InstancePlaceholder] and [method set_scene_instance_load_placeholder].
func get_scene_instance_load_placeholder() -> bool:
	pass;

## Returns the [SceneTree] that contains this node. If this node is not inside the tree, generates an error and returns [code]null[/code]. See also [method is_inside_tree].
func get_tree() -> SceneTree:
	pass;

## Returns the tree as a [String]. Used mainly for debugging purposes. This version displays the path relative to the current node, and is good for copy/pasting into the [method get_node] function. It also can be used in game UI/UX.
## May print, for example:
## [codeblock lang=text]
## TheGame
## TheGame/Menu
## TheGame/Menu/Label
## TheGame/Menu/Camera2D
## TheGame/SplashScreen
## TheGame/SplashScreen/Camera2D
## [/codeblock]
func get_tree_string() -> String:
	pass;

## Similar to [method get_tree_string], this returns the tree as a [String]. This version displays a more graphical representation similar to what is displayed in the Scene Dock. It is useful for inspecting larger trees.
## May print, for example:
## [codeblock lang=text]
## ┖╴TheGame
## ┠╴Menu
## ┃  ┠╴Label
## ┃  ┖╴Camera2D
## ┖╴SplashScreen
## ┖╴Camera2D
## [/codeblock]
func get_tree_string_pretty() -> String:
	pass;

## Returns the node's closest [Viewport] ancestor, if the node is inside the tree. Otherwise, returns [code]null[/code].
func get_viewport() -> Viewport:
	pass;

## Returns the [Window] that contains this node. If the node is in the main window, this is equivalent to getting the root node ([code]get_tree().get_root()[/code]).
func get_window() -> Window:
	pass;

## Returns [code]true[/code] if the [param path] points to a valid node. See also [method get_node].
func has_node(path: NodePath) -> bool:
	pass;

## Returns [code]true[/code] if [param path] points to a valid node and its subnames point to a valid [Resource], e.g. [code]Area2D/CollisionShape2D:shape[/code]. Properties that are not [Resource] types (such as nodes or other [Variant] types) are not considered. See also [method get_node_and_resource].
func has_node_and_resource(path: NodePath) -> bool:
	pass;

## Returns [code]true[/code] if the given [param node] is a direct or indirect child of this node.
func is_ancestor_of(node: Node) -> bool:
	pass;

## Returns [code]true[/code] if the node is folded (collapsed) in the Scene dock. This method is intended to be used in editor plugins and tools. See also [method set_display_folded].
func is_displayed_folded() -> bool:
	pass;

## Returns [code]true[/code] if [param node] has editable children enabled relative to this node. This method is intended to be used in editor plugins and tools. See also [method set_editable_instance].
func is_editable_instance(node: Node) -> bool:
	pass;

## Returns [code]true[/code] if the given [param node] occurs later in the scene hierarchy than this node. A node occurring later is usually processed last.
func is_greater_than(node: Node) -> bool:
	pass;

## Returns [code]true[/code] if this node has been added to the given [param group]. See [method add_to_group] and [method remove_from_group]. See also notes in the description, and the [SceneTree]'s group methods.
func is_in_group(group: StringName) -> bool:
	pass;

## Returns [code]true[/code] if this node is currently inside a [SceneTree]. See also [method get_tree].
func is_inside_tree() -> bool:
	pass;

## Returns [code]true[/code] if the local system is the multiplayer authority of this node.
func is_multiplayer_authority() -> bool:
	pass;

## Returns [code]true[/code] if the node is ready, i.e. it's inside scene tree and all its children are initialized.
## [method request_ready] resets it back to [code]false[/code].
func is_node_ready() -> bool:
	pass;

## Returns [code]true[/code] if the node is part of the scene currently opened in the editor.
func is_part_of_edited_scene() -> bool:
	pass;

## Returns [code]true[/code] if physics interpolation is enabled for this node (see [member physics_interpolation_mode]).
## [b]Note:[/b] Interpolation will only be active if both the flag is set [b]and[/b] physics interpolation is enabled within the [SceneTree]. This can be tested using [method is_physics_interpolated_and_enabled].
func is_physics_interpolated() -> bool:
	pass;

## Returns [code]true[/code] if physics interpolation is enabled (see [member physics_interpolation_mode]) [b]and[/b] enabled in the [SceneTree].
## This is a convenience version of [method is_physics_interpolated] that also checks whether physics interpolation is enabled globally.
## See [member SceneTree.physics_interpolation] and [member ProjectSettings.physics/common/physics_interpolation].
func is_physics_interpolated_and_enabled() -> bool:
	pass;

## Returns [code]true[/code] if physics processing is enabled (see [method set_physics_process]).
func is_physics_processing() -> bool:
	pass;

## Returns [code]true[/code] if internal physics processing is enabled (see [method set_physics_process_internal]).
func is_physics_processing_internal() -> bool:
	pass;

## Returns [code]true[/code] if processing is enabled (see [method set_process]).
func is_processing() -> bool:
	pass;

## Returns [code]true[/code] if the node is processing input (see [method set_process_input]).
func is_processing_input() -> bool:
	pass;

## Returns [code]true[/code] if internal processing is enabled (see [method set_process_internal]).
func is_processing_internal() -> bool:
	pass;

## Returns [code]true[/code] if the node is processing shortcuts (see [method set_process_shortcut_input]).
func is_processing_shortcut_input() -> bool:
	pass;

## Returns [code]true[/code] if the node is processing unhandled input (see [method set_process_unhandled_input]).
func is_processing_unhandled_input() -> bool:
	pass;

## Returns [code]true[/code] if the node is processing unhandled key input (see [method set_process_unhandled_key_input]).
func is_processing_unhandled_key_input() -> bool:
	pass;

## Moves [param child_node] to the given index. A node's index is the order among its siblings. If [param to_index] is negative, the index is counted from the end of the list. See also [method get_child] and [method get_index].
## [b]Note:[/b] The processing order of several engine callbacks ([method _ready], [method _process], etc.) and notifications sent through [method propagate_notification] is affected by tree order. [CanvasItem] nodes are also rendered in tree order. See also [member process_priority].
func move_child(child_node: Node, to_index: int) -> void:
	pass;

## Similar to [method call_deferred_thread_group], but for notifications.
func notify_deferred_thread_group(what: int) -> void:
	pass;

## Similar to [method call_thread_safe], but for notifications.
func notify_thread_safe(what: int) -> void:
	pass;

## Prints all orphan nodes (nodes outside the [SceneTree]). Useful for debugging.
## [b]Note:[/b] This method only works in debug builds. Does nothing in a project exported in release mode.
static func print_orphan_nodes() -> void:
	pass;

## Prints the node and its children to the console, recursively. The node does not have to be inside the tree. This method outputs [NodePath]s relative to this node, and is good for copy/pasting into [method get_node]. See also [method print_tree_pretty].
## May print, for example:
## [codeblock lang=text]
## .
## Menu
## Menu/Label
## Menu/Camera2D
## SplashScreen
## SplashScreen/Camera2D
## [/codeblock]
func print_tree() -> void:
	pass;

## Prints the node and its children to the console, recursively. The node does not have to be inside the tree. Similar to [method print_tree], but the graphical representation looks like what is displayed in the editor's Scene dock. It is useful for inspecting larger trees.
## May print, for example:
## [codeblock lang=text]
## ┖╴TheGame
## ┠╴Menu
## ┃  ┠╴Label
## ┃  ┖╴Camera2D
## ┖╴SplashScreen
## ┖╴Camera2D
## [/codeblock]
func print_tree_pretty() -> void:
	pass;

## Calls the given [param method] name, passing [param args] as arguments, on this node and all of its children, recursively.
## If [param parent_first] is [code]true[/code], the method is called on this node first, then on all of its children. If [code]false[/code], the children's methods are called first.
func propagate_call(method: StringName, args: Array = [], parent_first: bool = false) -> void:
	pass;

## Calls [method Object.notification] with [param what] on this node and all of its children, recursively.
func propagate_notification(what: int) -> void:
	pass;

## Queues this node to be deleted at the end of the current frame. When deleted, all of its children are deleted as well, and all references to the node and its children become invalid.
## Unlike with [method Object.free], the node is not deleted instantly, and it can still be accessed before deletion. It is also safe to call [method queue_free] multiple times. Use [method Object.is_queued_for_deletion] to check if the node will be deleted at the end of the frame.
## [b]Note:[/b] The node will only be freed after all other deferred calls are finished. Using this method is not always the same as calling [method Object.free] through [method Object.call_deferred].
func queue_free() -> void:
	pass;

## Removes a child [param node]. The [param node], along with its children, are [b]not[/b] deleted. To delete a node, see [method queue_free].
## [b]Note:[/b] When this node is inside the tree, this method sets the [member owner] of the removed [param node] (or its descendants) to [code]null[/code], if their [member owner] is no longer an ancestor (see [method is_ancestor_of]).
func remove_child(node: Node) -> void:
	pass;

## Removes the node from the given [param group]. Does nothing if the node is not in the [param group]. See also notes in the description, and the [SceneTree]'s group methods.
func remove_from_group(group: StringName) -> void:
	pass;

## Changes the parent of this [Node] to the [param new_parent]. The node needs to already have a parent. The node's [member owner] is preserved if its owner is still reachable from the new location (i.e., the node is still a descendant of the new parent after the operation).
## If [param keep_global_transform] is [code]true[/code], the node's global transform will be preserved if supported. [Node2D], [Node3D] and [Control] support this argument (but [Control] keeps only position).
func reparent(new_parent: Node, keep_global_transform: bool = true) -> void:
	pass;

## Replaces this node by the given [param node]. All children of this node are moved to [param node].
## If [param keep_groups] is [code]true[/code], the [param node] is added to the same groups that the replaced node is in (see [method add_to_group]).
## [b]Warning:[/b] The replaced node is removed from the tree, but it is [b]not[/b] deleted. To prevent memory leaks, store a reference to the node in a variable, or use [method Object.free].
func replace_by(node: Node, keep_groups: bool = false) -> void:
	pass;

## Requests [method _ready] to be called again the next time the node enters the tree. Does [b]not[/b] immediately call [method _ready].
## [b]Note:[/b] This method only affects the current node. If the node's children also need to request ready, this method needs to be called for each one of them. When the node and its children enter the tree again, the order of [method _ready] callbacks will be the same as normal.
func request_ready() -> void:
	pass;

## When physics interpolation is active, moving a node to a radically different transform (such as placement within a level) can result in a visible glitch as the object is rendered moving from the old to new position over the physics tick.
## That glitch can be prevented by calling this method, which temporarily disables interpolation until the physics tick is complete.
## The notification [constant NOTIFICATION_RESET_PHYSICS_INTERPOLATION] will be received by the node and all children recursively.
## [b]Note:[/b] This function should be called [b]after[/b] moving the node, rather than before.
func reset_physics_interpolation() -> void:
	pass;

## Sends a remote procedure call request for the given [param method] to peers on the network (and locally), sending additional arguments to the method called by the RPC. The call request will only be received by nodes with the same [NodePath], including the exact same [member name]. Behavior depends on the RPC configuration for the given [param method] (see [method rpc_config] and [annotation @GDScript.@rpc]). By default, methods are not exposed to RPCs.
## May return [constant OK] if the call is successful, [constant ERR_INVALID_PARAMETER] if the arguments passed in the [param method] do not match, [constant ERR_UNCONFIGURED] if the node's [member multiplayer] cannot be fetched (such as when the node is not inside the tree), [constant ERR_CONNECTION_ERROR] if [member multiplayer]'s connection is not available.
## [b]Note:[/b] You can only safely use RPCs on clients after you received the [signal MultiplayerAPI.connected_to_server] signal from the [MultiplayerAPI]. You also need to keep track of the connection state, either by the [MultiplayerAPI] signals like [signal MultiplayerAPI.server_disconnected] or by checking ([code]get_multiplayer().peer.get_connection_status() == CONNECTION_CONNECTED[/code]).
vararg func rpc(method: StringName) -> int:
	pass;

## Changes the RPC configuration for the given [param method]. [param config] should either be [code]null[/code] to disable the feature (as by default), or a [Dictionary] containing the following entries:
## - [code]rpc_mode[/code]: see [enum MultiplayerAPI.RPCMode];
## - [code]transfer_mode[/code]: see [enum MultiplayerPeer.TransferMode];
## - [code]call_local[/code]: if [code]true[/code], the method will also be called locally;
## - [code]channel[/code]: an [int] representing the channel to send the RPC on.
## [b]Note:[/b] In GDScript, this method corresponds to the [annotation @GDScript.@rpc] annotation, with various parameters passed ([code]@rpc(any)[/code], [code]@rpc(authority)[/code]...). See also the [url=$DOCS_URL/tutorials/networking/high_level_multiplayer.html]high-level multiplayer[/url] tutorial.
func rpc_config(method: StringName, config: Variant) -> void:
	pass;

## Sends a [method rpc] to a specific peer identified by [param peer_id] (see [method MultiplayerPeer.set_target_peer]).
## May return [constant OK] if the call is successful, [constant ERR_INVALID_PARAMETER] if the arguments passed in the [param method] do not match, [constant ERR_UNCONFIGURED] if the node's [member multiplayer] cannot be fetched (such as when the node is not inside the tree), [constant ERR_CONNECTION_ERROR] if [member multiplayer]'s connection is not available.
vararg func rpc_id(peer_id: int, method: StringName) -> int:
	pass;

## Similar to [method call_deferred_thread_group], but for setting properties.
func set_deferred_thread_group(property: StringName, value: Variant) -> void:
	pass;

## If set to [code]true[/code], the node appears folded in the Scene dock. As a result, all of its children are hidden. This method is intended to be used in editor plugins and tools, but it also works in release builds. See also [method is_displayed_folded].
func set_display_folded(fold: bool) -> void:
	pass;

## Set to [code]true[/code] to allow all nodes owned by [param node] to be available, and editable, in the Scene dock, even if their [member owner] is not the scene root. This method is intended to be used in editor plugins and tools, but it also works in release builds. See also [method is_editable_instance].
func set_editable_instance(node: Node, is_editable: bool) -> void:
	pass;

## Sets the node's multiplayer authority to the peer with the given peer [param id]. The multiplayer authority is the peer that has authority over the node on the network. Defaults to peer ID 1 (the server). Useful in conjunction with [method rpc_config] and the [MultiplayerAPI].
## If [param recursive] is [code]true[/code], the given peer is recursively set as the authority for all children of this node.
## [b]Warning:[/b] This does [b]not[/b] automatically replicate the new authority to other peers. It is the developer's responsibility to do so. You may replicate the new authority's information using [member MultiplayerSpawner.spawn_function], an RPC, or a [MultiplayerSynchronizer]. Furthermore, the parent's authority does [b]not[/b] propagate to newly added children.
func set_multiplayer_authority(id: int, recursive: bool = true) -> void:
	pass;

## If set to [code]true[/code], enables physics (fixed framerate) processing. When a node is being processed, it will receive a [constant NOTIFICATION_PHYSICS_PROCESS] at a fixed (usually 60 FPS, see [member Engine.physics_ticks_per_second] to change) interval (and the [method _physics_process] callback will be called if it exists).
## [b]Note:[/b] If [method _physics_process] is overridden, this will be automatically enabled before [method _ready] is called.
func set_physics_process(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables internal physics for this node. Internal physics processing happens in isolation from the normal [method _physics_process] calls and is used by some nodes internally to guarantee proper functioning even if the node is paused or physics processing is disabled for scripting ([method set_physics_process]).
## [b]Warning:[/b] Built-in nodes rely on internal processing for their internal logic. Disabling it is unsafe and may lead to unexpected behavior. Use this method if you know what you are doing.
func set_physics_process_internal(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables processing. When a node is being processed, it will receive a [constant NOTIFICATION_PROCESS] on every drawn frame (and the [method _process] callback will be called if it exists).
## [b]Note:[/b] If [method _process] is overridden, this will be automatically enabled before [method _ready] is called.
## [b]Note:[/b] This method only affects the [method _process] callback, i.e. it has no effect on other callbacks like [method _physics_process]. If you want to disable all processing for the node, set [member process_mode] to [constant PROCESS_MODE_DISABLED].
func set_process(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables input processing.
## [b]Note:[/b] If [method _input] is overridden, this will be automatically enabled before [method _ready] is called. Input processing is also already enabled for GUI controls, such as [Button] and [TextEdit].
func set_process_input(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables internal processing for this node. Internal processing happens in isolation from the normal [method _process] calls and is used by some nodes internally to guarantee proper functioning even if the node is paused or processing is disabled for scripting ([method set_process]).
## [b]Warning:[/b] Built-in nodes rely on internal processing for their internal logic. Disabling it is unsafe and may lead to unexpected behavior. Use this method if you know what you are doing.
func set_process_internal(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables shortcut processing for this node.
## [b]Note:[/b] If [method _shortcut_input] is overridden, this will be automatically enabled before [method _ready] is called.
func set_process_shortcut_input(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables unhandled input processing. It enables the node to receive all input that was not previously handled (usually by a [Control]).
## [b]Note:[/b] If [method _unhandled_input] is overridden, this will be automatically enabled before [method _ready] is called. Unhandled input processing is also already enabled for GUI controls, such as [Button] and [TextEdit].
func set_process_unhandled_input(enable: bool) -> void:
	pass;

## If set to [code]true[/code], enables unhandled key input processing.
## [b]Note:[/b] If [method _unhandled_key_input] is overridden, this will be automatically enabled before [method _ready] is called.
func set_process_unhandled_key_input(enable: bool) -> void:
	pass;

## If set to [code]true[/code], the node becomes a [InstancePlaceholder] when packed and instantiated from a [PackedScene]. See also [method get_scene_instance_load_placeholder].
func set_scene_instance_load_placeholder(load_placeholder: bool) -> void:
	pass;

## Similar to [method call_thread_safe], but for setting properties.
func set_thread_safe(property: StringName, value: Variant) -> void:
	pass;

## Makes this node inherit the translation domain from its parent node. If this node has no parent, the main translation domain will be used.
## This is the default behavior for all nodes. Calling [method Object.set_translation_domain] disables this behavior.
func set_translation_domain_inherited() -> void:
	pass;

## Refreshes the warnings displayed for this node in the Scene dock. Use [method _get_configuration_warnings] to customize the warning messages to display.
func update_configuration_warnings() -> void:
	pass;


func get_auto_translate_mode() -> int:
	return auto_translate_mode

func set_auto_translate_mode(value: int) -> void:
	auto_translate_mode = value

func get_editor_description() -> String:
	return editor_description

func set_editor_description(value: String) -> void:
	editor_description = value

func get_multiplayer() -> MultiplayerAPI:
	return multiplayer

func get_name() -> StringName:
	return name

func set_name(value: StringName) -> void:
	name = value

func get_owner() -> Node:
	return owner

func set_owner(value: Node) -> void:
	owner = value

func get_physics_interpolation_mode() -> int:
	return physics_interpolation_mode

func set_physics_interpolation_mode(value: int) -> void:
	physics_interpolation_mode = value

func get_process_mode() -> int:
	return process_mode

func set_process_mode(value: int) -> void:
	process_mode = value

func get_physics_process_priority() -> int:
	return process_physics_priority

func set_physics_process_priority(value: int) -> void:
	process_physics_priority = value

func get_process_priority() -> int:
	return process_priority

func set_process_priority(value: int) -> void:
	process_priority = value

func get_process_thread_group() -> int:
	return process_thread_group

func set_process_thread_group(value: int) -> void:
	process_thread_group = value

func get_process_thread_group_order() -> int:
	return process_thread_group_order

func set_process_thread_group_order(value: int) -> void:
	process_thread_group_order = value

func get_process_thread_messages() -> int:
	return process_thread_messages

func set_process_thread_messages(value: int) -> void:
	process_thread_messages = value

func get_scene_file_path() -> String:
	return scene_file_path

func set_scene_file_path(value: String) -> void:
	scene_file_path = value

func is_unique_name_in_owner() -> bool:
	return unique_name_in_owner

func set_unique_name_in_owner(value: bool) -> void:
	unique_name_in_owner = value

