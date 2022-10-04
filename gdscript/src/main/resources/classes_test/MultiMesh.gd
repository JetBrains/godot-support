#brief Provides high-performance drawing of a mesh multiple times using GPU instancing.
#desc MultiMesh provides low-level mesh instancing. Drawing thousands of [MeshInstance3D] nodes can be slow, since each object is submitted to the GPU then drawn individually.
#desc MultiMesh is much faster as it can draw thousands of instances with a single draw call, resulting in less API overhead.
#desc As a drawback, if the instances are too far away from each other, performance may be reduced as every single instance will always render (they are spatially indexed as one, for the whole object).
#desc Since instances may have any behavior, the AABB used for visibility must be provided by the user.
#desc [b]Note:[/b] A MultiMesh is a single object, therefore the same maximum lights per object restriction applies. This means, that once the maximum lights are consumed by one or more instances, the rest of the MultiMesh instances will [b]not[/b] receive any lighting.
#desc [b]Note:[/b] Blend Shapes will be ignored if used in a MultiMesh.
class_name MultiMesh

#desc Use this when using 2D transforms.
const TRANSFORM_2D = 0;

#desc Use this when using 3D transforms.
const TRANSFORM_3D = 1;


var buffer: PackedFloat32Array;

#desc See [method set_instance_color].
var color_array: PackedColorArray;

#desc See [method set_instance_custom_data].
var custom_data_array: PackedColorArray;

#desc Number of instances that will get drawn. This clears and (re)sizes the buffers. Setting data format or flags afterwards will have no effect.
#desc By default, all instances are drawn but you can limit this with [member visible_instance_count].
var instance_count: int;

#desc [Mesh] resource to be instanced.
#desc The looks of the individual instances can be modified using [method set_instance_color] and [method set_instance_custom_data].
var mesh: Mesh;

#desc See [method set_instance_transform_2d].
var transform_2d_array: PackedVector2Array;

#desc See [method set_instance_transform].
var transform_array: PackedVector3Array;

#desc Format of transform used to transform mesh, either 2D or 3D.
var transform_format: int;

#desc If [code]true[/code], the [MultiMesh] will use color data (see [method set_instance_color]). Can only be set when [member instance_count] is [code]0[/code] or less. This means that you need to call this method before setting the instance count, or temporarily reset it to [code]0[/code].
var use_colors: bool;

#desc If [code]true[/code], the [MultiMesh] will use custom data (see [method set_instance_custom_data]). Can only be set when [member instance_count] is [code]0[/code] or less. This means that you need to call this method before setting the instance count, or temporarily reset it to [code]0[/code].
var use_custom_data: bool;

#desc Limits the number of instances drawn, -1 draws all instances. Changing this does not change the sizes of the buffers.
var visible_instance_count: int;



#desc Returns the visibility axis-aligned bounding box in local space. See also [method VisualInstance3D.get_transformed_aabb].
func get_aabb() -> AABB:
	pass;

#desc Gets a specific instance's color multiplier.
func get_instance_color() -> Color:
	pass;

#desc Returns the custom data that has been set for a specific instance.
func get_instance_custom_data() -> Color:
	pass;

#desc Returns the [Transform3D] of a specific instance.
func get_instance_transform() -> Transform3D:
	pass;

#desc Returns the [Transform2D] of a specific instance.
func get_instance_transform_2d() -> Transform2D:
	pass;

#desc Sets the color of a specific instance by [i]multiplying[/i] the mesh's existing vertex colors. This allows for different color tinting per instance.
#desc For the color to take effect, ensure that [member use_colors] is [code]true[/code] on the [MultiMesh] and [member BaseMaterial3D.vertex_color_use_as_albedo] is [code]true[/code] on the material. If you intend to set an absolute color instead of tinting, make sure the material's albedo color is set to pure white ([code]Color(1, 1, 1)[/code]).
func set_instance_color(instance: int, color: Color) -> void:
	pass;

#desc Sets custom data for a specific instance. Although [Color] is used, it is just a container for 4 floating point numbers.
#desc For the custom data to be used, ensure that [member use_custom_data] is [code]true[/code].
#desc This custom instance data has to be manually accessed in your custom shader using [code]INSTANCE_CUSTOM[/code].
func set_instance_custom_data(instance: int, custom_data: Color) -> void:
	pass;

#desc Sets the [Transform3D] for a specific instance.
func set_instance_transform(instance: int, transform: Transform3D) -> void:
	pass;

#desc Sets the [Transform2D] for a specific instance.
func set_instance_transform_2d(instance: int, transform: Transform2D) -> void:
	pass;


