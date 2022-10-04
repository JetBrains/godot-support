extends VisualShaderNode
#brief Calculates a derivative within the visual shader graph.
#desc This node is only available in [code]Fragment[/code] and [code]Light[/code] visual shaders.
class_name VisualShaderNodeDerivativeFunc

#desc A floating-point scalar.
const OP_TYPE_SCALAR = 0;

#desc A 2D vector type.
const OP_TYPE_VECTOR_2D = 1;

#desc A 3D vector type.
const OP_TYPE_VECTOR_3D = 2;

#desc A 4D vector type.
const OP_TYPE_VECTOR_4D = 3;

#desc Represents the size of the [enum OpType] enum.
const OP_TYPE_MAX = 4;

#desc Sum of absolute derivative in [code]x[/code] and [code]y[/code].
const FUNC_SUM = 0;

#desc Derivative in [code]x[/code] using local differencing.
const FUNC_X = 1;

#desc Derivative in [code]y[/code] using local differencing.
const FUNC_Y = 2;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 3;


#desc A derivative function type. See [enum Function] for options.
var function: int;

#desc A type of operands and returned value. See [enum OpType] for options.
var op_type: int;




