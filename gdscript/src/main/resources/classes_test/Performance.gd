extends Object
class_name Performance

const TIME_FPS = 0;
const TIME_PROCESS = 1;
const TIME_PHYSICS_PROCESS = 2;
const MEMORY_STATIC = 3;
const MEMORY_STATIC_MAX = 4;
const MEMORY_MESSAGE_BUFFER_MAX = 5;
const OBJECT_COUNT = 6;
const OBJECT_RESOURCE_COUNT = 7;
const OBJECT_NODE_COUNT = 8;
const OBJECT_ORPHAN_NODE_COUNT = 9;
const RENDER_TOTAL_OBJECTS_IN_FRAME = 10;
const RENDER_TOTAL_PRIMITIVES_IN_FRAME = 11;
const RENDER_TOTAL_DRAW_CALLS_IN_FRAME = 12;
const RENDER_VIDEO_MEM_USED = 13;
const RENDER_TEXTURE_MEM_USED = 14;
const RENDER_BUFFER_MEM_USED = 15;
const PHYSICS_2D_ACTIVE_OBJECTS = 16;
const PHYSICS_2D_COLLISION_PAIRS = 17;
const PHYSICS_2D_ISLAND_COUNT = 18;
const PHYSICS_3D_ACTIVE_OBJECTS = 19;
const PHYSICS_3D_COLLISION_PAIRS = 20;
const PHYSICS_3D_ISLAND_COUNT = 21;
const AUDIO_OUTPUT_LATENCY = 22;
const MONITOR_MAX = 23;


func add_custom_monitor(id: StringName, callable: Callable, arguments: Array) -> void:
    pass;

func get_custom_monitor(id: StringName) -> Variant:
    pass;

func get_custom_monitor_names() -> Array:
    pass;

func get_monitor(monitor: int) -> float:
    pass;

func get_monitor_modification_time() -> int:
    pass;

func has_custom_monitor(id: StringName) -> bool:
    pass;

func remove_custom_monitor(id: StringName) -> void:
    pass;

