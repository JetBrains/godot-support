extends Object
class_name RenderingDevice

const BARRIER_MASK_RASTER = 1;

const BARRIER_MASK_COMPUTE = 2;

const BARRIER_MASK_TRANSFER = 4;

const BARRIER_MASK_ALL = 7;

const BARRIER_MASK_NO_BARRIER = 8;

#desc Rendering device type does not match any of the other enum values or is unknown.
const DEVICE_TYPE_OTHER = 0;

#desc Rendering device is an integrated GPU, which is typically [i](but not always)[/i] slower than dedicated GPUs ([constant DEVICE_TYPE_DISCRETE_GPU]). On Android and iOS, the rendering device type is always considered to be [constant DEVICE_TYPE_INTEGRATED_GPU].
const DEVICE_TYPE_INTEGRATED_GPU = 1;

#desc Rendering device is a dedicated GPU, which is typically [i](but not always)[/i] faster than integrated GPUs ([constant DEVICE_TYPE_INTEGRATED_GPU]).
const DEVICE_TYPE_DISCRETE_GPU = 2;

#desc Rendering device is an emulated GPU in a virtual environment. This is typically much slower than the host GPU, which means the expected performance level on a dedicated GPU will be roughly equivalent to [constant DEVICE_TYPE_INTEGRATED_GPU]. Virtual machine GPU passthrough (such as VFIO) will not report the device type as [constant DEVICE_TYPE_VIRTUAL_GPU]. Instead, the host GPU's device type will be reported as if the GPU was not emulated.
const DEVICE_TYPE_VIRTUAL_GPU = 3;

#desc Rendering device is provided by software emulation (such as Lavapipe or [url=https://github.com/google/swiftshader]SwiftShader[/url]). This is the slowest kind of rendering device available; it's typically much slower than [constant DEVICE_TYPE_INTEGRATED_GPU].
const DEVICE_TYPE_CPU = 4;

#desc Represents the size of the [enum DeviceType] enum.
const DEVICE_TYPE_MAX = 5;

const DRIVER_RESOURCE_VULKAN_DEVICE = 0;

const DRIVER_RESOURCE_VULKAN_PHYSICAL_DEVICE = 1;

const DRIVER_RESOURCE_VULKAN_INSTANCE = 2;

const DRIVER_RESOURCE_VULKAN_QUEUE = 3;

const DRIVER_RESOURCE_VULKAN_QUEUE_FAMILY_INDEX = 4;

const DRIVER_RESOURCE_VULKAN_IMAGE = 5;

const DRIVER_RESOURCE_VULKAN_IMAGE_VIEW = 6;

const DRIVER_RESOURCE_VULKAN_IMAGE_NATIVE_TEXTURE_FORMAT = 7;

const DRIVER_RESOURCE_VULKAN_SAMPLER = 8;

const DRIVER_RESOURCE_VULKAN_DESCRIPTOR_SET = 9;

const DRIVER_RESOURCE_VULKAN_BUFFER = 10;

const DRIVER_RESOURCE_VULKAN_COMPUTE_PIPELINE = 11;

const DRIVER_RESOURCE_VULKAN_RENDER_PIPELINE = 12;

const DATA_FORMAT_R4G4_UNORM_PACK8 = 0;

const DATA_FORMAT_R4G4B4A4_UNORM_PACK16 = 1;

const DATA_FORMAT_B4G4R4A4_UNORM_PACK16 = 2;

const DATA_FORMAT_R5G6B5_UNORM_PACK16 = 3;

const DATA_FORMAT_B5G6R5_UNORM_PACK16 = 4;

const DATA_FORMAT_R5G5B5A1_UNORM_PACK16 = 5;

const DATA_FORMAT_B5G5R5A1_UNORM_PACK16 = 6;

const DATA_FORMAT_A1R5G5B5_UNORM_PACK16 = 7;

const DATA_FORMAT_R8_UNORM = 8;

const DATA_FORMAT_R8_SNORM = 9;

const DATA_FORMAT_R8_USCALED = 10;

const DATA_FORMAT_R8_SSCALED = 11;

const DATA_FORMAT_R8_UINT = 12;

const DATA_FORMAT_R8_SINT = 13;

const DATA_FORMAT_R8_SRGB = 14;

const DATA_FORMAT_R8G8_UNORM = 15;

const DATA_FORMAT_R8G8_SNORM = 16;

const DATA_FORMAT_R8G8_USCALED = 17;

const DATA_FORMAT_R8G8_SSCALED = 18;

const DATA_FORMAT_R8G8_UINT = 19;

const DATA_FORMAT_R8G8_SINT = 20;

const DATA_FORMAT_R8G8_SRGB = 21;

const DATA_FORMAT_R8G8B8_UNORM = 22;

const DATA_FORMAT_R8G8B8_SNORM = 23;

const DATA_FORMAT_R8G8B8_USCALED = 24;

const DATA_FORMAT_R8G8B8_SSCALED = 25;

const DATA_FORMAT_R8G8B8_UINT = 26;

const DATA_FORMAT_R8G8B8_SINT = 27;

const DATA_FORMAT_R8G8B8_SRGB = 28;

const DATA_FORMAT_B8G8R8_UNORM = 29;

const DATA_FORMAT_B8G8R8_SNORM = 30;

const DATA_FORMAT_B8G8R8_USCALED = 31;

const DATA_FORMAT_B8G8R8_SSCALED = 32;

const DATA_FORMAT_B8G8R8_UINT = 33;

const DATA_FORMAT_B8G8R8_SINT = 34;

const DATA_FORMAT_B8G8R8_SRGB = 35;

