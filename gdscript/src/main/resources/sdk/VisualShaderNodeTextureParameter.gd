extends VisualShaderNodeParameter
#brief Performs a uniform texture lookup within the visual shader graph.
#desc Performs a lookup operation on the texture provided as a uniform for the shader.
class_name VisualShaderNodeTextureParameter

#desc No hints are added to the uniform declaration.
const TYPE_DATA = 0;

#desc Adds [code]source_color[/code] as hint to the uniform declaration for proper sRGB to linear conversion.
const TYPE_COLOR = 1;

#desc Adds [code]hint_normal[/code] as hint to the uniform declaration, which internally converts the texture for proper usage as normal map.
const TYPE_NORMAL_MAP = 2;

#desc Adds [code]hint_anisotropy[/code] as hint to the uniform declaration to use for a flowmap.
const TYPE_ANISOTROPY = 3;

#desc Represents the size of the [enum TextureType] enum.
const TYPE_MAX = 4;

#desc Defaults to fully opaque white color.
const COLOR_DEFAULT_WHITE = 0;

#desc Defaults to fully opaque black color.
const COLOR_DEFAULT_BLACK = 1;

#desc Defaults to fully transparent black color.
const COLOR_DEFAULT_TRANSPARENT = 2;

#desc Represents the size of the [enum ColorDefault] enum.
const COLOR_DEFAULT_MAX = 3;

const FILTER_DEFAULT = 0;

const FILTER_NEAREST = 1;

const FILTER_LINEAR = 2;

const FILTER_NEAREST_MIPMAP = 3;

const FILTER_LINEAR_MIPMAP = 4;

const FILTER_NEAREST_MIPMAP_ANISOTROPIC = 5;

const FILTER_LINEAR_MIPMAP_ANISOTROPIC = 6;

#desc Represents the size of the [enum TextureFilter] enum.
const FILTER_MAX = 7;

const REPEAT_DEFAULT = 0;

const REPEAT_ENABLED = 1;

const REPEAT_DISABLED = 2;

#desc Represents the size of the [enum TextureRepeat] enum.
const REPEAT_MAX = 3;


#desc Sets the default color if no texture is assigned to the uniform.
var color_default: int;

#desc Sets the texture filtering mode. See [enum TextureFilter] for options.
var texture_filter: int;

#desc Sets the texture repeating mode. See [enum TextureRepeat] for options.
var texture_repeat: int;

#desc Defines the type of data provided by the source texture. See [enum TextureType] for options.
var texture_type: int;




