#brief Contains functions to modify texture coordinates ([code]uv[/code]) to be used within the visual shader graph.
class_name VisualShaderNodeUVFunc

#desc Translates [code]uv[/code] by using [code]scale[/code] and [code]offset[/code] values using the following formula: [code]uv = uv + offset * scale[/code]. [code]uv[/code] port is connected to [code]UV[/code] built-in by default.
const FUNC_PANNING = 0;

#desc Scales [code]uv[/code] by using [code]scale[/code] and [code]pivot[/code] values using the following formula: [code]uv = (uv - pivot) * scale + pivot[/code]. [code]uv[/code] port is connected to [code]UV[/code] built-in by default.
const FUNC_SCALING = 1;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 2;


#desc A function to be applied to the texture coordinates. See [enum Function] for options.
var function: int;




