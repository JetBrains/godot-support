extends VisualShaderNodeUniform
class_name VisualShaderNodeIntUniform

const HINT_NONE = 0;
const HINT_RANGE = 1;
const HINT_RANGE_STEP = 2;

var default_value: int setget set_default_value, get_default_value;
var default_value_enabled: bool setget set_default_value_enabled, is_default_value_enabled;
var hint: int setget set_hint, get_hint;
var max: int setget set_max, get_max;
var min: int setget set_min, get_min;
var step: int setget set_step, get_step;

