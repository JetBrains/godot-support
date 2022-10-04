#brief Sprite node that contains multiple textures as frames to play for animation.
#desc [AnimatedSprite2D] is similar to the [Sprite2D] node, except it carries multiple textures as animation frames. Animations are created using a [SpriteFrames] resource, which allows you to import image files (or a folder containing said files) to provide the animation frames for the sprite. The [SpriteFrames] resource can be configured in the editor via the SpriteFrames bottom panel.
#desc After setting up [member frames], [method play] may be called. It's also possible to select an [member animation] and toggle [member playing], even within the editor.
#desc To pause the current animation, call [method stop] or set [member playing] to [code]false[/code]. Alternatively, setting [member speed_scale] to [code]0[/code] also preserves the current frame's elapsed time.
#desc [b]Note:[/b] You can associate a set of normal or specular maps by creating additional [SpriteFrames] resources with a [code]_normal[/code] or [code]_specular[/code] suffix. For example, having 3 [SpriteFrames] resources [code]run[/code], [code]run_normal[/code], and [code]run_specular[/code] will make it so the [code]run[/code] animation uses normal and specular maps.
class_name AnimatedSprite2D


#desc The current animation from the [member frames] resource. If this value changes, the [code]frame[/code] counter is reset.
var animation: StringName;

#desc If [code]true[/code], texture will be centered.
var centered: bool;

#desc If [code]true[/code], texture is flipped horizontally.
var flip_h: bool;

#desc If [code]true[/code], texture is flipped vertically.
var flip_v: bool;

#desc The displayed animation frame's index.
var frame: int;

#desc The [SpriteFrames] resource containing the animation(s). Allows you the option to load, edit, clear, make unique and save the states of the [SpriteFrames] resource.
var frames: SpriteFrames;

#desc The texture's drawing offset.
var offset: Vector2;

#desc If [code]true[/code], the [member animation] is currently playing. Setting this property to [code]false[/code] is the equivalent of calling [method stop].
var playing: bool;

#desc The animation speed is multiplied by this value. If set to a negative value, the animation is played in reverse. If set to [code]0[/code], the animation is paused, preserving the current frame's elapsed time.
var speed_scale: float;



#desc Plays the animation named [param anim]. If no [param anim] is provided, the current animation is played. If [param backwards] is [code]true[/code], the animation is played in reverse.
func play(anim: StringName, backwards: bool) -> void:
	pass;

#desc Stops the current [member animation] at the current [member frame].
#desc [b]Note:[/b] This method resets the current frame's elapsed time. If this behavior is undesired, consider setting [member speed_scale] to [code]0[/code], instead.
func stop() -> void:
	pass;


