extends AnimationNodeSync
#brief Plays an animation once in [AnimationNodeBlendTree].
#desc A resource to add to an [AnimationNodeBlendTree]. This node will execute a sub-animation and return once it finishes. Blend times for fading in and out can be customized, as well as filters.
class_name AnimationNodeOneShot

const MIX_MODE_BLEND = 0;

const MIX_MODE_ADD = 1;


#desc If [code]true[/code], the sub-animation will restart automatically after finishing.
var autorestart: bool;

#desc The delay after which the automatic restart is triggered, in seconds.
var autorestart_delay: float;

#desc If [member autorestart] is [code]true[/code], a random additional delay (in seconds) between 0 and this value will be added to [member autorestart_delay].
var autorestart_random_delay: float;

var fadein_time: float;

var fadeout_time: float;

var mix_mode: int;




