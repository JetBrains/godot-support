#brief 2D sprite node in a 3D world.
#desc A node that displays a 2D texture in a 3D environment. The texture displayed can be a region from a larger atlas texture, or a frame from a sprite sheet animation. See also [SpriteBase3D] where properties such as the billboard mode are defined.
class_name Sprite3D


#desc Current frame to display from sprite sheet. [member hframes] or [member vframes] must be greater than 1.
var frame: int;

#desc Coordinates of the frame to display from sprite sheet. This is as an alias for the [member frame] property. [member hframes] or [member vframes] must be greater than 1.
var frame_coords: Vector2i;

#desc The number of columns in the sprite sheet.
var hframes: int;

var region_enabled: bool;

#desc The region of the atlas texture to display. [member region_enabled] must be [code]true[/code].
var region_rect: Rect2;

#desc [Texture2D] object to draw. If [member GeometryInstance3D.material_override] is used, this will be overridden. The size information is still used.
var texture: Texture2D;

#desc The number of rows in the sprite sheet.
var vframes: int;




