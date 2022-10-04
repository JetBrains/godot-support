#brief A base node for nodes which samples 3D textures in the visual shader graph.
#desc A virtual class, use the descendants instead.
class_name VisualShaderNodeSample3D

#desc Creates internal uniform and provides a way to assign it within node.
const SOURCE_TEXTURE = 0;

#desc Use the uniform texture from sampler port.
const SOURCE_PORT = 1;

#desc Represents the size of the [enum Source] enum.
const SOURCE_MAX = 2;


#desc An input source type.
var source: int;