const DATA_FORMAT_R8G8B8A8_UNORM = 36;

const DATA_FORMAT_R8G8B8A8_SNORM = 37;

const DATA_FORMAT_R8G8B8A8_USCALED = 38;

const DATA_FORMAT_R8G8B8A8_SSCALED = 39;

const DATA_FORMAT_R8G8B8A8_UINT = 40;

const DATA_FORMAT_R8G8B8A8_SINT = 41;

const DATA_FORMAT_R8G8B8A8_SRGB = 42;

const DATA_FORMAT_B8G8R8A8_UNORM = 43;

const DATA_FORMAT_B8G8R8A8_SNORM = 44;

const DATA_FORMAT_B8G8R8A8_USCALED = 45;

const DATA_FORMAT_B8G8R8A8_SSCALED = 46;

const DATA_FORMAT_B8G8R8A8_UINT = 47;

const DATA_FORMAT_B8G8R8A8_SINT = 48;

const DATA_FORMAT_B8G8R8A8_SRGB = 49;

const DATA_FORMAT_A8B8G8R8_UNORM_PACK32 = 50;

const DATA_FORMAT_A8B8G8R8_SNORM_PACK32 = 51;

const DATA_FORMAT_A8B8G8R8_USCALED_PACK32 = 52;

const DATA_FORMAT_A8B8G8R8_SSCALED_PACK32 = 53;

const DATA_FORMAT_A8B8G8R8_UINT_PACK32 = 54;

const DATA_FORMAT_A8B8G8R8_SINT_PACK32 = 55;

const DATA_FORMAT_A8B8G8R8_SRGB_PACK32 = 56;

const DATA_FORMAT_A2R10G10B10_UNORM_PACK32 = 57;

const DATA_FORMAT_A2R10G10B10_SNORM_PACK32 = 58;

const DATA_FORMAT_A2R10G10B10_USCALED_PACK32 = 59;

const DATA_FORMAT_A2R10G10B10_SSCALED_PACK32 = 60;

const DATA_FORMAT_A2R10G10B10_UINT_PACK32 = 61;

const DATA_FORMAT_A2R10G10B10_SINT_PACK32 = 62;

const DATA_FORMAT_A2B10G10R10_UNORM_PACK32 = 63;

const DATA_FORMAT_A2B10G10R10_SNORM_PACK32 = 64;

const DATA_FORMAT_A2B10G10R10_USCALED_PACK32 = 65;

const DATA_FORMAT_A2B10G10R10_SSCALED_PACK32 = 66;

const DATA_FORMAT_A2B10G10R10_UINT_PACK32 = 67;

const DATA_FORMAT_A2B10G10R10_SINT_PACK32 = 68;

const DATA_FORMAT_R16_UNORM = 69;

const DATA_FORMAT_R16_SNORM = 70;

const DATA_FORMAT_R16_USCALED = 71;

const DATA_FORMAT_R16_SSCALED = 72;

const DATA_FORMAT_R16_UINT = 73;

const DATA_FORMAT_R16_SINT = 74;

const DATA_FORMAT_R16_SFLOAT = 75;

const DATA_FORMAT_R16G16_UNORM = 76;

const DATA_FORMAT_R16G16_SNORM = 77;

const DATA_FORMAT_R16G16_USCALED = 78;

const DATA_FORMAT_R16G16_SSCALED = 79;

const DATA_FORMAT_R16G16_UINT = 80;

const DATA_FORMAT_R16G16_SINT = 81;

const DATA_FORMAT_R16G16_SFLOAT = 82;

const DATA_FORMAT_R16G16B16_UNORM = 83;

const DATA_FORMAT_R16G16B16_SNORM = 84;

const DATA_FORMAT_R16G16B16_USCALED = 85;

const DATA_FORMAT_R16G16B16_SSCALED = 86;

const DATA_FORMAT_R16G16B16_UINT = 87;

const DATA_FORMAT_R16G16B16_SINT = 88;

const DATA_FORMAT_R16G16B16_SFLOAT = 89;

const DATA_FORMAT_R16G16B16A16_UNORM = 90;

const DATA_FORMAT_R16G16B16A16_SNORM = 91;

const DATA_FORMAT_R16G16B16A16_USCALED = 92;

const DATA_FORMAT_R16G16B16A16_SSCALED = 93;

const DATA_FORMAT_R16G16B16A16_UINT = 94;

const DATA_FORMAT_R16G16B16A16_SINT = 95;

const DATA_FORMAT_R16G16B16A16_SFLOAT = 96;

const DATA_FORMAT_R32_UINT = 97;

const DATA_FORMAT_R32_SINT = 98;

const DATA_FORMAT_R32_SFLOAT = 99;

const DATA_FORMAT_R32G32_UINT = 100;

const DATA_FORMAT_R32G32_SINT = 101;

const DATA_FORMAT_R32G32_SFLOAT = 102;

const DATA_FORMAT_R32G32B32_UINT = 103;

const DATA_FORMAT_R32G32B32_SINT = 104;

const DATA_FORMAT_R32G32B32_SFLOAT = 105;

const DATA_FORMAT_R32G32B32A32_UINT = 106;

const DATA_FORMAT_R32G32B32A32_SINT = 107;

const DATA_FORMAT_R32G32B32A32_SFLOAT = 108;

const DATA_FORMAT_R64_UINT = 109;

const DATA_FORMAT_R64_SINT = 110;

const DATA_FORMAT_R64_SFLOAT = 111;

const DATA_FORMAT_R64G64_UINT = 112;

const DATA_FORMAT_R64G64_SINT = 113;

const DATA_FORMAT_R64G64_SFLOAT = 114;

