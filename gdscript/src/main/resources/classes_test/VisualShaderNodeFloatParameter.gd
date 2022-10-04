#brief A scalar float parameter to be used within the visual shader graph.
#desc Translated to [code]uniform float[/code] in the shader language.
class_name VisualShaderNodeFloatParameter

#desc No hint used.
const HINT_NONE = 0;

#desc A range hint for scalar value, which limits possible input values between [member min] and [member max]. Translated to [code]hint_range(min, max)[/code] in shader code.
const HINT_RANGE = 1;

#desc A range hint for scalar value with step, which limits possible input values between [member min] and [member max], with a step (increment) of [member step]). Translated to [code]hint_range(min, max, step)[/code] in shader code.
const HINT_RANGE_STEP = 2;

#desc Represents the size of the [enum Hint] enum.
const HINT_MAX = 3;


#desc A default value to be assigned within the shader.
var default_value: float;

#desc Enables usage of the [member default_value].
var default_value_enabled: bool;

#desc A hint applied to the uniform, which controls the values it can take when set through the inspector.
var hint: int;

#desc Minimum value for range hints. Used if [member hint] is set to [constant HINT_RANGE] or [constant HINT_RANGE_STEP].
var max: float;

#desc Maximum value for range hints. Used if [member hint] is set to [constant HINT_RANGE] or [constant HINT_RANGE_STEP].
var min: float;

#desc Step (increment) value for the range hint with step. Used if [member hint] is set to [constant HINT_RANGE_STEP].
var step: float;




