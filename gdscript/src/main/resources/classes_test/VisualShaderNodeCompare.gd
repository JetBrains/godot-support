#brief A comparison function for common types within the visual shader graph.
#desc Compares [code]a[/code] and [code]b[/code] of [member type] by [member function]. Returns a boolean scalar. Translates to [code]if[/code] instruction in shader code.
class_name VisualShaderNodeCompare

#desc A floating-point scalar.
const CTYPE_SCALAR = 0;

#desc An integer scalar.
const CTYPE_SCALAR_INT = 1;

#desc A 2D vector type.
const CTYPE_VECTOR_2D = 2;

#desc A 3D vector type.
const CTYPE_VECTOR_3D = 3;

#desc A 4D vector type.
const CTYPE_VECTOR_4D = 4;

#desc A boolean type.
const CTYPE_BOOLEAN = 5;

#desc A transform ([code]mat4[/code]) type.
const CTYPE_TRANSFORM = 6;

#desc Represents the size of the [enum ComparisonType] enum.
const CTYPE_MAX = 7;

#desc Comparison for equality ([code]a == b[/code]).
const FUNC_EQUAL = 0;

#desc Comparison for inequality ([code]a != b[/code]).
const FUNC_NOT_EQUAL = 1;

#desc Comparison for greater than ([code]a > b[/code]). Cannot be used if [member type] set to [constant CTYPE_BOOLEAN] or [constant CTYPE_TRANSFORM].
const FUNC_GREATER_THAN = 2;

#desc Comparison for greater than or equal ([code]a >= b[/code]). Cannot be used if [member type] set to [constant CTYPE_BOOLEAN] or [constant CTYPE_TRANSFORM].
const FUNC_GREATER_THAN_EQUAL = 3;

#desc Comparison for less than ([code]a < b[/code]). Cannot be used if [member type] set to [constant CTYPE_BOOLEAN] or [constant CTYPE_TRANSFORM].
const FUNC_LESS_THAN = 4;

#desc Comparison for less than or equal ([code]a <= b[/code]). Cannot be used if [member type] set to [constant CTYPE_BOOLEAN] or [constant CTYPE_TRANSFORM].
const FUNC_LESS_THAN_EQUAL = 5;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 6;

#desc The result will be true if all of component in vector satisfy the comparison condition.
const COND_ALL = 0;

#desc The result will be true if any of component in vector satisfy the comparison condition.
const COND_ANY = 1;

#desc Represents the size of the [enum Condition] enum.
const COND_MAX = 2;


#desc Extra condition which is applied if [member type] is set to [constant CTYPE_VECTOR_3D].
var condition: int;

#desc A comparison function. See [enum Function] for options.
var function: int;

#desc The type to be used in the comparison. See [enum ComparisonType] for options.
var type: int;




