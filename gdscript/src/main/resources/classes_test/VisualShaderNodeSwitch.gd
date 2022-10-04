#brief A selector function for use within the visual shader graph.
#desc Returns an associated value of the [code]op_type[/code] type if the provided boolean value is [code]true[/code] or [code]false[/code].
class_name VisualShaderNodeSwitch

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

#desc A boolean type.
const OP_TYPE_BOOLEAN = 5;

#desc A transform type.
const OP_TYPE_TRANSFORM = 6;

#desc Represents the size of the [enum OpType] enum.
const OP_TYPE_MAX = 7;


#desc A type of operands and returned value.
var op_type: int;




