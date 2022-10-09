extends VisualShaderNode
#brief Clamps a value within the visual shader graph.
#desc Constrains a value to lie between [code]min[/code] and [code]max[/code] values.
class_name VisualShaderNodeClamp

#desc A floating-point scalar.
const OP_TYPE_FLOAT = 0;

#desc An integer scalar.
const OP_TYPE_INT = 1;

#desc A 2D vector type.
const OP_TYPE_VECTOR_2D = 2;

#desc A 3D vector type.
const OP_TYPE_VECTOR_3D = 3;

#desc A 4D vector type.
const OP_TYPE_VECTOR_4D = 4;

#desc Represents the size of the [enum OpType] enum.
const OP_TYPE_MAX = 5;


#desc A type of operands and returned value.
var op_type: int;




