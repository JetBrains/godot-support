#brief A 2D polygon.
#desc A Polygon2D is defined by a set of points. Each point is connected to the next, with the final point being connected to the first, resulting in a closed polygon. Polygon2Ds can be filled with color (solid or gradient) or filled with a given texture.
class_name Polygon2D


#desc If [code]true[/code], polygon edges will be anti-aliased.
var antialiased: bool;

var bones: Array;

#desc The polygon's fill color. If [code]texture[/code] is defined, it will be multiplied by this color. It will also be the default color for vertices not set in [code]vertex_colors[/code].
var color: Color;

var internal_vertex_count: int;

#desc Added padding applied to the bounding box when [member invert_enabled] is set to [code]true[/code]. Setting this value too small may result in a "Bad Polygon" error.
var invert_border: float;

#desc If [code]true[/code], the polygon will be inverted, containing the area outside the defined points and extending to the [member invert_border].
var invert_enabled: bool;

#desc The offset applied to each vertex.
var offset: Vector2;

#desc The polygon's list of vertices. The final point will be connected to the first.
#desc [b]Note:[/b] This returns a copy of the [PackedVector2Array] rather than a reference.
var polygon: PackedVector2Array;

var polygons: Array;

var skeleton: NodePath;

#desc The polygon's fill texture. Use [code]uv[/code] to set texture coordinates.
var texture: Texture2D;

#desc Amount to offset the polygon's [code]texture[/code]. If [code](0, 0)[/code] the texture's origin (its top-left corner) will be placed at the polygon's [code]position[/code].
var texture_offset: Vector2;

#desc The texture's rotation in radians.
var texture_rotation: float;

#desc Amount to multiply the [code]uv[/code] coordinates when using a [code]texture[/code]. Larger values make the texture smaller, and vice versa.
var texture_scale: Vector2;

#desc Texture coordinates for each vertex of the polygon. There should be one [code]uv[/code] per polygon vertex. If there are fewer, undefined vertices will use [code](0, 0)[/code].
var uv: PackedVector2Array;

#desc Color for each vertex. Colors are interpolated between vertices, resulting in smooth gradients. There should be one per polygon vertex. If there are fewer, undefined vertices will use [code]color[/code].
var vertex_colors: PackedColorArray;



#desc Adds a bone with the specified [param path] and [param weights].
func add_bone(path: NodePath, weights: PackedFloat32Array) -> void:
	pass;

#desc Removes all bones from this [Polygon2D].
func clear_bones() -> void:
	pass;

#desc Removes the specified bone from this [Polygon2D].
func erase_bone() -> void:
	pass;

#desc Returns the number of bones in this [Polygon2D].
func get_bone_count() -> int:
	pass;

#desc Returns the path to the node associated with the specified bone.
func get_bone_path() -> NodePath:
	pass;

#desc Returns the height values of the specified bone.
func get_bone_weights() -> PackedFloat32Array:
	pass;

#desc Sets the path to the node associated with the specified bone.
func set_bone_path(index: int, path: NodePath) -> void:
	pass;

#desc Sets the weight values for the specified bone.
func set_bone_weights(index: int, weights: PackedFloat32Array) -> void:
	pass;


