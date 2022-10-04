#brief Class representing a prism-shaped [PrimitiveMesh].
#desc Class representing a prism-shaped [PrimitiveMesh].
class_name PrismMesh


#desc Displacement of the upper edge along the X axis. 0.0 positions edge straight above the bottom-left edge.
var left_to_right: float;

#desc Size of the prism.
var size: Vector3;

#desc Number of added edge loops along the Z axis.
var subdivide_depth: int;

#desc Number of added edge loops along the Y axis.
var subdivide_height: int;

#desc Number of added edge loops along the X axis.
var subdivide_width: int;




