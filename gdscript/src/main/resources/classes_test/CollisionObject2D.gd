extends Node2D
#brief Base node for 2D collision objects.
#desc CollisionObject2D is the base class for 2D physics objects. It can hold any number of 2D collision [Shape2D]s. Each shape must be assigned to a [i]shape owner[/i]. The CollisionObject2D can have any number of shape owners. Shape owners are not nodes and do not appear in the editor, but are accessible through code using the [code]shape_owner_*[/code] methods.
#desc [b]Note:[/b] Only collisions between objects within the same canvas ([Viewport] canvas or [CanvasLayer]) are supported. The behavior of collisions between objects in different canvases is undefined.
class_name CollisionObject2D

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], remove from the physics simulation to stop all physics interactions with this [CollisionObject2D].
#desc Automatically re-added to the physics simulation when the [Node] is processed again.
const DISABLE_MODE_REMOVE = 0;

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], make the body static. Doesn't affect [Area2D]. [PhysicsBody2D] can't be affected by forces or other bodies while static.
#desc Automatically set [PhysicsBody2D] back to its original mode when the [Node] is processed again.
const DISABLE_MODE_MAKE_STATIC = 1;

#desc When [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED], do not affect the physics simulation.
const DISABLE_MODE_KEEP_ACTIVE = 2;


#desc The physics layers this CollisionObject2D is in. Collision objects can exist in one or more of 32 different layers. See also [member collision_mask].
#desc [b]Note:[/b] Object A can detect a contact with object B only if object B is in any of the layers that object A scans. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_layer: int;

#desc The physics layers this CollisionObject2D scans. Collision objects can scan one or more of 32 different layers. See also [member collision_layer].
#desc [b]Note:[/b] Object A can detect a contact with object B only if object B is in any of the layers that object A scans. See [url=$DOCS_URL/tutorials/physics/physics_introduction.html#collision-layers-and-masks]Collision layers and masks[/url] in the documentation for more information.
var collision_mask: int;

#desc The priority used to solve colliding when occurring penetration. The higher the priority is, the lower the penetration into the object will be. This can for example be used to prevent the player from breaking through the boundaries of a level.
var collision_priority: float;

#desc Defines the behavior in physics when [member Node.process_mode] is set to [constant Node.PROCESS_MODE_DISABLED]. See [enum DisableMode] for more details about the different modes.
var disable_mode: int;

#desc If [code]true[/code], this object is pickable. A pickable object can detect the mouse pointer entering/leaving, and if the mouse is inside it, report input events. Requires at least one [member collision_layer] bit to be set.
var input_pickable: bool;



#desc Accepts unhandled [InputEvent]s. [param shape_idx] is the child index of the clicked [Shape2D]. Connect to the [code]input_event[/code] signal to easily pick up these events.
#desc [b]Note:[/b] [method _input_event] requires [member input_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be set.
func _input_event(viewport: Viewport, event: InputEvent, shape_idx: int) -> void:
	pass;

#desc Called when the mouse pointer enters any of this object's shapes. Requires [member input_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be set. Note that moving between different shapes within a single [CollisionObject2D] won't cause this function to be called.
func _mouse_enter() -> void:
	pass;

#desc Called when the mouse pointer exits all this object's shapes. Requires [member input_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be set. Note that moving between different shapes within a single [CollisionObject2D] won't cause this function to be called.
func _mouse_exit() -> void:
	pass;

#desc Called when the mouse pointer enters any of this object's shapes or moves from one shape to another. [param shape_idx] is the child index of the newly entered [Shape2D]. Requires [member input_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be called.
func _mouse_shape_enter(shape_idx: int) -> void:
	pass;

#desc Called when the mouse pointer exits any of this object's shapes. [param shape_idx] is the child index of the exited [Shape2D]. Requires [member input_pickable] to be [code]true[/code] and at least one [member collision_layer] bit to be called.
func _mouse_shape_exit(shape_idx: int) -> void:
	pass;

#desc Creates a new shape owner for the given object. Returns [code]owner_id[/code] of the new owner for future reference.
func create_shape_owner(owner: Object) -> int:
	pass;

#desc Returns whether or not the specified layer of the [member collision_layer] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_layer_value(layer_number: int) -> bool:
	pass;

#desc Returns whether or not the specified layer of the [member collision_mask] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_mask_value(layer_number: int) -> bool:
	pass;

#desc Returns the object's [RID].
func get_rid() -> RID:
	pass;

#desc Returns the [code]one_way_collision_margin[/code] of the shape owner identified by given [param owner_id].
func get_shape_owner_one_way_collision_margin(owner_id: int) -> float:
	pass;

#desc Returns an [Array] of [code]owner_id[/code] identifiers. You can use these ids in other methods that take [code]owner_id[/code] as an argument.
func get_shape_owners() -> PackedInt32Array:
	pass;

#desc If [code]true[/code], the shape owner and its shapes are disabled.
func is_shape_owner_disabled(owner_id: int) -> bool:
	pass;

#desc Returns [code]true[/code] if collisions for the shape owner originating from this [CollisionObject2D] will not be reported to collided with [CollisionObject2D]s.
func is_shape_owner_one_way_collision_enabled(owner_id: int) -> bool:
	pass;

#desc Removes the given shape owner.
func remove_shape_owner(owner_id: int) -> void:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member collision_layer], given a [param layer_number] between 1 and 32.
func set_collision_layer_value(layer_number: int, value: bool) -> void:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member collision_mask], given a [param layer_number] between 1 and 32.
func set_collision_mask_value(layer_number: int, value: bool) -> void:
	pass;

#desc Returns the [code]owner_id[/code] of the given shape.
func shape_find_owner(shape_index: int) -> int:
	pass;

#desc Adds a [Shape2D] to the shape owner.
func shape_owner_add_shape(owner_id: int, shape: Shape2D) -> void:
	pass;

#desc Removes all shapes from the shape owner.
func shape_owner_clear_shapes(owner_id: int) -> void:
	pass;

#desc Returns the parent object of the given shape owner.
func shape_owner_get_owner(owner_id: int) -> Object:
	pass;

#desc Returns the [Shape2D] with the given id from the given shape owner.
func shape_owner_get_shape(owner_id: int, shape_id: int) -> Shape2D:
	pass;

#desc Returns the number of shapes the given shape owner contains.
func shape_owner_get_shape_count(owner_id: int) -> int:
	pass;

#desc Returns the child index of the [Shape2D] with the given id from the given shape owner.
func shape_owner_get_shape_index(owner_id: int, shape_id: int) -> int:
	pass;

#desc Returns the shape owner's [Transform2D].
func shape_owner_get_transform(owner_id: int) -> Transform2D:
	pass;

#desc Removes a shape from the given shape owner.
func shape_owner_remove_shape(owner_id: int, shape_id: int) -> void:
	pass;

#desc If [code]true[/code], disables the given shape owner.
func shape_owner_set_disabled(owner_id: int, disabled: bool) -> void:
	pass;

#desc If [param enable] is [code]true[/code], collisions for the shape owner originating from this [CollisionObject2D] will not be reported to collided with [CollisionObject2D]s.
func shape_owner_set_one_way_collision(owner_id: int, enable: bool) -> void:
	pass;

#desc Sets the [code]one_way_collision_margin[/code] of the shape owner identified by given [param owner_id] to [param margin] pixels.
func shape_owner_set_one_way_collision_margin(owner_id: int, margin: float) -> void:
	pass;

#desc Sets the [Transform2D] of the given shape owner.
func shape_owner_set_transform(owner_id: int, transform: Transform2D) -> void:
	pass;


