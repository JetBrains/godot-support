extends VisualShaderNode
#brief A floating-point scalar operator to be used within the visual shader graph.
#desc Applies [member operator] to two floating-point inputs: [code]a[/code] and [code]b[/code].
class_name VisualShaderNodeFloatOp

#desc Sums two numbers using [code]a + b[/code].
const OP_ADD = 0;

#desc Subtracts two numbers using [code]a - b[/code].
const OP_SUB = 1;

#desc Multiplies two numbers using [code]a * b[/code].
const OP_MUL = 2;

#desc Divides two numbers using [code]a / b[/code].
const OP_DIV = 3;

#desc Calculates the remainder of two numbers. Translates to [code]mod(a, b)[/code] in the Godot Shader Language.
const OP_MOD = 4;

#desc Raises the [code]a[/code] to the power of [code]b[/code]. Translates to [code]pow(a, b)[/code] in the Godot Shader Language.
const OP_POW = 5;

#desc Returns the greater of two numbers. Translates to [code]max(a, b)[/code] in the Godot Shader Language.
const OP_MAX = 6;

#desc Returns the lesser of two numbers. Translates to [code]min(a, b)[/code] in the Godot Shader Language.
const OP_MIN = 7;

#desc Returns the arc-tangent of the parameters. Translates to [code]atan(a, b)[/code] in the Godot Shader Language.
const OP_ATAN2 = 8;

#desc Generates a step function by comparing [code]b[/code](x) to [code]a[/code](edge). Returns 0.0 if [code]x[/code] is smaller than [code]edge[/code] and otherwise 1.0. Translates to [code]step(a, b)[/code] in the Godot Shader Language.
const OP_STEP = 9;

#desc Represents the size of the [enum Operator] enum.
const OP_ENUM_SIZE = 10;


#desc An operator to be applied to the inputs. See [enum Operator] for options.
var operator: int;




