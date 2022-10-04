#brief Base class of anything 2D.
#desc Base class of anything 2D. Canvas items are laid out in a tree; children inherit and extend their parent's transform. [CanvasItem] is extended by [Control] for anything GUI-related, and by [Node2D] for anything related to the 2D engine.
#desc Any [CanvasItem] can draw. For this, [method queue_redraw] is called by the engine, then [constant NOTIFICATION_DRAW] will be received on idle time to request redraw. Because of this, canvas items don't need to be redrawn on every frame, improving the performance significantly. Several functions for drawing on the [CanvasItem] are provided (see [code]draw_*[/code] functions). However, they can only be used inside [method _draw], its corresponding [method Object._notification] or methods connected to the [signal draw] signal.
#desc Canvas items are drawn in tree order. By default, children are on top of their parents so a root [CanvasItem] will be drawn behind everything. This behavior can be changed on a per-item basis.
#desc A [CanvasItem] can also be hidden, which will also hide its children. It provides many ways to change parameters such as modulation (for itself and its children) and self modulation (only for itself), as well as its blend mode.
#desc Ultimately, a transform notification can be requested, which will notify the node that its global position changed in case the parent tree changed.
#desc [b]Note:[/b] Unless otherwise specified, all methods that have angle parameters must have angles specified as [i]radians[/i]. To convert degrees to radians, use [method @GlobalScope.deg_to_rad].
class_name CanvasItem

#desc The [CanvasItem]'s global transform has changed. This notification is only received if enabled by [method set_notify_transform].
const NOTIFICATION_TRANSFORM_CHANGED = 2000;

#desc The [CanvasItem]'s local transform has changed. This notification is only received if enabled by [method set_notify_local_transform].
const NOTIFICATION_LOCAL_TRANSFORM_CHANGED = 35;

#desc The [CanvasItem] is requested to draw (see [method _draw]).
const NOTIFICATION_DRAW = 30;

#desc The [CanvasItem]'s visibility has changed.
const NOTIFICATION_VISIBILITY_CHANGED = 31;

#desc The [CanvasItem] has entered the canvas.
const NOTIFICATION_ENTER_CANVAS = 32;

#desc The [CanvasItem] has exited the canvas.
const NOTIFICATION_EXIT_CANVAS = 33;

#desc The [CanvasItem] will inherit the filter from its parent.
const TEXTURE_FILTER_PARENT_NODE = 0;

#desc The texture filter reads from the nearest pixel only. The simplest and fastest method of filtering. Useful for pixel art.
const TEXTURE_FILTER_NEAREST = 1;

#desc The texture filter blends between the nearest four pixels. Use this for most cases where you want to avoid a pixelated style.
const TEXTURE_FILTER_LINEAR = 2;

#desc The texture filter reads from the nearest pixel in the nearest mipmap. This is the fastest way to read from textures with mipmaps.
const TEXTURE_FILTER_NEAREST_WITH_MIPMAPS = 3;

#desc The texture filter blends between the nearest 4 pixels and between the nearest 2 mipmaps. Use this for non-pixel art textures that may be viewed at a low scale (e.g. due to [Camera2D] zoom), as mipmaps are important to smooth out pixels that are smaller than on-screen pixels.
const TEXTURE_FILTER_LINEAR_WITH_MIPMAPS = 4;

#desc The texture filter reads from the nearest pixel, but selects a mipmap based on the angle between the surface and the camera view. This reduces artifacts on surfaces that are almost in line with the camera. The anisotropic filtering level can be changed by adjusting [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
#desc [b]Note:[/b] This texture filter is rarely useful in 2D projects. [constant TEXTURE_FILTER_NEAREST_WITH_MIPMAPS] is usually more appropriate.
const TEXTURE_FILTER_NEAREST_WITH_MIPMAPS_ANISOTROPIC = 5;

#desc The texture filter blends between the nearest 4 pixels and selects a mipmap based on the angle between the surface and the camera view. This reduces artifacts on surfaces that are almost in line with the camera. This is the slowest of the filtering options, but results in the highest quality texturing. The anisotropic filtering level can be changed by adjusting [member ProjectSettings.rendering/textures/default_filters/anisotropic_filtering_level].
#desc [b]Note:[/b] This texture filter is rarely useful in 2D projects. [constant TEXTURE_FILTER_LINEAR_WITH_MIPMAPS] is usually more appropriate.
const TEXTURE_FILTER_LINEAR_WITH_MIPMAPS_ANISOTROPIC = 6;

