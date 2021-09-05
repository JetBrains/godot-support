extends MainLoop
class_name SceneTree

const GROUP_CALL_DEFAULT = 0;
const GROUP_CALL_REVERSE = 1;
const GROUP_CALL_REALTIME = 2;
const GROUP_CALL_UNIQUE = 4;

var current_scene: Node setget set_current_scene, get_current_scene;
var debug_collisions_hint: bool setget set_debug_collisions_hint, is_debugging_collisions_hint;
var debug_navigation_hint: bool setget set_debug_navigation_hint, is_debugging_navigation_hint;
var edited_scene_root: Node setget set_edited_scene_root, get_edited_scene_root;
var multiplayer: MultiplayerAPI setget set_multiplayer, get_multiplayer;
var multiplayer_poll: bool setget set_multiplayer_poll_enabled, is_multiplayer_poll_enabled;
var paused: bool setget set_pause, is_paused;
var root: Window setget , get_root;

func call_group(group: StringName, method: StringName) -> Variant:
    pass;

func call_group_flags(flags: int, group: StringName, method: StringName) -> Variant:
    pass;

func change_scene(path: String) -> int:
    pass;

func change_scene_to(packed_scene: PackedScene) -> int:
    pass;

func create_timer(time_sec: float, process_always: bool) -> SceneTreeTimer:
    pass;

func create_tween() -> Tween:
    pass;

func get_first_node_in_group(group: StringName) -> Node:
    pass;

func get_frame() -> int:
    pass;

func get_node_count() -> int:
    pass;

func get_nodes_in_group(group: StringName) -> Array:
    pass;

func get_processed_tweens() -> Array:
    pass;

func has_group(name: StringName) -> bool:
    pass;

func notify_group(group: StringName, notification: int) -> void:
    pass;

func notify_group_flags(call_flags: int, group: StringName, notification: int) -> void:
    pass;

func queue_delete(obj: Object) -> void:
    pass;

func quit(exit_code: int) -> void:
    pass;

func reload_current_scene() -> int:
    pass;

func set_auto_accept_quit(enabled: bool) -> void:
    pass;

func set_group(group: StringName, property: String, value: Variant) -> void:
    pass;

func set_group_flags(call_flags: int, group: StringName, property: String, value: Variant) -> void:
    pass;

func set_quit_on_go_back(enabled: bool) -> void:
    pass;

