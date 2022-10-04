#brief General-purpose sprite node.
#desc A node that displays a 2D texture. The texture displayed can be a region from a larger atlas texture, or a frame from a sprite sheet animation.
class_name Sprite2D


#desc If [code]true[/code], texture is centered.
var centered: bool;

#desc If [code]true[/code], texture is flipped horizontally.
var flip_h: bool;

#desc If [code]true[/code], texture is flipped vertically.
var flip_v: bool;

#desc Current frame to display from sprite sheet. [member hframes] or [member vframes] must be greater than 1.
var frame: int;

#desc Coordinates of the frame to display from sprite sheet. This is as an alias for the [member frame] property. [member hframes] or [member vframes] must be greater than 1.
var frame_coords: Vector2i;

#desc The number of columns in the sprite sheet.
var hframes: int;

#desc The texture's drawing offset.
var offset: Vector2;

#desc If [code]true[/code], texture is cut from a larger atlas texture. See [member region_rect].
var region_enabled: bool;

#desc If [code]true[/code], the outermost pixels get blurred out. [member region_enabled] must be [code]true[/code].
var region_filter_clip_enabled: bool;

#desc The region of the atlas texture to display. [member region_enabled] must be [code]true[/code].
var region_rect: Rect2;

#desc [Texture2D] object to draw.
var texture: Texture2D;

#desc The number of rows in the sprite sheet.
var vframes: int;



#desc Returns a [Rect2] representing the Sprite2D's boundary in local coordinates. Can be used to detect if the Sprite2D was clicked. Example:
#desc [codeblocks]
#desc [gdscript]
#desc func _input(event):
#desc if event is InputEventMouseButton and event.pressed and event.button_index == MOUSE_BUTTON_LEFT:
#desc if get_rect().has_point(to_local(event.position)):
#desc print("A click!")
#desc [/gdscript]
#desc [csharp]
#desc public override void _Input(InputEvent inputEvent)
#desc {
#desc if (inputEvent is InputEventMouseButton inputEventMouse)
#desc {
#desc if (inputEventMouse.Pressed && inputEventMouse.ButtonIndex == (int)ButtonList.Left)
#desc {
#desc if (GetRect().HasPoint(ToLocal(inputEventMouse.Position)))
#desc {
#desc GD.Print("A click!");
#desc }
#desc }
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
func get_rect() -> Rect2:
	pass;

#desc Returns [code]true[/code], if the pixel at the given position is opaque and [code]false[/code] in other case.
#desc [b]Note:[/b] It also returns [code]false[/code], if the sprite's texture is [code]null[/code] or if the given position is invalid.
func is_pixel_opaque() -> bool:
	pass;


