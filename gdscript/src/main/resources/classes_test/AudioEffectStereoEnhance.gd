#brief An audio effect that can be used to adjust the intensity of stereo panning.
#desc An audio effect that can be used to adjust the intensity of stereo panning.
class_name AudioEffectStereoEnhance


#desc Values greater than 1.0 increase intensity of any panning on audio passing through this effect, whereas values less than 1.0 will decrease the panning intensity. A value of 0.0 will downmix audio to mono.
var pan_pullout: float;

var surround: float;

var time_pullout_ms: float;




