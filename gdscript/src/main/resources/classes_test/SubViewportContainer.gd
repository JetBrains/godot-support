#brief Control for holding [SubViewport]s.
#desc A [Container] node that holds a [SubViewport]. It uses the [SubViewport]'s size as minimum size, unless [member stretch] is enabled.
#desc [b]Note:[/b] Changing a SubViewportContainer's [member Control.scale] will cause its contents to appear distorted. To change its visual size without causing distortion, adjust the node's margins instead (if it's not already in a container).
#desc [b]Note:[/b] The SubViewportContainer forwards mouse-enter and mouse-exit notifications to its sub-viewports.
class_name SubViewportContainer


#desc If [code]true[/code], the sub-viewport will be automatically resized to the control's size.
var stretch: bool;

#desc Divides the sub-viewport's effective resolution by this value while preserving its scale. This can be used to speed up rendering.
#desc For example, a 1280×720 sub-viewport with [member stretch_shrink] set to [code]2[/code] will be rendered at 640×360 while occupying the same size in the container.
#desc [b]Note:[/b] [member stretch] must be [code]true[/code] for this property to work.
var stretch_shrink: int;




