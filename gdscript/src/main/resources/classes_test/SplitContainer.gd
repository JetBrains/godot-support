#brief Container for splitting and adjusting.
#desc Container for splitting two [Control]s vertically or horizontally, with a grabber that allows adjusting the split offset or ratio.
class_name SplitContainer

#desc The split dragger is visible when the cursor hovers it.
const DRAGGER_VISIBLE = 0;

#desc The split dragger is never visible.
const DRAGGER_HIDDEN = 1;

#desc The split dragger is never visible and its space collapsed.
const DRAGGER_HIDDEN_COLLAPSED = 2;


#desc If [code]true[/code], the area of the first [Control] will be collapsed and the dragger will be disabled.
var collapsed: bool;

#desc Determines the dragger's visibility. See [enum DraggerVisibility] for details.
var dragger_visibility: int;

#desc The initial offset of the splitting between the two [Control]s, with [code]0[/code] being at the end of the first [Control].
var split_offset: int;

#desc If [code]true[/code], the [SplitContainer] will arrange its children vertically, rather than horizontally.
#desc Can't be changed when using [HSplitContainer] and [VSplitContainer].
var vertical: bool;



#desc Clamps the [member split_offset] value to not go outside the currently possible minimal and maximum values.
func clamp_split_offset() -> void:
	pass;


