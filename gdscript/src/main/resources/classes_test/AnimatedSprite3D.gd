extends SpriteBase3D
#brief 2D sprite node in 3D world, that can use multiple 2D textures for animation.
#desc [AnimatedSprite3D] is similar to the [Sprite3D] node, except it carries multiple textures as animation [member frames]. Animations are created using a [SpriteFrames] resource, which allows you to import image files (or a folder containing said files) to provide the animation frames for the sprite. The [SpriteFrames] resource can be configured in the editor via the SpriteFrames bottom panel.
#desc After setting up [member frames], [method play] may be called. It's also possible to select an [member animation] and toggle [member playing], even within the editor.
#desc To pause the current animation, call [method stop] or set [member playing] to [code]false[/code]. Alternatively, setting [member speed_scale] to [code]0[/code] also preserves the current frame's elapsed time.
class_name AnimatedSprite3D


#desc The current animation from the [code]frames[/code] resource. If this value changes, the [code]frame[/code] counter is reset.
var animation: StringName;

#desc The displayed animation frame's index.
var frame: int;

#desc The [SpriteFrames] resource containing the animation(s).
var frames: SpriteFrames;

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


