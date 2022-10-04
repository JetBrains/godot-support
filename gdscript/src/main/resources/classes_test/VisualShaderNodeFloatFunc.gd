extends VisualShaderNode
#brief A scalar floating-point function to be used within the visual shader graph.
#desc Accept a floating-point scalar ([code]x[/code]) to the input port and transform it according to [member function].
class_name VisualShaderNodeFloatFunc

#desc Returns the sine of the parameter. Translates to [code]sin(x)[/code] in the Godot Shader Language.
const FUNC_SIN = 0;

#desc Returns the cosine of the parameter. Translates to [code]cos(x)[/code] in the Godot Shader Language.
const FUNC_COS = 1;

#desc Returns the tangent of the parameter. Translates to [code]tan(x)[/code] in the Godot Shader Language.
const FUNC_TAN = 2;

#desc Returns the arc-sine of the parameter. Translates to [code]asin(x)[/code] in the Godot Shader Language.
const FUNC_ASIN = 3;

#desc Returns the arc-cosine of the parameter. Translates to [code]acos(x)[/code] in the Godot Shader Language.
const FUNC_ACOS = 4;

#desc Returns the arc-tangent of the parameter. Translates to [code]atan(x)[/code] in the Godot Shader Language.
const FUNC_ATAN = 5;

#desc Returns the hyperbolic sine of the parameter. Translates to [code]sinh(x)[/code] in the Godot Shader Language.
const FUNC_SINH = 6;

#desc Returns the hyperbolic cosine of the parameter. Translates to [code]cosh(x)[/code] in the Godot Shader Language.
const FUNC_COSH = 7;

#desc Returns the hyperbolic tangent of the parameter. Translates to [code]tanh(x)[/code] in the Godot Shader Language.
const FUNC_TANH = 8;

#desc Returns the natural logarithm of the parameter. Translates to [code]log(x)[/code] in the Godot Shader Language.
const FUNC_LOG = 9;

#desc Returns the natural exponentiation of the parameter. Translates to [code]exp(x)[/code] in the Godot Shader Language.
const FUNC_EXP = 10;

#desc Returns the square root of the parameter. Translates to [code]sqrt(x)[/code] in the Godot Shader Language.
const FUNC_SQRT = 11;

#desc Returns the absolute value of the parameter. Translates to [code]abs(x)[/code] in the Godot Shader Language.
const FUNC_ABS = 12;

#desc Extracts the sign of the parameter. Translates to [code]sign(x)[/code] in the Godot Shader Language.
const FUNC_SIGN = 13;

#desc Finds the nearest integer less than or equal to the parameter. Translates to [code]floor(x)[/code] in the Godot Shader Language.
const FUNC_FLOOR = 14;

#desc Finds the nearest integer to the parameter. Translates to [code]round(x)[/code] in the Godot Shader Language.
const FUNC_ROUND = 15;

#desc Finds the nearest integer that is greater than or equal to the parameter. Translates to [code]ceil(x)[/code] in the Godot Shader Language.
const FUNC_CEIL = 16;

#desc Computes the fractional part of the argument. Translates to [code]fract(x)[/code] in the Godot Shader Language.
const FUNC_FRACT = 17;

#desc Clamps the value between [code]0.0[/code] and [code]1.0[/code] using [code]min(max(x, 0.0), 1.0)[/code].
const FUNC_SATURATE = 18;

#desc Negates the [code]x[/code] using [code]-(x)[/code].
const FUNC_NEGATE = 19;

#desc Returns the arc-hyperbolic-cosine of the parameter. Translates to [code]acosh(x)[/code] in the Godot Shader Language.
const FUNC_ACOSH = 20;

#desc Returns the arc-hyperbolic-sine of the parameter. Translates to [code]asinh(x)[/code] in the Godot Shader Language.
const FUNC_ASINH = 21;

#desc Returns the arc-hyperbolic-tangent of the parameter. Translates to [code]atanh(x)[/code] in the Godot Shader Language.
const FUNC_ATANH = 22;

#desc Convert a quantity in radians to degrees. Translates to [code]degrees(x)[/code] in the Godot Shader Language.
const FUNC_DEGREES = 23;

#desc Returns 2 raised by the power of the parameter. Translates to [code]exp2(x)[/code] in the Godot Shader Language.
const FUNC_EXP2 = 24;

#desc Returns the inverse of the square root of the parameter. Translates to [code]inversesqrt(x)[/code] in the Godot Shader Language.
const FUNC_INVERSE_SQRT = 25;

#desc Returns the base 2 logarithm of the parameter. Translates to [code]log2(x)[/code] in the Godot Shader Language.
const FUNC_LOG2 = 26;

#desc Convert a quantity in degrees to radians. Translates to [code]radians(x)[/code] in the Godot Shader Language.
const FUNC_RADIANS = 27;

#desc Finds reciprocal value of dividing 1 by [code]x[/code] (i.e. [code]1 / x[/code]).
const FUNC_RECIPROCAL = 28;

#desc Finds the nearest even integer to the parameter. Translates to [code]roundEven(x)[/code] in the Godot Shader Language.
const FUNC_ROUNDEVEN = 29;

#desc Returns a value equal to the nearest integer to [code]x[/code] whose absolute value is not larger than the absolute value of [code]x[/code]. Translates to [code]trunc(x)[/code] in the Godot Shader Language.
const FUNC_TRUNC = 30;

#desc Subtracts scalar [code]x[/code] from 1 (i.e. [code]1 - x[/code]).
const FUNC_ONEMINUS = 31;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 32;


#desc A function to be applied to the scalar. See [enum Function] for options.
var function: int;




