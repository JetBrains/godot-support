extends VisualShaderNode
class_name VisualShaderNodeUniform

const QUAL_NONE = 0;
const QUAL_GLOBAL = 1;
const QUAL_INSTANCE = 2;

var qualifier: int setget set_qualifier, get_qualifier;
var uniform_name: String setget set_uniform_name, get_uniform_name;

