#brief Point sampler for a [Path3D].
#desc This node takes its parent [Path3D], and returns the coordinates of a point within it, given a distance from the first vertex.
#desc It is useful for making other nodes follow a path, without coding the movement pattern. For that, the nodes must be children of this node. The descendant nodes will then move accordingly when setting the [member progress] in this node.
class_name PathFollow3D

#desc Forbids the PathFollow3D to rotate.
const ROTATION_NONE = 0;

#desc Allows the PathFollow3D to rotate in the Y axis only.
const ROTATION_Y = 1;

#desc Allows the PathFollow3D to rotate in both the X, and Y axes.
const ROTATION_XY = 2;

#desc Allows the PathFollow3D to rotate in any axis.
const ROTATION_XYZ = 3;

#desc Uses the up vector information in a [Curve3D] to enforce orientation. This rotation mode requires the [Path3D]'s [member Curve3D.up_vector_enabled] property to be set to [code]true[/code].
const ROTATION_ORIENTED = 4;


#desc If [code]true[/code], the position between two cached points is interpolated cubically, and linearly otherwise.
#desc The points along the [Curve3D] of the [Path3D] are precomputed before use, for faster calculations. The point at the requested offset is then calculated interpolating between two adjacent cached points. This may present a problem if the curve makes sharp turns, as the cached points may not follow the curve closely enough.
#desc There are two answers to this problem: either increase the number of cached points and increase memory consumption, or make a cubic interpolation between two points at the cost of (slightly) slower calculations.
var cubic_interp: bool;

#desc The node's offset along the curve.
var h_offset: float;

#desc If [code]true[/code], any offset outside the path's length will wrap around, instead of stopping at the ends. Use it for cyclic paths.
var loop: bool;

#desc The distance from the first vertex, measured in 3D units along the path. Changing this value sets this node's position to a point within the path.
var progress: float;

#desc The distance from the first vertex, considering 0.0 as the first vertex and 1.0 as the last. This is just another way of expressing the progress within the path, as the progress supplied is multiplied internally by the path's length.
var progress_ratio: float;

#desc Allows or forbids rotation on one or more axes, depending on the [enum RotationMode] constants being used.
var rotation_mode: int;

#desc The node's offset perpendicular to the curve.
var v_offset: float;




