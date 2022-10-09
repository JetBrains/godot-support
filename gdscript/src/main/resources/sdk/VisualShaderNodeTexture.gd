extends VisualShaderNode
#brief Performs a 2D texture lookup within the visual shader graph.
#desc Performs a lookup operation on the provided texture, with support for multiple texture sources to choose from.
class_name VisualShaderNodeTexture

#desc Use the texture given as an argument for this function.
const SOURCE_TEXTURE = 0;

#desc Use the current viewport's texture as the source.
const SOURCE_SCREEN = 1;

#desc Use the texture from this shader's texture built-in (e.g. a texture of a [Sprite2D]).
const SOURCE_2D_TEXTURE = 2;

#desc Use the texture from this shader's normal map built-in.
const SOURCE_2D_NORMAL = 3;

#desc Use the depth texture available for this shader.
const SOURCE_DEPTH = 4;

#desc Use the texture provided in the input port for this function.
const SOURCE_PORT = 5;

#desc Represents the size of the [enum Source] enum.
const SOURCE_MAX = 6;

#desc No hints are added to the uniform declaration.
const TYPE_DATA = 0;

#desc Adds [code]hint_albedo[/code] as hint to the uniform declaration for proper sRGB to linear conversion.
const TYPE_COLOR = 1;

#desc Adds [code]hint_normal[/code] as hint to the uniform declaration, which internally converts the texture for proper usage as normal map.
const TYPE_NORMAL_MAP = 2;

#desc Represents the size of the [enum TextureType] enum.
const TYPE_MAX = 3;


#desc Determines the source for the lookup. See [enum Source] for options.
var source: int;

#desc The source texture, if needed for the selected [member source].
var texture: Texture2D;

#desc Specifies the type of the texture if [member source] is set to [constant SOURCE_TEXTURE]. See [enum TextureType] for options.
var texture_type: int;