#desc Represents the size of the [enum TextureFilter] enum.
const TEXTURE_FILTER_MAX = 7;

#desc The [CanvasItem] will inherit the filter from its parent.
const TEXTURE_REPEAT_PARENT_NODE = 0;

#desc Texture will not repeat.
const TEXTURE_REPEAT_DISABLED = 1;

#desc Texture will repeat normally.
const TEXTURE_REPEAT_ENABLED = 2;

#desc Texture will repeat in a 2x2 tiled mode, where elements at even positions are mirrored.
const TEXTURE_REPEAT_MIRROR = 3;

#desc Represents the size of the [enum TextureRepeat] enum.
const TEXTURE_REPEAT_MAX = 4;


var clip_children: bool;

#desc The rendering layers in which this [CanvasItem] responds to [Light2D] nodes.
var light_mask: int;

#desc The material applied to textures on this [CanvasItem].
var material: Material;

#desc The color applied to textures on this [CanvasItem].
var modulate: Color;

#desc The color applied to textures on this [CanvasItem]. This is not inherited by children [CanvasItem]s.
var self_modulate: Color;

#desc If [code]true[/code], the object draws behind its parent.
var show_behind_parent: bool;

#desc The texture filtering mode to use on this [CanvasItem].
var texture_filter: int;

#desc The texture repeating mode to use on this [CanvasItem].
var texture_repeat: int;

#desc If [code]true[/code], this [CanvasItem] will [i]not[/i] inherit its transform from parent [CanvasItem]s. Its draw order will also be changed to make it draw on top of other [CanvasItem]s that do not have [member top_level] set to [code]true[/code]. The [CanvasItem] will effectively act as if it was placed as a child of a bare [Node].
var top_level: bool;

#desc If [code]true[/code], the parent [CanvasItem]'s [member material] property is used as this one's material.
var use_parent_material: bool;

#desc If [code]true[/code], this [CanvasItem] is drawn. The node is only visible if all of its antecedents are visible as well (in other words, [method is_visible_in_tree] must return [code]true[/code]).
#desc [b]Note:[/b] For controls that inherit [Popup], the correct way to make them visible is to call one of the multiple [code]popup*()[/code] functions instead.
var visible: bool;



#desc Called when [CanvasItem] has been requested to redraw (after [method queue_redraw] is called, either manually or by the engine).
#desc Corresponds to the [constant NOTIFICATION_DRAW] notification in [method Object._notification].
virtual func _draw() -> void:
	pass;

#desc Subsequent drawing commands will be ignored unless they fall within the specified animation slice. This is a faster way to implement animations that loop on background rather than redrawing constantly.
func draw_animation_slice(animation_length: float, slice_begin: float, slice_end: float, offset: float) -> void:
	pass;

#desc Draws a unfilled arc between the given angles. The larger the value of [param point_count], the smoother the curve. See also [method draw_circle].
func draw_arc(center: Vector2, radius: float, start_angle: float, end_angle: float, point_count: int, color: Color, width: float, antialiased: bool) -> void:
	pass;

#desc Draws a string first character using a custom font.
func draw_char(font: Font, pos: Vector2, char: String, font_size: int, modulate: Color) -> void:
	pass;

#desc Draws a string first character outline using a custom font.
func draw_char_outline(font: Font, pos: Vector2, char: String, font_size: int, size: int, modulate: Color) -> void:
	pass;

#desc Draws a colored, filled circle. See also [method draw_arc], [method draw_polyline] and [method draw_polygon].
func draw_circle(position: Vector2, radius: float, color: Color) -> void:
	pass;

#desc Draws a colored polygon of any number of points, convex or concave. Unlike [method draw_polygon], a single color must be specified for the whole polygon.
func draw_colored_polygon(points: PackedVector2Array, color: Color, uvs: PackedVector2Array, texture: Texture2D) -> void:
	pass;

#desc Draws a dashed line from a 2D point to another, with a given color and width. See also [method draw_multiline] and [method draw_polyline].
func draw_dashed_line(from: Vector2, to: Vector2, color: Color, width: float, dash: float) -> void:
	pass;

#desc After submitting all animations slices via [method draw_animation_slice], this function can be used to revert drawing to its default state (all subsequent drawing commands will be visible). If you don't care about this particular use case, usage of this function after submitting the slices is not required.
func draw_end_animation() -> void:
	pass;

