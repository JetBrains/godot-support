extends VisualShaderNode
#brief A node that controls how the object faces the camera to be used within the visual shader graph.
#desc The output port of this node needs to be connected to [code]Model View Matrix[/code] port of [VisualShaderNodeOutput].
class_name VisualShaderNodeBillboard

#desc Billboarding is disabled and the node does nothing.
const BILLBOARD_TYPE_DISABLED = 0;

#desc A standard billboarding algorithm is enabled.
const BILLBOARD_TYPE_ENABLED = 1;

#desc A billboarding algorithm to rotate around Y-axis is enabled.
const BILLBOARD_TYPE_FIXED_Y = 2;

#desc A billboarding algorithm designed to use on particles is enabled.
const BILLBOARD_TYPE_PARTICLES = 3;

#desc Represents the size of the [enum BillboardType] enum.
const BILLBOARD_TYPE_MAX = 4;


#desc Controls how the object faces the camera. See [enum BillboardType].
var billboard_type: int;

#desc If [code]true[/code], the shader will keep the scale set for the mesh. Otherwise, the scale is lost when billboarding.
var keep_scale: bool;




