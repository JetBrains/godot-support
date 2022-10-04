#brief Base class for all [i]scene[/i] objects.
#desc Nodes are Godot's building blocks. They can be assigned as the child of another node, resulting in a tree arrangement. A given node can contain any number of nodes as children with the requirement that all siblings (direct children of a node) should have unique names.
#desc A tree of nodes is called a [i]scene[/i]. Scenes can be saved to the disk and then instantiated into other scenes. This allows for very high flexibility in the architecture and data model of Godot projects.
#desc [b]Scene tree:[/b] The [SceneTree] contains the active tree of nodes. When a node is added to the scene tree, it receives the [constant NOTIFICATION_ENTER_TREE] notification and its [method _enter_tree] callback is triggered. Child nodes are always added [i]after[/i] their parent node, i.e. the [method _enter_tree] callback of a parent node will be triggered before its child's.
#desc Once all nodes have been added in the scene tree, they receive the [constant NOTIFICATION_READY] notification and their respective [method _ready] callbacks are triggered. For groups of nodes, the [method _ready] callback is called in reverse order, starting with the children and moving up to the parent nodes.
#desc This means that when adding a node to the scene tree, the following order will be used for the callbacks: [method _enter_tree] of the parent, [method _enter_tree] of the children, [method _ready] of the children and finally [method _ready] of the parent (recursively for the entire scene tree).
#desc [b]Processing:[/b] Nodes can override the "process" state, so that they receive a callback on each frame requesting them to process (do something). Normal processing (callback [method _process], toggled with [method set_process]) happens as fast as possible and is dependent on the frame rate, so the processing time [i]delta[/i] (in seconds) is passed as an argument. Physics processing (callback [method _physics_process], toggled with [method set_physics_process]) happens a fixed number of times per second (60 by default) and is useful for code related to the physics engine.
#desc Nodes can also process input events. When present, the [method _input] function will be called for each input that the program receives. In many cases, this can be overkill (unless used for simple projects), and the [method _unhandled_input] function might be preferred; it is called when the input event was not handled by anyone else (typically, GUI [Control] nodes), ensuring that the node only receives the events that were meant for it.
#desc To keep track of the scene hierarchy (especially when instancing scenes into other scenes), an "owner" can be set for the node with the [member owner] property. This keeps track of who instantiated what. This is mostly useful when writing editors and tools, though.
#desc Finally, when a node is freed with [method Object.free] or [method queue_free], it will also free all its children.
#desc [b]Groups:[/b] Nodes can be added to as many groups as you want to be easy to manage, you could create groups like "enemies" or "collectables" for example, depending on your game. See [method add_to_group], [method is_in_group] and [method remove_from_group]. You can then retrieve all nodes in these groups, iterate them and even call methods on groups via the methods on [SceneTree].
#desc [b]Networking with nodes:[/b] After connecting to a server (or making one, see [ENetMultiplayerPeer]), it is possible to use the built-in RPC (remote procedure call) system to communicate over the network. By calling [method rpc] with a method name, it will be called locally and in all connected peers (peers = clients and the server that accepts connections). To identify which node receives the RPC call, Godot will use its [NodePath] (make sure node names are the same on all peers). Also, take a look at the high-level networking tutorial and corresponding demos.
#desc [b]Note:[/b] The [code]script[/code] property is part of the [Object] class, not [Node]. It isn't exposed like most properties but does have a setter and getter ([code]set_script()[/code] and [code]get_script()[/code]).
class_name Node

#desc Notification received when the node enters a [SceneTree].
#desc This notification is emitted [i]before[/i] the related [signal tree_entered].
const NOTIFICATION_ENTER_TREE = 10;

#desc Notification received when the node is about to exit a [SceneTree].
#desc This notification is emitted [i]after[/i] the related [signal tree_exiting].
const NOTIFICATION_EXIT_TREE = 11;

#desc Notification received when the node is moved in the parent.
const NOTIFICATION_MOVED_IN_PARENT = 12;

#desc Notification received when the node is ready. See [method _ready].
const NOTIFICATION_READY = 13;

#desc Notification received when the node is paused.
const NOTIFICATION_PAUSED = 14;

#desc Notification received when the node is unpaused.
const NOTIFICATION_UNPAUSED = 15;

#desc Notification received every frame when the physics process flag is set (see [method set_physics_process]).
const NOTIFICATION_PHYSICS_PROCESS = 16;

#desc Notification received every frame when the process flag is set (see [method set_process]).
const NOTIFICATION_PROCESS = 17;

#desc Notification received when a node is set as a child of another node.
#desc [b]Note:[/b] This doesn't mean that a node entered the [SceneTree].
const NOTIFICATION_PARENTED = 18;

#desc Notification received when a node is unparented (parent removed it from the list of children).
const NOTIFICATION_UNPARENTED = 19;

#desc Notification received by scene owner when its scene is instantiated.
const NOTIFICATION_SCENE_INSTANTIATED = 20;

#desc Notification received when a drag operation begins. All nodes receive this notification, not only the dragged one.
#desc Can be triggered either by dragging a [Control] that provides drag data (see [method Control._get_drag_data]) or using [method Control.force_drag].
#desc Use [method Viewport.gui_get_drag_data] to get the dragged data.
const NOTIFICATION_DRAG_BEGIN = 21;

#desc Notification received when a drag operation ends.
#desc Use [method Viewport.gui_is_drag_successful] to check if the drag succeeded.
const NOTIFICATION_DRAG_END = 22;

#desc Notification received when the node's name or one of its parents' name is changed. This notification is [i]not[/i] received when the node is removed from the scene tree to be added to another parent later on.
const NOTIFICATION_PATH_RENAMED = 23;

#desc Notification received every frame when the internal process flag is set (see [method set_process_internal]).
const NOTIFICATION_INTERNAL_PROCESS = 25;