const DATA_FORMAT_R64G64B64_UINT = 115;

const DATA_FORMAT_R64G64B64_SINT = 116;

const DATA_FORMAT_R64G64B64_SFLOAT = 117;

const DATA_FORMAT_R64G64B64A64_UINT = 118;

const DATA_FORMAT_R64G64B64A64_SINT = 119;

const DATA_FORMAT_R64G64B64A64_SFLOAT = 120;

const DATA_FORMAT_B10G11R11_UFLOAT_PACK32 = 121;

const DATA_FORMAT_E5B9G9R9_UFLOAT_PACK32 = 122;

const DATA_FORMAT_D16_UNORM = 123;

const DATA_FORMAT_X8_D24_UNORM_PACK32 = 124;

const DATA_FORMAT_D32_SFLOAT = 125;

const DATA_FORMAT_S8_UINT = 126;

const DATA_FORMAT_D16_UNORM_S8_UINT = 127;

const DATA_FORMAT_D24_UNORM_S8_UINT = 128;

const DATA_FORMAT_D32_SFLOAT_S8_UINT = 129;

const DATA_FORMAT_BC1_RGB_UNORM_BLOCK = 130;

const DATA_FORMAT_BC1_RGB_SRGB_BLOCK = 131;

const DATA_FORMAT_BC1_RGBA_UNORM_BLOCK = 132;

const DATA_FORMAT_BC1_RGBA_SRGB_BLOCK = 133;

const DATA_FORMAT_BC2_UNORM_BLOCK = 134;

const DATA_FORMAT_BC2_SRGB_BLOCK = 135;

const DATA_FORMAT_BC3_UNORM_BLOCK = 136;

const DATA_FORMAT_BC3_SRGB_BLOCK = 137;

const DATA_FORMAT_BC4_UNORM_BLOCK = 138;

const DATA_FORMAT_BC4_SNORM_BLOCK = 139;

const DATA_FORMAT_BC5_UNORM_BLOCK = 140;

const DATA_FORMAT_BC5_SNORM_BLOCK = 141;

const DATA_FORMAT_BC6H_UFLOAT_BLOCK = 142;

const DATA_FORMAT_BC6H_SFLOAT_BLOCK = 143;

const DATA_FORMAT_BC7_UNORM_BLOCK = 144;

const DATA_FORMAT_BC7_SRGB_BLOCK = 145;

const DATA_FORMAT_ETC2_R8G8B8_UNORM_BLOCK = 146;

const DATA_FORMAT_ETC2_R8G8B8_SRGB_BLOCK = 147;

const DATA_FORMAT_ETC2_R8G8B8A1_UNORM_BLOCK = 148;

const DATA_FORMAT_ETC2_R8G8B8A1_SRGB_BLOCK = 149;

const DATA_FORMAT_ETC2_R8G8B8A8_UNORM_BLOCK = 150;

const DATA_FORMAT_ETC2_R8G8B8A8_SRGB_BLOCK = 151;

const DATA_FORMAT_EAC_R11_UNORM_BLOCK = 152;

const DATA_FORMAT_EAC_R11_SNORM_BLOCK = 153;

const DATA_FORMAT_EAC_R11G11_UNORM_BLOCK = 154;

const DATA_FORMAT_EAC_R11G11_SNORM_BLOCK = 155;

const DATA_FORMAT_ASTC_4x4_UNORM_BLOCK = 156;

const DATA_FORMAT_ASTC_4x4_SRGB_BLOCK = 157;

const DATA_FORMAT_ASTC_5x4_UNORM_BLOCK = 158;

const DATA_FORMAT_ASTC_5x4_SRGB_BLOCK = 159;

const DATA_FORMAT_ASTC_5x5_UNORM_BLOCK = 160;

const DATA_FORMAT_ASTC_5x5_SRGB_BLOCK = 161;

const DATA_FORMAT_ASTC_6x5_UNORM_BLOCK = 162;

const DATA_FORMAT_ASTC_6x5_SRGB_BLOCK = 163;

const DATA_FORMAT_ASTC_6x6_UNORM_BLOCK = 164;

const DATA_FORMAT_ASTC_6x6_SRGB_BLOCK = 165;

const DATA_FORMAT_ASTC_8x5_UNORM_BLOCK = 166;

const DATA_FORMAT_ASTC_8x5_SRGB_BLOCK = 167;

const DATA_FORMAT_ASTC_8x6_UNORM_BLOCK = 168;

const DATA_FORMAT_ASTC_8x6_SRGB_BLOCK = 169;

const DATA_FORMAT_ASTC_8x8_UNORM_BLOCK = 170;

const DATA_FORMAT_ASTC_8x8_SRGB_BLOCK = 171;

const DATA_FORMAT_ASTC_10x5_UNORM_BLOCK = 172;

const DATA_FORMAT_ASTC_10x5_SRGB_BLOCK = 173;

const DATA_FORMAT_ASTC_10x6_UNORM_BLOCK = 174;

const DATA_FORMAT_ASTC_10x6_SRGB_BLOCK = 175;

const DATA_FORMAT_ASTC_10x8_UNORM_BLOCK = 176;

const DATA_FORMAT_ASTC_10x8_SRGB_BLOCK = 177;

const DATA_FORMAT_ASTC_10x10_UNORM_BLOCK = 178;

const DATA_FORMAT_ASTC_10x10_SRGB_BLOCK = 179;

const DATA_FORMAT_ASTC_12x10_UNORM_BLOCK = 180;

const DATA_FORMAT_ASTC_12x10_SRGB_BLOCK = 181;

const DATA_FORMAT_ASTC_12x12_UNORM_BLOCK = 182;

