extends Object
class_name UndoRedo

const MERGE_DISABLE = 0;
const MERGE_ENDS = 1;
const MERGE_ALL = 2;


func add_do_method(object: Object, method: StringName) -> void:
    pass;

func add_do_property(object: Object, property: StringName, value: Variant) -> void:
    pass;

func add_do_reference(object: Object) -> void:
    pass;

func add_undo_method(object: Object, method: StringName) -> void:
    pass;

func add_undo_property(object: Object, property: StringName, value: Variant) -> void:
    pass;

func add_undo_reference(object: Object) -> void:
    pass;

func clear_history(increase_version: bool) -> void:
    pass;

func commit_action(execute: bool) -> void:
    pass;

func create_action(name: String, merge_mode: int) -> void:
    pass;

func get_action_name(id: int) -> String:
    pass;

func get_current_action() -> int:
    pass;

func get_current_action_name() -> String:
    pass;

func get_history_count() -> int:
    pass;

func get_version() -> int:
    pass;

func has_redo() -> bool:
    pass;

func has_undo() -> bool:
    pass;

func is_committing_action() -> bool:
    pass;

func redo() -> bool:
    pass;

func undo() -> bool:
    pass;

