extends PrimitiveMesh
#brief Class representing a cylindrical [PrimitiveMesh].
#desc Class representing a cylindrical [PrimitiveMesh]. This class can be used to create cones by setting either the [member top_radius] or [member bottom_radius] properties to [code]0.0[/code].
class_name CylinderMesh


#desc Bottom radius of the cylinder. If set to [code]0.0[/code], the bottom faces will not be generated, resulting in a conic shape. See also [member cap_bottom].
var bottom_radius: float;

#desc If [code]true[/code], generates a cap at the bottom of the cylinder. This can be set to [code]false[/code] to speed up generation and rendering when the cap is never seen by the camera. See also [member bottom_radius].
#desc [b]Note:[/b] If [member bottom_radius] is [code]0.0[/code], cap generation is always skipped even if [member cap_bottom] is [code]true[/code].
var cap_bottom: bool;

#desc If [code]true[/code], generates a cap at the top of the cylinder. This can be set to [code]false[/code] to speed up generation and rendering when the cap is never seen by the camera. See also [member top_radius].
#desc [b]Note:[/b] If [member top_radius] is [code]0.0[/code], cap generation is always skipped even if [member cap_top] is [code]true[/code].
var cap_top: bool;

#desc Full height of the cylinder.
var height: float;

#desc Number of radial segments on the cylinder. Higher values result in a more detailed cylinder/cone at the cost of performance.
var radial_segments: int;

#desc Number of edge rings along the height of the cylinder. Changing [member rings] does not have any visual impact unless a shader or procedural mesh tool is used to alter the vertex data. Higher values result in more subdivisions, which can be used to create smoother-looking effects with shaders or procedural mesh tools (at the cost of performance). When not altering the vertex data using a shader or procedural mesh tool, [member rings] should be kept to its default value.
var rings: int;

#desc Top radius of the cylinder. If set to [code]0.0[/code], the top faces will not be generated, resulting in a conic shape. See also [member cap_top].
var top_radius: float;