const DATA_FORMAT_ASTC_12x12_SRGB_BLOCK = 183;

const DATA_FORMAT_G8B8G8R8_422_UNORM = 184;

const DATA_FORMAT_B8G8R8G8_422_UNORM = 185;

const DATA_FORMAT_G8_B8_R8_3PLANE_420_UNORM = 186;

const DATA_FORMAT_G8_B8R8_2PLANE_420_UNORM = 187;

const DATA_FORMAT_G8_B8_R8_3PLANE_422_UNORM = 188;

const DATA_FORMAT_G8_B8R8_2PLANE_422_UNORM = 189;

const DATA_FORMAT_G8_B8_R8_3PLANE_444_UNORM = 190;

const DATA_FORMAT_R10X6_UNORM_PACK16 = 191;

const DATA_FORMAT_R10X6G10X6_UNORM_2PACK16 = 192;

const DATA_FORMAT_R10X6G10X6B10X6A10X6_UNORM_4PACK16 = 193;

const DATA_FORMAT_G10X6B10X6G10X6R10X6_422_UNORM_4PACK16 = 194;

const DATA_FORMAT_B10X6G10X6R10X6G10X6_422_UNORM_4PACK16 = 195;

const DATA_FORMAT_G10X6_B10X6_R10X6_3PLANE_420_UNORM_3PACK16 = 196;

const DATA_FORMAT_G10X6_B10X6R10X6_2PLANE_420_UNORM_3PACK16 = 197;

const DATA_FORMAT_G10X6_B10X6_R10X6_3PLANE_422_UNORM_3PACK16 = 198;

const DATA_FORMAT_G10X6_B10X6R10X6_2PLANE_422_UNORM_3PACK16 = 199;

const DATA_FORMAT_G10X6_B10X6_R10X6_3PLANE_444_UNORM_3PACK16 = 200;

const DATA_FORMAT_R12X4_UNORM_PACK16 = 201;

const DATA_FORMAT_R12X4G12X4_UNORM_2PACK16 = 202;

const DATA_FORMAT_R12X4G12X4B12X4A12X4_UNORM_4PACK16 = 203;

const DATA_FORMAT_G12X4B12X4G12X4R12X4_422_UNORM_4PACK16 = 204;

const DATA_FORMAT_B12X4G12X4R12X4G12X4_422_UNORM_4PACK16 = 205;

const DATA_FORMAT_G12X4_B12X4_R12X4_3PLANE_420_UNORM_3PACK16 = 206;

const DATA_FORMAT_G12X4_B12X4R12X4_2PLANE_420_UNORM_3PACK16 = 207;

const DATA_FORMAT_G12X4_B12X4_R12X4_3PLANE_422_UNORM_3PACK16 = 208;

const DATA_FORMAT_G12X4_B12X4R12X4_2PLANE_422_UNORM_3PACK16 = 209;

const DATA_FORMAT_G12X4_B12X4_R12X4_3PLANE_444_UNORM_3PACK16 = 210;

const DATA_FORMAT_G16B16G16R16_422_UNORM = 211;

const DATA_FORMAT_B16G16R16G16_422_UNORM = 212;

const DATA_FORMAT_G16_B16_R16_3PLANE_420_UNORM = 213;

const DATA_FORMAT_G16_B16R16_2PLANE_420_UNORM = 214;

const DATA_FORMAT_G16_B16_R16_3PLANE_422_UNORM = 215;

const DATA_FORMAT_G16_B16R16_2PLANE_422_UNORM = 216;

const DATA_FORMAT_G16_B16_R16_3PLANE_444_UNORM = 217;

const DATA_FORMAT_MAX = 218;

const TEXTURE_TYPE_1D = 0;

const TEXTURE_TYPE_2D = 1;

const TEXTURE_TYPE_3D = 2;

const TEXTURE_TYPE_CUBE = 3;

const TEXTURE_TYPE_1D_ARRAY = 4;

const TEXTURE_TYPE_2D_ARRAY = 5;

const TEXTURE_TYPE_CUBE_ARRAY = 6;

const TEXTURE_TYPE_MAX = 7;

const TEXTURE_SAMPLES_1 = 0;

const TEXTURE_SAMPLES_2 = 1;

const TEXTURE_SAMPLES_4 = 2;

const TEXTURE_SAMPLES_8 = 3;

const TEXTURE_SAMPLES_16 = 4;

const TEXTURE_SAMPLES_32 = 5;

const TEXTURE_SAMPLES_64 = 6;

const TEXTURE_SAMPLES_MAX = 7;

const TEXTURE_USAGE_SAMPLING_BIT = 1;

const TEXTURE_USAGE_COLOR_ATTACHMENT_BIT = 2;

const TEXTURE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT = 4;

const TEXTURE_USAGE_STORAGE_BIT = 8;

const TEXTURE_USAGE_STORAGE_ATOMIC_BIT = 16;

const TEXTURE_USAGE_CPU_READ_BIT = 32;

const TEXTURE_USAGE_CAN_UPDATE_BIT = 64;

const TEXTURE_USAGE_CAN_COPY_FROM_BIT = 128;

const TEXTURE_USAGE_CAN_COPY_TO_BIT = 256;

const TEXTURE_USAGE_INPUT_ATTACHMENT_BIT = 512;

const TEXTURE_SWIZZLE_IDENTITY = 0;

const TEXTURE_SWIZZLE_ZERO = 1;

const TEXTURE_SWIZZLE_ONE = 2;

const TEXTURE_SWIZZLE_R = 3;

const TEXTURE_SWIZZLE_G = 4;

const TEXTURE_SWIZZLE_B = 5;

