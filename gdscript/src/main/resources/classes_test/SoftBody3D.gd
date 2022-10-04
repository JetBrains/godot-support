#brief A soft mesh physics body.
#desc A deformable physics body. Used to create elastic or deformable objects such as cloth, rubber, or other flexible materials.
#desc [b]Note:[/b] There are many known bugs in [SoftBody3D]. Therefore, it's not recommended to use them for things that can affect gameplay (such as a player character made entirely out of soft bodies).
class_name SoftBody3D

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], remove from the physics simulation to stop all physics interactions with this [SoftBody3D].
#desc Automatically re-added to the physics simulation when the [Node] is processed again.
const DISABLE_MODE_REMOVE = 0;

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], do not affect the physics simulation.
const DISABLE_MODE_KEEP_ACTIVE = 1;


#desc The physics layers this SoftBody3D [b]is in[/b]. Collision objects can exist in one or more of 32 different layers. See also [member collision_mask].
#desc [b]Note:[/b] Object A can detect a contact with object B only if object B is in any of the layers that object A scans. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_layer: int;

#desc The physics layers this SoftBody3D [b]scans[/b]. Collision objects can scan one or more of 32 different layers. See also [member collision_layer].
#desc [b]Note:[/b] Object A can detect a contact with object B only if object B is in any of the layers that object A scans. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

var damping_coefficient: float;

#desc Defines the behavior in physics when [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED]. See [enum DisableMode] for more details about the different modes.
var disable_mode: int;

var drag_coefficient: float;

var linear_stiffness: float;

#desc [NodePath] to a [CollisionObject3D] this SoftBody3D should avoid clipping.
var parent_collision_ignore: NodePath;

var pressure_coefficient: float;

#desc If [code]true[/code], the [SoftBody3D] will respond to [RayCast3D]s.
var ray_pickable: bool;

#desc Increasing this value will improve the resulting simulation, but can affect performance. Use with care.
var simulation_precision: int;

#desc The SoftBody3D's mass.
var total_mass: float;



#desc Adds a body to the list of bodies that this body can't collide with.
func add_collision_exception_with() -> void:
	pass;

#desc Returns an array of nodes that were added as collision exceptions for this body.
func get_collision_exceptions() -> PhysicsBody3D[]:
	pass;

#desc Returns whether or not the specified layer of the [member collision_layer] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_layer_value() -> bool:
	pass;

#desc Returns whether or not the specified layer of the [member collision_mask] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_mask_value() -> bool:
	pass;

func get_physics_rid() -> RID:
	pass;

#desc Returns local translation of a vertex in the surface array.
func get_point_transform() -> Vector3:
	pass;

#desc Returns [code]true[/code] if vertex is set to pinned.
func is_point_pinned() -> bool:
	pass;

#desc Removes a body from the list of bodies that this body can't collide with.
func remove_collision_exception_with() -> void:
	pass;

#desc Based on [code]value[/code], enables or disables the specified layer in the [member collision_layer], given a [param layer_number] between 1 and 32.
func set_collision_layer_value(layer_number: int, value: bool) -> void:
	pass;

#desc Based on [code]value[/code], enables or disables the specified layer in the [member collision_mask], given a [param layer_number] between 1 and 32.
func set_collision_mask_value(layer_number: int, value: bool) -> void:
	pass;

#desc Sets the pinned state of a surface vertex. When set to [code]true[/code], the optional [param attachment_path] can define a [Node3D] the pinned vertex will be attached to.
func set_point_pinned(point_index: int, pinned: bool, attachment_path: NodePath) -> void:
	pass;


