extends VisualShaderNode
#brief A scalar integer function to be used within the visual shader graph.
#desc Accept an integer scalar ([code]x[/code]) to the input port and transform it according to [member function].
class_name VisualShaderNodeIntFunc

#desc Returns the absolute value of the parameter. Translates to [code]abs(x)[/code] in the Godot Shader Language.
const FUNC_ABS = 0;

#desc Negates the [code]x[/code] using [code]-(x)[/code].
const FUNC_NEGATE = 1;

#desc Extracts the sign of the parameter. Translates to [code]sign(x)[/code] in the Godot Shader Language.
const FUNC_SIGN = 2;

#desc Returns the result of bitwise [code]NOT[/code] operation on the integer. Translates to [code]~a[/code] in the Godot Shader Language.
const FUNC_BITWISE_NOT = 3;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 4;


#desc A function to be applied to the scalar. See [enum Function] for options.
var function: int;