const TEXTURE_SWIZZLE_A = 6;

const TEXTURE_SWIZZLE_MAX = 7;

const TEXTURE_SLICE_2D = 0;

const TEXTURE_SLICE_CUBEMAP = 1;

const TEXTURE_SLICE_3D = 2;

const SAMPLER_FILTER_NEAREST = 0;

const SAMPLER_FILTER_LINEAR = 1;

const SAMPLER_REPEAT_MODE_REPEAT = 0;

const SAMPLER_REPEAT_MODE_MIRRORED_REPEAT = 1;

const SAMPLER_REPEAT_MODE_CLAMP_TO_EDGE = 2;

const SAMPLER_REPEAT_MODE_CLAMP_TO_BORDER = 3;

const SAMPLER_REPEAT_MODE_MIRROR_CLAMP_TO_EDGE = 4;

const SAMPLER_REPEAT_MODE_MAX = 5;

const SAMPLER_BORDER_COLOR_FLOAT_TRANSPARENT_BLACK = 0;

const SAMPLER_BORDER_COLOR_INT_TRANSPARENT_BLACK = 1;

const SAMPLER_BORDER_COLOR_FLOAT_OPAQUE_BLACK = 2;

const SAMPLER_BORDER_COLOR_INT_OPAQUE_BLACK = 3;

const SAMPLER_BORDER_COLOR_FLOAT_OPAQUE_WHITE = 4;

const SAMPLER_BORDER_COLOR_INT_OPAQUE_WHITE = 5;

const SAMPLER_BORDER_COLOR_MAX = 6;

const VERTEX_FREQUENCY_VERTEX = 0;

const VERTEX_FREQUENCY_INSTANCE = 1;

const INDEX_BUFFER_FORMAT_UINT16 = 0;

const INDEX_BUFFER_FORMAT_UINT32 = 1;

const STORAGE_BUFFER_USAGE_DISPATCH_INDIRECT = 1;

const UNIFORM_TYPE_SAMPLER = 0;

const UNIFORM_TYPE_SAMPLER_WITH_TEXTURE = 1;

const UNIFORM_TYPE_TEXTURE = 2;

const UNIFORM_TYPE_IMAGE = 3;

const UNIFORM_TYPE_TEXTURE_BUFFER = 4;

const UNIFORM_TYPE_SAMPLER_WITH_TEXTURE_BUFFER = 5;

const UNIFORM_TYPE_IMAGE_BUFFER = 6;

const UNIFORM_TYPE_UNIFORM_BUFFER = 7;

const UNIFORM_TYPE_STORAGE_BUFFER = 8;

const UNIFORM_TYPE_INPUT_ATTACHMENT = 9;

const UNIFORM_TYPE_MAX = 10;

const RENDER_PRIMITIVE_POINTS = 0;

const RENDER_PRIMITIVE_LINES = 1;

const RENDER_PRIMITIVE_LINES_WITH_ADJACENCY = 2;

const RENDER_PRIMITIVE_LINESTRIPS = 3;

const RENDER_PRIMITIVE_LINESTRIPS_WITH_ADJACENCY = 4;

const RENDER_PRIMITIVE_TRIANGLES = 5;

const RENDER_PRIMITIVE_TRIANGLES_WITH_ADJACENCY = 6;

const RENDER_PRIMITIVE_TRIANGLE_STRIPS = 7;

const RENDER_PRIMITIVE_TRIANGLE_STRIPS_WITH_AJACENCY = 8;

const RENDER_PRIMITIVE_TRIANGLE_STRIPS_WITH_RESTART_INDEX = 9;

const RENDER_PRIMITIVE_TESSELATION_PATCH = 10;

const RENDER_PRIMITIVE_MAX = 11;

const POLYGON_CULL_DISABLED = 0;

const POLYGON_CULL_FRONT = 1;

const POLYGON_CULL_BACK = 2;

const POLYGON_FRONT_FACE_CLOCKWISE = 0;

const POLYGON_FRONT_FACE_COUNTER_CLOCKWISE = 1;

const STENCIL_OP_KEEP = 0;

const STENCIL_OP_ZERO = 1;

const STENCIL_OP_REPLACE = 2;

const STENCIL_OP_INCREMENT_AND_CLAMP = 3;

const STENCIL_OP_DECREMENT_AND_CLAMP = 4;

const STENCIL_OP_INVERT = 5;

const STENCIL_OP_INCREMENT_AND_WRAP = 6;

const STENCIL_OP_DECREMENT_AND_WRAP = 7;

const STENCIL_OP_MAX = 8;

const COMPARE_OP_NEVER = 0;

const COMPARE_OP_LESS = 1;

const COMPARE_OP_EQUAL = 2;

const COMPARE_OP_LESS_OR_EQUAL = 3;

const COMPARE_OP_GREATER = 4;

const COMPARE_OP_NOT_EQUAL = 5;

const COMPARE_OP_GREATER_OR_EQUAL = 6;

const COMPARE_OP_ALWAYS = 7;

const COMPARE_OP_MAX = 8;

const LOGIC_OP_CLEAR = 0;

const LOGIC_OP_AND = 1;

const LOGIC_OP_AND_REVERSE = 2;

const LOGIC_OP_COPY = 3;

const LOGIC_OP_AND_INVERTED = 4;

const LOGIC_OP_NO_OP = 5;

const LOGIC_OP_XOR = 6;

const LOGIC_OP_OR = 7;

const LOGIC_OP_NOR = 8;

const LOGIC_OP_EQUIVALENT = 9;

const LOGIC_OP_INVERT = 10;

const LOGIC_OP_OR_REVERSE = 11;

