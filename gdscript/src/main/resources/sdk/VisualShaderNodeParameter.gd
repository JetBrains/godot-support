extends VisualShaderNode
#brief A base type for the parameters within the visual shader graph.
#desc A parameter represents a variable in the shader which is set externally, i.e. from the [ShaderMaterial]. Parameters are exposed as properties in the [ShaderMaterial] and can be assigned from the inspector or from a script.
class_name VisualShaderNodeParameter

const QUAL_NONE = 0;

const QUAL_GLOBAL = 1;

const QUAL_INSTANCE = 2;

#desc Represents the size of the [enum Qualifier] enum.
const QUAL_MAX = 3;


#desc Name of the parameter, by which it can be accessed through the [ShaderMaterial] properties.
var parameter_name: String;

var qualifier: int;




