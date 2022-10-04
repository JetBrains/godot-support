#brief Editor-only node for defining a collision polygon in 3D space.
#desc Allows editing a concave or convex collision polygon's vertices on a selected plane. Can also set a depth perpendicular to that plane. This class is only available in the editor. It will not appear in the scene tree at run-time. Creates several [ConvexPolygonShape3D]s at run-time to represent the original polygon using convex decomposition.
#desc [b]Note:[/b] Since this is an editor-only helper, properties modified during gameplay will have no effect.
class_name CollisionPolygon3D


#desc Length that the resulting collision extends in either direction perpendicular to its polygon.
var depth: float;

#desc If [code]true[/code], no collision will be produced.
var disabled: bool;

#desc The collision margin for the generated [Shape3D]. See [member Shape3D.margin] for more details.
var margin: float;

#desc Array of vertices which define the polygon.
#desc [b]Note:[/b] The returned value is a copy of the original. Methods which mutate the size or properties of the return value will not impact the original polygon. To change properties of the polygon, assign it to a temporary variable and make changes before reassigning the [code]polygon[/code] member.
var polygon: PackedVector2Array;




