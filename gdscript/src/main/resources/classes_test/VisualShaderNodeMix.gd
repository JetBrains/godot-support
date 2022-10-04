#brief Linearly interpolates between two values within the visual shader graph.
#desc Translates to [code]mix(a, b, weight)[/code] in the shader language.
class_name VisualShaderNodeMix

#desc A floating-point scalar.
const OP_TYPE_SCALAR = 0;

#desc A 2D vector type.
const OP_TYPE_VECTOR_2D = 1;

#desc The [code]a[/code] and [code]b[/code] ports use a 2D vector type. The [code]weight[/code] port uses a scalar type.
const OP_TYPE_VECTOR_2D_SCALAR = 2;

#desc A 3D vector type.
const OP_TYPE_VECTOR_3D = 3;

#desc The [code]a[/code] and [code]b[/code] ports use a 3D vector type. The [code]weight[/code] port uses a scalar type.
const OP_TYPE_VECTOR_3D_SCALAR = 4;

#desc A 4D vector type.
const OP_TYPE_VECTOR_4D = 5;

#desc The [code]a[/code] and [code]b[/code] ports use a 4D vector type. The [code]weight[/code] port uses a scalar type.
const OP_TYPE_VECTOR_4D_SCALAR = 6;

#desc Represents the size of the [enum OpType] enum.
const OP_TYPE_MAX = 7;


#desc A type of operands and returned value.
var op_type: int;




