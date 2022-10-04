extends StyleBox
#brief [StyleBox] that displays a single line.
#desc [StyleBox] that displays a single line of a given color and thickness. It can be used to draw things like separators.
class_name StyleBoxLine


#desc The line's color.
var color: Color;

#desc The number of pixels the line will extend before the [StyleBoxLine]'s bounds. If set to a negative value, the line will begin inside the [StyleBoxLine]'s bounds.
var grow_begin: float;

#desc The number of pixels the line will extend past the [StyleBoxLine]'s bounds. If set to a negative value, the line will end inside the [StyleBoxLine]'s bounds.
var grow_end: float;

#desc The line's thickness in pixels.
var thickness: int;

#desc If [code]true[/code], the line will be vertical. If [code]false[/code], the line will be horizontal.
var vertical: bool;




