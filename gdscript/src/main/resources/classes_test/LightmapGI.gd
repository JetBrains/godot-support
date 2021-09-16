extends VisualInstance3D
class_name LightmapGI
const BAKE_QUALITY_LOW = 0;
const BAKE_QUALITY_MEDIUM = 1;
const BAKE_QUALITY_HIGH = 2;
const BAKE_QUALITY_ULTRA = 3;
const GENERATE_PROBES_DISABLED = 0;
const GENERATE_PROBES_SUBDIV_4 = 1;
const GENERATE_PROBES_SUBDIV_8 = 2;
const GENERATE_PROBES_SUBDIV_16 = 3;
const GENERATE_PROBES_SUBDIV_32 = 4;
const BAKE_ERROR_OK = 0;
const BAKE_ERROR_NO_LIGHTMAPPER = 1;
const BAKE_ERROR_NO_SAVE_PATH = 2;
const BAKE_ERROR_NO_MESHES = 3;
const BAKE_ERROR_MESHES_INVALID = 4;
const BAKE_ERROR_CANT_CREATE_IMAGE = 5;
const BAKE_ERROR_USER_ABORTED = 6;
const ENVIRONMENT_MODE_DISABLED = 0;
const ENVIRONMENT_MODE_SCENE = 1;
const ENVIRONMENT_MODE_CUSTOM_SKY = 2;
const ENVIRONMENT_MODE_CUSTOM_COLOR = 3;

var bias: float;
var bounces: int;
var directional: bool;
var environment_custom_color: Color;
var environment_custom_energy: float;
var environment_custom_sky: Sky;
var environment_mode: int;
var generate_probes_subdiv: int;
var interior: bool;
var light_data: LightmapGIData;
var max_texture_size: int;
var quality: int;
var use_denoiser: bool;

