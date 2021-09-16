extends Resource
class_name AnimationNodeStateMachineTransition
const SWITCH_MODE_IMMEDIATE = 0;
const SWITCH_MODE_SYNC = 1;
const SWITCH_MODE_AT_END = 2;

var advance_condition: StringName;
var auto_advance: bool;
var disabled: bool;
var priority: int;
var switch_mode: int;
var xfade_time: float;

