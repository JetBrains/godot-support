extends Resource
class_name Shape2D


var custom_solver_bias: float setget set_custom_solver_bias, get_custom_solver_bias;

func collide(local_xform: Transform2D, with_shape: Shape2D, shape_xform: Transform2D) -> bool:
    pass;

func collide_and_get_contacts(local_xform: Transform2D, with_shape: Shape2D, shape_xform: Transform2D) -> Array:
    pass;

func collide_with_motion(local_xform: Transform2D, local_motion: Vector2, with_shape: Shape2D, shape_xform: Transform2D, shape_motion: Vector2) -> bool:
    pass;

func collide_with_motion_and_get_contacts(local_xform: Transform2D, local_motion: Vector2, with_shape: Shape2D, shape_xform: Transform2D, shape_motion: Vector2) -> Array:
    pass;

func draw(canvas_item: RID, color: Color) -> void:
    pass;

