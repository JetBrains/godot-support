#brief Audio stream that generates sounds procedurally.
#desc This audio stream does not play back sounds, but expects a script to generate audio data for it instead. See also [AudioStreamGeneratorPlayback].
#desc See also [AudioEffectSpectrumAnalyzer] for performing real-time audio spectrum analysis.
#desc [b]Note:[/b] Due to performance constraints, this class is best used from C# or from a compiled language via GDExtension. If you still want to use this class from GDScript, consider using a lower [member mix_rate] such as 11,025 Hz or 22,050 Hz.
class_name AudioStreamGenerator


#desc The length of the buffer to generate (in seconds). Lower values result in less latency, but require the script to generate audio data faster, resulting in increased CPU usage and more risk for audio cracking if the CPU can't keep up.
var buffer_length: float;

#desc The sample rate to use (in Hz). Higher values are more demanding for the CPU to generate, but result in better quality.
#desc In games, common sample rates in use are [code]11025[/code], [code]16000[/code], [code]22050[/code], [code]32000[/code], [code]44100[/code], and [code]48000[/code].
#desc According to the [url=https://en.wikipedia.org/wiki/Nyquist%E2%80%93Shannon_sampling_theorem]Nyquist-Shannon sampling theorem[/url], there is no quality difference to human hearing when going past 40,000 Hz (since most humans can only hear up to ~20,000 Hz, often less). If you are generating lower-pitched sounds such as voices, lower sample rates such as [code]32000[/code] or [code]22050[/code] may be usable with no loss in quality.
var mix_rate: float;




