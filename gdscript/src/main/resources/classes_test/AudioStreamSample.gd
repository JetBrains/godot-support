extends AudioStream
class_name AudioStreamSample

const FORMAT_8_BITS = 0;
const FORMAT_16_BITS = 1;
const FORMAT_IMA_ADPCM = 2;
const LOOP_DISABLED = 0;
const LOOP_FORWARD = 1;
const LOOP_PING_PONG = 2;
const LOOP_BACKWARD = 3;

var data: PackedByteArray setget set_data, get_data;
var format: int setget set_format, get_format;
var loop_begin: int setget set_loop_begin, get_loop_begin;
var loop_end: int setget set_loop_end, get_loop_end;
var loop_mode: int setget set_loop_mode, get_loop_mode;
var mix_rate: int setget set_mix_rate, get_mix_rate;
var stereo: bool setget set_stereo, is_stereo;

func save_to_wav(path: String) -> int:
    pass;

