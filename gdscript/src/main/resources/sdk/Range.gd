extends Control
#brief Abstract base class for range-based controls.
#desc Range is a base class for [Control] nodes that change a floating-point [member value] between a [member min_value] and [member max_value], using a configured [member step] and [member page] size. See e.g. [ScrollBar] and [Slider] for examples of higher level nodes using Range.
class_name Range


#desc If [code]true[/code], [member value] may be greater than [member max_value].
var allow_greater: bool;

#desc If [code]true[/code], [member value] may be less than [member min_value].
var allow_lesser: bool;

#desc If [code]true[/code], and [code]min_value[/code] is greater than 0, [code]value[/code] will be represented exponentially rather than linearly.
var exp_edit: bool;

#desc Maximum value. Range is clamped if [code]value[/code] is greater than [code]max_value[/code].
var max_value: float;

#desc Minimum value. Range is clamped if [code]value[/code] is less than [code]min_value[/code].
var min_value: float;

#desc Page size. Used mainly for [ScrollBar]. ScrollBar's length is its size multiplied by [code]page[/code] over the difference between [code]min_value[/code] and [code]max_value[/code].
var page: float;

#desc The value mapped between 0 and 1.
var ratio: float;

#desc If [code]true[/code], [code]value[/code] will always be rounded to the nearest integer.
var rounded: bool;

#desc If greater than 0, [code]value[/code] will always be rounded to a multiple of [code]step[/code]. If [code]rounded[/code] is also [code]true[/code], [code]value[/code] will first be rounded to a multiple of [code]step[/code] then rounded to the nearest integer.
var step: float;

#desc Range's current value.
var value: float;



#desc Called when the [Range]'s value is changed (following the same conditions as [signal value_changed]).
func _value_changed(new_value: float) -> void:
	pass;

#desc Binds two [Range]s together along with any ranges previously grouped with either of them. When any of range's member variables change, it will share the new value with all other ranges in its group.
func share(with: Node) -> void:
	pass;

#desc Stops the [Range] from sharing its member variables with any other.
func unshare() -> void:
	pass;