#desc Notification received every frame when the internal physics process flag is set (see [method set_physics_process_internal]).
const NOTIFICATION_INTERNAL_PHYSICS_PROCESS = 26;

#desc Notification received when the node is ready, just before [constant NOTIFICATION_READY] is received. Unlike the latter, it's sent every time the node enters tree, instead of only once.
const NOTIFICATION_POST_ENTER_TREE = 27;

#desc Notification received when the node is disabled. See [constant PROCESS_MODE_DISABLED].
const NOTIFICATION_DISABLED = 28;

#desc Notification received when the node is enabled again after being disabled. See [constant PROCESS_MODE_DISABLED].
const NOTIFICATION_ENABLED = 29;

#desc Notification received right before the scene with the node is saved in the editor. This notification is only sent in the Godot editor and will not occur in exported projects.
const NOTIFICATION_EDITOR_PRE_SAVE = 9001;

#desc Notification received right after the scene with the node is saved in the editor. This notification is only sent in the Godot editor and will not occur in exported projects.
const NOTIFICATION_EDITOR_POST_SAVE = 9002;

#desc Notification received from the OS when the mouse enters the game window.
#desc Implemented on desktop and web platforms.
const NOTIFICATION_WM_MOUSE_ENTER = 1002;

#desc Notification received from the OS when the mouse leaves the game window.
#desc Implemented on desktop and web platforms.
const NOTIFICATION_WM_MOUSE_EXIT = 1003;

#desc Notification received from the OS when the node's parent [Window] is focused. This may be a change of focus between two windows of the same engine instance, or from the OS desktop or a third-party application to a window of the game (in which case [constant NOTIFICATION_APPLICATION_FOCUS_IN] is also emitted).
const NOTIFICATION_WM_WINDOW_FOCUS_IN = 1004;

#desc Notification received from the OS when the node's parent [Window] is defocused. This may be a change of focus between two windows of the same engine instance, or from a window of the game to the OS desktop or a third-party application (in which case [constant NOTIFICATION_APPLICATION_FOCUS_OUT] is also emitted).
const NOTIFICATION_WM_WINDOW_FOCUS_OUT = 1005;

#desc Notification received from the OS when a close request is sent (e.g. closing the window with a "Close" button or [kbd]Alt + F4[/kbd]).
#desc Implemented on desktop platforms.
const NOTIFICATION_WM_CLOSE_REQUEST = 1006;

#desc Notification received from the OS when a go back request is sent (e.g. pressing the "Back" button on Android).
#desc Specific to the Android platform.
const NOTIFICATION_WM_GO_BACK_REQUEST = 1007;

const NOTIFICATION_WM_SIZE_CHANGED = 1008;

const NOTIFICATION_WM_DPI_CHANGE = 1009;

#desc Notification received when the mouse enters the viewport.
const NOTIFICATION_VP_MOUSE_ENTER = 1010;

#desc Notification received when the mouse leaves the viewport.
const NOTIFICATION_VP_MOUSE_EXIT = 1011;

#desc Notification received from the OS when the application is exceeding its allocated memory.
#desc Specific to the iOS platform.
const NOTIFICATION_OS_MEMORY_WARNING = 2009;

#desc Notification received when translations may have changed. Can be triggered by the user changing the locale. Can be used to respond to language changes, for example to change the UI strings on the fly. Useful when working with the built-in translation support, like [method Object.tr].
const NOTIFICATION_TRANSLATION_CHANGED = 2010;

#desc Notification received from the OS when a request for "About" information is sent.
#desc Specific to the macOS platform.
const NOTIFICATION_WM_ABOUT = 2011;

#desc Notification received from Godot's crash handler when the engine is about to crash.
#desc Implemented on desktop platforms if the crash handler is enabled.
const NOTIFICATION_CRASH = 2012;

#desc Notification received from the OS when an update of the Input Method Engine occurs (e.g. change of IME cursor position or composition string).
#desc Specific to the macOS platform.
const NOTIFICATION_OS_IME_UPDATE = 2013;

#desc Notification received from the OS when the application is resumed.
#desc Specific to the Android platform.
const NOTIFICATION_APPLICATION_RESUMED = 2014;

#desc Notification received from the OS when the application is paused.
#desc Specific to the Android platform.
const NOTIFICATION_APPLICATION_PAUSED = 2015;

#desc Notification received from the OS when the application is focused, i.e. when changing the focus from the OS desktop or a thirdparty application to any open window of the Godot instance.
#desc Implemented on desktop platforms.
const NOTIFICATION_APPLICATION_FOCUS_IN = 2016;

#desc Notification received from the OS when the application is defocused, i.e. when changing the focus from any open window of the Godot instance to the OS desktop or a thirdparty application.
#desc Implemented on desktop platforms.
const NOTIFICATION_APPLICATION_FOCUS_OUT = 2017;

#desc Notification received when text server is changed.
const NOTIFICATION_TEXT_SERVER_CHANGED = 2018;

#desc Inherits process mode from the node's parent. For the root node, it is equivalent to [constant PROCESS_MODE_PAUSABLE]. Default.
const PROCESS_MODE_INHERIT = 0;

#desc Stops processing when the [SceneTree] is paused (process when unpaused). This is the inverse of [constant PROCESS_MODE_WHEN_PAUSED].
const PROCESS_MODE_PAUSABLE = 1;

#desc Only process when the [SceneTree] is paused (don't process when unpaused). This is the inverse of [constant PROCESS_MODE_PAUSABLE].
const PROCESS_MODE_WHEN_PAUSED = 2;

#desc Always process. Continue processing always, ignoring the [SceneTree]'s paused property. This is the inverse of [constant PROCESS_MODE_DISABLED].
const PROCESS_MODE_ALWAYS = 3;

#desc Never process. Completely disables processing, ignoring the [SceneTree]'s paused property. This is the inverse of [constant PROCESS_MODE_ALWAYS].
const PROCESS_MODE_DISABLED = 4;