const LOGIC_OP_COPY_INVERTED = 12;

const LOGIC_OP_OR_INVERTED = 13;

const LOGIC_OP_NAND = 14;

const LOGIC_OP_SET = 15;

const LOGIC_OP_MAX = 16;

const BLEND_FACTOR_ZERO = 0;

const BLEND_FACTOR_ONE = 1;

const BLEND_FACTOR_SRC_COLOR = 2;

const BLEND_FACTOR_ONE_MINUS_SRC_COLOR = 3;

const BLEND_FACTOR_DST_COLOR = 4;

const BLEND_FACTOR_ONE_MINUS_DST_COLOR = 5;

const BLEND_FACTOR_SRC_ALPHA = 6;

const BLEND_FACTOR_ONE_MINUS_SRC_ALPHA = 7;

const BLEND_FACTOR_DST_ALPHA = 8;

const BLEND_FACTOR_ONE_MINUS_DST_ALPHA = 9;

const BLEND_FACTOR_CONSTANT_COLOR = 10;

const BLEND_FACTOR_ONE_MINUS_CONSTANT_COLOR = 11;

const BLEND_FACTOR_CONSTANT_ALPHA = 12;

const BLEND_FACTOR_ONE_MINUS_CONSTANT_ALPHA = 13;

const BLEND_FACTOR_SRC_ALPHA_SATURATE = 14;

const BLEND_FACTOR_SRC1_COLOR = 15;

const BLEND_FACTOR_ONE_MINUS_SRC1_COLOR = 16;

const BLEND_FACTOR_SRC1_ALPHA = 17;

const BLEND_FACTOR_ONE_MINUS_SRC1_ALPHA = 18;

const BLEND_FACTOR_MAX = 19;

const BLEND_OP_ADD = 0;

const BLEND_OP_SUBTRACT = 1;

const BLEND_OP_REVERSE_SUBTRACT = 2;

const BLEND_OP_MINIMUM = 3;

const BLEND_OP_MAXIMUM = 4;

const BLEND_OP_MAX = 5;

const DYNAMIC_STATE_LINE_WIDTH = 1;

const DYNAMIC_STATE_DEPTH_BIAS = 2;

const DYNAMIC_STATE_BLEND_CONSTANTS = 4;

const DYNAMIC_STATE_DEPTH_BOUNDS = 8;

const DYNAMIC_STATE_STENCIL_COMPARE_MASK = 16;

const DYNAMIC_STATE_STENCIL_WRITE_MASK = 32;

const DYNAMIC_STATE_STENCIL_REFERENCE = 64;

const INITIAL_ACTION_CLEAR = 0;

const INITIAL_ACTION_CLEAR_REGION = 1;

const INITIAL_ACTION_CLEAR_REGION_CONTINUE = 2;

const INITIAL_ACTION_KEEP = 3;

const INITIAL_ACTION_DROP = 4;

const INITIAL_ACTION_CONTINUE = 5;

const INITIAL_ACTION_MAX = 6;

const FINAL_ACTION_READ = 0;

const FINAL_ACTION_DISCARD = 1;

const FINAL_ACTION_CONTINUE = 2;

const FINAL_ACTION_MAX = 3;

const SHADER_STAGE_VERTEX = 0;

const SHADER_STAGE_FRAGMENT = 1;

const SHADER_STAGE_TESSELATION_CONTROL = 2;

const SHADER_STAGE_TESSELATION_EVALUATION = 3;

const SHADER_STAGE_COMPUTE = 4;

const SHADER_STAGE_MAX = 5;

const SHADER_STAGE_VERTEX_BIT = 1;

const SHADER_STAGE_FRAGMENT_BIT = 2;

const SHADER_STAGE_TESSELATION_CONTROL_BIT = 4;

const SHADER_STAGE_TESSELATION_EVALUATION_BIT = 8;

const SHADER_STAGE_COMPUTE_BIT = 16;

const SHADER_LANGUAGE_GLSL = 0;

const SHADER_LANGUAGE_HLSL = 1;

const PIPELINE_SPECIALIZATION_CONSTANT_TYPE_BOOL = 0;

const PIPELINE_SPECIALIZATION_CONSTANT_TYPE_INT = 1;

const PIPELINE_SPECIALIZATION_CONSTANT_TYPE_FLOAT = 2;

const LIMIT_MAX_BOUND_UNIFORM_SETS = 0;

const LIMIT_MAX_FRAMEBUFFER_COLOR_ATTACHMENTS = 1;

const LIMIT_MAX_TEXTURES_PER_UNIFORM_SET = 2;

const LIMIT_MAX_SAMPLERS_PER_UNIFORM_SET = 3;

const LIMIT_MAX_STORAGE_BUFFERS_PER_UNIFORM_SET = 4;

const LIMIT_MAX_STORAGE_IMAGES_PER_UNIFORM_SET = 5;

const LIMIT_MAX_UNIFORM_BUFFERS_PER_UNIFORM_SET = 6;

const LIMIT_MAX_DRAW_INDEXED_INDEX = 7;

const LIMIT_MAX_FRAMEBUFFER_HEIGHT = 8;

const LIMIT_MAX_FRAMEBUFFER_WIDTH = 9;

const LIMIT_MAX_TEXTURE_ARRAY_LAYERS = 10;

const LIMIT_MAX_TEXTURE_SIZE_1D = 11;

const LIMIT_MAX_TEXTURE_SIZE_2D = 12;

const LIMIT_MAX_TEXTURE_SIZE_3D = 13;

const LIMIT_MAX_TEXTURE_SIZE_CUBE = 14;

