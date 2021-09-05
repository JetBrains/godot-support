extends RefCounted
class_name RDTextureFormat


var array_layers: int setget set_array_layers, get_array_layers;
var depth: int setget set_depth, get_depth;
var format: int setget set_format, get_format;
var height: int setget set_height, get_height;
var mipmaps: int setget set_mipmaps, get_mipmaps;
var samples: int setget set_samples, get_samples;
var texture_type: int setget set_texture_type, get_texture_type;
var usage_bits: int setget set_usage_bits, get_usage_bits;
var width: int setget set_width, get_width;

func add_shareable_format(format: int) -> void:
    pass;

func remove_shareable_format(format: int) -> void:
    pass;