#desc Duplicate the node's signals.
const DUPLICATE_SIGNALS = 1;

#desc Duplicate the node's groups.
const DUPLICATE_GROUPS = 2;

#desc Duplicate the node's scripts.
const DUPLICATE_SCRIPTS = 4;

#desc Duplicate using instancing.
#desc An instance stays linked to the original so when the original changes, the instance changes too.
const DUPLICATE_USE_INSTANCING = 8;

#desc Node will not be internal.
const INTERNAL_MODE_DISABLED = 0;

#desc Node will be placed at the front of parent's node list, before any non-internal sibling.
const INTERNAL_MODE_FRONT = 1;

#desc Node will be placed at the back of parent's node list, after any non-internal sibling.
const INTERNAL_MODE_BACK = 2;


#desc Add a custom description to a node. It will be displayed in a tooltip when hovered in editor's scene tree.
var editor_description: String;

#desc The [MultiplayerAPI] instance associated with this node. See [method SceneTree.get_multiplayer].
var multiplayer: MultiplayerAPI;

#desc The name of the node. This name is unique among the siblings (other child nodes from the same parent). When set to an existing name, the node will be automatically renamed.
#desc [b]Note:[/b] Auto-generated names might include the [code]@[/code] character, which is reserved for unique names when using [method add_child]. When setting the name manually, any [code]@[/code] will be removed.
var name: StringName;

#desc The node owner. A node can have any other node as owner (as long as it is a valid parent, grandparent, etc. ascending in the tree). When saving a node (using [PackedScene]), all the nodes it owns will be saved with it. This allows for the creation of complex [SceneTree]s, with instancing and subinstancing.
#desc [b]Note:[/b] If you want a child to be persisted to a [PackedScene], you must set [member owner] in addition to calling [method add_child]. This is typically relevant for [url=$DOCS_URL/tutorials/misc/running_code_in_the_editor.html]tool scripts[/url] and [url=$DOCS_URL/tutorials/plugins/editor/index.html]editor plugins[/url]. If [method add_child] is called without setting [member owner], the newly added [Node] will not be visible in the scene tree, though it will be visible in the 2D/3D view.
var owner: Node;

#desc Can be used to pause or unpause the node, or make the node paused based on the [SceneTree], or make it inherit the process mode from its parent (default).
var process_mode: int;

#desc The node's priority in the execution order of the enabled processing callbacks (i.e. [constant NOTIFICATION_PROCESS], [constant NOTIFICATION_PHYSICS_PROCESS] and their internal counterparts). Nodes whose process priority value is [i]lower[/i] will have their processing callbacks executed first.
var process_priority: int;

#desc If a scene is instantiated from a file, its topmost node contains the absolute file path from which it was loaded in [member scene_file_path] (e.g. [code]res://levels/1.tscn[/code]). Otherwise, [member scene_file_path] is set to an empty string.
var scene_file_path: String;

#desc Sets this node's name as a unique name in its [member owner]. This allows the node to be accessed as [code]%Name[/code] instead of the full path, from any node within that scene.
#desc If another node with the same owner already had that name declared as unique, that other node's name will no longer be set as having a unique name.
var unique_name_in_owner: bool;



#desc Called when the node enters the [SceneTree] (e.g. upon instancing, scene changing, or after calling [method add_child] in a script). If the node has children, its [method _enter_tree] callback will be called first, and then that of the children.
#desc Corresponds to the [constant NOTIFICATION_ENTER_TREE] notification in [method Object._notification].
virtual func _enter_tree() -> void:
	pass;

#desc Called when the node is about to leave the [SceneTree] (e.g. upon freeing, scene changing, or after calling [method remove_child] in a script). If the node has children, its [method _exit_tree] callback will be called last, after all its children have left the tree.
#desc Corresponds to the [constant NOTIFICATION_EXIT_TREE] notification in [method Object._notification] and signal [signal tree_exiting]. To get notified when the node has already left the active tree, connect to the [signal tree_exited].
virtual func _exit_tree() -> void:
	pass;

#desc The elements in the array returned from this method are displayed as warnings in the Scene Dock if the script that overrides it is a [code]tool[/code] script.
#desc Returning an empty array produces no warnings.
#desc Call [method update_configuration_warnings] when the warnings need to be updated for this node.
virtual const func _get_configuration_warnings() -> PackedStringArray:
	pass;

#desc Called when there is an input event. The input event propagates up through the node tree until a node consumes it.
#desc It is only called if input processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_input].
#desc To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
#desc For gameplay input, [method _unhandled_input] and [method _unhandled_key_input] are usually a better fit as they allow the GUI to intercept the events first.
#desc [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
virtual func _input(event: InputEvent) -> void:
	pass;

#desc Called during the physics processing step of the main loop. Physics processing means that the frame rate is synced to the physics, i.e. the [param delta] variable should be constant. [param delta] is in seconds.
#desc It is only called if physics processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_physics_process].
#desc Corresponds to the [constant NOTIFICATION_PHYSICS_PROCESS] notification in [method Object._notification].
#desc [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
virtual func _physics_process(delta: float) -> void:
	pass;

#desc Called during the processing step of the main loop. Processing happens at every frame and as fast as possible, so the [param delta] time since the previous frame is not constant. [param delta] is in seconds.
#desc It is only called if processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process].
#desc Corresponds to the [constant NOTIFICATION_PROCESS] notification in [method Object._notification].
#desc [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
virtual func _process(delta: float) -> void:
	pass;

#desc Called when the node is "ready", i.e. when both the node and its children have entered the scene tree. If the node has children, their [method _ready] callbacks get triggered first, and the parent node will receive the ready notification afterwards.
#desc Corresponds to the [constant NOTIFICATION_READY] notification in [method Object._notification]. See also the [code]@onready[/code] annotation for variables.
#desc Usually used for initialization. For even earlier initialization, [method Object._init] may be used. See also [method _enter_tree].
#desc [b]Note:[/b] [method _ready] may be called only once for each node. After removing a node from the scene tree and adding it again, [code]_ready[/code] will not be called a second time. This can be bypassed by requesting another call with [method request_ready], which may be called anywhere before adding the node again.
virtual func _ready() -> void:
	pass;

