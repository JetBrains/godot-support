extends VisualShaderNodeVectorBase
#brief A vector function to be used within the visual shader graph.
#desc A visual shader node able to perform different functions using vectors.
class_name VisualShaderNodeVectorFunc

#desc Normalizes the vector so that it has a length of [code]1[/code] but points in the same direction.
const FUNC_NORMALIZE = 0;

#desc Clamps the value between [code]0.0[/code] and [code]1.0[/code].
const FUNC_SATURATE = 1;

#desc Returns the opposite value of the parameter.
const FUNC_NEGATE = 2;

#desc Returns [code]1/vector[/code].
const FUNC_RECIPROCAL = 3;

#desc Returns the absolute value of the parameter.
const FUNC_ABS = 4;

#desc Returns the arc-cosine of the parameter.
const FUNC_ACOS = 5;

#desc Returns the inverse hyperbolic cosine of the parameter.
const FUNC_ACOSH = 6;

#desc Returns the arc-sine of the parameter.
const FUNC_ASIN = 7;

#desc Returns the inverse hyperbolic sine of the parameter.
const FUNC_ASINH = 8;

#desc Returns the arc-tangent of the parameter.
const FUNC_ATAN = 9;

#desc Returns the inverse hyperbolic tangent of the parameter.
const FUNC_ATANH = 10;

#desc Finds the nearest integer that is greater than or equal to the parameter.
const FUNC_CEIL = 11;

#desc Returns the cosine of the parameter.
const FUNC_COS = 12;

#desc Returns the hyperbolic cosine of the parameter.
const FUNC_COSH = 13;

#desc Converts a quantity in radians to degrees.
const FUNC_DEGREES = 14;

#desc Base-e Exponential.
const FUNC_EXP = 15;

#desc Base-2 Exponential.
const FUNC_EXP2 = 16;

#desc Finds the nearest integer less than or equal to the parameter.
const FUNC_FLOOR = 17;

#desc Computes the fractional part of the argument.
const FUNC_FRACT = 18;

#desc Returns the inverse of the square root of the parameter.
const FUNC_INVERSE_SQRT = 19;

#desc Natural logarithm.
const FUNC_LOG = 20;

#desc Base-2 logarithm.
const FUNC_LOG2 = 21;

#desc Converts a quantity in degrees to radians.
const FUNC_RADIANS = 22;

#desc Finds the nearest integer to the parameter.
const FUNC_ROUND = 23;

#desc Finds the nearest even integer to the parameter.
const FUNC_ROUNDEVEN = 24;

#desc Extracts the sign of the parameter, i.e. returns [code]-1[/code] if the parameter is negative, [code]1[/code] if it's positive and [code]0[/code] otherwise.
const FUNC_SIGN = 25;

#desc Returns the sine of the parameter.
const FUNC_SIN = 26;

#desc Returns the hyperbolic sine of the parameter.
const FUNC_SINH = 27;

#desc Returns the square root of the parameter.
const FUNC_SQRT = 28;

#desc Returns the tangent of the parameter.
const FUNC_TAN = 29;

#desc Returns the hyperbolic tangent of the parameter.
const FUNC_TANH = 30;

#desc Returns a value equal to the nearest integer to the parameter whose absolute value is not larger than the absolute value of the parameter.
const FUNC_TRUNC = 31;

#desc Returns [code]1.0 - vector[/code].
const FUNC_ONEMINUS = 32;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 33;


#desc The function to be performed. See [enum Function] for options.
var function: int;




