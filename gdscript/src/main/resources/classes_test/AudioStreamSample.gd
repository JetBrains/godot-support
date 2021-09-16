extends AudioStream
class_name AudioStreamSample
const FORMAT_8_BITS = 0;
const FORMAT_16_BITS = 1;
const FORMAT_IMA_ADPCM = 2;
const LOOP_DISABLED = 0;
const LOOP_FORWARD = 1;
const LOOP_PING_PONG = 2;
const LOOP_BACKWARD = 3;

var data: PackedByteArray;
var format: int;
var loop_begin: int;
var loop_end: int;
var loop_mode: int;
var mix_rate: int;
var stereo: bool;

func save_to_wav(path: String) -> int:
    pass;
