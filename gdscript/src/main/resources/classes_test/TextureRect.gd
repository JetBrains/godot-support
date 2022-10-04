#brief Control for drawing textures.
#desc Used to draw icons and sprites in a user interface. The texture's placement can be controlled with the [member stretch_mode] property. It can scale, tile, or stay centered inside its bounding rectangle.
class_name TextureRect

#desc Scale to fit the node's bounding rectangle.
const STRETCH_SCALE = 0;

#desc Tile inside the node's bounding rectangle.
const STRETCH_TILE = 1;

#desc The texture keeps its original size and stays in the bounding rectangle's top-left corner.
const STRETCH_KEEP = 2;

#desc The texture keeps its original size and stays centered in the node's bounding rectangle.
const STRETCH_KEEP_CENTERED = 3;

#desc Scale the texture to fit the node's bounding rectangle, but maintain the texture's aspect ratio.
const STRETCH_KEEP_ASPECT = 4;

#desc Scale the texture to fit the node's bounding rectangle, center it and maintain its aspect ratio.
const STRETCH_KEEP_ASPECT_CENTERED = 5;

#desc Scale the texture so that the shorter side fits the bounding rectangle. The other side clips to the node's limits.
const STRETCH_KEEP_ASPECT_COVERED = 6;


#desc If [code]true[/code], texture is flipped horizontally.
var flip_h: bool;

#desc If [code]true[/code], texture is flipped vertically.
var flip_v: bool;

#desc If [code]true[/code], the size of the texture won't be considered for minimum size calculation, so the [TextureRect] can be shrunk down past the texture size. Useful for preventing [TextureRect]s from breaking GUI layout regardless of their texture size.
var ignore_texture_size: bool;

var mouse_filter: int;

#desc Controls the texture's behavior when resizing the node's bounding rectangle. See [enum StretchMode].
var stretch_mode: int;

#desc The node's [Texture2D] resource.
var texture: Texture2D;




