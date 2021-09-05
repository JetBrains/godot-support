extends Resource
class_name SkeletonModificationStack2D


var enabled: bool setget set_enabled, get_enabled;
var modification_count: int setget set_modification_count, get_modification_count;
var strength: float setget set_strength, get_strength;

func add_modification(modification: SkeletonModification2D) -> void:
    pass;

func delete_modification(mod_idx: int) -> void:
    pass;

func enable_all_modifications(enabled: bool) -> void:
    pass;

func execute(delta: float, execution_mode: int) -> void:
    pass;

func get_is_setup() -> bool:
    pass;

func get_modification(mod_idx: int) -> SkeletonModification2D:
    pass;

func get_skeleton() -> Skeleton2D:
    pass;

func set_modification(mod_idx: int, modification: SkeletonModification2D) -> void:
    pass;

func setup() -> void:
    pass;

