#brief Base node for collision objects.
#desc CollisionObject3D is the base class for physics objects. It can hold any number of collision [Shape3D]s. Each shape must be assigned to a [i]shape owner[/i]. The CollisionObject3D can have any number of shape owners. Shape owners are not nodes and do not appear in the editor, but are accessible through code using the [code]shape_owner_*[/code] methods.
class_name CollisionObject3D

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], remove from the physics simulation to stop all physics interactions with this [CollisionObject3D].
#desc Automatically re-added to the physics simulation when the [Node] is processed again.
const DISABLE_MODE_REMOVE = 0;

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], make the body static. Doesn't affect [Area2D]. [PhysicsBody3D] can't be affected by forces or other bodies while static.
#desc Automatically set [PhysicsBody3D] back to its original mode when the [Node] is processed again.
const DISABLE_MODE_MAKE_STATIC = 1;

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], do not affect the physics simulation.
const DISABLE_MODE_KEEP_ACTIVE = 2;


#desc The physics layers this CollisionObject3D [b]is in[/b]. Collision objects can exist in one or more of 32 different layers. See also [member collision_mask].
#desc [b]Note:[/b] Object A can detect a contact with object B only if object B is in any of the layers that object A scans. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_layer: int;

#desc The physics layers this CollisionObject3D [b]scans[/b]. Collision objects can scan one or more of 32 different layers. See also [member collision_layer].
#desc [b]Note:[/b] Object A can detect a contact with object B only if object B is in any of the layers that object A scans. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The priority used to solve colliding when occurring penetration. The higher the priority is, the lower the penetration into the object will be. This can for example be used to prevent the player from breaking through the boundaries of a level.
var collision_priority: float;

#desc Defines the behavior in physics when [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED]. See [enum DisableMode] for more details about the different modes.
var disable_mode: int;

#desc If [code]true[/code], the [CollisionObject3D] will continue to receive input events as the mouse is dragged across its shapes.
var input_capture_on_drag: bool;

#desc If [code]true[/code], this object is pickable. A pickable object can detect the mouse pointer entering/leaving, and if the mouse is inside it, report input events. Requires at least one [member collision_layer] bit to be set.
var input_ray_pickable: bool;



#desc Receives unhandled [InputEvent]s. [param position] is the location in world space of the mouse pointer on the surface of the shape with index [param shape_idx] and [param normal] is the normal vector of the surface at that point. Connect to the [signal input_event] signal to easily pick up these events.
#desc [b]Note:[/b] [method _input_event] requires [member input_ray_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be set.
virtual func _input_event(camera: Camera3D, event: InputEvent, position: Vector3, normal: Vector3, shape_idx: int) -> void:
	pass;

#desc Called when the mouse pointer enters any of this object's shapes. Requires [member input_ray_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be set. Note that moving between different shapes within a single [CollisionObject3D] won't cause this function to be called.
virtual func _mouse_enter() -> void:
	pass;

#desc Called when the mouse pointer exits all this object's shapes. Requires [member input_ray_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be set. Note that moving between different shapes within a single [CollisionObject3D] won't cause this function to be called.
virtual func _mouse_exit() -> void:
	pass;

#desc Creates a new shape owner for the given object. Returns [code]owner_id[/code] of the new owner for future reference.
func create_shape_owner() -> int:
	pass;

#desc Returns whether or not the specified layer of the [member collision_layer] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_layer_value() -> bool:
	pass;

#desc Returns whether or not the specified layer of the [member collision_mask] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_mask_value() -> bool:
	pass;

#desc Returns the object's [RID].
func get_rid() -> RID:
	pass;

#desc Returns an [Array] of [code]owner_id[/code] identifiers. You can use these ids in other methods that take [code]owner_id[/code] as an argument.
func get_shape_owners() -> PackedInt32Array:
	pass;

#desc If [code]true[/code], the shape owner and its shapes are disabled.
func is_shape_owner_disabled() -> bool:
	pass;

#desc Removes the given shape owner.
func remove_shape_owner() -> void:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member collision_layer], given a [param layer_number] between 1 and 32.
func set_collision_layer_value(layer_number: int, value: bool) -> void:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member collision_mask], given a [param layer_number] between 1 and 32.
func set_collision_mask_value(layer_number: int, value: bool) -> void:
	pass;

#desc Returns the [code]owner_id[/code] of the given shape.
func shape_find_owner() -> int:
	pass;

#desc Adds a [Shape3D] to the shape owner.
func shape_owner_add_shape(owner_id: int, shape: Shape3D) -> void:
	pass;

#desc Removes all shapes from the shape owner.
func shape_owner_clear_shapes() -> void:
	pass;

#desc Returns the parent object of the given shape owner.
func shape_owner_get_owner() -> Object:
	pass;

#desc Returns the [Shape3D] with the given id from the given shape owner.
func shape_owner_get_shape(owner_id: int, shape_id: int) -> Shape3D:
	pass;

#desc Returns the number of shapes the given shape owner contains.
func shape_owner_get_shape_count() -> int:
	pass;

#desc Returns the child index of the [Shape3D] with the given id from the given shape owner.
func shape_owner_get_shape_index(owner_id: int, shape_id: int) -> int:
	pass;

#desc Returns the shape owner's [Transform3D].
func shape_owner_get_transform() -> Transform3D:
	pass;

#desc Removes a shape from the given shape owner.
func shape_owner_remove_shape(owner_id: int, shape_id: int) -> void:
	pass;

#desc If [code]true[/code], disables the given shape owner.
func shape_owner_set_disabled(owner_id: int, disabled: bool) -> void:
	pass;

#desc Sets the [Transform3D] of the given shape owner.
func shape_owner_set_transform(owner_id: int, transform: Transform3D) -> void:
	pass;


