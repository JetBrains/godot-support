extends VisualShaderNode
#brief An integer scalar operator to be used within the visual shader graph.
#desc Applies [member operator] to two integer inputs: [code]a[/code] and [code]b[/code].
class_name VisualShaderNodeIntOp

#desc Sums two numbers using [code]a + b[/code].
const OP_ADD = 0;

#desc Subtracts two numbers using [code]a - b[/code].
const OP_SUB = 1;

#desc Multiplies two numbers using [code]a * b[/code].
const OP_MUL = 2;

#desc Divides two numbers using [code]a / b[/code].
const OP_DIV = 3;

#desc Calculates the remainder of two numbers using [code]a % b[/code].
const OP_MOD = 4;

#desc Returns the greater of two numbers. Translates to [code]max(a, b)[/code] in the Godot Shader Language.
const OP_MAX = 5;

#desc Returns the lesser of two numbers. Translates to [code]max(a, b)[/code] in the Godot Shader Language.
const OP_MIN = 6;

#desc Returns the result of bitwise [code]AND[/code] operation on the integer. Translates to [code]a & b[/code] in the Godot Shader Language.
const OP_BITWISE_AND = 7;

#desc Returns the result of bitwise [code]OR[/code] operation for two integers. Translates to [code]a | b[/code] in the Godot Shader Language.
const OP_BITWISE_OR = 8;

#desc Returns the result of bitwise [code]XOR[/code] operation for two integers. Translates to [code]a ^ b[/code] in the Godot Shader Language.
const OP_BITWISE_XOR = 9;

#desc Returns the result of bitwise left shift operation on the integer. Translates to [code]a << b[/code] in the Godot Shader Language.
const OP_BITWISE_LEFT_SHIFT = 10;

#desc Returns the result of bitwise right shift operation on the integer. Translates to [code]a >> b[/code] in the Godot Shader Language.
const OP_BITWISE_RIGHT_SHIFT = 11;

#desc Represents the size of the [enum Operator] enum.
const OP_ENUM_SIZE = 12;


#desc An operator to be applied to the inputs. See [enum Operator] for options.
var operator: int;




