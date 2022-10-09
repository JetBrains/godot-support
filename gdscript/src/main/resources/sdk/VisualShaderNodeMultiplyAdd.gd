extends VisualShaderNode
#brief Performs a fused multiply-add operation within the visual shader graph.
#desc Uses three operands to compute [code](a * b + c)[/code] expression.
class_name VisualShaderNodeMultiplyAdd

#desc A floating-point scalar type.
const OP_TYPE_SCALAR = 0;

#desc A 2D vector type.
const OP_TYPE_VECTOR_2D = 1;

#desc A 3D vector type.
const OP_TYPE_VECTOR_3D = 2;

#desc A 4D vector type.
const OP_TYPE_VECTOR_4D = 3;

#desc Represents the size of the [enum OpType] enum.
const OP_TYPE_MAX = 4;


#desc A type of operands and returned value.
var op_type: int;




