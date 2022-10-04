#brief Stores audio data loaded from WAV files.
#desc AudioStreamWAV stores sound samples loaded from WAV files. To play the stored sound, use an [AudioStreamPlayer] (for non-positional audio) or [AudioStreamPlayer2D]/[AudioStreamPlayer3D] (for positional audio). The sound can be looped.
#desc This class can also be used to store dynamically-generated PCM audio data. See also [AudioStreamGenerator] for procedural audio generation.
class_name AudioStreamWAV

#desc 8-bit audio codec.
const FORMAT_8_BITS = 0;

#desc 16-bit audio codec.
const FORMAT_16_BITS = 1;

#desc Audio is compressed using IMA ADPCM.
const FORMAT_IMA_ADPCM = 2;

#desc Audio does not loop.
const LOOP_DISABLED = 0;

#desc Audio loops the data between [member loop_begin] and [member loop_end], playing forward only.
const LOOP_FORWARD = 1;

#desc Audio loops the data between [member loop_begin] and [member loop_end], playing back and forth.
const LOOP_PINGPONG = 2;

#desc Audio loops the data between [member loop_begin] and [member loop_end], playing backward only.
const LOOP_BACKWARD = 3;


#desc Contains the audio data in bytes.
#desc [b]Note:[/b] This property expects signed PCM8 data. To convert unsigned PCM8 to signed PCM8, subtract 128 from each byte.
var data: PackedByteArray;

#desc Audio format. See [enum Format] constants for values.
var format: int;

#desc The loop start point (in number of samples, relative to the beginning of the sample). This information will be imported automatically from the WAV file if present.
var loop_begin: int;

#desc The loop end point (in number of samples, relative to the beginning of the sample). This information will be imported automatically from the WAV file if present.
var loop_end: int;

#desc The loop mode. This information will be imported automatically from the WAV file if present. See [enum LoopMode] constants for values.
var loop_mode: int;

#desc The sample rate for mixing this audio. Higher values require more storage space, but result in better quality.
#desc In games, common sample rates in use are [code]11025[/code], [code]16000[/code], [code]22050[/code], [code]32000[/code], [code]44100[/code], and [code]48000[/code].
#desc According to the [url=https://en.wikipedia.org/wiki/Nyquist%E2%80%93Shannon_sampling_theorem]Nyquist-Shannon sampling theorem[/url], there is no quality difference to human hearing when going past 40,000 Hz (since most humans can only hear up to ~20,000 Hz, often less). If you are using lower-pitched sounds such as voices, lower sample rates such as [code]32000[/code] or [code]22050[/code] may be usable with no loss in quality.
var mix_rate: int;

#desc If [code]true[/code], audio is stereo.
var stereo: bool;



#desc Saves the AudioStreamWAV as a WAV file to [param path]. Samples with IMA ADPCM format can't be saved.
#desc [b]Note:[/b] A [code].wav[/code] extension is automatically appended to [param path] if it is missing.
func save_to_wav(path: String) -> int:
	pass;


