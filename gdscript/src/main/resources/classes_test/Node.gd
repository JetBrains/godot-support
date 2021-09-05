extends Object
class_name Node

const NOTIFICATION_ENTER_TREE = 10;
const NOTIFICATION_EXIT_TREE = 11;
const NOTIFICATION_MOVED_IN_PARENT = 12;
const NOTIFICATION_READY = 13;
const NOTIFICATION_PAUSED = 14;
const NOTIFICATION_UNPAUSED = 15;
const NOTIFICATION_PHYSICS_PROCESS = 16;
const NOTIFICATION_PROCESS = 17;
const NOTIFICATION_PARENTED = 18;
const NOTIFICATION_UNPARENTED = 19;
const NOTIFICATION_INSTANCED = 20;
const NOTIFICATION_DRAG_BEGIN = 21;
const NOTIFICATION_DRAG_END = 22;
const NOTIFICATION_PATH_CHANGED = 23;
const NOTIFICATION_INTERNAL_PROCESS = 25;
const NOTIFICATION_INTERNAL_PHYSICS_PROCESS = 26;
const NOTIFICATION_POST_ENTER_TREE = 27;
const NOTIFICATION_DISABLED = 28;
const NOTIFICATION_ENABLED = 29;
const NOTIFICATION_EDITOR_PRE_SAVE = 9001;
const NOTIFICATION_EDITOR_POST_SAVE = 9002;
const NOTIFICATION_WM_MOUSE_ENTER = 1002;
const NOTIFICATION_WM_MOUSE_EXIT = 1003;
const NOTIFICATION_WM_WINDOW_FOCUS_IN = 1004;
const NOTIFICATION_WM_WINDOW_FOCUS_OUT = 1005;
const NOTIFICATION_WM_CLOSE_REQUEST = 1006;
const NOTIFICATION_WM_GO_BACK_REQUEST = 1007;
const NOTIFICATION_WM_SIZE_CHANGED = 1008;
const NOTIFICATION_OS_MEMORY_WARNING = 2009;
const NOTIFICATION_TRANSLATION_CHANGED = 2010;
const NOTIFICATION_WM_ABOUT = 2011;
const NOTIFICATION_CRASH = 2012;
const NOTIFICATION_OS_IME_UPDATE = 2013;
const NOTIFICATION_APPLICATION_RESUMED = 2014;
const NOTIFICATION_APPLICATION_PAUSED = 2015;
const NOTIFICATION_APPLICATION_FOCUS_IN = 2016;
const NOTIFICATION_APPLICATION_FOCUS_OUT = 2017;
const NOTIFICATION_TEXT_SERVER_CHANGED = 2018;
const PROCESS_MODE_INHERIT = 0;
const PROCESS_MODE_PAUSABLE = 1;
const PROCESS_MODE_WHEN_PAUSED = 2;
const PROCESS_MODE_ALWAYS = 3;
const PROCESS_MODE_DISABLED = 4;
const DUPLICATE_SIGNALS = 1;
const DUPLICATE_GROUPS = 2;
const DUPLICATE_SCRIPTS = 4;
const DUPLICATE_USE_INSTANCING = 8;

var custom_multiplayer: MultiplayerAPI setget set_custom_multiplayer, get_custom_multiplayer;
var filename: String setget set_filename, get_filename;
var multiplayer: MultiplayerAPI setget , get_multiplayer;
var name: String setget set_name, get_name;
var owner: Node setget set_owner, get_owner;
var process_mode: int setget set_process_mode, get_process_mode;
var process_priority: int setget set_process_priority, get_process_priority;

func _enter_tree() -> void:
    pass;

func _exit_tree() -> void:
    pass;

func _get_configuration_warnings() -> String[]:
    pass;

func _input(event: InputEvent) -> void:
    pass;

func _physics_process(delta: float) -> void:
    pass;

func _process(delta: float) -> void:
    pass;

func _ready() -> void:
    pass;

func _unhandled_input(event: InputEvent) -> void:
    pass;

func _unhandled_key_input(event: InputEventKey) -> void:
    pass;