#desc Draws a textured rectangle region of the font texture with LCD sub-pixel anti-aliasing at a given position, optionally modulated by a color.
#desc Texture is drawn using the following blend operation, blend mode of the [CanvasItemMaterial] is ignored:
#desc [codeblock]
#desc dst.r = texture.r * modulate.r * modulate.a + dst.r * (1.0 - texture.r * modulate.a);
#desc dst.g = texture.g * modulate.g * modulate.a + dst.g * (1.0 - texture.g * modulate.a);
#desc dst.b = texture.b * modulate.b * modulate.a + dst.b * (1.0 - texture.b * modulate.a);
#desc dst.a = modulate.a + dst.a * (1.0 - modulate.a);
#desc [/codeblock]
func draw_lcd_texture_rect_region(texture: Texture2D, rect: Rect2, src_rect: Rect2, modulate: Color) -> void:
	pass;

#desc Draws a line from a 2D point to another, with a given color and width. It can be optionally antialiased. See also [method draw_multiline] and [method draw_polyline].
func draw_line(from: Vector2, to: Vector2, color: Color, width: float, antialiased: bool) -> void:
	pass;

#desc Draws a [Mesh] in 2D, using the provided texture. See [MeshInstance2D] for related documentation.
func draw_mesh(mesh: Mesh, texture: Texture2D, transform: Transform2D, modulate: Color) -> void:
	pass;

#desc Draws a textured rectangle region of the multi-channel signed distance field texture at a given position, optionally modulated by a color. See [member FontFile.multichannel_signed_distance_field] for more information and caveats about MSDF font rendering.
#desc If [param outline] is positive, each alpha channel value of pixel in region is set to maximum value of true distance in the [param outline] radius.
#desc Value of the [param pixel_range] should the same that was used during distance field texture generation.
func draw_msdf_texture_rect_region(texture: Texture2D, rect: Rect2, src_rect: Rect2, modulate: Color, outline: float, pixel_range: float) -> void:
	pass;

#desc Draws multiple disconnected lines with a uniform [param color]. When drawing large amounts of lines, this is faster than using individual [method draw_line] calls. To draw interconnected lines, use [method draw_polyline] instead.
func draw_multiline(points: PackedVector2Array, color: Color, width: float) -> void:
	pass;

#desc Draws multiple disconnected lines with a uniform [param width] and segment-by-segment coloring. Colors assigned to line segments match by index between [param points] and [param colors]. When drawing large amounts of lines, this is faster than using individual [method draw_line] calls. To draw interconnected lines, use [method draw_polyline_colors] instead.
func draw_multiline_colors(points: PackedVector2Array, colors: PackedColorArray, width: float) -> void:
	pass;

#desc Breaks [param text] into lines and draws it using the specified [param font] at the [param pos] (top-left corner). The text will have its color multiplied by [param modulate]. If [param width] is greater than or equal to 0, the text will be clipped if it exceeds the specified width.
func draw_multiline_string(font: Font, pos: Vector2, text: String, alignment: int, width: float, font_size: int, max_lines: int, modulate: Color, brk_flags: int, jst_flags: int, direction: int, orientation: int) -> void:
	pass;

#desc Breaks [param text] to the lines and draws text outline using the specified [param font] at the [param pos] (top-left corner). The text will have its color multiplied by [param modulate]. If [param width] is greater than or equal to 0, the text will be clipped if it exceeds the specified width.
func draw_multiline_string_outline(font: Font, pos: Vector2, text: String, alignment: int, width: float, font_size: int, max_lines: int, size: int, modulate: Color, brk_flags: int, jst_flags: int, direction: int, orientation: int) -> void:
	pass;

#desc Draws a [MultiMesh] in 2D with the provided texture. See [MultiMeshInstance2D] for related documentation.
func draw_multimesh(multimesh: MultiMesh, texture: Texture2D) -> void:
	pass;

#desc Draws a solid polygon of any number of points, convex or concave. Unlike [method draw_colored_polygon], each point's color can be changed individually. See also [method draw_polyline] and [method draw_polyline_colors].
func draw_polygon(points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, texture: Texture2D) -> void:
	pass;

#desc Draws interconnected line segments with a uniform [param color] and [param width] and optional antialiasing. When drawing large amounts of lines, this is faster than using individual [method draw_line] calls. To draw disconnected lines, use [method draw_multiline] instead. See also [method draw_polygon].
func draw_polyline(points: PackedVector2Array, color: Color, width: float, antialiased: bool) -> void:
	pass;

