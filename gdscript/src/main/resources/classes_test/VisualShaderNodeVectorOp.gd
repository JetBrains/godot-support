extends VisualShaderNodeVectorBase
#brief A vector operator to be used within the visual shader graph.
#desc A visual shader node for use of vector operators. Operates on vector [code]a[/code] and vector [code]b[/code].
class_name VisualShaderNodeVectorOp

#desc Adds two vectors.
const OP_ADD = 0;

#desc Subtracts a vector from a vector.
const OP_SUB = 1;

#desc Multiplies two vectors.
const OP_MUL = 2;

#desc Divides vector by vector.
const OP_DIV = 3;

#desc Returns the remainder of the two vectors.
const OP_MOD = 4;

#desc Returns the value of the first parameter raised to the power of the second, for each component of the vectors.
const OP_POW = 5;

#desc Returns the greater of two values, for each component of the vectors.
const OP_MAX = 6;

#desc Returns the lesser of two values, for each component of the vectors.
const OP_MIN = 7;

#desc Calculates the cross product of two vectors.
const OP_CROSS = 8;

#desc Returns the arc-tangent of the parameters.
const OP_ATAN2 = 9;

#desc Returns the vector that points in the direction of reflection. [code]a[/code] is incident vector and [code]b[/code] is the normal vector.
const OP_REFLECT = 10;

#desc Vector step operator. Returns [code]0.0[/code] if [code]a[/code] is smaller than [code]b[/code] and [code]1.0[/code] otherwise.
const OP_STEP = 11;

#desc Represents the size of the [enum Operator] enum.
const OP_ENUM_SIZE = 12;


#desc The operator to be used. See [enum Operator] for options.
var operator: int;