func add_child(node: Node, legible_unique_name: bool) -> void:
    pass;

func add_sibling(sibling: Node, legible_unique_name: bool) -> void:
    pass;

func add_to_group(group: StringName, persistent: bool) -> void:
    pass;

func can_process() -> bool:
    pass;

func create_tween() -> Tween:
    pass;

func duplicate(flags: int) -> Node:
    pass;

func find_node(mask: String, recursive: bool, owned: bool) -> Node:
    pass;

func find_parent(mask: String) -> Node:
    pass;

func get_child(idx: int) -> Node:
    pass;

func get_child_count() -> int:
    pass;

func get_children() -> Node[]:
    pass;

func get_editor_description() -> String:
    pass;

func get_groups() -> Array:
    pass;

func get_index() -> int:
    pass;

func get_network_master() -> int:
    pass;

func get_node(path: NodePath) -> Node:
    pass;

func get_node_and_resource(path: NodePath) -> Array:
    pass;

func get_node_or_null(path: NodePath) -> Node:
    pass;

func get_parent() -> Node:
    pass;

func get_path() -> NodePath:
    pass;

func get_path_to(node: Node) -> NodePath:
    pass;

func get_physics_process_delta_time() -> float:
    pass;

func get_process_delta_time() -> float:
    pass;

func get_scene_instance_load_placeholder() -> bool:
    pass;

func get_tree() -> SceneTree:
    pass;

func get_viewport() -> Viewport:
    pass;

func has_node(path: NodePath) -> bool:
    pass;

func has_node_and_resource(path: NodePath) -> bool:
    pass;

func is_ancestor_of(node: Node) -> bool:
    pass;

func is_displayed_folded() -> bool:
    pass;

func is_greater_than(node: Node) -> bool:
    pass;

func is_in_group(group: StringName) -> bool:
    pass;

func is_inside_tree() -> bool:
    pass;

func is_network_master() -> bool:
    pass;

func is_physics_processing() -> bool:
    pass;

func is_physics_processing_internal() -> bool:
    pass;

func is_processing() -> bool:
    pass;

func is_processing_input() -> bool:
    pass;

func is_processing_internal() -> bool:
    pass;

func is_processing_unhandled_input() -> bool:
    pass;

func is_processing_unhandled_key_input() -> bool:
    pass;

func move_child(child_node: Node, to_position: int) -> void:
    pass;

func print_stray_nodes() -> void:
    pass;

func print_tree() -> void:
    pass;

func print_tree_pretty() -> void:
    pass;

func propagate_call(method: StringName, args: Array, parent_first: bool) -> void:
    pass;

func propagate_notification(what: int) -> void:
    pass;

func queue_free() -> void:
    pass;

func raise() -> void:
    pass;

func remove_and_skip() -> void:
    pass;

func remove_child(node: Node) -> void:
    pass;

func remove_from_group(group: StringName) -> void:
    pass;

func replace_by(node: Node, keep_groups: bool) -> void:
    pass;

func request_ready() -> void:
    pass;

func rpc(method: StringName) -> Variant:
    pass;

func rpc_config(method: StringName, rpc_mode: int, transfer_mode: int, channel: int) -> int:
    pass;

func rpc_id(peer_id: int, method: StringName) -> Variant:
    pass;

func set_display_folded(fold: bool) -> void:
    pass;

func set_editor_description(editor_description: String) -> void:
    pass;

func set_network_master(id: int, recursive: bool) -> void:
    pass;

func set_physics_process(enable: bool) -> void:
    pass;

func set_physics_process_internal(enable: bool) -> void:
    pass;

func set_process(enable: bool) -> void:
    pass;

func set_process_input(enable: bool) -> void:
    pass;

func set_process_internal(enable: bool) -> void:
    pass;

func set_process_unhandled_input(enable: bool) -> void:
    pass;

func set_process_unhandled_key_input(enable: bool) -> void:
    pass;

func set_scene_instance_load_placeholder(load_placeholder: bool) -> void:
    pass;

func update_configuration_warnings() -> void:
    pass;

