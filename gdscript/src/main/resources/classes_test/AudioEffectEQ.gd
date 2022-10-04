extends AudioEffect
#brief Base class for audio equalizers. Gives you control over frequencies.
#brief Use it to create a custom equalizer if [AudioEffectEQ6], [AudioEffectEQ10] or [AudioEffectEQ21] don't fit your needs.
#desc AudioEffectEQ gives you control over frequencies. Use it to compensate for existing deficiencies in audio. AudioEffectEQs are useful on the Master bus to completely master a mix and give it more character. They are also useful when a game is run on a mobile device, to adjust the mix to that kind of speakers (it can be added but disabled when headphones are plugged).
class_name AudioEffectEQ




#desc Returns the number of bands of the equalizer.
func get_band_count() -> int:
	pass;

#desc Returns the band's gain at the specified index, in dB.
func get_band_gain_db(band_idx: int) -> float:
	pass;

#desc Sets band's gain at the specified index, in dB.
func set_band_gain_db(band_idx: int, volume_db: float) -> void:
	pass;


