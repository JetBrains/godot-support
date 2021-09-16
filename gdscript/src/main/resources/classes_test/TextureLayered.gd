extends Texture
class_name TextureLayered
const LAYERED_TYPE_2D_ARRAY = 0;
const LAYERED_TYPE_CUBEMAP = 1;
const LAYERED_TYPE_CUBEMAP_ARRAY = 2;


func get_format() -> int:
    pass;
func get_height() -> int:
    pass;
func get_layer_data(layer: int) -> Image:
    pass;
func get_layered_type() -> int:
    pass;
func get_layers() -> int:
    pass;
func get_width() -> int:
    pass;
func has_mipmaps() -> bool:
    pass;
