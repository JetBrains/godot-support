extends Container
class_name AspectRatioContainer
const STRETCH_WIDTH_CONTROLS_HEIGHT = 0;
const STRETCH_HEIGHT_CONTROLS_WIDTH = 1;
const STRETCH_FIT = 2;
const STRETCH_COVER = 3;
const ALIGN_BEGIN = 0;
const ALIGN_CENTER = 1;
const ALIGN_END = 2;

var alignment_horizontal: int;
var alignment_vertical: int;
var ratio: float;
var stretch_mode: int;