#desc Called when an [InputEventKey] or [InputEventShortcut] hasn't been consumed by [method _input] or any GUI [Control] item. The input event propagates up through the node tree until a node consumes it.
#desc It is only called if shortcut processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_shortcut_input].
#desc To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
#desc This method can be used to handle shortcuts.
#desc [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not orphan).
virtual func _shortcut_input(event: InputEvent) -> void:
	pass;

#desc Called when an [InputEvent] hasn't been consumed by [method _input] or any GUI [Control] item. The input event propagates up through the node tree until a node consumes it.
#desc It is only called if unhandled input processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_unhandled_input].
#desc To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
#desc For gameplay input, this and [method _unhandled_key_input] are usually a better fit than [method _input] as they allow the GUI to intercept the events first.
#desc [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
virtual func _unhandled_input(event: InputEvent) -> void:
	pass;

#desc Called when an [InputEventKey] or [InputEventShortcut] hasn't been consumed by [method _input] or any GUI [Control] item. The input event propagates up through the node tree until a node consumes it.
#desc It is only called if unhandled key input processing is enabled, which is done automatically if this method is overridden, and can be toggled with [method set_process_unhandled_key_input].
#desc To consume the input event and stop it propagating further to other nodes, [method Viewport.set_input_as_handled] can be called.
#desc This method can be used to handle Unicode character input with [kbd]Alt[/kbd], [kbd]Alt + Ctrl[/kbd], and [kbd]Alt + Shift[/kbd] modifiers, after shortcuts were handled.
#desc For gameplay input, this and [method _unhandled_input] are usually a better fit than [method _input] as they allow the GUI to intercept the events first.
#desc [b]Note:[/b] This method is only called if the node is present in the scene tree (i.e. if it's not an orphan).
virtual func _unhandled_key_input(event: InputEvent) -> void:
	pass;

#desc Adds a child [param node]. Nodes can have any number of children, but every child must have a unique name. Child nodes are automatically deleted when the parent node is deleted, so an entire scene can be removed by deleting its topmost node.
#desc If [param force_readable_name] is [code]true[/code], improves the readability of the added [param node]. If not named, the [param node] is renamed to its type, and if it shares [member name] with a sibling, a number is suffixed more appropriately. This operation is very slow. As such, it is recommended leaving this to [code]false[/code], which assigns a dummy name featuring [code]@[/code] in both situations.
#desc If [param internal] is different than [constant INTERNAL_MODE_DISABLED], the child will be added as internal node. Such nodes are ignored by methods like [method get_children], unless their parameter [code]include_internal[/code] is [code]true[/code].The intended usage is to hide the internal nodes from the user, so the user won't accidentally delete or modify them. Used by some GUI nodes, e.g. [ColorPicker]. See [enum InternalMode] for available modes.
#desc [b]Note:[/b] If the child node already has a parent, the function will fail. Use [method remove_child] first to remove the node from its current parent. For example:
#desc [codeblocks]
#desc [gdscript]
#desc var child_node = get_child(0)
#desc if child_node.get_parent():
#desc child_node.get_parent().remove_child(child_node)
#desc add_child(child_node)
#desc [/gdscript]
#desc [csharp]
#desc Node childNode = GetChild(0);
#desc if (childNode.GetParent() != null)
#desc {
#desc childNode.GetParent().RemoveChild(childNode);
#desc }
#desc AddChild(childNode);
#desc [/csharp]
#desc [/codeblocks]
#desc If you need the child node to be added below a specific node in the list of children, use [method add_sibling] instead of this method.
#desc [b]Note:[/b] If you want a child to be persisted to a [PackedScene], you must set [member owner] in addition to calling [method add_child]. This is typically relevant for [url=$DOCS_URL/tutorials/misc/running_code_in_the_editor.html]tool scripts[/url] and [url=$DOCS_URL/tutorials/plugins/editor/index.html]editor plugins[/url]. If [method add_child] is called without setting [member owner], the newly added [Node] will not be visible in the scene tree, though it will be visible in the 2D/3D view.
func add_child(node: Node, force_readable_name: bool, internal: int) -> void:
	pass;

#desc Adds a [param sibling] node to current's node parent, at the same level as that node, right below it.
#desc If [param force_readable_name] is [code]true[/code], improves the readability of the added [param sibling]. If not named, the [param sibling] is renamed to its type, and if it shares [member name] with a sibling, a number is suffixed more appropriately. This operation is very slow. As such, it is recommended leaving this to [code]false[/code], which assigns a dummy name featuring [code]@[/code] in both situations.
#desc Use [method add_child] instead of this method if you don't need the child node to be added below a specific node in the list of children.
#desc [b]Note:[/b] If this node is internal, the new sibling will be internal too (see [code]internal[/code] parameter in [method add_child]).
func add_sibling(sibling: Node, force_readable_name: bool) -> void:
	pass;

#desc Adds the node to a group. Groups are helpers to name and organize a subset of nodes, for example "enemies" or "collectables". A node can be in any number of groups. Nodes can be assigned a group at any time, but will not be added until they are inside the scene tree (see [method is_inside_tree]). See notes in the description, and the group methods in [SceneTree].
#desc The [param persistent] option is used when packing node to [PackedScene] and saving to file. Non-persistent groups aren't stored.
#desc [b]Note:[/b] For performance reasons, the order of node groups is [i]not[/i] guaranteed. The order of node groups should not be relied upon as it can vary across project runs.
func add_to_group(group: StringName, persistent: bool) -> void:
	pass;