const LIMIT_MAX_TEXTURES_PER_SHADER_STAGE = 15;

const LIMIT_MAX_SAMPLERS_PER_SHADER_STAGE = 16;

const LIMIT_MAX_STORAGE_BUFFERS_PER_SHADER_STAGE = 17;

const LIMIT_MAX_STORAGE_IMAGES_PER_SHADER_STAGE = 18;

const LIMIT_MAX_UNIFORM_BUFFERS_PER_SHADER_STAGE = 19;

const LIMIT_MAX_PUSH_CONSTANT_SIZE = 20;

const LIMIT_MAX_UNIFORM_BUFFER_SIZE = 21;

const LIMIT_MAX_VERTEX_INPUT_ATTRIBUTE_OFFSET = 22;

const LIMIT_MAX_VERTEX_INPUT_ATTRIBUTES = 23;

const LIMIT_MAX_VERTEX_INPUT_BINDINGS = 24;

const LIMIT_MAX_VERTEX_INPUT_BINDING_STRIDE = 25;

const LIMIT_MIN_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 26;

const LIMIT_MAX_COMPUTE_SHARED_MEMORY_SIZE = 27;

const LIMIT_MAX_COMPUTE_WORKGROUP_COUNT_X = 28;

const LIMIT_MAX_COMPUTE_WORKGROUP_COUNT_Y = 29;

const LIMIT_MAX_COMPUTE_WORKGROUP_COUNT_Z = 30;

const LIMIT_MAX_COMPUTE_WORKGROUP_INVOCATIONS = 31;

const LIMIT_MAX_COMPUTE_WORKGROUP_SIZE_X = 32;

const LIMIT_MAX_COMPUTE_WORKGROUP_SIZE_Y = 33;

const LIMIT_MAX_COMPUTE_WORKGROUP_SIZE_Z = 34;

const MEMORY_TEXTURES = 0;

const MEMORY_BUFFERS = 1;

const MEMORY_TOTAL = 2;

const INVALID_ID = -1;

const INVALID_FORMAT_ID = -1;




func barrier(from: int, to: int) -> void:
	pass;

func buffer_clear(buffer: RID, offset: int, size_bytes: int, post_barrier: int) -> int:
	pass;

func buffer_get_data(buffer: RID) -> PackedByteArray:
	pass;

func buffer_update(buffer: RID, offset: int, size_bytes: int, data: PackedByteArray, post_barrier: int) -> int:
	pass;

func capture_timestamp(name: String) -> void:
	pass;

func compute_list_add_barrier(compute_list: int) -> void:
	pass;

func compute_list_begin(allow_draw_overlap: bool) -> int:
	pass;

func compute_list_bind_compute_pipeline(compute_list: int, compute_pipeline: RID) -> void:
	pass;

func compute_list_bind_uniform_set(compute_list: int, uniform_set: RID, set_index: int) -> void:
	pass;

func compute_list_dispatch(compute_list: int, x_groups: int, y_groups: int, z_groups: int) -> void:
	pass;

func compute_list_end(post_barrier: int) -> void:
	pass;

func compute_list_set_push_constant(compute_list: int, buffer: PackedByteArray, size_bytes: int) -> void:
	pass;

func compute_pipeline_create(shader: RID, specialization_constants: RDPipelineSpecializationConstant[]) -> RID:
	pass;

func compute_pipeline_is_valid(compute_pieline: RID) -> bool:
	pass;

func create_local_device() -> RenderingDevice:
	pass;

func draw_command_begin_label(name: String, color: Color) -> void:
	pass;

func draw_command_end_label() -> void:
	pass;

func draw_command_insert_label(name: String, color: Color) -> void:
	pass;

func draw_list_begin(framebuffer: RID, initial_color_action: int, final_color_action: int, initial_depth_action: int, final_depth_action: int, clear_color_values: PackedColorArray, clear_depth: float, clear_stencil: int, region: Rect2, storage_textures: Array) -> int:
	pass;

func draw_list_begin_for_screen(screen: int, clear_color: Color) -> int:
	pass;

func draw_list_begin_split(framebuffer: RID, splits: int, initial_color_action: int, final_color_action: int, initial_depth_action: int, final_depth_action: int, clear_color_values: PackedColorArray, clear_depth: float, clear_stencil: int, region: Rect2, storage_textures: RID[]) -> PackedInt64Array:
	pass;

func draw_list_bind_index_array(draw_list: int, index_array: RID) -> void:
	pass;

func draw_list_bind_render_pipeline(draw_list: int, render_pipeline: RID) -> void:
	pass;

func draw_list_bind_uniform_set(draw_list: int, uniform_set: RID, set_index: int) -> void:
	pass;

func draw_list_bind_vertex_array(draw_list: int, vertex_array: RID) -> void:
	pass;

func draw_list_disable_scissor(draw_list: int) -> void:
	pass;

func draw_list_draw(draw_list: int, use_indices: bool, instances: int, procedural_vertex_count: int) -> void:
	pass;

func draw_list_enable_scissor(draw_list: int, rect: Rect2) -> void:
	pass;

func draw_list_end(post_barrier: int) -> void:
	pass;

#desc Sets blend constants for draw list, blend constants are used only if the graphics pipeline is created with [code]DYNAMIC_STATE_BLEND_CONSTANTS[/code] flag set.
func draw_list_set_blend_constants(draw_list: int, color: Color) -> void:
	pass;

func draw_list_set_push_constant(draw_list: int, buffer: PackedByteArray, size_bytes: int) -> void:
	pass;

func draw_list_switch_to_next_pass() -> int:
	pass;

func draw_list_switch_to_next_pass_split(splits: int) -> PackedInt64Array:
	pass;

