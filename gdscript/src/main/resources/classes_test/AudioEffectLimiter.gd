#brief Adds a soft-clip limiter audio effect to an Audio bus.
#desc A limiter is similar to a compressor, but it's less flexible and designed to disallow sound going over a given dB threshold. Adding one in the Master bus is always recommended to reduce the effects of clipping.
#desc Soft clipping starts to reduce the peaks a little below the threshold level and progressively increases its effect as the input level increases such that the threshold is never exceeded.
class_name AudioEffectLimiter


#desc The waveform's maximum allowed value, in decibels. Value can range from -20 to -0.1.
var ceiling_db: float;

#desc Applies a gain to the limited waves, in decibels. Value can range from 0 to 6.
var soft_clip_db: float;

var soft_clip_ratio: float;

#desc Threshold from which the limiter begins to be active, in decibels. Value can range from -30 to 0.
var threshold_db: float;




