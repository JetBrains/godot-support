extends Container
class_name SplitContainer
const DRAGGER_VISIBLE = 0;
const DRAGGER_HIDDEN = 1;
const DRAGGER_HIDDEN_COLLAPSED = 2;

var collapsed: bool;
var dragger_visibility: int;
var split_offset: int;

func clamp_split_offset() -> void:
    pass;