func framebuffer_create(textures: RID[], validate_with_format: int, view_count: int) -> RID:
	pass;

func framebuffer_create_empty(size: Vector2i, samples: int, validate_with_format: int) -> RID:
	pass;

func framebuffer_create_multipass(textures: RID[], passes: RDFramebufferPass[], validate_with_format: int, view_count: int) -> RID:
	pass;

func framebuffer_format_create(attachments: RDAttachmentFormat[], view_count: int) -> int:
	pass;

func framebuffer_format_create_empty(samples: int) -> int:
	pass;

func framebuffer_format_create_multipass(attachments: RDAttachmentFormat[], passes: RDFramebufferPass[], view_count: int) -> int:
	pass;

func framebuffer_format_get_texture_samples(format: int, render_pass: int) -> int:
	pass;

func framebuffer_get_format(framebuffer: RID) -> int:
	pass;

func framebuffer_is_valid(framebuffer: RID) -> bool:
	pass;

func free_rid(rid: RID) -> void:
	pass;

func full_barrier() -> void:
	pass;

func get_captured_timestamp_cpu_time(index: int) -> int:
	pass;

func get_captured_timestamp_gpu_time(index: int) -> int:
	pass;

func get_captured_timestamp_name(index: int) -> String:
	pass;

func get_captured_timestamps_count() -> int:
	pass;

func get_captured_timestamps_frame() -> int:
	pass;

func get_device_name() -> String:
	pass;

func get_device_pipeline_cache_uuid() -> String:
	pass;

func get_device_vendor_name() -> String:
	pass;

func get_driver_resource(resource: int, rid: RID, index: int) -> int:
	pass;

func get_frame_delay() -> int:
	pass;

func get_memory_usage(type: int) -> int:
	pass;

func index_array_create(index_buffer: RID, index_offset: int, index_count: int) -> RID:
	pass;

func index_buffer_create(size_indices: int, format: int, data: PackedByteArray, use_restart_indices: bool) -> RID:
	pass;

func limit_get(limit: int) -> int:
	pass;

func render_pipeline_create(shader: RID, framebuffer_format: int, vertex_format: int, primitive: int, rasterization_state: RDPipelineRasterizationState, multisample_state: RDPipelineMultisampleState, stencil_state: RDPipelineDepthStencilState, color_blend_state: RDPipelineColorBlendState, dynamic_state_flags: int, for_render_pass: int, specialization_constants: RDPipelineSpecializationConstant[]) -> RID:
	pass;

func render_pipeline_is_valid(render_pipeline: RID) -> bool:
	pass;

func sampler_create(state: RDSamplerState) -> RID:
	pass;

func screen_get_framebuffer_format() -> int:
	pass;

func screen_get_height(screen: int) -> int:
	pass;

func screen_get_width(screen: int) -> int:
	pass;

func set_resource_name(id: RID, name: String) -> void:
	pass;

func shader_compile_binary_from_spirv(spirv_data: RDShaderSPIRV, name: String) -> PackedByteArray:
	pass;

func shader_compile_spirv_from_source(shader_source: RDShaderSource, allow_cache: bool) -> RDShaderSPIRV:
	pass;

func shader_create_from_bytecode(binary_data: PackedByteArray) -> RID:
	pass;

func shader_create_from_spirv(spirv_data: RDShaderSPIRV, name: String) -> RID:
	pass;

func shader_get_vertex_input_attribute_mask(shader: RID) -> int:
	pass;

func storage_buffer_create(size_bytes: int, data: PackedByteArray, usage: int) -> RID:
	pass;

func submit() -> void:
	pass;

func sync() -> void:
	pass;

func texture_buffer_create(size_bytes: int, format: int, data: PackedByteArray) -> RID:
	pass;

func texture_clear(texture: RID, color: Color, base_mipmap: int, mipmap_count: int, base_layer: int, layer_count: int, post_barrier: int) -> int:
	pass;

func texture_copy(from_texture: RID, to_texture: RID, from_pos: Vector3, to_pos: Vector3, size: Vector3, src_mipmap: int, dst_mipmap: int, src_layer: int, dst_layer: int, post_barrier: int) -> int:
	pass;

func texture_create(format: RDTextureFormat, view: RDTextureView, data: PackedByteArray[]) -> RID:
	pass;

func texture_create_shared(view: RDTextureView, with_texture: RID) -> RID:
	pass;

func texture_create_shared_from_slice(view: RDTextureView, with_texture: RID, layer: int, mipmap: int, mipmaps: int, slice_type: int) -> RID:
	pass;

func texture_get_data(texture: RID, layer: int) -> PackedByteArray:
	pass;

func texture_is_format_supported_for_usage(format: int, usage_flags: int) -> bool:
	pass;

func texture_is_shared(texture: RID) -> bool:
	pass;

func texture_is_valid(texture: RID) -> bool:
	pass;

func texture_resolve_multisample(from_texture: RID, to_texture: RID, post_barrier: int) -> int:
	pass;

func texture_update(texture: RID, layer: int, data: PackedByteArray, post_barrier: int) -> int:
	pass;

func uniform_buffer_create(size_bytes: int, data: PackedByteArray) -> RID:
	pass;

func uniform_set_create(uniforms: RDUniform[], shader: RID, shader_set: int) -> RID:
	pass;

func uniform_set_is_valid(uniform_set: RID) -> bool:
	pass;

func vertex_buffer_create(size_bytes: int, data: PackedByteArray, use_as_storage: bool) -> RID:
	pass;

func vertex_format_create(vertex_descriptions: RDVertexAttribute[]) -> int:
	pass;


