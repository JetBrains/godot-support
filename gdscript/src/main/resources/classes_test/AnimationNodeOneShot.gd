extends AnimationNode
class_name AnimationNodeOneShot
const MIX_MODE_BLEND = 0;
const MIX_MODE_ADD = 1;

var autorestart: bool;
var autorestart_delay: float;
var autorestart_random_delay: float;
var fadein_time: float;
var fadeout_time: float;
var sync: bool;

func get_mix_mode() -> int:
    pass;
func set_mix_mode(mode: int) -> void:
    pass;
