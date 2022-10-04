extends VisualShaderNode
#brief A base type for the nodes using different vector types within the visual shader graph.
class_name VisualShaderNodeVectorBase

#desc A 2D vector type.
const OP_TYPE_VECTOR_2D = 0;

#desc A 3D vector type.
const OP_TYPE_VECTOR_3D = 1;

#desc A 4D vector type.
const OP_TYPE_VECTOR_4D = 2;

#desc Represents the size of the [enum OpType] enum.
const OP_TYPE_MAX = 3;


#desc A base type.
var op_type: int;




