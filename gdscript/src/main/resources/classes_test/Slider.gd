#brief Base class for GUI sliders.
#desc Base class for GUI sliders.
#desc [b]Note:[/b] The [signal Range.changed] and [signal Range.value_changed] signals are part of the [Range] class which this class inherits from.
class_name Slider


#desc If [code]true[/code], the slider can be interacted with. If [code]false[/code], the value can be changed only by code.
var editable: bool;

#desc If [code]true[/code], the value can be changed using the mouse wheel.
var scrollable: bool;

#desc Number of ticks displayed on the slider, including border ticks. Ticks are uniformly-distributed value markers.
var tick_count: int;

#desc If [code]true[/code], the slider will display ticks for minimum and maximum values.
var ticks_on_borders: bool;




