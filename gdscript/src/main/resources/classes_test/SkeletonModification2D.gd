extends Resource
class_name SkeletonModification2D

var enabled: bool;
var execution_mode: int;

func _draw_editor_gizmo() -> void:
    pass;
func _execute(delta: float) -> void:
    pass;
func _setup_modification(modification_stack: SkeletonModificationStack2D) -> void:
    pass;
func clamp_angle(angle: float, min: float, max: float, invert: bool) -> float:
    pass;
func get_editor_draw_gizmo() -> bool:
    pass;
func get_is_setup() -> bool:
    pass;
func get_modification_stack() -> SkeletonModificationStack2D:
    pass;
func set_editor_draw_gizmo(draw_gizmo: bool) -> void:
    pass;
func set_is_setup(is_setup: bool) -> void:
    pass;
