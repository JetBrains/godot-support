#brief 2D sprite node in 3D environment.
#desc A node that displays 2D texture information in a 3D environment. See also [Sprite3D] where many other properties are defined.
class_name SpriteBase3D

#desc If set, the texture's transparency and the opacity are used to make those parts of the sprite invisible.
const FLAG_TRANSPARENT = 0;

#desc If set, lights in the environment affect the sprite.
const FLAG_SHADED = 1;

#desc If set, texture can be seen from the back as well. If not, the texture is invisible when looking at it from behind.
const FLAG_DOUBLE_SIDED = 2;

#desc Disables the depth test, so this object is drawn on top of all others. However, objects drawn after it in the draw order may cover it.
const FLAG_DISABLE_DEPTH_TEST = 3;

#desc Label is scaled by depth so that it always appears the same size on screen.
const FLAG_FIXED_SIZE = 4;

#desc Represents the size of the [enum DrawFlags] enum.
const FLAG_MAX = 5;

#desc This mode performs standard alpha blending. It can display translucent areas, but transparency sorting issues may be visible when multiple transparent materials are overlapping.
const ALPHA_CUT_DISABLED = 0;

#desc This mode only allows fully transparent or fully opaque pixels. Harsh edges will be visible unless some form of screen-space antialiasing is enabled (see [member ProjectSettings.rendering/anti_aliasing/quality/screen_space_aa]). On the bright side, this mode doesn't suffer from transparency sorting issues when multiple transparent materials are overlapping. This mode is also known as [i]alpha testing[/i] or [i]1-bit transparency[/i].
const ALPHA_CUT_DISCARD = 1;

#desc This mode draws fully opaque pixels in the depth prepass. This is slower than [constant ALPHA_CUT_DISABLED] or [constant ALPHA_CUT_DISCARD], but it allows displaying translucent areas and smooth edges while using proper sorting.
const ALPHA_CUT_OPAQUE_PREPASS = 2;


#desc The alpha cutting mode to use for the sprite. See [enum AlphaCutMode] for possible values.
var alpha_cut: int;

#desc The direction in which the front of the texture faces.
var axis: int;

#desc The billboard mode to use for the sprite. See [enum BaseMaterial3D.BillboardMode] for possible values.
var billboard: int;

#desc If [code]true[/code], texture will be centered.
var centered: bool;

#desc If [code]true[/code], texture can be seen from the back as well, if [code]false[/code], it is invisible when looking at it from behind.
var double_sided: bool;

#desc If [code]true[/code], the label is rendered at the same size regardless of distance.
var fixed_size: bool;

#desc If [code]true[/code], texture is flipped horizontally.
var flip_h: bool;

#desc If [code]true[/code], texture is flipped vertically.
var flip_v: bool;

#desc A color value used to [i]multiply[/i] the texture's colors. Can be used for mood-coloring or to simulate the color of light.
#desc [b]Note:[/b] If a [member GeometryInstance3D.material_override] is defined on the [SpriteBase3D], the material override must be configured to take vertex colors into account for albedo. Otherwise, the color defined in [member modulate] will be ignored. For a [BaseMaterial3D], [member BaseMaterial3D.vertex_color_use_as_albedo] must be [code]true[/code]. For a [ShaderMaterial], [code]ALBEDO *= COLOR.rgb;[/code] must be inserted in the shader's [code]fragment()[/code] function.
var modulate: Color;

#desc If [code]true[/code], depth testing is disabled and the object will be drawn in render order.
var no_depth_test: bool;

#desc The texture's drawing offset.
var offset: Vector2;

#desc The size of one pixel's width on the sprite to scale it in 3D.
var pixel_size: float;

#desc Sets the render priority for the sprite. Higher priority objects will be sorted in front of lower priority objects.
#desc [b]Note:[/b] This only applies if [member alpha_cut] is set to [constant ALPHA_CUT_DISABLED] (default value).
#desc [b]Note:[/b] This only applies to sorting of transparent objects. This will not impact how transparent objects are sorted relative to opaque objects. This is because opaque objects are not sorted, while transparent objects are sorted from back to front (subject to priority).
var render_priority: int;

#desc If [code]true[/code], the [Light3D] in the [Environment] has effects on the sprite.
var shaded: bool;

#desc Filter flags for the texture. See [enum BaseMaterial3D.TextureFilter] for options.
var texture_filter: int;

#desc If [code]true[/code], the texture's transparency and the opacity are used to make those parts of the sprite invisible.
var transparent: bool;



#desc Returns a [TriangleMesh] with the sprite's vertices following its current configuration (such as its [member axis] and [member pixel_size]).
func generate_triangle_mesh() -> TriangleMesh:
	pass;

#desc Returns the value of the specified flag.
func get_draw_flag() -> bool:
	pass;

#desc Returns the rectangle representing this sprite.
func get_item_rect() -> Rect2:
	pass;

#desc If [code]true[/code], the specified flag will be enabled. See [enum SpriteBase3D.DrawFlags] for a list of flags.
func set_draw_flag(flag: int, enabled: bool) -> void:
	pass;