#desc Returns [code]true[/code] if the node can process while the scene tree is paused (see [member process_mode]). Always returns [code]true[/code] if the scene tree is not paused, and [code]false[/code] if the node is not in the tree.
func can_process() -> bool:
	pass;

#desc Creates a new [Tween] and binds it to this node. This is equivalent of doing:
#desc [codeblocks]
#desc [gdscript]
#desc get_tree().create_tween().bind_node(self)
#desc [/gdscript]
#desc [csharp]
#desc GetTree().CreateTween().BindNode(this);
#desc [/csharp]
#desc [/codeblocks]
func create_tween() -> Tween:
	pass;

#desc Duplicates the node, returning a new node.
#desc You can fine-tune the behavior using the [param flags] (see [enum DuplicateFlags]).
#desc [b]Note:[/b] It will not work properly if the node contains a script with constructor arguments (i.e. needs to supply arguments to [method Object._init] method). In that case, the node will be duplicated without a script.
func duplicate(flags: int) -> Node:
	pass;

#desc Finds the first descendant of this node whose name matches [param pattern] as in [method String.match].
#desc [param pattern] does not match against the full path, just against individual node names. It is case-sensitive, with [code]"*"[/code] matching zero or more characters and [code]"?"[/code] matching any single character except [code]"."[/code]).
#desc If [param recursive] is [code]true[/code], all child nodes are included, even if deeply nested. Nodes are checked in tree order, so this node's first direct child is checked first, then its own direct children, etc., before moving to the second direct child, and so on. If [param recursive] is [code]false[/code], only this node's direct children are matched.
#desc If [param owned] is [code]true[/code], this method only finds nodes who have an assigned [member Node.owner]. This is especially important for scenes instantiated through a script, because those scenes don't have an owner.
#desc Returns [code]null[/code] if no matching [Node] is found.
#desc [b]Note:[/b] As this method walks through all the descendants of the node, it is the slowest way to get a reference to another node. Whenever possible, consider using [method get_node] with unique names instead (see [member unique_name_in_owner]), or caching the node references into variable.
#desc [b]Note:[/b] To find all descendant nodes matching a pattern or a class type, see [method find_children].
func find_child(pattern: String, recursive: bool, owned: bool) -> Node:
	pass;

#desc Finds descendants of this node whose name matches [param pattern] as in [method String.match], and/or type matches [param type] as in [method Object.is_class].
#desc [param pattern] does not match against the full path, just against individual node names. It is case-sensitive, with [code]"*"[/code] matching zero or more characters and [code]"?"[/code] matching any single character except [code]"."[/code]).
#desc [param type] will check equality or inheritance, and is case-sensitive. [code]"Object"[/code] will match a node whose type is [code]"Node"[/code] but not the other way around.
#desc If [param recursive] is [code]true[/code], all child nodes are included, even if deeply nested. Nodes are checked in tree order, so this node's first direct child is checked first, then its own direct children, etc., before moving to the second direct child, and so on. If [param recursive] is [code]false[/code], only this node's direct children are matched.
#desc If [param owned] is [code]true[/code], this method only finds nodes who have an assigned [member Node.owner]. This is especially important for scenes instantiated through a script, because those scenes don't have an owner.
#desc Returns an empty array if no matching nodes are found.
#desc [b]Note:[/b] As this method walks through all the descendants of the node, it is the slowest way to get references to other nodes. Whenever possible, consider caching the node references into variables.
#desc [b]Note:[/b] If you only want to find the first descendant node that matches a pattern, see [method find_child].
func find_children(pattern: String, type: String, recursive: bool, owned: bool) -> Node[]:
	pass;

#desc Finds the first parent of the current node whose name matches [param pattern] as in [method String.match].
#desc [param pattern] does not match against the full path, just against individual node names. It is case-sensitive, with [code]"*"[/code] matching zero or more characters and [code]"?"[/code] matching any single character except [code]"."[/code]).
#desc [b]Note:[/b] As this method walks upwards in the scene tree, it can be slow in large, deeply nested scene trees. Whenever possible, consider using [method get_node] with unique names instead (see [member unique_name_in_owner]), or caching the node references into variable.
func find_parent(pattern: String) -> Node:
	pass;

#desc Returns a child node by its index (see [method get_child_count]). This method is often used for iterating all children of a node.
#desc Negative indices access the children from the last one.
#desc If [param include_internal] is [code]false[/code], internal children are skipped (see [code]internal[/code] parameter in [method add_child]).
#desc To access a child node via its name, use [method get_node].
func get_child(idx: int, include_internal: bool) -> Node:
	pass;

#desc Returns the number of child nodes.
#desc If [param include_internal] is [code]false[/code], internal children aren't counted (see [code]internal[/code] parameter in [method add_child]).
func get_child_count(include_internal: bool) -> int:
	pass;

#desc Returns an array of references to node's children.
#desc If [param include_internal] is [code]false[/code], the returned array won't include internal children (see [code]internal[/code] parameter in [method add_child]).
func get_children(include_internal: bool) -> Node[]:
	pass;

#desc Returns an array listing the groups that the node is a member of.
#desc [b]Note:[/b] For performance reasons, the order of node groups is [i]not[/i] guaranteed. The order of node groups should not be relied upon as it can vary across project runs.
#desc [b]Note:[/b] The engine uses some group names internally (all starting with an underscore). To avoid conflicts with internal groups, do not add custom groups whose name starts with an underscore. To exclude internal groups while looping over [method get_groups], use the following snippet:
#desc [codeblocks]
#desc [gdscript]
#desc # Stores the node's non-internal groups only (as an array of Strings).
#desc var non_internal_groups = []
#desc for group in get_groups():
#desc if not group.begins_with("_"):
#desc non_internal_groups.push_back(group)
#desc [/gdscript]
#desc [csharp]
#desc // Stores the node's non-internal groups only (as a List of strings).
#desc List<string> nonInternalGroups = new List<string>();
#desc foreach (string group in GetGroups())
#desc {
#desc if (!group.BeginsWith("_"))
#desc nonInternalGroups.Add(group);
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_groups() -> StringName[]:
	pass;

