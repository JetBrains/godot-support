#brief A parallax scrolling layer to be used with [ParallaxBackground].
#desc A ParallaxLayer must be the child of a [ParallaxBackground] node. Each ParallaxLayer can be set to move at different speeds relative to the camera movement or the [member ParallaxBackground.scroll_offset] value.
#desc This node's children will be affected by its scroll offset.
#desc [b]Note:[/b] Any changes to this node's position and scale made after it enters the scene will be ignored.
class_name ParallaxLayer


#desc The ParallaxLayer's [Texture2D] mirroring. Useful for creating an infinite scrolling background. If an axis is set to [code]0[/code], the [Texture2D] will not be mirrored.
#desc If the length of the viewport axis is bigger than twice the mirrored axis size, it will not repeat infinitely, as the parallax layer only draws 2 instances of the texture at any one time.
var motion_mirroring: Vector2;

#desc The ParallaxLayer's offset relative to the parent ParallaxBackground's [member ParallaxBackground.scroll_offset].
var motion_offset: Vector2;

#desc Multiplies the ParallaxLayer's motion. If an axis is set to [code]0[/code], it will not scroll.
var motion_scale: Vector2;




