extends AudioStream
#brief Wraps a pool of audio streams with pitch and volume shifting.
#desc Picks a random AudioStream from the pool, depending on the playback mode, and applies random pitch shifting and volume shifting during playback.
class_name AudioStreamRandomizer

#desc Pick a stream at random according to the probability weights chosen for each stream, but avoid playing the same stream twice in a row whenever possible.
const PLAYBACK_RANDOM_NO_REPEATS = 0;

#desc Pick a stream at random according to the probability weights chosen for each stream.
const PLAYBACK_RANDOM = 1;

#desc Play streams in the order they appear in the stream pool.
const PLAYBACK_SEQUENTIAL = 2;


#desc Controls how this AudioStreamRandomizer picks which AudioStream to play next.
var playback_mode: int;

#desc The intensity of random pitch variation. A value of 1 means no variation.
var random_pitch: float;

#desc The intensity of random volume variation. A value of 0 means no variation.
var random_volume_offset_db: float;

#desc The number of streams in the stream pool.
var streams_count: int;



#desc Insert a stream at the specified index.
func add_stream(index: int) -> void:
	pass;

#desc Returns the stream at the specified index.
func get_stream(index: int) -> AudioStream:
	pass;

#desc Returns the probability weight associated with the stream at the given index.
func get_stream_probability_weight(index: int) -> float:
	pass;

#desc Move a stream from one index to another.
func move_stream(index_from: int, index_to: int) -> void:
	pass;

#desc Remove the stream at the specified index.
func remove_stream(index: int) -> void:
	pass;

#desc Set the AudioStream at the specified index.
func set_stream(index: int, stream: AudioStream) -> void:
	pass;

#desc Set the probability weight of the stream at the specified index. The higher this value, the more likely that the randomizer will choose this stream during random playback modes.
func set_stream_probability_weight(index: int, weight: float) -> void:
	pass;


