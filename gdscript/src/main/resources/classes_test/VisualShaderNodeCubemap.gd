#brief A [Cubemap] sampling node to be used within the visual shader graph.
#desc Translated to [code]texture(cubemap, vec3)[/code] in the shader language. Returns a color vector and alpha channel as scalar.
class_name VisualShaderNodeCubemap

#desc Use the [Cubemap] set via [member cube_map]. If this is set to [member source], the [code]samplerCube[/code] port is ignored.
const SOURCE_TEXTURE = 0;

#desc Use the [Cubemap] sampler reference passed via the [code]samplerCube[/code] port. If this is set to [member source], the [member cube_map] texture is ignored.
const SOURCE_PORT = 1;

#desc Represents the size of the [enum Source] enum.
const SOURCE_MAX = 2;

#desc No hints are added to the uniform declaration.
const TYPE_DATA = 0;

#desc Adds [code]hint_albedo[/code] as hint to the uniform declaration for proper sRGB to linear conversion.
const TYPE_COLOR = 1;

#desc Adds [code]hint_normal[/code] as hint to the uniform declaration, which internally converts the texture for proper usage as normal map.
const TYPE_NORMAL_MAP = 2;

#desc Represents the size of the [enum TextureType] enum.
const TYPE_MAX = 3;


#desc The [Cubemap] texture to sample when using [constant SOURCE_TEXTURE] as [member source].
var cube_map: Cubemap;

#desc Defines which source should be used for the sampling. See [enum Source] for options.
var source: int;

#desc Defines the type of data provided by the source texture. See [enum TextureType] for options.
var texture_type: int;