#desc Draws interconnected line segments with a uniform [param width] and segment-by-segment coloring, and optional antialiasing. Colors assigned to line segments match by index between [param points] and [param colors]. When drawing large amounts of lines, this is faster than using individual [method draw_line] calls. To draw disconnected lines, use [method draw_multiline_colors] instead. See also [method draw_polygon].
func draw_polyline_colors(points: PackedVector2Array, colors: PackedColorArray, width: float, antialiased: bool) -> void:
	pass;

#desc Draws a custom primitive. 1 point for a point, 2 points for a line, 3 points for a triangle, and 4 points for a quad. If 0 points or more than 4 points are specified, nothing will be drawn and an error message will be printed. See also [method draw_line], [method draw_polyline], [method draw_polygon], and [method draw_rect].
func draw_primitive(points: PackedVector2Array, colors: PackedColorArray, uvs: PackedVector2Array, texture: Texture2D, width: float) -> void:
	pass;

#desc Draws a rectangle. If [param filled] is [code]true[/code], the rectangle will be filled with the [param color] specified. If [param filled] is [code]false[/code], the rectangle will be drawn as a stroke with the [param color] and [param width] specified.
#desc [b]Note:[/b] [param width] is only effective if [param filled] is [code]false[/code].
func draw_rect(rect: Rect2, color: Color, filled: bool, width: float) -> void:
	pass;

#desc Sets a custom transform for drawing via components. Anything drawn afterwards will be transformed by this.
func draw_set_transform(position: Vector2, rotation: float, scale: Vector2) -> void:
	pass;

#desc Sets a custom transform for drawing via matrix. Anything drawn afterwards will be transformed by this.
func draw_set_transform_matrix() -> void:
	pass;

#desc Draws [param text] using the specified [param font] at the [param pos] (bottom-left corner using the baseline of the font). The text will have its color multiplied by [param modulate]. If [param width] is greater than or equal to 0, the text will be clipped if it exceeds the specified width.
#desc [b]Example using the default project font:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc # If using this method in a script that redraws constantly, move the
#desc # `default_font` declaration to a member variable assigned in `_ready()`
#desc # so the Control is only created once.
#desc var default_font = ThemeDB.fallback_font
#desc var default_font_size = ThemeDB.fallback_font_size
#desc draw_string(default_font, Vector2(64, 64), "Hello world", HORIZONTAL_ALIGNMENT_LEFT, -1, default_font_size)
#desc [/gdscript]
#desc [csharp]
#desc // If using this method in a script that redraws constantly, move the
#desc // `default_font` declaration to a member variable assigned in `_Ready()`
#desc // so the Control is only created once.
#desc Font defaultFont = ThemeDB.FallbackFont;
#desc int defaultFontSize = ThemeDB.FallbackFontSize;
#desc DrawString(defaultFont, new Vector2(64, 64), "Hello world", HORIZONTAL_ALIGNMENT_LEFT, -1, defaultFontSize);
#desc [/csharp]
#desc [/codeblocks]
#desc See also [method Font.draw_string].
func draw_string(font: Font, pos: Vector2, text: String, alignment: int, width: float, font_size: int, modulate: Color, jst_flags: int, direction: int, orientation: int) -> void:
	pass;

#desc Draws [param text] outline using the specified [param font] at the [param pos] (bottom-left corner using the baseline of the font). The text will have its color multiplied by [param modulate]. If [param width] is greater than or equal to 0, the text will be clipped if it exceeds the specified width.
func draw_string_outline(font: Font, pos: Vector2, text: String, alignment: int, width: float, font_size: int, size: int, modulate: Color, jst_flags: int, direction: int, orientation: int) -> void:
	pass;

#desc Draws a styled rectangle.
func draw_style_box(style_box: StyleBox, rect: Rect2) -> void:
	pass;

#desc Draws a texture at a given position.
func draw_texture(texture: Texture2D, position: Vector2, modulate: Color) -> void:
	pass;

#desc Draws a textured rectangle at a given position, optionally modulated by a color. If [param transpose] is [code]true[/code], the texture will have its X and Y coordinates swapped.
func draw_texture_rect(texture: Texture2D, rect: Rect2, tile: bool, modulate: Color, transpose: bool) -> void:
	pass;

