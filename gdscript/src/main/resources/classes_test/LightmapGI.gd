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

var bias: float setget set_bias, get_bias;
var bounces: int setget set_bounces, get_bounces;
var directional: bool setget set_directional, is_directional;
var environment_custom_color: Color setget set_environment_custom_color, get_environment_custom_color;
var environment_custom_energy: float setget set_environment_custom_energy, get_environment_custom_energy;
var environment_custom_sky: Sky setget set_environment_custom_sky, get_environment_custom_sky;
var environment_mode: int setget set_environment_mode, get_environment_mode;
var generate_probes_subdiv: int setget set_generate_probes, get_generate_probes;
var interior: bool setget set_interior, is_interior;
var light_data: LightmapGIData setget set_light_data, get_light_data;
var max_texture_size: int setget set_max_texture_size, get_max_texture_size;
var quality: int setget set_bake_quality, get_bake_quality;
var use_denoiser: bool setget set_use_denoiser, is_using_denoiser;

