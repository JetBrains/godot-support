extends Container
class_name AspectRatioContainer

const STRETCH_WIDTH_CONTROLS_HEIGHT = 0;
const STRETCH_HEIGHT_CONTROLS_WIDTH = 1;
const STRETCH_FIT = 2;
const STRETCH_COVER = 3;
const ALIGN_BEGIN = 0;
const ALIGN_CENTER = 1;
const ALIGN_END = 2;

var alignment_horizontal: int setget set_alignment_horizontal, get_alignment_horizontal;
var alignment_vertical: int setget set_alignment_vertical, get_alignment_vertical;
var ratio: float setget set_ratio, get_ratio;
var stretch_mode: int setget set_stretch_mode, get_stretch_mode;

