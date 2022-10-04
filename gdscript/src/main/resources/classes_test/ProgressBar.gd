#brief General-purpose progress bar.
#desc General-purpose progress bar. Shows fill percentage from right to left.
class_name ProgressBar

#desc The progress bar fills from begin to end horizontally, according to the language direction. If [method Control.is_layout_rtl] returns [code]false[/code], it fills from left to right, and if it returns [code]true[/code], it fills from right to left.
const FILL_BEGIN_TO_END = 0;

#desc The progress bar fills from end to begin horizontally, according to the language direction. If [method Control.is_layout_rtl] returns [code]false[/code], it fills from right to left, and if it returns [code]true[/code], it fills from left to right.
const FILL_END_TO_BEGIN = 1;

#desc The progress fills from top to bottom.
const FILL_TOP_TO_BOTTOM = 2;

#desc The progress fills from bottom to top.
const FILL_BOTTOM_TO_TOP = 3;


#desc The fill direction. See [enum FillMode] for possible values.
var fill_mode: int;

#desc If [code]true[/code], the fill percentage is displayed on the bar.
var show_percentage: bool;




