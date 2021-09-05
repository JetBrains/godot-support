extends AnimationNode
class_name AnimationNodeOneShot

const MIX_MODE_BLEND = 0;
const MIX_MODE_ADD = 1;

var autorestart: bool setget set_autorestart, has_autorestart;
var autorestart_delay: float setget set_autorestart_delay, get_autorestart_delay;
var autorestart_random_delay: float setget set_autorestart_random_delay, get_autorestart_random_delay;
var fadein_time: float setget set_fadein_time, get_fadein_time;
var fadeout_time: float setget set_fadeout_time, get_fadeout_time;
var sync: bool setget set_use_sync, is_using_sync;

func get_mix_mode() -> int:
    pass;

func set_mix_mode(mode: int) -> void:
    pass;

