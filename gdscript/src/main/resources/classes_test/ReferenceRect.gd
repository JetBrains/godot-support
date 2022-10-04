#brief Reference frame for GUI.
#desc A rectangle box that displays only a [member border_color] border color around its rectangle. [ReferenceRect] has no fill [Color]. If you need to display a rectangle filled with a solid color, consider using [ColorRect] instead.
class_name ReferenceRect


#desc Sets the border [Color] of the [ReferenceRect].
var border_color: Color;

#desc Sets the border width of the [ReferenceRect]. The border grows both inwards and outwards with respect to the rectangle box.
var border_width: float;

#desc If [code]true[/code], the [ReferenceRect] will only be visible while in editor. Otherwise, [ReferenceRect] will be visible in the running project.
var editor_only: bool;




