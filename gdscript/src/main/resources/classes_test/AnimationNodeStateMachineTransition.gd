extends Resource
class_name AnimationNodeStateMachineTransition

const SWITCH_MODE_IMMEDIATE = 0;
const SWITCH_MODE_SYNC = 1;
const SWITCH_MODE_AT_END = 2;

var advance_condition: String setget set_advance_condition, get_advance_condition;
var auto_advance: bool setget set_auto_advance, has_auto_advance;
var disabled: bool setget set_disabled, is_disabled;
var priority: int setget set_priority, get_priority;
var switch_mode: int setget set_switch_mode, get_switch_mode;
var xfade_time: float setget set_xfade_time, get_xfade_time;