#desc Returns the node's order in the scene tree branch. For example, if called on the first child node the position is [code]0[/code].
#desc If [param include_internal] is [code]false[/code], the index won't take internal children into account, i.e. first non-internal child will have index of 0 (see [code]internal[/code] parameter in [method add_child]).
func get_index(include_internal: bool) -> int:
	pass;

#desc Returns the peer ID of the multiplayer authority for this node. See [method set_multiplayer_authority].
func get_multiplayer_authority() -> int:
	pass;

#desc Fetches a node. The [NodePath] can be either a relative path (from the current node) or an absolute path (in the scene tree) to a node. If the path does not exist, [code]null[/code] is returned and an error is logged. Attempts to access methods on the return value will result in an "Attempt to call <method> on a null instance." error.
#desc [b]Note:[/b] Fetching absolute paths only works when the node is inside the scene tree (see [method is_inside_tree]).
#desc [b]Example:[/b] Assume your current node is Character and the following tree:
#desc [codeblock]
#desc /root
#desc /root/Character
#desc /root/Character/Sword
#desc /root/Character/Backpack/Dagger
#desc /root/MyGame
#desc /root/Swamp/Alligator
#desc /root/Swamp/Mosquito
#desc /root/Swamp/Goblin
#desc [/codeblock]
#desc Possible paths are:
#desc [codeblocks]
#desc [gdscript]
#desc get_node("Sword")
#desc get_node("Backpack/Dagger")
#desc get_node("../Swamp/Alligator")
#desc get_node("/root/MyGame")
#desc [/gdscript]
#desc [csharp]
#desc GetNode("Sword");
#desc GetNode("Backpack/Dagger");
#desc GetNode("../Swamp/Alligator");
#desc GetNode("/root/MyGame");
#desc [/csharp]
#desc [/codeblocks]
func get_node(path: NodePath) -> Node:
	pass;

#desc Fetches a node and one of its resources as specified by the [NodePath]'s subname (e.g. [code]Area2D/CollisionShape2D:shape[/code]). If several nested resources are specified in the [NodePath], the last one will be fetched.
#desc The return value is an array of size 3: the first index points to the [Node] (or [code]null[/code] if not found), the second index points to the [Resource] (or [code]null[/code] if not found), and the third index is the remaining [NodePath], if any.
#desc For example, assuming that [code]Area2D/CollisionShape2D[/code] is a valid node and that its [code]shape[/code] property has been assigned a [RectangleShape2D] resource, one could have this kind of output:
#desc [codeblocks]
#desc [gdscript]
#desc print(get_node_and_resource("Area2D/CollisionShape2D")) # [[CollisionShape2D:1161], Null, ]
#desc print(get_node_and_resource("Area2D/CollisionShape2D:shape")) # [[CollisionShape2D:1161], [RectangleShape2D:1156], ]
#desc print(get_node_and_resource("Area2D/CollisionShape2D:shape:extents")) # [[CollisionShape2D:1161], [RectangleShape2D:1156], :extents]
#desc [/gdscript]
#desc [csharp]
#desc GD.Print(GetNodeAndResource("Area2D/CollisionShape2D")); // [[CollisionShape2D:1161], Null, ]
#desc GD.Print(GetNodeAndResource("Area2D/CollisionShape2D:shape")); // [[CollisionShape2D:1161], [RectangleShape2D:1156], ]
#desc GD.Print(GetNodeAndResource("Area2D/CollisionShape2D:shape:extents")); // [[CollisionShape2D:1161], [RectangleShape2D:1156], :extents]
#desc [/csharp]
#desc [/codeblocks]
func get_node_and_resource(path: NodePath) -> Array:
	pass;

#desc Similar to [method get_node], but does not log an error if [param path] does not point to a valid [Node].
func get_node_or_null(path: NodePath) -> Node:
	pass;

#desc Returns the parent node of the current node, or [code]null[/code] if the node lacks a parent.
func get_parent() -> Node:
	pass;

#desc Returns the absolute path of the current node. This only works if the current node is inside the scene tree (see [method is_inside_tree]).
func get_path() -> NodePath:
	pass;

#desc Returns the relative [NodePath] from this node to the specified [param node]. Both nodes must be in the same scene or the function will fail.
func get_path_to(node: Node) -> NodePath:
	pass;

#desc Returns the time elapsed (in seconds) since the last physics-bound frame (see [method _physics_process]). This is always a constant value in physics processing unless the frames per second is changed via [member Engine.physics_ticks_per_second].
func get_physics_process_delta_time() -> float:
	pass;

#desc Returns the time elapsed (in seconds) since the last process callback. This value may vary from frame to frame.
func get_process_delta_time() -> float:
	pass;

#desc Returns [code]true[/code] if this is an instance load placeholder. See [InstancePlaceholder].
func get_scene_instance_load_placeholder() -> bool:
	pass;

#desc Returns the [SceneTree] that contains this node.
func get_tree() -> SceneTree:
	pass;

#desc Returns the node's [Viewport].
func get_viewport() -> Viewport:
	pass;

#desc Returns [code]true[/code] if the node that the [NodePath] points to exists.
func has_node(path: NodePath) -> bool:
	pass;

#desc Returns [code]true[/code] if the [NodePath] points to a valid node and its subname points to a valid resource, e.g. [code]Area2D/CollisionShape2D:shape[/code]. Properties with a non-[Resource] type (e.g. nodes or primitive math types) are not considered resources.
func has_node_and_resource(path: NodePath) -> bool:
	pass;

#desc Returns [code]true[/code] if the given node is a direct or indirect child of the current node.
func is_ancestor_of(node: Node) -> bool:
	pass;

