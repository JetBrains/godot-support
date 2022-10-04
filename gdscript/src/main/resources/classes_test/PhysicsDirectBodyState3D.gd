extends Object
#brief Direct access object to a physics body in the [PhysicsServer3D].
#desc Provides direct access to a physics body in the [PhysicsServer3D], allowing safe changes to physics properties. This object is passed via the direct state callback of rigid bodies, and is intended for changing the direct state of that body. See [method RigidBody3D._integrate_forces].
class_name PhysicsDirectBodyState3D


#desc The body's rotational velocity in [i]radians[/i] per second.
var angular_velocity: Vector3;

#desc The body's center of mass position relative to the body's center in the global coordinate system.
var center_of_mass: Vector3;

#desc The body's center of mass position in the body's local coordinate system.
var center_of_mass_local: Vector3;

#desc The inverse of the inertia of the body.
var inverse_inertia: Vector3;

#desc The inverse of the mass of the body.
var inverse_mass: float;

#desc The body's linear velocity in units per second.
var linear_velocity: Vector3;

var principal_inertia_axes: Basis;

#desc If [code]true[/code], this body is currently sleeping (not active).
var sleeping: bool;

#desc The timestep (delta) used for the simulation.
var step: float;

#desc The rate at which the body stops rotating, if there are not any other forces moving it.
var total_angular_damp: float;

#desc The total gravity vector being currently applied to this body.
var total_gravity: Vector3;

#desc The rate at which the body stops moving, if there are not any other forces moving it.
var total_linear_damp: float;

#desc The body's transformation matrix.
var transform: Transform3D;



#desc Adds a constant directional force without affecting rotation that keeps being applied over time until cleared with [code]constant_force = Vector3(0, 0, 0)[/code].
#desc This is equivalent to using [method add_constant_force] at the body's center of mass.
func add_constant_central_force(force: Vector3) -> void:
	pass;

#desc Adds a constant positioned force to the body that keeps being applied over time until cleared with [code]constant_force = Vector3(0, 0, 0)[/code].
#desc [param position] is the offset from the body origin in global coordinates.
func add_constant_force(force: Vector3, position: Vector3) -> void:
	pass;

#desc Adds a constant rotational force without affecting position that keeps being applied over time until cleared with [code]constant_torque = Vector3(0, 0, 0)[/code].
func add_constant_torque(torque: Vector3) -> void:
	pass;

#desc Applies a directional force without affecting rotation. A force is time dependent and meant to be applied every physics update.
#desc This is equivalent to using [method apply_force] at the body's center of mass.
func apply_central_force(force: Vector3) -> void:
	pass;

#desc Applies a directional impulse without affecting rotation.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc This is equivalent to using [method apply_impulse] at the body's center of mass.
func apply_central_impulse(impulse: Vector3) -> void:
	pass;

#desc Applies a positioned force to the body. A force is time dependent and meant to be applied every physics update.
#desc [param position] is the offset from the body origin in global coordinates.
func apply_force(force: Vector3, position: Vector3) -> void:
	pass;

#desc Applies a positioned impulse to the body.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc [param position] is the offset from the body origin in global coordinates.
func apply_impulse(impulse: Vector3, position: Vector3) -> void:
	pass;

#desc Applies a rotational force without affecting position. A force is time dependent and meant to be applied every physics update.
func apply_torque(torque: Vector3) -> void:
	pass;

#desc Applies a rotational impulse to the body without affecting the position.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
func apply_torque_impulse(impulse: Vector3) -> void:
	pass;

#desc Returns the body's total constant positional forces applied during each physics update.
#desc See [method add_constant_force] and [method add_constant_central_force].
func get_constant_force() -> Vector3:
	pass;

#desc Returns the body's total constant rotational forces applied during each physics update.
#desc See [method add_constant_torque].
func get_constant_torque() -> Vector3:
	pass;

#desc Returns the collider's [RID].
func get_contact_collider(contact_idx: int) -> RID:
	pass;

#desc Returns the collider's object id.
func get_contact_collider_id(contact_idx: int) -> int:
	pass;

#desc Returns the collider object.
func get_contact_collider_object(contact_idx: int) -> Object:
	pass;

#desc Returns the contact position in the collider.
func get_contact_collider_position(contact_idx: int) -> Vector3:
	pass;

#desc Returns the collider's shape index.
func get_contact_collider_shape(contact_idx: int) -> int:
	pass;

#desc Returns the linear velocity vector at the collider's contact point.
func get_contact_collider_velocity_at_position(contact_idx: int) -> Vector3:
	pass;

#desc Returns the number of contacts this body has with other bodies.
#desc [b]Note:[/b] By default, this returns 0 unless bodies are configured to monitor contacts. See [member RigidBody3D.contact_monitor].
func get_contact_count() -> int:
	pass;

#desc Impulse created by the contact. Only implemented for Bullet physics.
func get_contact_impulse(contact_idx: int) -> float:
	pass;

#desc Returns the local normal at the contact point.
func get_contact_local_normal(contact_idx: int) -> Vector3:
	pass;

#desc Returns the local position of the contact point.
func get_contact_local_position(contact_idx: int) -> Vector3:
	pass;

#desc Returns the local shape index of the collision.
func get_contact_local_shape(contact_idx: int) -> int:
	pass;

#desc Returns the current state of the space, useful for queries.
func get_space_state() -> PhysicsDirectSpaceState3D:
	pass;

#desc Returns the body's velocity at the given relative position, including both translation and rotation.
func get_velocity_at_local_position(local_position: Vector3) -> Vector3:
	pass;

#desc Calls the built-in force integration code.
func integrate_forces() -> void:
	pass;

#desc Sets the body's total constant positional forces applied during each physics update.
#desc See [method add_constant_force] and [method add_constant_central_force].
func set_constant_force(force: Vector3) -> void:
	pass;

#desc Sets the body's total constant rotational forces applied during each physics update.
#desc See [method add_constant_torque].
func set_constant_torque(torque: Vector3) -> void:
	pass;


