extends Viewport
#brief Creates a sub-view into the screen.
#desc [SubViewport] is a [Viewport] that isn't a [Window], i.e. it doesn't draw anything by itself. To display something, [SubViewport]'s [member size] must be non-zero and it should be either put inside a [SubViewportContainer] or assigned to a [ViewportTexture].
class_name SubViewport

#desc Always clear the render target before drawing.
const CLEAR_MODE_ALWAYS = 0;

#desc Never clear the render target.
const CLEAR_MODE_NEVER = 1;

#desc Clear the render target on the next frame, then switch to [constant CLEAR_MODE_NEVER].
const CLEAR_MODE_ONCE = 2;

#desc Do not update the render target.
const UPDATE_DISABLED = 0;

#desc Update the render target once, then switch to [constant UPDATE_DISABLED].
const UPDATE_ONCE = 1;

#desc Update the render target only when it is visible. This is the default value.
const UPDATE_WHEN_VISIBLE = 2;

#desc Update the render target only when its parent is visible.
const UPDATE_WHEN_PARENT_VISIBLE = 3;

#desc Always update the render target.
const UPDATE_ALWAYS = 4;


#desc The clear mode when the sub-viewport is used as a render target.
#desc [b]Note:[/b] This property is intended for 2D usage.
var render_target_clear_mode: int;

#desc The update mode when the sub-viewport is used as a render target.
var render_target_update_mode: int;

#desc The width and height of the sub-viewport. Must be set to a value greater than or equal to 2 pixels on both dimensions. Otherwise, nothing will be displayed.
var size: Vector2i;

#desc The 2D size override of the sub-viewport. If either the width or height is [code]0[/code], the override is disabled.
var size_2d_override: Vector2i;

#desc If [code]true[/code], the 2D size override affects stretch as well.
var size_2d_override_stretch: bool;