#desc Returns [code]true[/code] if the node is folded (collapsed) in the Scene dock. This method is only intended for use with editor tooling.
func is_displayed_folded() -> bool:
	pass;

#desc Returns [code]true[/code] if [param node] has editable children enabled relative to this node. This method is only intended for use with editor tooling.
func is_editable_instance(node: Node) -> bool:
	pass;

#desc Returns [code]true[/code] if the given node occurs later in the scene hierarchy than the current node.
func is_greater_than(node: Node) -> bool:
	pass;

#desc Returns [code]true[/code] if this node is in the specified group. See notes in the description, and the group methods in [SceneTree].
func is_in_group(group: StringName) -> bool:
	pass;

#desc Returns [code]true[/code] if this node is currently inside a [SceneTree].
func is_inside_tree() -> bool:
	pass;

#desc Returns [code]true[/code] if the local system is the multiplayer authority of this node.
func is_multiplayer_authority() -> bool:
	pass;

#desc Returns [code]true[/code] if physics processing is enabled (see [method set_physics_process]).
func is_physics_processing() -> bool:
	pass;

#desc Returns [code]true[/code] if internal physics processing is enabled (see [method set_physics_process_internal]).
func is_physics_processing_internal() -> bool:
	pass;

#desc Returns [code]true[/code] if processing is enabled (see [method set_process]).
func is_processing() -> bool:
	pass;

#desc Returns [code]true[/code] if the node is processing input (see [method set_process_input]).
func is_processing_input() -> bool:
	pass;

#desc Returns [code]true[/code] if internal processing is enabled (see [method set_process_internal]).
func is_processing_internal() -> bool:
	pass;

#desc Returns [code]true[/code] if the node is processing shortcuts (see [method set_process_shortcut_input]).
func is_processing_shortcut_input() -> bool:
	pass;

#desc Returns [code]true[/code] if the node is processing unhandled input (see [method set_process_unhandled_input]).
func is_processing_unhandled_input() -> bool:
	pass;

#desc Returns [code]true[/code] if the node is processing unhandled key input (see [method set_process_unhandled_key_input]).
func is_processing_unhandled_key_input() -> bool:
	pass;

#desc Moves a child node to a different position (order) among the other children. Since calls, signals, etc are performed by tree order, changing the order of children nodes may be useful. If [param to_position] is negative, the index will be counted from the end.
#desc [b]Note:[/b] Internal children can only be moved within their expected "internal range" (see [code]internal[/code] parameter in [method add_child]).
func move_child(child_node: Node, to_position: int) -> void:
	pass;

#desc Prints all orphan nodes (nodes outside the [SceneTree]). Used for debugging.
#desc [b]Note:[/b] [method print_orphan_nodes] only works in debug builds. When called in a project exported in release mode, [method print_orphan_nodes] will not print anything.
func print_orphan_nodes() -> void:
	pass;

#desc Prints the tree to stdout. Used mainly for debugging purposes. This version displays the path relative to the current node, and is good for copy/pasting into the [method get_node] function.
#desc [b]Example output:[/b]
#desc [codeblock]
#desc TheGame
#desc TheGame/Menu
#desc TheGame/Menu/Label
#desc TheGame/Menu/Camera2D
#desc TheGame/SplashScreen
#desc TheGame/SplashScreen/Camera2D
#desc [/codeblock]
func print_tree() -> void:
	pass;

#desc Similar to [method print_tree], this prints the tree to stdout. This version displays a more graphical representation similar to what is displayed in the scene inspector. It is useful for inspecting larger trees.
#desc [b]Example output:[/b]
#desc [codeblock]
#desc ┖╴TheGame
#desc ┠╴Menu
#desc ┃  ┠╴Label
#desc ┃  ┖╴Camera2D
#desc ┖╴SplashScreen
#desc ┖╴Camera2D
#desc [/codeblock]
func print_tree_pretty() -> void:
	pass;

#desc Calls the given method (if present) with the arguments given in [param args] on this node and recursively on all its children. If the [param parent_first] argument is [code]true[/code], the method will be called on the current node first, then on all its children. If [param parent_first] is [code]false[/code], the children will be called first.
func propagate_call(method: StringName, args: Array, parent_first: bool) -> void:
	pass;

#desc Notifies the current node and all its children recursively by calling [method Object.notification] on all of them.
func propagate_notification(what: int) -> void:
	pass;

#desc Queues a node for deletion at the end of the current frame. When deleted, all of its child nodes will be deleted as well. This method ensures it's safe to delete the node, contrary to [method Object.free]. Use [method Object.is_queued_for_deletion] to check whether a node will be deleted at the end of the frame.
func queue_free() -> void:
	pass;

#desc Removes a child node. The node is NOT deleted and must be deleted manually.
#desc [b]Note:[/b] This function may set the [member owner] of the removed Node (or its descendants) to be [code]null[/code], if that [member owner] is no longer a parent or ancestor.
func remove_child(node: Node) -> void:
	pass;

#desc Removes a node from a group. See notes in the description, and the group methods in [SceneTree].
func remove_from_group(group: StringName) -> void:
	pass;

#desc Replaces a node in a scene by the given one. Subscriptions that pass through this node will be lost.
#desc If [param keep_groups] is [code]true[/code], the [param node] is added to the same groups that the replaced node is in.
#desc [b]Note:[/b] The given node will become the new parent of any child nodes that the replaced node had.
#desc [b]Note:[/b] The replaced node is not automatically freed, so you either need to keep it in a variable for later use or free it using [method Object.free].
func replace_by(node: Node, keep_groups: bool) -> void:
	pass;

#desc Requests that [code]_ready[/code] be called again. Note that the method won't be called immediately, but is scheduled for when the node is added to the scene tree again (see [method _ready]). [code]_ready[/code] is called only for the node which requested it, which means that you need to request ready for each child if you want them to call [code]_ready[/code] too (in which case, [code]_ready[/code] will be called in the same order as it would normally).
func request_ready() -> void:
	pass;

