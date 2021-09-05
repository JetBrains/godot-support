extends RefCounted
class_name Thread

const PRIORITY_LOW = 0;
const PRIORITY_NORMAL = 1;
const PRIORITY_HIGH = 2;


func get_id() -> String:
    pass;

func is_active() -> bool:
    pass;

func start(instance: Object, method: StringName, userdata: Variant, priority: int) -> int:
    pass;

func wait_to_finish() -> Variant:
    pass;

