#brief Parent of all visual 3D nodes.
#desc The [VisualInstance3D] is used to connect a resource to a visual representation. All visual 3D nodes inherit from the [VisualInstance3D]. In general, you should not access the [VisualInstance3D] properties directly as they are accessed and managed by the nodes that inherit from [VisualInstance3D]. [VisualInstance3D] is the node representation of the [RenderingServer] instance.
class_name VisualInstance3D


#desc The render layer(s) this [VisualInstance3D] is drawn on.
#desc This object will only be visible for [Camera3D]s whose cull mask includes the render object this [VisualInstance3D] is set to.
#desc For [Light3D]s, this can be used to control which [VisualInstance3D]s are affected by a specific light. For [GPUParticles3D], this can be used to control which particles are effected by a specific attractor. For [Decal]s, this can be used to control which [VisualInstance3D]s are affected by a specific decal.
var layers: int;



virtual const func _get_aabb() -> AABB:
	pass;

#desc Returns the [AABB] (also known as the bounding box) for this [VisualInstance3D]. See also [method get_transformed_aabb].
func get_aabb() -> AABB:
	pass;

#desc Returns the RID of the resource associated with this [VisualInstance3D]. For example, if the Node is a [MeshInstance3D], this will return the RID of the associated [Mesh].
func get_base() -> RID:
	pass;

#desc Returns the RID of this instance. This RID is the same as the RID returned by [method RenderingServer.instance_create]. This RID is needed if you want to call [RenderingServer] functions directly on this [VisualInstance3D].
func get_instance() -> RID:
	pass;

#desc Returns whether or not the specified layer of the [member layers] is enabled, given a [code]layer_number[/code] between 1 and 20.
func get_layer_mask_value() -> bool:
	pass;

#desc Returns the transformed [AABB] (also known as the bounding box) for this [VisualInstance3D].
#desc Transformed in this case means the [AABB] plus the position, rotation, and scale of the [Node3D]'s [Transform3D]. See also [method get_aabb].
func get_transformed_aabb() -> AABB:
	pass;

#desc Sets the resource that is instantiated by this [VisualInstance3D], which changes how the engine handles the [VisualInstance3D] under the hood. Equivalent to [method RenderingServer.instance_set_base].
func set_base() -> void:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member layers], given a [param layer_number] between 1 and 20.
func set_layer_mask_value(layer_number: int, value: bool) -> void:
	pass;


