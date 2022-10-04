#brief Adds a compressor audio effect to an audio bus.
#brief Reduces sounds that exceed a certain threshold level, smooths out the dynamics and increases the overall volume.
#desc Dynamic range compressor reduces the level of the sound when the amplitude goes over a certain threshold in Decibels. One of the main uses of a compressor is to increase the dynamic range by clipping as little as possible (when sound goes over 0dB).
#desc Compressor has many uses in the mix:
#desc - In the Master bus to compress the whole output (although an [AudioEffectLimiter] is probably better).
#desc - In voice channels to ensure they sound as balanced as possible.
#desc - Sidechained. This can reduce the sound level sidechained with another audio bus for threshold detection. This technique is common in video game mixing to the level of music and SFX while voices are being heard.
#desc - Accentuates transients by using a wider attack, making effects sound more punchy.
class_name AudioEffectCompressor


#desc Compressor's reaction time when the signal exceeds the threshold, in microseconds. Value can range from 20 to 2000.
var attack_us: float;

#desc Gain applied to the output signal.
var gain: float;

#desc Balance between original signal and effect signal. Value can range from 0 (totally dry) to 1 (totally wet).
var mix: float;

#desc Amount of compression applied to the audio once it passes the threshold level. The higher the ratio, the more the loud parts of the audio will be compressed. Value can range from 1 to 48.
var ratio: float;

#desc Compressor's delay time to stop reducing the signal after the signal level falls below the threshold, in milliseconds. Value can range from 20 to 2000.
var release_ms: float;

#desc Reduce the sound level using another audio bus for threshold detection.
var sidechain: StringName;

#desc The level above which compression is applied to the audio. Value can range from -60 to 0.
var threshold: float;




