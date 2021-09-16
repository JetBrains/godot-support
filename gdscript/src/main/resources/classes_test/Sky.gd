extends Resource
class_name Sky
const RADIANCE_SIZE_32 = 0;
const RADIANCE_SIZE_64 = 1;
const RADIANCE_SIZE_128 = 2;
const RADIANCE_SIZE_256 = 3;
const RADIANCE_SIZE_512 = 4;
const RADIANCE_SIZE_1024 = 5;
const RADIANCE_SIZE_2048 = 6;
const RADIANCE_SIZE_MAX = 7;
const PROCESS_MODE_AUTOMATIC = 0;
const PROCESS_MODE_QUALITY = 1;
const PROCESS_MODE_INCREMENTAL = 2;
const PROCESS_MODE_REALTIME = 3;

var process_mode: int;
var radiance_size: int;
var sky_material: Material;

