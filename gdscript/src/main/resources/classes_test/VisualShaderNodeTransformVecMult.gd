#brief Multiplies a [Transform3D] and a [Vector3] within the visual shader graph.
#desc A multiplication operation on a transform (4x4 matrix) and a vector, with support for different multiplication operators.
class_name VisualShaderNodeTransformVecMult

#desc Multiplies transform [code]a[/code] by the vector [code]b[/code].
const OP_AxB = 0;

#desc Multiplies vector [code]b[/code] by the transform [code]a[/code].
const OP_BxA = 1;

#desc Multiplies transform [code]a[/code] by the vector [code]b[/code], skipping the last row and column of the transform.
const OP_3x3_AxB = 2;

#desc Multiplies vector [code]b[/code] by the transform [code]a[/code], skipping the last row and column of the transform.
const OP_3x3_BxA = 3;

#desc Represents the size of the [enum Operator] enum.
const OP_MAX = 4;


#desc The multiplication type to be performed. See [enum Operator] for options.
var operator: int;




