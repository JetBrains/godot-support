extends Container
class_name SplitContainer

const DRAGGER_VISIBLE = 0;
const DRAGGER_HIDDEN = 1;
const DRAGGER_HIDDEN_COLLAPSED = 2;

var collapsed: bool setget set_collapsed, is_collapsed;
var dragger_visibility: int setget set_dragger_visibility, get_dragger_visibility;
var split_offset: int setget set_split_offset, get_split_offset;

func clamp_split_offset() -> void:
    pass;

