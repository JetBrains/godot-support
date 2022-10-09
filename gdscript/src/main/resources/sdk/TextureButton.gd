extends BaseButton
#brief Texture-based button. Supports Pressed, Hover, Disabled and Focused states.
#desc [TextureButton] has the same functionality as [Button], except it uses sprites instead of Godot's [Theme] resource. It is faster to create, but it doesn't support localization like more complex [Control]s.
#desc The "normal" state must contain a texture ([member texture_normal]); other textures are optional.
#desc See also [BaseButton] which contains common properties and methods associated with this node.
class_name TextureButton

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

#desc Scale the texture to fit the node's bounding rectangle, center it, and maintain its aspect ratio.
const STRETCH_KEEP_ASPECT_CENTERED = 5;

#desc Scale the texture so that the shorter side fits the bounding rectangle. The other side clips to the node's limits.
const STRETCH_KEEP_ASPECT_COVERED = 6;


#desc If [code]true[/code], texture is flipped horizontally.
var flip_h: bool;

#desc If [code]true[/code], texture is flipped vertically.
var flip_v: bool;

#desc If [code]true[/code], the size of the texture won't be considered for minimum size calculation, so the [TextureButton] can be shrunk down past the texture size.
var ignore_texture_size: bool;

#desc Controls the texture's behavior when you resize the node's bounding rectangle. See the [enum StretchMode] constants for available options.
var stretch_mode: int;

#desc Pure black and white [BitMap] image to use for click detection. On the mask, white pixels represent the button's clickable area. Use it to create buttons with curved shapes.
var texture_click_mask: BitMap;

#desc Texture to display when the node is disabled. See [member BaseButton.disabled].
var texture_disabled: Texture2D;

#desc Texture to display when the node has mouse or keyboard focus. [member texture_focused] is displayed [i]over[/i] the base texture, so a partially transparent texture should be used to ensure the base texture remains visible. A texture that represents an outline or an underline works well for this purpose. To disable the focus visual effect, assign a fully transparent texture of any size. Note that disabling the focus visual effect will harm keyboard/controller navigation usability, so this is not recommended for accessibility reasons.
var texture_focused: Texture2D;

#desc Texture to display when the mouse hovers the node.
var texture_hover: Texture2D;

#desc Texture to display by default, when the node is [b]not[/b] in the disabled, focused, hover or pressed state.
var texture_normal: Texture2D;

#desc Texture to display on mouse down over the node, if the node has keyboard focus and the player presses the Enter key or if the player presses the [member BaseButton.shortcut] key.
var texture_pressed: Texture2D;




