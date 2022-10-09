extends VisualShaderNode
#brief A [Transform3D] operator to be used within the visual shader graph.
#desc Applies [member operator] to two transform (4x4 matrices) inputs.
class_name VisualShaderNodeTransformOp

#desc Multiplies transform [code]a[/code] by the transform [code]b[/code].
const OP_AxB = 0;

#desc Multiplies transform [code]b[/code] by the transform [code]a[/code].
const OP_BxA = 1;

#desc Performs a component-wise multiplication of transform [code]a[/code] by the transform [code]b[/code].
const OP_AxB_COMP = 2;

#desc Performs a component-wise multiplication of transform [code]b[/code] by the transform [code]a[/code].
const OP_BxA_COMP = 3;

#desc Adds two transforms.
const OP_ADD = 4;

#desc Subtracts the transform [code]a[/code] from the transform [code]b[/code].
const OP_A_MINUS_B = 5;

#desc Subtracts the transform [code]b[/code] from the transform [code]a[/code].
const OP_B_MINUS_A = 6;

#desc Divides the transform [code]a[/code] by the transform [code]b[/code].
const OP_A_DIV_B = 7;

#desc Divides the transform [code]b[/code] by the transform [code]a[/code].
const OP_B_DIV_A = 8;

#desc Represents the size of the [enum Operator] enum.
const OP_MAX = 9;


#desc The type of the operation to be performed on the transforms. See [enum Operator] for options.
var operator: int;




