extends VisualShaderNode
class_name VisualShaderNodeCompare

const CTYPE_SCALAR = 0;
const CTYPE_SCALAR_INT = 1;
const CTYPE_VECTOR = 2;
const CTYPE_BOOLEAN = 3;
const CTYPE_TRANSFORM = 4;
const FUNC_EQUAL = 0;
const FUNC_NOT_EQUAL = 1;
const FUNC_GREATER_THAN = 2;
const FUNC_GREATER_THAN_EQUAL = 3;
const FUNC_LESS_THAN = 4;
const FUNC_LESS_THAN_EQUAL = 5;
const COND_ALL = 0;
const COND_ANY = 1;

var condition: int setget set_condition, get_condition;
var function: int setget set_function, get_function;
var type: int setget set_comparison_type, get_comparison_type;

