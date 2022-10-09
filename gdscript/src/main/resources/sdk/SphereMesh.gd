extends PrimitiveMesh
#brief Class representing a spherical [PrimitiveMesh].
#desc Class representing a spherical [PrimitiveMesh].
class_name SphereMesh


#desc Full height of the sphere.
var height: float;

#desc If [code]true[/code], a hemisphere is created rather than a full sphere.
#desc [b]Note:[/b] To get a regular hemisphere, the height and radius of the sphere must be equal.
var is_hemisphere: bool;

#desc Number of radial segments on the sphere.
var radial_segments: int;

#desc Radius of sphere.
var radius: float;

#desc Number of segments along the height of the sphere.
var rings: int;