#desc Draws a textured rectangle region at a given position, optionally modulated by a color. If [param transpose] is [code]true[/code], the texture will have its X and Y coordinates swapped.
func draw_texture_rect_region(texture: Texture2D, rect: Rect2, src_rect: Rect2, modulate: Color, transpose: bool, clip_uv: bool) -> void:
	pass;

#desc Forces the transform to update. Transform changes in physics are not instant for performance reasons. Transforms are accumulated and then set. Use this if you need an up-to-date transform when doing physics operations.
func force_update_transform() -> void:
	pass;

#desc Returns the [RID] of the [World2D] canvas where this item is in.
func get_canvas() -> RID:
	pass;

#desc Returns the canvas item RID used by [RenderingServer] for this item.
func get_canvas_item() -> RID:
	pass;

#desc Returns the transform matrix of this item's canvas.
func get_canvas_transform() -> Transform2D:
	pass;

#desc Returns the mouse's position in the [CanvasLayer] that this [CanvasItem] is in using the coordinate system of the [CanvasLayer].
func get_global_mouse_position() -> Vector2:
	pass;

#desc Returns the global transform matrix of this item.
func get_global_transform() -> Transform2D:
	pass;

#desc Returns the global transform matrix of this item in relation to the canvas.
func get_global_transform_with_canvas() -> Transform2D:
	pass;

#desc Returns the mouse's position in this [CanvasItem] using the local coordinate system of this [CanvasItem].
func get_local_mouse_position() -> Vector2:
	pass;

#desc Returns the transform of this [CanvasItem] in global screen coordinates (i.e. taking window position into account). Mostly useful for editor plugins.
#desc Equals to [method get_global_transform] if the window is embedded (see [member Viewport.gui_embed_subwindows]).
func get_screen_transform() -> Transform2D:
	pass;

#desc Returns the transform matrix of this item.
func get_transform() -> Transform2D:
	pass;

#desc Returns the viewport's boundaries as a [Rect2].
func get_viewport_rect() -> Rect2:
	pass;

#desc Returns this item's transform in relation to the viewport.
func get_viewport_transform() -> Transform2D:
	pass;

#desc Returns the [World2D] where this item is in.
func get_world_2d() -> World2D:
	pass;

#desc Hide the [CanvasItem] if it's currently visible. This is equivalent to setting [member visible] to [code]false[/code].
func hide() -> void:
	pass;

#desc Returns [code]true[/code] if local transform notifications are communicated to children.
func is_local_transform_notification_enabled() -> bool:
	pass;

#desc Returns [code]true[/code] if global transform notifications are communicated to children.
func is_transform_notification_enabled() -> bool:
	pass;

#desc Returns [code]true[/code] if the node is present in the [SceneTree], its [member visible] property is [code]true[/code] and all its antecedents are also visible. If any antecedent is hidden, this node will not be visible in the scene tree, and is consequently not drawn (see [method _draw]).
func is_visible_in_tree() -> bool:
	pass;

#desc Assigns [param screen_point] as this node's new local transform.
func make_canvas_position_local() -> Vector2:
	pass;

#desc Transformations issued by [param event]'s inputs are applied in local space instead of global space.
func make_input_local() -> InputEvent:
	pass;

#desc Moves this node to display on top of its siblings. This has more use in [Control], as [Node2D] can be ordered with [member Node2D.z_index].
#desc Internally, the node is moved to the bottom of parent's children list. The method has no effect on nodes without a parent.
func move_to_front() -> void:
	pass;

#desc Queues the [CanvasItem] to redraw. During idle time, if [CanvasItem] is visible, [constant NOTIFICATION_DRAW] is sent and [method _draw] is called. This only occurs [b]once[/b] per frame, even if this method has been called multiple times.
func queue_redraw() -> void:
	pass;

#desc If [param enable] is [code]true[/code], this node will receive [constant NOTIFICATION_LOCAL_TRANSFORM_CHANGED] when its local transform changes.
func set_notify_local_transform() -> void:
	pass;

#desc If [param enable] is [code]true[/code], this node will receive [constant NOTIFICATION_TRANSFORM_CHANGED] when its global transform changes.
func set_notify_transform() -> void:
	pass;

#desc Show the [CanvasItem] if it's currently hidden. This is equivalent to setting [member visible] to [code]true[/code]. For controls that inherit [Popup], the correct way to make them visible is to call one of the multiple [code]popup*()[/code] functions instead.
func show() -> void:
	pass;