#desc Sends a remote procedure call request for the given [param method] to peers on the network (and locally), optionally sending all additional arguments as arguments to the method called by the RPC. The call request will only be received by nodes with the same [NodePath], including the exact same node name. Behaviour depends on the RPC configuration for the given method, see [method rpc_config]. Methods are not exposed to RPCs by default. Returns [code]null[/code].
#desc [b]Note:[/b] You can only safely use RPCs on clients after you received the [code]connected_to_server[/code] signal from the [MultiplayerAPI]. You also need to keep track of the connection state, either by the [MultiplayerAPI] signals like [code]server_disconnected[/code] or by checking [code]get_multiplayer().peer.get_connection_status() == CONNECTION_CONNECTED[/code].
vararg func rpc(method: StringName) -> int:
	pass;

#desc Changes the RPC mode for the given [param method] with the given [param config] which should be [code]null[/code] (to disable) or a [Dictionary] in the form:
#desc [codeblock]
#desc {
#desc rpc_mode = MultiplayerAPI.RPCMode,
#desc transfer_mode = MultiplayerPeer.TranferMode,
#desc call_local = false,
#desc channel = 0,
#desc }
#desc [/codeblock]
#desc See [enum MultiplayerAPI.RPCMode] and [enum MultiplayerPeer.TransferMode]. An alternative is annotating methods and properties with the corresponding annotation ([code]@rpc(any)[/code], [code]@rpc(authority)[/code]). By default, methods are not exposed to networking (and RPCs).
func rpc_config(method: StringName, config: Variant) -> void:
	pass;

#desc Sends a [method rpc] to a specific peer identified by [param peer_id] (see [method MultiplayerPeer.set_target_peer]). Returns [code]null[/code].
vararg func rpc_id(peer_id: int, method: StringName) -> int:
	pass;

#desc Sets the folded state of the node in the Scene dock. This method is only intended for use with editor tooling.
func set_display_folded(fold: bool) -> void:
	pass;

#desc Sets the editable children state of [param node] relative to this node. This method is only intended for use with editor tooling.
func set_editable_instance(node: Node, is_editable: bool) -> void:
	pass;

#desc Sets the node's multiplayer authority to the peer with the given peer ID. The multiplayer authority is the peer that has authority over the node on the network. Useful in conjunction with [method rpc_config] and the [MultiplayerAPI]. Inherited from the parent node by default, which ultimately defaults to peer ID 1 (the server). If [param recursive], the given peer is recursively set as the authority for all children of this node.
func set_multiplayer_authority(id: int, recursive: bool) -> void:
	pass;

#desc Enables or disables physics (i.e. fixed framerate) processing. When a node is being processed, it will receive a [constant NOTIFICATION_PHYSICS_PROCESS] at a fixed (usually 60 FPS, see [member Engine.physics_ticks_per_second] to change) interval (and the [method _physics_process] callback will be called if exists). Enabled automatically if [method _physics_process] is overridden. Any calls to this before [method _ready] will be ignored.
func set_physics_process(enable: bool) -> void:
	pass;

#desc Enables or disables internal physics for this node. Internal physics processing happens in isolation from the normal [method _physics_process] calls and is used by some nodes internally to guarantee proper functioning even if the node is paused or physics processing is disabled for scripting ([method set_physics_process]). Only useful for advanced uses to manipulate built-in nodes' behavior.
#desc [b]Warning:[/b] Built-in Nodes rely on the internal processing for their own logic, so changing this value from your code may lead to unexpected behavior. Script access to this internal logic is provided for specific advanced uses, but is unsafe and not supported.
func set_physics_process_internal(enable: bool) -> void:
	pass;

#desc Enables or disables processing. When a node is being processed, it will receive a [constant NOTIFICATION_PROCESS] on every drawn frame (and the [method _process] callback will be called if exists). Enabled automatically if [method _process] is overridden. Any calls to this before [method _ready] will be ignored.
func set_process(enable: bool) -> void:
	pass;

#desc Enables or disables input processing. This is not required for GUI controls! Enabled automatically if [method _input] is overridden. Any calls to this before [method _ready] will be ignored.
func set_process_input(enable: bool) -> void:
	pass;

#desc Enables or disabled internal processing for this node. Internal processing happens in isolation from the normal [method _process] calls and is used by some nodes internally to guarantee proper functioning even if the node is paused or processing is disabled for scripting ([method set_process]). Only useful for advanced uses to manipulate built-in nodes' behavior.
#desc [b]Warning:[/b] Built-in Nodes rely on the internal processing for their own logic, so changing this value from your code may lead to unexpected behavior. Script access to this internal logic is provided for specific advanced uses, but is unsafe and not supported.
func set_process_internal(enable: bool) -> void:
	pass;

#desc Enables shortcut processing. Enabled automatically if [method _shortcut_input] is overridden. Any calls to this before [method _ready] will be ignored.
func set_process_shortcut_input(enable: bool) -> void:
	pass;

#desc Enables unhandled input processing. This is not required for GUI controls! It enables the node to receive all input that was not previously handled (usually by a [Control]). Enabled automatically if [method _unhandled_input] is overridden. Any calls to this before [method _ready] will be ignored.
func set_process_unhandled_input(enable: bool) -> void:
	pass;

#desc Enables unhandled key input processing. Enabled automatically if [method _unhandled_key_input] is overridden. Any calls to this before [method _ready] will be ignored.
func set_process_unhandled_key_input(enable: bool) -> void:
	pass;

#desc Sets whether this is an instance load placeholder. See [InstancePlaceholder].
func set_scene_instance_load_placeholder(load_placeholder: bool) -> void:
	pass;

#desc Updates the warning displayed for this node in the Scene Dock.
#desc Use [method _get_configuration_warnings] to setup the warning message to display.
func update_configuration_warnings() -> void:
	pass;


