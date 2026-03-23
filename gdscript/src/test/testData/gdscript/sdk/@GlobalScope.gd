class_name _GlobalScope

#enum Side
enum {
    SIDE_LEFT = 0,
    SIDE_TOP = 1,
    SIDE_RIGHT = 2,
    SIDE_BOTTOM = 3,
}
#enum Corner
enum {
    CORNER_TOP_LEFT = 0,
    CORNER_TOP_RIGHT = 1,
    CORNER_BOTTOM_RIGHT = 2,
    CORNER_BOTTOM_LEFT = 3,
}
#enum Orientation
enum {
    VERTICAL = 1,
    HORIZONTAL = 0,
}
#enum ClockDirection
enum {
    CLOCKWISE = 0,
    COUNTERCLOCKWISE = 1,
}
#enum HorizontalAlignment
enum {
    HORIZONTAL_ALIGNMENT_LEFT = 0,
    HORIZONTAL_ALIGNMENT_CENTER = 1,
    HORIZONTAL_ALIGNMENT_RIGHT = 2,
    HORIZONTAL_ALIGNMENT_FILL = 3,
}
#enum VerticalAlignment
enum {
    VERTICAL_ALIGNMENT_TOP = 0,
    VERTICAL_ALIGNMENT_CENTER = 1,
    VERTICAL_ALIGNMENT_BOTTOM = 2,
    VERTICAL_ALIGNMENT_FILL = 3,
}
#enum InlineAlignment
enum {
    INLINE_ALIGNMENT_TOP_TO = 0,
    INLINE_ALIGNMENT_CENTER_TO = 1,
    INLINE_ALIGNMENT_BASELINE_TO = 3,
    INLINE_ALIGNMENT_BOTTOM_TO = 2,
    INLINE_ALIGNMENT_TO_TOP = 0,
    INLINE_ALIGNMENT_TO_CENTER = 4,
    INLINE_ALIGNMENT_TO_BASELINE = 8,
    INLINE_ALIGNMENT_TO_BOTTOM = 12,
    INLINE_ALIGNMENT_TOP = 0,
    INLINE_ALIGNMENT_CENTER = 5,
    INLINE_ALIGNMENT_BOTTOM = 14,
    INLINE_ALIGNMENT_IMAGE_MASK = 3,
    INLINE_ALIGNMENT_TEXT_MASK = 12,
}
#enum EulerOrder
enum {
    EULER_ORDER_XYZ = 0,
    EULER_ORDER_XZY = 1,
    EULER_ORDER_YXZ = 2,
    EULER_ORDER_YZX = 3,
    EULER_ORDER_ZXY = 4,
    EULER_ORDER_ZYX = 5,
}
#enum Key
enum {
    KEY_NONE = 0,
    KEY_SPECIAL = 4194304,
    KEY_ESCAPE = 4194305,
    KEY_TAB = 4194306,
    KEY_BACKTAB = 4194307,
    KEY_BACKSPACE = 4194308,
    KEY_ENTER = 4194309,
    KEY_KP_ENTER = 4194310,
    KEY_INSERT = 4194311,
    KEY_DELETE = 4194312,
    KEY_PAUSE = 4194313,
    KEY_PRINT = 4194314,
    KEY_SYSREQ = 4194315,
    KEY_CLEAR = 4194316,
    KEY_HOME = 4194317,
    KEY_END = 4194318,
    KEY_LEFT = 4194319,
    KEY_UP = 4194320,
    KEY_RIGHT = 4194321,
    KEY_DOWN = 4194322,
    KEY_PAGEUP = 4194323,
    KEY_PAGEDOWN = 4194324,
    KEY_SHIFT = 4194325,
    KEY_CTRL = 4194326,
    KEY_META = 4194327,
    KEY_ALT = 4194328,
    KEY_CAPSLOCK = 4194329,
    KEY_NUMLOCK = 4194330,
    KEY_SCROLLLOCK = 4194331,
    KEY_F1 = 4194332,
    KEY_F2 = 4194333,
    KEY_F3 = 4194334,
    KEY_F4 = 4194335,
    KEY_F5 = 4194336,
    KEY_F6 = 4194337,
    KEY_F7 = 4194338,
    KEY_F8 = 4194339,
    KEY_F9 = 4194340,
    KEY_F10 = 4194341,
    KEY_F11 = 4194342,
    KEY_F12 = 4194343,
    KEY_F13 = 4194344,
    KEY_F14 = 4194345,
    KEY_F15 = 4194346,
    KEY_F16 = 4194347,
    KEY_F17 = 4194348,
    KEY_F18 = 4194349,
    KEY_F19 = 4194350,
    KEY_F20 = 4194351,
    KEY_F21 = 4194352,
    KEY_F22 = 4194353,
    KEY_F23 = 4194354,
    KEY_F24 = 4194355,
    KEY_F25 = 4194356,
    KEY_F26 = 4194357,
    KEY_F27 = 4194358,
    KEY_F28 = 4194359,
    KEY_F29 = 4194360,
    KEY_F30 = 4194361,
    KEY_F31 = 4194362,
    KEY_F32 = 4194363,
    KEY_F33 = 4194364,
    KEY_F34 = 4194365,
    KEY_F35 = 4194366,
    KEY_KP_MULTIPLY = 4194433,
    KEY_KP_DIVIDE = 4194434,
    KEY_KP_SUBTRACT = 4194435,
    KEY_KP_PERIOD = 4194436,
    KEY_KP_ADD = 4194437,
    KEY_KP_0 = 4194438,
    KEY_KP_1 = 4194439,
    KEY_KP_2 = 4194440,
    KEY_KP_3 = 4194441,
    KEY_KP_4 = 4194442,
    KEY_KP_5 = 4194443,
    KEY_KP_6 = 4194444,
    KEY_KP_7 = 4194445,
    KEY_KP_8 = 4194446,
    KEY_KP_9 = 4194447,
    KEY_MENU = 4194370,
    KEY_HYPER = 4194371,
    KEY_HELP = 4194373,
    KEY_BACK = 4194376,
    KEY_FORWARD = 4194377,
    KEY_STOP = 4194378,
    KEY_REFRESH = 4194379,
    KEY_VOLUMEDOWN = 4194380,
    KEY_VOLUMEMUTE = 4194381,
    KEY_VOLUMEUP = 4194382,
    KEY_MEDIAPLAY = 4194388,
    KEY_MEDIASTOP = 4194389,
    KEY_MEDIAPREVIOUS = 4194390,
    KEY_MEDIANEXT = 4194391,
    KEY_MEDIARECORD = 4194392,
    KEY_HOMEPAGE = 4194393,
    KEY_FAVORITES = 4194394,
    KEY_SEARCH = 4194395,
    KEY_STANDBY = 4194396,
    KEY_OPENURL = 4194397,
    KEY_LAUNCHMAIL = 4194398,
    KEY_LAUNCHMEDIA = 4194399,
    KEY_LAUNCH0 = 4194400,
    KEY_LAUNCH1 = 4194401,
    KEY_LAUNCH2 = 4194402,
    KEY_LAUNCH3 = 4194403,
    KEY_LAUNCH4 = 4194404,
    KEY_LAUNCH5 = 4194405,
    KEY_LAUNCH6 = 4194406,
    KEY_LAUNCH7 = 4194407,
    KEY_LAUNCH8 = 4194408,
    KEY_LAUNCH9 = 4194409,
    KEY_LAUNCHA = 4194410,
    KEY_LAUNCHB = 4194411,
    KEY_LAUNCHC = 4194412,
    KEY_LAUNCHD = 4194413,
    KEY_LAUNCHE = 4194414,
    KEY_LAUNCHF = 4194415,
    KEY_GLOBE = 4194416,
    KEY_KEYBOARD = 4194417,
    KEY_JIS_EISU = 4194418,
    KEY_JIS_KANA = 4194419,
    KEY_UNKNOWN = 8388607,
    KEY_SPACE = 32,
    KEY_EXCLAM = 33,
    KEY_QUOTEDBL = 34,
    KEY_NUMBERSIGN = 35,
    KEY_DOLLAR = 36,
    KEY_PERCENT = 37,
    KEY_AMPERSAND = 38,
    KEY_APOSTROPHE = 39,
    KEY_PARENLEFT = 40,
    KEY_PARENRIGHT = 41,
    KEY_ASTERISK = 42,
    KEY_PLUS = 43,
    KEY_COMMA = 44,
    KEY_MINUS = 45,
    KEY_PERIOD = 46,
    KEY_SLASH = 47,
    KEY_0 = 48,
    KEY_1 = 49,
    KEY_2 = 50,
    KEY_3 = 51,
    KEY_4 = 52,
    KEY_5 = 53,
    KEY_6 = 54,
    KEY_7 = 55,
    KEY_8 = 56,
    KEY_9 = 57,
    KEY_COLON = 58,
    KEY_SEMICOLON = 59,
    KEY_LESS = 60,
    KEY_EQUAL = 61,
    KEY_GREATER = 62,
    KEY_QUESTION = 63,
    KEY_AT = 64,
    KEY_A = 65,
    KEY_B = 66,
    KEY_C = 67,
    KEY_D = 68,
    KEY_E = 69,
    KEY_F = 70,
    KEY_G = 71,
    KEY_H = 72,
    KEY_I = 73,
    KEY_J = 74,
    KEY_K = 75,
    KEY_L = 76,
    KEY_M = 77,
    KEY_N = 78,
    KEY_O = 79,
    KEY_P = 80,
    KEY_Q = 81,
    KEY_R = 82,
    KEY_S = 83,
    KEY_T = 84,
    KEY_U = 85,
    KEY_V = 86,
    KEY_W = 87,
    KEY_X = 88,
    KEY_Y = 89,
    KEY_Z = 90,
    KEY_BRACKETLEFT = 91,
    KEY_BACKSLASH = 92,
    KEY_BRACKETRIGHT = 93,
    KEY_ASCIICIRCUM = 94,
    KEY_UNDERSCORE = 95,
    KEY_QUOTELEFT = 96,
    KEY_BRACELEFT = 123,
    KEY_BAR = 124,
    KEY_BRACERIGHT = 125,
    KEY_ASCIITILDE = 126,
    KEY_YEN = 165,
    KEY_SECTION = 167,
}
#enum KeyModifierMask
enum {
    KEY_CODE_MASK = 8388607,
    KEY_MODIFIER_MASK = 2130706432,
    KEY_MASK_CMD_OR_CTRL = 16777216,
    KEY_MASK_SHIFT = 33554432,
    KEY_MASK_ALT = 67108864,
    KEY_MASK_META = 134217728,
    KEY_MASK_CTRL = 268435456,
    KEY_MASK_KPAD = 536870912,
    KEY_MASK_GROUP_SWITCH = 1073741824,
}
#enum KeyLocation
enum {
    KEY_LOCATION_UNSPECIFIED = 0,
    KEY_LOCATION_LEFT = 1,
    KEY_LOCATION_RIGHT = 2,
}
#enum MouseButton
enum {
    MOUSE_BUTTON_NONE = 0,
    MOUSE_BUTTON_LEFT = 1,
    MOUSE_BUTTON_RIGHT = 2,
    MOUSE_BUTTON_MIDDLE = 3,
    MOUSE_BUTTON_WHEEL_UP = 4,
    MOUSE_BUTTON_WHEEL_DOWN = 5,
    MOUSE_BUTTON_WHEEL_LEFT = 6,
    MOUSE_BUTTON_WHEEL_RIGHT = 7,
    MOUSE_BUTTON_XBUTTON1 = 8,
    MOUSE_BUTTON_XBUTTON2 = 9,
}
#enum MouseButtonMask
enum {
    MOUSE_BUTTON_MASK_LEFT = 1,
    MOUSE_BUTTON_MASK_RIGHT = 2,
    MOUSE_BUTTON_MASK_MIDDLE = 4,
    MOUSE_BUTTON_MASK_MB_XBUTTON1 = 128,
    MOUSE_BUTTON_MASK_MB_XBUTTON2 = 256,
}
#enum JoyButton
enum {
    JOY_BUTTON_INVALID = -1,
    JOY_BUTTON_A = 0,
    JOY_BUTTON_B = 1,
    JOY_BUTTON_X = 2,
    JOY_BUTTON_Y = 3,
    JOY_BUTTON_BACK = 4,
    JOY_BUTTON_GUIDE = 5,
    JOY_BUTTON_START = 6,
    JOY_BUTTON_LEFT_STICK = 7,
    JOY_BUTTON_RIGHT_STICK = 8,
    JOY_BUTTON_LEFT_SHOULDER = 9,
    JOY_BUTTON_RIGHT_SHOULDER = 10,
    JOY_BUTTON_DPAD_UP = 11,
    JOY_BUTTON_DPAD_DOWN = 12,
    JOY_BUTTON_DPAD_LEFT = 13,
    JOY_BUTTON_DPAD_RIGHT = 14,
    JOY_BUTTON_MISC1 = 15,
    JOY_BUTTON_PADDLE1 = 16,
    JOY_BUTTON_PADDLE2 = 17,
    JOY_BUTTON_PADDLE3 = 18,
    JOY_BUTTON_PADDLE4 = 19,
    JOY_BUTTON_TOUCHPAD = 20,
    JOY_BUTTON_SDL_MAX = 21,
    JOY_BUTTON_MAX = 128,
}
#enum JoyAxis
enum {
    JOY_AXIS_INVALID = -1,
    JOY_AXIS_LEFT_X = 0,
    JOY_AXIS_LEFT_Y = 1,
    JOY_AXIS_RIGHT_X = 2,
    JOY_AXIS_RIGHT_Y = 3,
    JOY_AXIS_TRIGGER_LEFT = 4,
    JOY_AXIS_TRIGGER_RIGHT = 5,
    JOY_AXIS_SDL_MAX = 6,
    JOY_AXIS_MAX = 10,
}
#enum MIDIMessage
enum {
    MIDI_MESSAGE_NONE = 0,
    MIDI_MESSAGE_NOTE_OFF = 8,
    MIDI_MESSAGE_NOTE_ON = 9,
    MIDI_MESSAGE_AFTERTOUCH = 10,
    MIDI_MESSAGE_CONTROL_CHANGE = 11,
    MIDI_MESSAGE_PROGRAM_CHANGE = 12,
    MIDI_MESSAGE_CHANNEL_PRESSURE = 13,
    MIDI_MESSAGE_PITCH_BEND = 14,
    MIDI_MESSAGE_SYSTEM_EXCLUSIVE = 240,
    MIDI_MESSAGE_QUARTER_FRAME = 241,
    MIDI_MESSAGE_SONG_POSITION_POINTER = 242,
    MIDI_MESSAGE_SONG_SELECT = 243,
    MIDI_MESSAGE_TUNE_REQUEST = 246,
    MIDI_MESSAGE_TIMING_CLOCK = 248,
    MIDI_MESSAGE_START = 250,
    MIDI_MESSAGE_CONTINUE = 251,
    MIDI_MESSAGE_STOP = 252,
    MIDI_MESSAGE_ACTIVE_SENSING = 254,
    MIDI_MESSAGE_SYSTEM_RESET = 255,
}
#enum Error
enum {
    OK = 0,
    FAILED = 1,
    ERR_UNAVAILABLE = 2,
    ERR_UNCONFIGURED = 3,
    ERR_UNAUTHORIZED = 4,
    ERR_PARAMETER_RANGE_ERROR = 5,
    ERR_OUT_OF_MEMORY = 6,
    ERR_FILE_NOT_FOUND = 7,
    ERR_FILE_BAD_DRIVE = 8,
    ERR_FILE_BAD_PATH = 9,
    ERR_FILE_NO_PERMISSION = 10,
    ERR_FILE_ALREADY_IN_USE = 11,
    ERR_FILE_CANT_OPEN = 12,
    ERR_FILE_CANT_WRITE = 13,
    ERR_FILE_CANT_READ = 14,
    ERR_FILE_UNRECOGNIZED = 15,
    ERR_FILE_CORRUPT = 16,
    ERR_FILE_MISSING_DEPENDENCIES = 17,
    ERR_FILE_EOF = 18,
    ERR_CANT_OPEN = 19,
    ERR_CANT_CREATE = 20,
    ERR_QUERY_FAILED = 21,
    ERR_ALREADY_IN_USE = 22,
    ERR_LOCKED = 23,
    ERR_TIMEOUT = 24,
    ERR_CANT_CONNECT = 25,
    ERR_CANT_RESOLVE = 26,
    ERR_CONNECTION_ERROR = 27,
    ERR_CANT_ACQUIRE_RESOURCE = 28,
    ERR_CANT_FORK = 29,
    ERR_INVALID_DATA = 30,
    ERR_INVALID_PARAMETER = 31,
    ERR_ALREADY_EXISTS = 32,
    ERR_DOES_NOT_EXIST = 33,
    ERR_DATABASE_CANT_READ = 34,
    ERR_DATABASE_CANT_WRITE = 35,
    ERR_COMPILATION_FAILED = 36,
    ERR_METHOD_NOT_FOUND = 37,
    ERR_LINK_FAILED = 38,
    ERR_SCRIPT_FAILED = 39,
    ERR_CYCLIC_LINK = 40,
    ERR_INVALID_DECLARATION = 41,
    ERR_DUPLICATE_SYMBOL = 42,
    ERR_PARSE_ERROR = 43,
    ERR_BUSY = 44,
    ERR_SKIP = 45,
    ERR_HELP = 46,
    ERR_BUG = 47,
    ERR_PRINTER_ON_FIRE = 48,
}
#enum PropertyHint
enum {
    PROPERTY_HINT_NONE = 0,
    PROPERTY_HINT_RANGE = 1,
    PROPERTY_HINT_ENUM = 2,
    PROPERTY_HINT_ENUM_SUGGESTION = 3,
    PROPERTY_HINT_EXP_EASING = 4,
    PROPERTY_HINT_LINK = 5,
    PROPERTY_HINT_FLAGS = 6,
    PROPERTY_HINT_LAYERS_2D_RENDER = 7,
    PROPERTY_HINT_LAYERS_2D_PHYSICS = 8,
    PROPERTY_HINT_LAYERS_2D_NAVIGATION = 9,
    PROPERTY_HINT_LAYERS_3D_RENDER = 10,
    PROPERTY_HINT_LAYERS_3D_PHYSICS = 11,
    PROPERTY_HINT_LAYERS_3D_NAVIGATION = 12,
    PROPERTY_HINT_LAYERS_AVOIDANCE = 37,
    PROPERTY_HINT_FILE = 13,
    PROPERTY_HINT_DIR = 14,
    PROPERTY_HINT_GLOBAL_FILE = 15,
    PROPERTY_HINT_GLOBAL_DIR = 16,
    PROPERTY_HINT_RESOURCE_TYPE = 17,
    PROPERTY_HINT_MULTILINE_TEXT = 18,
    PROPERTY_HINT_EXPRESSION = 19,
    PROPERTY_HINT_PLACEHOLDER_TEXT = 20,
    PROPERTY_HINT_COLOR_NO_ALPHA = 21,
    PROPERTY_HINT_OBJECT_ID = 22,
    PROPERTY_HINT_TYPE_STRING = 23,
    PROPERTY_HINT_NODE_PATH_TO_EDITED_NODE = 24,
    PROPERTY_HINT_OBJECT_TOO_BIG = 25,
    PROPERTY_HINT_NODE_PATH_VALID_TYPES = 26,
    PROPERTY_HINT_SAVE_FILE = 27,
    PROPERTY_HINT_GLOBAL_SAVE_FILE = 28,
    PROPERTY_HINT_INT_IS_OBJECTID = 29,
    PROPERTY_HINT_INT_IS_POINTER = 30,
    PROPERTY_HINT_ARRAY_TYPE = 31,
    PROPERTY_HINT_DICTIONARY_TYPE = 38,
    PROPERTY_HINT_LOCALE_ID = 32,
    PROPERTY_HINT_LOCALIZABLE_STRING = 33,
    PROPERTY_HINT_NODE_TYPE = 34,
    PROPERTY_HINT_HIDE_QUATERNION_EDIT = 35,
    PROPERTY_HINT_PASSWORD = 36,
    PROPERTY_HINT_TOOL_BUTTON = 39,
    PROPERTY_HINT_ONESHOT = 40,
    PROPERTY_HINT_GROUP_ENABLE = 42,
    PROPERTY_HINT_INPUT_NAME = 43,
    PROPERTY_HINT_MAX = 44,
}
#enum PropertyUsageFlags
enum {
    PROPERTY_USAGE_NONE = 0,
    PROPERTY_USAGE_STORAGE = 2,
    PROPERTY_USAGE_EDITOR = 4,
    PROPERTY_USAGE_INTERNAL = 8,
    PROPERTY_USAGE_CHECKABLE = 16,
    PROPERTY_USAGE_CHECKED = 32,
    PROPERTY_USAGE_GROUP = 64,
    PROPERTY_USAGE_CATEGORY = 128,
    PROPERTY_USAGE_SUBGROUP = 256,
    PROPERTY_USAGE_CLASS_IS_BITFIELD = 512,
    PROPERTY_USAGE_NO_INSTANCE_STATE = 1024,
    PROPERTY_USAGE_RESTART_IF_CHANGED = 2048,
    PROPERTY_USAGE_SCRIPT_VARIABLE = 4096,
    PROPERTY_USAGE_STORE_IF_NULL = 8192,
    PROPERTY_USAGE_UPDATE_ALL_IF_MODIFIED = 16384,
    PROPERTY_USAGE_SCRIPT_DEFAULT_VALUE = 32768,
    PROPERTY_USAGE_CLASS_IS_ENUM = 65536,
    PROPERTY_USAGE_NIL_IS_VARIANT = 131072,
    PROPERTY_USAGE_ARRAY = 262144,
    PROPERTY_USAGE_ALWAYS_DUPLICATE = 524288,
    PROPERTY_USAGE_NEVER_DUPLICATE = 1048576,
    PROPERTY_USAGE_HIGH_END_GFX = 2097152,
    PROPERTY_USAGE_NODE_PATH_FROM_SCENE_ROOT = 4194304,
    PROPERTY_USAGE_RESOURCE_NOT_PERSISTENT = 8388608,
    PROPERTY_USAGE_KEYING_INCREMENTS = 16777216,
    PROPERTY_USAGE_DEFERRED_SET_RESOURCE = 33554432,
    PROPERTY_USAGE_EDITOR_INSTANTIATE_OBJECT = 67108864,
    PROPERTY_USAGE_EDITOR_BASIC_SETTING = 134217728,
    PROPERTY_USAGE_READ_ONLY = 268435456,
    PROPERTY_USAGE_SECRET = 536870912,
    PROPERTY_USAGE_DEFAULT = 6,
    PROPERTY_USAGE_NO_EDITOR = 2,
}
#enum MethodFlags
enum {
    METHOD_FLAG_NORMAL = 1,
    METHOD_FLAG_EDITOR = 2,
    METHOD_FLAG_CONST = 4,
    METHOD_FLAG_VIRTUAL = 8,
    METHOD_FLAG_VARARG = 16,
    METHOD_FLAG_STATIC = 32,
    METHOD_FLAG_OBJECT_CORE = 64,
    METHOD_FLAG_VIRTUAL_REQUIRED = 128,
    METHOD_FLAGS_DEFAULT = 1,
}
#enum Variant.Type
enum {
    TYPE_NIL = 0,
    TYPE_BOOL = 1,
    TYPE_INT = 2,
    TYPE_FLOAT = 3,
    TYPE_STRING = 4,
    TYPE_VECTOR2 = 5,
    TYPE_VECTOR2I = 6,
    TYPE_RECT2 = 7,
    TYPE_RECT2I = 8,
    TYPE_VECTOR3 = 9,
    TYPE_VECTOR3I = 10,
    TYPE_TRANSFORM2D = 11,
    TYPE_VECTOR4 = 12,
    TYPE_VECTOR4I = 13,
    TYPE_PLANE = 14,
    TYPE_QUATERNION = 15,
    TYPE_AABB = 16,
    TYPE_BASIS = 17,
    TYPE_TRANSFORM3D = 18,
    TYPE_PROJECTION = 19,
    TYPE_COLOR = 20,
    TYPE_STRING_NAME = 21,
    TYPE_NODE_PATH = 22,
    TYPE_RID = 23,
    TYPE_OBJECT = 24,
    TYPE_CALLABLE = 25,
    TYPE_SIGNAL = 26,
    TYPE_DICTIONARY = 27,
    TYPE_ARRAY = 28,
    TYPE_PACKED_BYTE_ARRAY = 29,
    TYPE_PACKED_INT32_ARRAY = 30,
    TYPE_PACKED_INT64_ARRAY = 31,
    TYPE_PACKED_FLOAT32_ARRAY = 32,
    TYPE_PACKED_FLOAT64_ARRAY = 33,
    TYPE_PACKED_STRING_ARRAY = 34,
    TYPE_PACKED_VECTOR2_ARRAY = 35,
    TYPE_PACKED_VECTOR3_ARRAY = 36,
    TYPE_PACKED_COLOR_ARRAY = 37,
    TYPE_PACKED_VECTOR4_ARRAY = 38,
    TYPE_MAX = 39,
}
#enum Variant.Operator
enum {
    OP_EQUAL = 0,
    OP_NOT_EQUAL = 1,
    OP_LESS = 2,
    OP_LESS_EQUAL = 3,
    OP_GREATER = 4,
    OP_GREATER_EQUAL = 5,
    OP_ADD = 6,
    OP_SUBTRACT = 7,
    OP_MULTIPLY = 8,
    OP_DIVIDE = 9,
    OP_NEGATE = 10,
    OP_POSITIVE = 11,
    OP_MODULE = 12,
    OP_POWER = 13,
    OP_SHIFT_LEFT = 14,
    OP_SHIFT_RIGHT = 15,
    OP_BIT_AND = 16,
    OP_BIT_OR = 17,
    OP_BIT_XOR = 18,
    OP_BIT_NEGATE = 19,
    OP_AND = 20,
    OP_OR = 21,
    OP_XOR = 22,
    OP_NOT = 23,
    OP_IN = 24,
    OP_MAX = 25,
}
#enum Side
enum Side {
    SIDE_LEFT = 0,
    SIDE_TOP = 1,
    SIDE_RIGHT = 2,
    SIDE_BOTTOM = 3,
}
#enum Corner
enum Corner {
    CORNER_TOP_LEFT = 0,
    CORNER_TOP_RIGHT = 1,
    CORNER_BOTTOM_RIGHT = 2,
    CORNER_BOTTOM_LEFT = 3,
}
#enum Orientation
enum Orientation {
    VERTICAL = 1,
    HORIZONTAL = 0,
}
#enum ClockDirection
enum ClockDirection {
    CLOCKWISE = 0,
    COUNTERCLOCKWISE = 1,
}
#enum HorizontalAlignment
enum HorizontalAlignment {
    HORIZONTAL_ALIGNMENT_LEFT = 0,
    HORIZONTAL_ALIGNMENT_CENTER = 1,
    HORIZONTAL_ALIGNMENT_RIGHT = 2,
    HORIZONTAL_ALIGNMENT_FILL = 3,
}
#enum VerticalAlignment
enum VerticalAlignment {
    VERTICAL_ALIGNMENT_TOP = 0,
    VERTICAL_ALIGNMENT_CENTER = 1,
    VERTICAL_ALIGNMENT_BOTTOM = 2,
    VERTICAL_ALIGNMENT_FILL = 3,
}
#enum InlineAlignment
enum InlineAlignment {
    INLINE_ALIGNMENT_TOP_TO = 0,
    INLINE_ALIGNMENT_CENTER_TO = 1,
    INLINE_ALIGNMENT_BASELINE_TO = 3,
    INLINE_ALIGNMENT_BOTTOM_TO = 2,
    INLINE_ALIGNMENT_TO_TOP = 0,
    INLINE_ALIGNMENT_TO_CENTER = 4,
    INLINE_ALIGNMENT_TO_BASELINE = 8,
    INLINE_ALIGNMENT_TO_BOTTOM = 12,
    INLINE_ALIGNMENT_TOP = 0,
    INLINE_ALIGNMENT_CENTER = 5,
    INLINE_ALIGNMENT_BOTTOM = 14,
    INLINE_ALIGNMENT_IMAGE_MASK = 3,
    INLINE_ALIGNMENT_TEXT_MASK = 12,
}
#enum EulerOrder
enum EulerOrder {
    EULER_ORDER_XYZ = 0,
    EULER_ORDER_XZY = 1,
    EULER_ORDER_YXZ = 2,
    EULER_ORDER_YZX = 3,
    EULER_ORDER_ZXY = 4,
    EULER_ORDER_ZYX = 5,
}
#enum Key
enum Key {
    KEY_NONE = 0,
    KEY_SPECIAL = 4194304,
    KEY_ESCAPE = 4194305,
    KEY_TAB = 4194306,
    KEY_BACKTAB = 4194307,
    KEY_BACKSPACE = 4194308,
    KEY_ENTER = 4194309,
    KEY_KP_ENTER = 4194310,
    KEY_INSERT = 4194311,
    KEY_DELETE = 4194312,
    KEY_PAUSE = 4194313,
    KEY_PRINT = 4194314,
    KEY_SYSREQ = 4194315,
    KEY_CLEAR = 4194316,
    KEY_HOME = 4194317,
    KEY_END = 4194318,
    KEY_LEFT = 4194319,
    KEY_UP = 4194320,
    KEY_RIGHT = 4194321,
    KEY_DOWN = 4194322,
    KEY_PAGEUP = 4194323,
    KEY_PAGEDOWN = 4194324,
    KEY_SHIFT = 4194325,
    KEY_CTRL = 4194326,
    KEY_META = 4194327,
    KEY_ALT = 4194328,
    KEY_CAPSLOCK = 4194329,
    KEY_NUMLOCK = 4194330,
    KEY_SCROLLLOCK = 4194331,
    KEY_F1 = 4194332,
    KEY_F2 = 4194333,
    KEY_F3 = 4194334,
    KEY_F4 = 4194335,
    KEY_F5 = 4194336,
    KEY_F6 = 4194337,
    KEY_F7 = 4194338,
    KEY_F8 = 4194339,
    KEY_F9 = 4194340,
    KEY_F10 = 4194341,
    KEY_F11 = 4194342,
    KEY_F12 = 4194343,
    KEY_F13 = 4194344,
    KEY_F14 = 4194345,
    KEY_F15 = 4194346,
    KEY_F16 = 4194347,
    KEY_F17 = 4194348,
    KEY_F18 = 4194349,
    KEY_F19 = 4194350,
    KEY_F20 = 4194351,
    KEY_F21 = 4194352,
    KEY_F22 = 4194353,
    KEY_F23 = 4194354,
    KEY_F24 = 4194355,
    KEY_F25 = 4194356,
    KEY_F26 = 4194357,
    KEY_F27 = 4194358,
    KEY_F28 = 4194359,
    KEY_F29 = 4194360,
    KEY_F30 = 4194361,
    KEY_F31 = 4194362,
    KEY_F32 = 4194363,
    KEY_F33 = 4194364,
    KEY_F34 = 4194365,
    KEY_F35 = 4194366,
    KEY_KP_MULTIPLY = 4194433,
    KEY_KP_DIVIDE = 4194434,
    KEY_KP_SUBTRACT = 4194435,
    KEY_KP_PERIOD = 4194436,
    KEY_KP_ADD = 4194437,
    KEY_KP_0 = 4194438,
    KEY_KP_1 = 4194439,
    KEY_KP_2 = 4194440,
    KEY_KP_3 = 4194441,
    KEY_KP_4 = 4194442,
    KEY_KP_5 = 4194443,
    KEY_KP_6 = 4194444,
    KEY_KP_7 = 4194445,
    KEY_KP_8 = 4194446,
    KEY_KP_9 = 4194447,
    KEY_MENU = 4194370,
    KEY_HYPER = 4194371,
    KEY_HELP = 4194373,
    KEY_BACK = 4194376,
    KEY_FORWARD = 4194377,
    KEY_STOP = 4194378,
    KEY_REFRESH = 4194379,
    KEY_VOLUMEDOWN = 4194380,
    KEY_VOLUMEMUTE = 4194381,
    KEY_VOLUMEUP = 4194382,
    KEY_MEDIAPLAY = 4194388,
    KEY_MEDIASTOP = 4194389,
    KEY_MEDIAPREVIOUS = 4194390,
    KEY_MEDIANEXT = 4194391,
    KEY_MEDIARECORD = 4194392,
    KEY_HOMEPAGE = 4194393,
    KEY_FAVORITES = 4194394,
    KEY_SEARCH = 4194395,
    KEY_STANDBY = 4194396,
    KEY_OPENURL = 4194397,
    KEY_LAUNCHMAIL = 4194398,
    KEY_LAUNCHMEDIA = 4194399,
    KEY_LAUNCH0 = 4194400,
    KEY_LAUNCH1 = 4194401,
    KEY_LAUNCH2 = 4194402,
    KEY_LAUNCH3 = 4194403,
    KEY_LAUNCH4 = 4194404,
    KEY_LAUNCH5 = 4194405,
    KEY_LAUNCH6 = 4194406,
    KEY_LAUNCH7 = 4194407,
    KEY_LAUNCH8 = 4194408,
    KEY_LAUNCH9 = 4194409,
    KEY_LAUNCHA = 4194410,
    KEY_LAUNCHB = 4194411,
    KEY_LAUNCHC = 4194412,
    KEY_LAUNCHD = 4194413,
    KEY_LAUNCHE = 4194414,
    KEY_LAUNCHF = 4194415,
    KEY_GLOBE = 4194416,
    KEY_KEYBOARD = 4194417,
    KEY_JIS_EISU = 4194418,
    KEY_JIS_KANA = 4194419,
    KEY_UNKNOWN = 8388607,
    KEY_SPACE = 32,
    KEY_EXCLAM = 33,
    KEY_QUOTEDBL = 34,
    KEY_NUMBERSIGN = 35,
    KEY_DOLLAR = 36,
    KEY_PERCENT = 37,
    KEY_AMPERSAND = 38,
    KEY_APOSTROPHE = 39,
    KEY_PARENLEFT = 40,
    KEY_PARENRIGHT = 41,
    KEY_ASTERISK = 42,
    KEY_PLUS = 43,
    KEY_COMMA = 44,
    KEY_MINUS = 45,
    KEY_PERIOD = 46,
    KEY_SLASH = 47,
    KEY_0 = 48,
    KEY_1 = 49,
    KEY_2 = 50,
    KEY_3 = 51,
    KEY_4 = 52,
    KEY_5 = 53,
    KEY_6 = 54,
    KEY_7 = 55,
    KEY_8 = 56,
    KEY_9 = 57,
    KEY_COLON = 58,
    KEY_SEMICOLON = 59,
    KEY_LESS = 60,
    KEY_EQUAL = 61,
    KEY_GREATER = 62,
    KEY_QUESTION = 63,
    KEY_AT = 64,
    KEY_A = 65,
    KEY_B = 66,
    KEY_C = 67,
    KEY_D = 68,
    KEY_E = 69,
    KEY_F = 70,
    KEY_G = 71,
    KEY_H = 72,
    KEY_I = 73,
    KEY_J = 74,
    KEY_K = 75,
    KEY_L = 76,
    KEY_M = 77,
    KEY_N = 78,
    KEY_O = 79,
    KEY_P = 80,
    KEY_Q = 81,
    KEY_R = 82,
    KEY_S = 83,
    KEY_T = 84,
    KEY_U = 85,
    KEY_V = 86,
    KEY_W = 87,
    KEY_X = 88,
    KEY_Y = 89,
    KEY_Z = 90,
    KEY_BRACKETLEFT = 91,
    KEY_BACKSLASH = 92,
    KEY_BRACKETRIGHT = 93,
    KEY_ASCIICIRCUM = 94,
    KEY_UNDERSCORE = 95,
    KEY_QUOTELEFT = 96,
    KEY_BRACELEFT = 123,
    KEY_BAR = 124,
    KEY_BRACERIGHT = 125,
    KEY_ASCIITILDE = 126,
    KEY_YEN = 165,
    KEY_SECTION = 167,
}
#enum KeyModifierMask
enum KeyModifierMask {
    KEY_CODE_MASK = 8388607,
    KEY_MODIFIER_MASK = 2130706432,
    KEY_MASK_CMD_OR_CTRL = 16777216,
    KEY_MASK_SHIFT = 33554432,
    KEY_MASK_ALT = 67108864,
    KEY_MASK_META = 134217728,
    KEY_MASK_CTRL = 268435456,
    KEY_MASK_KPAD = 536870912,
    KEY_MASK_GROUP_SWITCH = 1073741824,
}
#enum KeyLocation
enum KeyLocation {
    KEY_LOCATION_UNSPECIFIED = 0,
    KEY_LOCATION_LEFT = 1,
    KEY_LOCATION_RIGHT = 2,
}
#enum MouseButton
enum MouseButton {
    MOUSE_BUTTON_NONE = 0,
    MOUSE_BUTTON_LEFT = 1,
    MOUSE_BUTTON_RIGHT = 2,
    MOUSE_BUTTON_MIDDLE = 3,
    MOUSE_BUTTON_WHEEL_UP = 4,
    MOUSE_BUTTON_WHEEL_DOWN = 5,
    MOUSE_BUTTON_WHEEL_LEFT = 6,
    MOUSE_BUTTON_WHEEL_RIGHT = 7,
    MOUSE_BUTTON_XBUTTON1 = 8,
    MOUSE_BUTTON_XBUTTON2 = 9,
}
#enum MouseButtonMask
enum MouseButtonMask {
    MOUSE_BUTTON_MASK_LEFT = 1,
    MOUSE_BUTTON_MASK_RIGHT = 2,
    MOUSE_BUTTON_MASK_MIDDLE = 4,
    MOUSE_BUTTON_MASK_MB_XBUTTON1 = 128,
    MOUSE_BUTTON_MASK_MB_XBUTTON2 = 256,
}
#enum JoyButton
enum JoyButton {
    JOY_BUTTON_INVALID = -1,
    JOY_BUTTON_A = 0,
    JOY_BUTTON_B = 1,
    JOY_BUTTON_X = 2,
    JOY_BUTTON_Y = 3,
    JOY_BUTTON_BACK = 4,
    JOY_BUTTON_GUIDE = 5,
    JOY_BUTTON_START = 6,
    JOY_BUTTON_LEFT_STICK = 7,
    JOY_BUTTON_RIGHT_STICK = 8,
    JOY_BUTTON_LEFT_SHOULDER = 9,
    JOY_BUTTON_RIGHT_SHOULDER = 10,
    JOY_BUTTON_DPAD_UP = 11,
    JOY_BUTTON_DPAD_DOWN = 12,
    JOY_BUTTON_DPAD_LEFT = 13,
    JOY_BUTTON_DPAD_RIGHT = 14,
    JOY_BUTTON_MISC1 = 15,
    JOY_BUTTON_PADDLE1 = 16,
    JOY_BUTTON_PADDLE2 = 17,
    JOY_BUTTON_PADDLE3 = 18,
    JOY_BUTTON_PADDLE4 = 19,
    JOY_BUTTON_TOUCHPAD = 20,
    JOY_BUTTON_SDL_MAX = 21,
    JOY_BUTTON_MAX = 128,
}
#enum JoyAxis
enum JoyAxis {
    JOY_AXIS_INVALID = -1,
    JOY_AXIS_LEFT_X = 0,
    JOY_AXIS_LEFT_Y = 1,
    JOY_AXIS_RIGHT_X = 2,
    JOY_AXIS_RIGHT_Y = 3,
    JOY_AXIS_TRIGGER_LEFT = 4,
    JOY_AXIS_TRIGGER_RIGHT = 5,
    JOY_AXIS_SDL_MAX = 6,
    JOY_AXIS_MAX = 10,
}
#enum MIDIMessage
enum MIDIMessage {
    MIDI_MESSAGE_NONE = 0,
    MIDI_MESSAGE_NOTE_OFF = 8,
    MIDI_MESSAGE_NOTE_ON = 9,
    MIDI_MESSAGE_AFTERTOUCH = 10,
    MIDI_MESSAGE_CONTROL_CHANGE = 11,
    MIDI_MESSAGE_PROGRAM_CHANGE = 12,
    MIDI_MESSAGE_CHANNEL_PRESSURE = 13,
    MIDI_MESSAGE_PITCH_BEND = 14,
    MIDI_MESSAGE_SYSTEM_EXCLUSIVE = 240,
    MIDI_MESSAGE_QUARTER_FRAME = 241,
    MIDI_MESSAGE_SONG_POSITION_POINTER = 242,
    MIDI_MESSAGE_SONG_SELECT = 243,
    MIDI_MESSAGE_TUNE_REQUEST = 246,
    MIDI_MESSAGE_TIMING_CLOCK = 248,
    MIDI_MESSAGE_START = 250,
    MIDI_MESSAGE_CONTINUE = 251,
    MIDI_MESSAGE_STOP = 252,
    MIDI_MESSAGE_ACTIVE_SENSING = 254,
    MIDI_MESSAGE_SYSTEM_RESET = 255,
}
#enum Error
enum Error {
    OK = 0,
    FAILED = 1,
    ERR_UNAVAILABLE = 2,
    ERR_UNCONFIGURED = 3,
    ERR_UNAUTHORIZED = 4,
    ERR_PARAMETER_RANGE_ERROR = 5,
    ERR_OUT_OF_MEMORY = 6,
    ERR_FILE_NOT_FOUND = 7,
    ERR_FILE_BAD_DRIVE = 8,
    ERR_FILE_BAD_PATH = 9,
    ERR_FILE_NO_PERMISSION = 10,
    ERR_FILE_ALREADY_IN_USE = 11,
    ERR_FILE_CANT_OPEN = 12,
    ERR_FILE_CANT_WRITE = 13,
    ERR_FILE_CANT_READ = 14,
    ERR_FILE_UNRECOGNIZED = 15,
    ERR_FILE_CORRUPT = 16,
    ERR_FILE_MISSING_DEPENDENCIES = 17,
    ERR_FILE_EOF = 18,
    ERR_CANT_OPEN = 19,
    ERR_CANT_CREATE = 20,
    ERR_QUERY_FAILED = 21,
    ERR_ALREADY_IN_USE = 22,
    ERR_LOCKED = 23,
    ERR_TIMEOUT = 24,
    ERR_CANT_CONNECT = 25,
    ERR_CANT_RESOLVE = 26,
    ERR_CONNECTION_ERROR = 27,
    ERR_CANT_ACQUIRE_RESOURCE = 28,
    ERR_CANT_FORK = 29,
    ERR_INVALID_DATA = 30,
    ERR_INVALID_PARAMETER = 31,
    ERR_ALREADY_EXISTS = 32,
    ERR_DOES_NOT_EXIST = 33,
    ERR_DATABASE_CANT_READ = 34,
    ERR_DATABASE_CANT_WRITE = 35,
    ERR_COMPILATION_FAILED = 36,
    ERR_METHOD_NOT_FOUND = 37,
    ERR_LINK_FAILED = 38,
    ERR_SCRIPT_FAILED = 39,
    ERR_CYCLIC_LINK = 40,
    ERR_INVALID_DECLARATION = 41,
    ERR_DUPLICATE_SYMBOL = 42,
    ERR_PARSE_ERROR = 43,
    ERR_BUSY = 44,
    ERR_SKIP = 45,
    ERR_HELP = 46,
    ERR_BUG = 47,
    ERR_PRINTER_ON_FIRE = 48,
}
#enum PropertyHint
enum PropertyHint {
    PROPERTY_HINT_NONE = 0,
    PROPERTY_HINT_RANGE = 1,
    PROPERTY_HINT_ENUM = 2,
    PROPERTY_HINT_ENUM_SUGGESTION = 3,
    PROPERTY_HINT_EXP_EASING = 4,
    PROPERTY_HINT_LINK = 5,
    PROPERTY_HINT_FLAGS = 6,
    PROPERTY_HINT_LAYERS_2D_RENDER = 7,
    PROPERTY_HINT_LAYERS_2D_PHYSICS = 8,
    PROPERTY_HINT_LAYERS_2D_NAVIGATION = 9,
    PROPERTY_HINT_LAYERS_3D_RENDER = 10,
    PROPERTY_HINT_LAYERS_3D_PHYSICS = 11,
    PROPERTY_HINT_LAYERS_3D_NAVIGATION = 12,
    PROPERTY_HINT_LAYERS_AVOIDANCE = 37,
    PROPERTY_HINT_FILE = 13,
    PROPERTY_HINT_DIR = 14,
    PROPERTY_HINT_GLOBAL_FILE = 15,
    PROPERTY_HINT_GLOBAL_DIR = 16,
    PROPERTY_HINT_RESOURCE_TYPE = 17,
    PROPERTY_HINT_MULTILINE_TEXT = 18,
    PROPERTY_HINT_EXPRESSION = 19,
    PROPERTY_HINT_PLACEHOLDER_TEXT = 20,
    PROPERTY_HINT_COLOR_NO_ALPHA = 21,
    PROPERTY_HINT_OBJECT_ID = 22,
    PROPERTY_HINT_TYPE_STRING = 23,
    PROPERTY_HINT_NODE_PATH_TO_EDITED_NODE = 24,
    PROPERTY_HINT_OBJECT_TOO_BIG = 25,
    PROPERTY_HINT_NODE_PATH_VALID_TYPES = 26,
    PROPERTY_HINT_SAVE_FILE = 27,
    PROPERTY_HINT_GLOBAL_SAVE_FILE = 28,
    PROPERTY_HINT_INT_IS_OBJECTID = 29,
    PROPERTY_HINT_INT_IS_POINTER = 30,
    PROPERTY_HINT_ARRAY_TYPE = 31,
    PROPERTY_HINT_DICTIONARY_TYPE = 38,
    PROPERTY_HINT_LOCALE_ID = 32,
    PROPERTY_HINT_LOCALIZABLE_STRING = 33,
    PROPERTY_HINT_NODE_TYPE = 34,
    PROPERTY_HINT_HIDE_QUATERNION_EDIT = 35,
    PROPERTY_HINT_PASSWORD = 36,
    PROPERTY_HINT_TOOL_BUTTON = 39,
    PROPERTY_HINT_ONESHOT = 40,
    PROPERTY_HINT_GROUP_ENABLE = 42,
    PROPERTY_HINT_INPUT_NAME = 43,
    PROPERTY_HINT_MAX = 44,
}
#enum PropertyUsageFlags
enum PropertyUsageFlags {
    PROPERTY_USAGE_NONE = 0,
    PROPERTY_USAGE_STORAGE = 2,
    PROPERTY_USAGE_EDITOR = 4,
    PROPERTY_USAGE_INTERNAL = 8,
    PROPERTY_USAGE_CHECKABLE = 16,
    PROPERTY_USAGE_CHECKED = 32,
    PROPERTY_USAGE_GROUP = 64,
    PROPERTY_USAGE_CATEGORY = 128,
    PROPERTY_USAGE_SUBGROUP = 256,
    PROPERTY_USAGE_CLASS_IS_BITFIELD = 512,
    PROPERTY_USAGE_NO_INSTANCE_STATE = 1024,
    PROPERTY_USAGE_RESTART_IF_CHANGED = 2048,
    PROPERTY_USAGE_SCRIPT_VARIABLE = 4096,
    PROPERTY_USAGE_STORE_IF_NULL = 8192,
    PROPERTY_USAGE_UPDATE_ALL_IF_MODIFIED = 16384,
    PROPERTY_USAGE_SCRIPT_DEFAULT_VALUE = 32768,
    PROPERTY_USAGE_CLASS_IS_ENUM = 65536,
    PROPERTY_USAGE_NIL_IS_VARIANT = 131072,
    PROPERTY_USAGE_ARRAY = 262144,
    PROPERTY_USAGE_ALWAYS_DUPLICATE = 524288,
    PROPERTY_USAGE_NEVER_DUPLICATE = 1048576,
    PROPERTY_USAGE_HIGH_END_GFX = 2097152,
    PROPERTY_USAGE_NODE_PATH_FROM_SCENE_ROOT = 4194304,
    PROPERTY_USAGE_RESOURCE_NOT_PERSISTENT = 8388608,
    PROPERTY_USAGE_KEYING_INCREMENTS = 16777216,
    PROPERTY_USAGE_DEFERRED_SET_RESOURCE = 33554432,
    PROPERTY_USAGE_EDITOR_INSTANTIATE_OBJECT = 67108864,
    PROPERTY_USAGE_EDITOR_BASIC_SETTING = 134217728,
    PROPERTY_USAGE_READ_ONLY = 268435456,
    PROPERTY_USAGE_SECRET = 536870912,
    PROPERTY_USAGE_DEFAULT = 6,
    PROPERTY_USAGE_NO_EDITOR = 2,
}
#enum MethodFlags
enum MethodFlags {
    METHOD_FLAG_NORMAL = 1,
    METHOD_FLAG_EDITOR = 2,
    METHOD_FLAG_CONST = 4,
    METHOD_FLAG_VIRTUAL = 8,
    METHOD_FLAG_VARARG = 16,
    METHOD_FLAG_STATIC = 32,
    METHOD_FLAG_OBJECT_CORE = 64,
    METHOD_FLAG_VIRTUAL_REQUIRED = 128,
    METHOD_FLAGS_DEFAULT = 1,
}
## The [AudioServer] singleton.
var AudioServer: AudioServer

## The [CameraServer] singleton.
var CameraServer: CameraServer

## The [ClassDB] singleton.
var ClassDB: ClassDB

## The [DisplayServer] singleton.
var DisplayServer: DisplayServer

## The [EditorInterface] singleton.
## [b]Note:[/b] Only available in editor builds.
var EditorInterface: EditorInterface

## The [Engine] singleton.
var Engine: Engine

## The [EngineDebugger] singleton.
var EngineDebugger: EngineDebugger

## The [GDExtensionManager] singleton.
var GDExtensionManager: GDExtensionManager

## The [Geometry2D] singleton.
var Geometry2D: Geometry2D

## The [Geometry3D] singleton.
var Geometry3D: Geometry3D

## The [IP] singleton.
var IP: IP

## The [Input] singleton.
var Input: Input

## The [InputMap] singleton.
var InputMap: InputMap

## The [JavaClassWrapper] singleton.
## [b]Note:[/b] Only implemented on Android.
var JavaClassWrapper: JavaClassWrapper

## The [JavaScriptBridge] singleton.
## [b]Note:[/b] Only implemented on the Web platform.
var JavaScriptBridge: JavaScriptBridge

## The [Marshalls] singleton.
var Marshalls: Marshalls

## The [NativeMenu] singleton.
## [b]Note:[/b] Only implemented on macOS.
var NativeMenu: NativeMenu

## The [NavigationMeshGenerator] singleton.
var NavigationMeshGenerator: NavigationMeshGenerator

## The [NavigationServer2D] singleton.
var NavigationServer2D: NavigationServer2D

## The [NavigationServer3D] singleton.
var NavigationServer3D: NavigationServer3D

## The [OS] singleton.
var OS: OS

## The [Performance] singleton.
var Performance: Performance

## The [PhysicsServer2D] singleton.
var PhysicsServer2D: PhysicsServer2D

## The [PhysicsServer2DManager] singleton.
var PhysicsServer2DManager: PhysicsServer2DManager

## The [PhysicsServer3D] singleton.
var PhysicsServer3D: PhysicsServer3D

## The [PhysicsServer3DManager] singleton.
var PhysicsServer3DManager: PhysicsServer3DManager

## The [ProjectSettings] singleton.
var ProjectSettings: ProjectSettings

## The [RenderingServer] singleton.
var RenderingServer: RenderingServer

## The [ResourceLoader] singleton.
var ResourceLoader: ResourceLoader

## The [ResourceSaver] singleton.
var ResourceSaver: ResourceSaver

## The [ResourceUID] singleton.
var ResourceUID: ResourceUID

## The [TextServerManager] singleton.
var TextServerManager: TextServerManager

## The [ThemeDB] singleton.
var ThemeDB: ThemeDB

## The [Time] singleton.
var Time: Time

## The [TranslationServer] singleton.
var TranslationServer: TranslationServer

## The [WorkerThreadPool] singleton.
var WorkerThreadPool: WorkerThreadPool

## The [XRServer] singleton.
var XRServer: XRServer



## Returns the absolute value of a [Variant] parameter [param x] (i.e. non-negative value). Supported types: [int], [float], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i].
## [codeblock]
## var a = abs(-1)
## # a is 1
## var b = abs(-1.2)
## # b is 1.2
## var c = abs(Vector2(-3.5, -4))
## # c is (3.5, 4)
## var d = abs(Vector2i(-5, -6))
## # d is (5, 6)
## var e = abs(Vector3(-7, 8.5, -3.8))
## # e is (7, 8.5, 3.8)
## var f = abs(Vector3i(-7, -8, -9))
## # f is (7, 8, 9)
## [/codeblock]
## [b]Note:[/b] For better type safety, use [method absf], [method absi], [method Vector2.abs], [method Vector2i.abs], [method Vector3.abs], [method Vector3i.abs], [method Vector4.abs], or [method Vector4i.abs].
func abs(x: Variant) -> Variant:
	pass;

## Returns the absolute value of float parameter [param x] (i.e. positive value).
## [codeblock]
## # a is 1.2
## var a = absf(-1.2)
## [/codeblock]
func absf(x: float) -> float:
	pass;

## Returns the absolute value of int parameter [param x] (i.e. positive value).
## [codeblock]
## # a is 1
## var a = absi(-1)
## [/codeblock]
func absi(x: int) -> int:
	pass;

## Returns the arc cosine of [param x] in radians. Use to get the angle of cosine [param x]. [param x] will be clamped between [code]-1.0[/code] and [code]1.0[/code] (inclusive), in order to prevent [method acos] from returning [constant @GDScript.NAN].
## [codeblock]
## # c is 0.523599 or 30 degrees if converted with rad_to_deg(c)
## var c = acos(0.866025)
## [/codeblock]
func acos(x: float) -> float:
	pass;

## Returns the hyperbolic arc (also called inverse) cosine of [param x], returning a value in radians. Use it to get the angle from an angle's cosine in hyperbolic space if [param x] is larger or equal to 1. For values of [param x] lower than 1, it will return 0, in order to prevent [method acosh] from returning [constant @GDScript.NAN].
## [codeblock]
## var a = acosh(2) # Returns 1.31695789692482
## cosh(a) # Returns 2
## var b = acosh(-1) # Returns 0
## [/codeblock]
func acosh(x: float) -> float:
	pass;

## Returns the difference between the two angles (in radians), in the range of [code][-PI, +PI][/code]. When [param from] and [param to] are opposite, returns [code]-PI[/code] if [param from] is smaller than [param to], or [code]PI[/code] otherwise.
func angle_difference(from: float, to: float) -> float:
	pass;

## Returns the arc sine of [param x] in radians. Use to get the angle of sine [param x]. [param x] will be clamped between [code]-1.0[/code] and [code]1.0[/code] (inclusive), in order to prevent [method asin] from returning [constant @GDScript.NAN].
## [codeblock]
## # s is 0.523599 or 30 degrees if converted with rad_to_deg(s)
## var s = asin(0.5)
## [/codeblock]
func asin(x: float) -> float:
	pass;

## Returns the hyperbolic arc (also called inverse) sine of [param x], returning a value in radians. Use it to get the angle from an angle's sine in hyperbolic space.
## [codeblock]
## var a = asinh(0.9) # Returns 0.8088669356527824
## sinh(a) # Returns 0.9
## [/codeblock]
func asinh(x: float) -> float:
	pass;

## Returns the arc tangent of [param x] in radians. Use it to get the angle from an angle's tangent in trigonometry.
## The method cannot know in which quadrant the angle should fall. See [method atan2] if you have both [code]y[/code] and [code skip-lint]x[/code].
## [codeblock]
## var a = atan(0.5) # a is 0.463648
## [/codeblock]
## If [param x] is between [code]-PI / 2[/code] and [code]PI / 2[/code] (inclusive), [code]atan(tan(x))[/code] is equal to [param x].
func atan(x: float) -> float:
	pass;

## Returns the arc tangent of [code]y/x[/code] in radians. Use to get the angle of tangent [code]y/x[/code]. To compute the value, the method takes into account the sign of both arguments in order to determine the quadrant.
## Important note: The Y coordinate comes first, by convention.
## [codeblock]
## var a = atan2(0, -1) # a is 3.141593
## [/codeblock]
func atan2(y: float, x: float) -> float:
	pass;

## Returns the hyperbolic arc (also called inverse) tangent of [param x], returning a value in radians. Use it to get the angle from an angle's tangent in hyperbolic space if [param x] is between -1 and 1 (non-inclusive).
## In mathematics, the inverse hyperbolic tangent is only defined for -1 < [param x] < 1 in the real set, so values equal or lower to -1 for [param x] return negative [constant @GDScript.INF] and values equal or higher than 1 return positive [constant @GDScript.INF] in order to prevent [method atanh] from returning [constant @GDScript.NAN].
## [codeblock]
## var a = atanh(0.9) # Returns 1.47221948958322
## tanh(a) # Returns 0.9
## var b = atanh(-2) # Returns -inf
## tanh(b) # Returns -1
## [/codeblock]
func atanh(x: float) -> float:
	pass;

## Returns the derivative at the given [param t] on a one-dimensional [url=https://en.wikipedia.org/wiki/B%C3%A9zier_curve]Bézier curve[/url] defined by the given [param control_1], [param control_2], and [param end] points.
func bezier_derivative(start: float, control_1: float, control_2: float, end: float, t: float) -> float:
	pass;

## Returns the point at the given [param t] on a one-dimensional [url=https://en.wikipedia.org/wiki/B%C3%A9zier_curve]Bézier curve[/url] defined by the given [param control_1], [param control_2], and [param end] points.
func bezier_interpolate(start: float, control_1: float, control_2: float, end: float, t: float) -> float:
	pass;

## Decodes a byte array back to a [Variant] value, without decoding objects.
## [b]Note:[/b] If you need object deserialization, see [method bytes_to_var_with_objects].
func bytes_to_var(bytes: PackedByteArray) -> Variant:
	pass;

## Decodes a byte array back to a [Variant] value. Decoding objects is allowed.
## [b]Warning:[/b] Deserialized object can contain code which gets executed. Do not use this option if the serialized object comes from untrusted sources to avoid potential security threats (remote code execution).
func bytes_to_var_with_objects(bytes: PackedByteArray) -> Variant:
	pass;

## Rounds [param x] upward (towards positive infinity), returning the smallest whole number that is not less than [param x]. Supported types: [int], [float], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i].
## [codeblock]
## var i = ceil(1.45) # i is 2.0
## i = ceil(1.001)    # i is 2.0
## [/codeblock]
## See also [method floor], [method round], and [method snapped].
## [b]Note:[/b] For better type safety, use [method ceilf], [method ceili], [method Vector2.ceil], [method Vector3.ceil], or [method Vector4.ceil].
func ceil(x: Variant) -> Variant:
	pass;

## Rounds [param x] upward (towards positive infinity), returning the smallest whole number that is not less than [param x].
## A type-safe version of [method ceil], returning a [float].
func ceilf(x: float) -> float:
	pass;

## Rounds [param x] upward (towards positive infinity), returning the smallest whole number that is not less than [param x].
## A type-safe version of [method ceil], returning an [int].
func ceili(x: float) -> int:
	pass;

## Clamps the [param value], returning a [Variant] not less than [param min] and not more than [param max]. Any values that can be compared with the less than and greater than operators will work.
## [codeblock]
## var a = clamp(-10, -1, 5)
## # a is -1
## var b = clamp(8.1, 0.9, 5.5)
## # b is 5.5
## [/codeblock]
## [b]Note:[/b] For better type safety, use [method clampf], [method clampi], [method Vector2.clamp], [method Vector2i.clamp], [method Vector3.clamp], [method Vector3i.clamp], [method Vector4.clamp], [method Vector4i.clamp], or [method Color.clamp] (not currently supported by this method).
## [b]Note:[/b] When using this on vectors it will [i]not[/i] perform component-wise clamping, and will pick [param min] if [code]value < min[/code] or [param max] if [code]value > max[/code]. To perform component-wise clamping use the methods listed above.
func clamp(value: Variant, min: Variant, max: Variant) -> Variant:
	pass;

## Clamps the [param value], returning a [float] not less than [param min] and not more than [param max].
## [codeblock]
## var speed = 42.1
## var a = clampf(speed, 1.0, 20.5) # a is 20.5
## speed = -10.0
## var b = clampf(speed, -1.0, 1.0) # b is -1.0
## [/codeblock]
func clampf(value: float, min: float, max: float) -> float:
	pass;

## Clamps the [param value], returning an [int] not less than [param min] and not more than [param max].
## [codeblock]
## var speed = 42
## var a = clampi(speed, 1, 20) # a is 20
## speed = -10
## var b = clampi(speed, -1, 1) # b is -1
## [/codeblock]
func clampi(value: int, min: int, max: int) -> int:
	pass;

## Returns the cosine of angle [param angle_rad] in radians.
## [codeblock]
## cos(PI * 2)         # Returns 1.0
## cos(PI)             # Returns -1.0
## cos(deg_to_rad(90)) # Returns 0.0
## [/codeblock]
func cos(angle_rad: float) -> float:
	pass;

## Returns the hyperbolic cosine of [param x] in radians.
## [codeblock]
## print(cosh(1)) # Prints 1.543081
## [/codeblock]
func cosh(x: float) -> float:
	pass;

## Cubic interpolates between two values by the factor defined in [param weight] with [param pre] and [param post] values.
func cubic_interpolate(from: float, to: float, pre: float, post: float, weight: float) -> float:
	pass;

## Cubic interpolates between two rotation values with shortest path by the factor defined in [param weight] with [param pre] and [param post] values. See also [method lerp_angle].
func cubic_interpolate_angle(from: float, to: float, pre: float, post: float, weight: float) -> float:
	pass;

## Cubic interpolates between two rotation values with shortest path by the factor defined in [param weight] with [param pre] and [param post] values. See also [method lerp_angle].
## It can perform smoother interpolation than [method cubic_interpolate] by the time values.
func cubic_interpolate_angle_in_time(from: float, to: float, pre: float, post: float, weight: float, to_t: float, pre_t: float, post_t: float) -> float:
	pass;

## Cubic interpolates between two values by the factor defined in [param weight] with [param pre] and [param post] values.
## It can perform smoother interpolation than [method cubic_interpolate] by the time values.
func cubic_interpolate_in_time(from: float, to: float, pre: float, post: float, weight: float, to_t: float, pre_t: float, post_t: float) -> float:
	pass;

## Converts from decibels to linear energy (audio).
func db_to_linear(db: float) -> float:
	pass;

## Converts an angle expressed in degrees to radians.
## [codeblock]
## var r = deg_to_rad(180) # r is 3.141593
## [/codeblock]
func deg_to_rad(deg: float) -> float:
	pass;

## Returns an "eased" value of [param x] based on an easing function defined with [param curve]. This easing function is based on an exponent. The [param curve] can be any floating-point number, with specific values leading to the following behaviors:
## [codeblock lang=text]
## - Lower than -1.0 (exclusive): Ease in-out
## - -1.0: Linear
## - Between -1.0 and 0.0 (exclusive): Ease out-in
## - 0.0: Constant
## - Between 0.0 to 1.0 (exclusive): Ease out
## - 1.0: Linear
## - Greater than 1.0 (exclusive): Ease in
## [/codeblock]
## [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/ease_cheatsheet.png]ease() curve values cheatsheet[/url]
## See also [method smoothstep]. If you need to perform more advanced transitions, use [method Tween.interpolate_value].
func ease(x: float, curve: float) -> float:
	pass;

## Returns a human-readable name for the given [enum Error] code.
## [codeblock]
## print(OK)                              # Prints 0
## print(error_string(OK))                # Prints "OK"
## print(error_string(ERR_BUSY))          # Prints "Busy"
## print(error_string(ERR_OUT_OF_MEMORY)) # Prints "Out of memory"
## [/codeblock]
func error_string(error: int) -> String:
	pass;

## The natural exponential function. It raises the mathematical constant [i]e[/i] to the power of [param x] and returns it.
## [i]e[/i] has an approximate value of 2.71828, and can be obtained with [code]exp(1)[/code].
## For exponents to other bases use the method [method pow].
## [codeblock]
## var a = exp(2) # Approximately 7.39
## [/codeblock]
func exp(x: float) -> float:
	pass;

## Rounds [param x] downward (towards negative infinity), returning the largest whole number that is not more than [param x]. Supported types: [int], [float], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i].
## [codeblock]
## var a = floor(2.99) # a is 2.0
## a = floor(-2.99)    # a is -3.0
## [/codeblock]
## See also [method ceil], [method round], and [method snapped].
## [b]Note:[/b] For better type safety, use [method floorf], [method floori], [method Vector2.floor], [method Vector3.floor], or [method Vector4.floor].
func floor(x: Variant) -> Variant:
	pass;

## Rounds [param x] downward (towards negative infinity), returning the largest whole number that is not more than [param x].
## A type-safe version of [method floor], returning a [float].
func floorf(x: float) -> float:
	pass;

## Rounds [param x] downward (towards negative infinity), returning the largest whole number that is not more than [param x].
## A type-safe version of [method floor], returning an [int].
## [b]Note:[/b] This function is [i]not[/i] the same as [code]int(x)[/code], which rounds towards 0.
func floori(x: float) -> int:
	pass;

## Returns the floating-point remainder of [param x] divided by [param y], keeping the sign of [param x].
## [codeblock]
## var remainder = fmod(7, 5.5) # remainder is 1.5
## [/codeblock]
## For the integer remainder operation, use the [code]%[/code] operator.
func fmod(x: float, y: float) -> float:
	pass;

## Returns the floating-point modulus of [param x] divided by [param y], wrapping equally in positive and negative.
## [codeblock]
## print(" (x)  (fmod(x, 1.5))   (fposmod(x, 1.5))")
## for i in 7:
## var x = i * 0.5 - 1.5
## print("%4.1f           %4.1f  | %4.1f" % [x, fmod(x, 1.5), fposmod(x, 1.5)])
## [/codeblock]
## Prints:
## [codeblock lang=text]
## (x)  (fmod(x, 1.5))   (fposmod(x, 1.5))
## -1.5           -0.0  |  0.0
## -1.0           -1.0  |  0.5
## -0.5           -0.5  |  1.0
## 0.0            0.0  |  0.0
## 0.5            0.5  |  0.5
## 1.0            1.0  |  1.0
## 1.5            0.0  |  0.0
## [/codeblock]
func fposmod(x: float, y: float) -> float:
	pass;

## Returns the integer hash of the passed [param variable].
## [codeblocks]
## [gdscript]
## print(hash("a")) # Prints 177670
## [/gdscript]
## [csharp]
## GD.Print(GD.Hash("a")); // Prints 177670
## [/csharp]
## [/codeblocks]
func hash(variable: Variant) -> int:
	pass;

## Returns the [Object] that corresponds to [param instance_id]. All Objects have a unique instance ID. See also [method Object.get_instance_id].
## [codeblocks]
## [gdscript]
## var drink = "water"
## func _ready():
## var id = get_instance_id()
## var instance = instance_from_id(id)
## print(instance.foo) # Prints "water"
## [/gdscript]
## [csharp]
## public partial class MyNode : Node
## {
## public string Drink { get; set; } = "water";
## public override void _Ready()
## {
## ulong id = GetInstanceId();
## var instance = (MyNode)InstanceFromId(Id);
## GD.Print(instance.Drink); // Prints "water"
## }
## }
## [/csharp]
## [/codeblocks]
func instance_from_id(instance_id: int) -> Object:
	pass;

## Returns an interpolation or extrapolation factor considering the range specified in [param from] and [param to], and the interpolated value specified in [param weight]. The returned value will be between [code]0.0[/code] and [code]1.0[/code] if [param weight] is between [param from] and [param to] (inclusive). If [param weight] is located outside this range, then an extrapolation factor will be returned (return value lower than [code]0.0[/code] or greater than [code]1.0[/code]). Use [method clamp] on the result of [method inverse_lerp] if this is not desired.
## [codeblock]
## # The interpolation ratio in the `lerp()` call below is 0.75.
## var middle = lerp(20, 30, 0.75)
## # middle is now 27.5.
## # Now, we pretend to have forgotten the original ratio and want to get it back.
## var ratio = inverse_lerp(20, 30, 27.5)
## # ratio is now 0.75.
## [/codeblock]
## See also [method lerp], which performs the reverse of this operation, and [method remap] to map a continuous series of values to another.
func inverse_lerp(from: float, to: float, weight: float) -> float:
	pass;

## Returns [code]true[/code] if [param a] and [param b] are approximately equal to each other.
## Here, "approximately equal" means that [param a] and [param b] are within a small internal epsilon of each other, which scales with the magnitude of the numbers.
## Infinity values of the same sign are considered equal.
func is_equal_approx(a: float, b: float) -> bool:
	pass;

## Returns whether [param x] is a finite value, i.e. it is not [constant @GDScript.NAN], positive infinity, or negative infinity. See also [method is_inf] and [method is_nan].
func is_finite(x: float) -> bool:
	pass;

## Returns [code]true[/code] if [param x] is either positive infinity or negative infinity. See also [method is_finite] and [method is_nan].
func is_inf(x: float) -> bool:
	pass;

## Returns [code]true[/code] if the Object that corresponds to [param id] is a valid object (e.g. has not been deleted from memory). All Objects have a unique instance ID.
func is_instance_id_valid(id: int) -> bool:
	pass;

## Returns [code]true[/code] if [param instance] is a valid Object (e.g. has not been deleted from memory).
func is_instance_valid(instance: Variant) -> bool:
	pass;

## Returns [code]true[/code] if [param x] is a NaN ("Not a Number" or invalid) value. This method is needed as [constant @GDScript.NAN] is not equal to itself, which means [code]x == NAN[/code] can't be used to check whether a value is a NaN.
func is_nan(x: float) -> bool:
	pass;

## Returns [code]true[/code], for value types, if [param a] and [param b] share the same value. Returns [code]true[/code], for reference types, if the references of [param a] and [param b] are the same.
## [codeblock]
## # Vector2 is a value type
## var vec2_a = Vector2(0, 0)
## var vec2_b = Vector2(0, 0)
## var vec2_c = Vector2(1, 1)
## is_same(vec2_a, vec2_a)  # true
## is_same(vec2_a, vec2_b)  # true
## is_same(vec2_a, vec2_c)  # false
## # Array is a reference type
## var arr_a = []
## var arr_b = []
## is_same(arr_a, arr_a)  # true
## is_same(arr_a, arr_b)  # false
## [/codeblock]
## These are [Variant] value types: [code]null[/code], [bool], [int], [float], [String], [StringName], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i], [Rect2], [Rect2i], [Transform2D], [Transform3D], [Plane], [Quaternion], [AABB], [Basis], [Projection], [Color], [NodePath], [RID], [Callable] and [Signal].
## These are [Variant] reference types: [Object], [Dictionary], [Array], [PackedByteArray], [PackedInt32Array], [PackedInt64Array], [PackedFloat32Array], [PackedFloat64Array], [PackedStringArray], [PackedVector2Array], [PackedVector3Array], [PackedVector4Array], and [PackedColorArray].
func is_same(a: Variant, b: Variant) -> bool:
	pass;

## Returns [code]true[/code] if [param x] is zero or almost zero. The comparison is done using a tolerance calculation with a small internal epsilon.
## This function is faster than using [method is_equal_approx] with one value as zero.
func is_zero_approx(x: float) -> bool:
	pass;

## Linearly interpolates between two values by the factor defined in [param weight]. To perform interpolation, [param weight] should be between [code]0.0[/code] and [code]1.0[/code] (inclusive). However, values outside this range are allowed and can be used to perform [i]extrapolation[/i]. If this is not desired, use [method clampf] to limit [param weight].
## Both [param from] and [param to] must be the same type. Supported types: [int], [float], [Vector2], [Vector3], [Vector4], [Color], [Quaternion], [Basis], [Transform2D], [Transform3D].
## [codeblock]
## lerp(0, 4, 0.75) # Returns 3.0
## [/codeblock]
## See also [method inverse_lerp] which performs the reverse of this operation. To perform eased interpolation with [method lerp], combine it with [method ease] or [method smoothstep]. See also [method remap] to map a continuous series of values to another.
## [b]Note:[/b] For better type safety, use [method lerpf], [method Vector2.lerp], [method Vector3.lerp], [method Vector4.lerp], [method Color.lerp], [method Quaternion.slerp], [method Basis.slerp], [method Transform2D.interpolate_with], or [method Transform3D.interpolate_with].
func lerp(from: Variant, to: Variant, weight: Variant) -> Variant:
	pass;

## Linearly interpolates between two angles (in radians) by a [param weight] value between 0.0 and 1.0.
## Similar to [method lerp], but interpolates correctly when the angles wrap around [constant @GDScript.TAU]. To perform eased interpolation with [method lerp_angle], combine it with [method ease] or [method smoothstep].
## [codeblock]
## extends Sprite
## var elapsed = 0.0
## func _process(delta):
## var min_angle = deg_to_rad(0.0)
## var max_angle = deg_to_rad(90.0)
## rotation = lerp_angle(min_angle, max_angle, elapsed)
## elapsed += delta
## [/codeblock]
## [b]Note:[/b] This function lerps through the shortest path between [param from] and [param to]. However, when these two angles are approximately [code]PI + k * TAU[/code] apart for any integer [code]k[/code], it's not obvious which way they lerp due to floating-point precision errors. For example, [code]lerp_angle(0, PI, weight)[/code] lerps counter-clockwise, while [code]lerp_angle(0, PI + 5 * TAU, weight)[/code] lerps clockwise.
func lerp_angle(from: float, to: float, weight: float) -> float:
	pass;

## Linearly interpolates between two values by the factor defined in [param weight]. To perform interpolation, [param weight] should be between [code]0.0[/code] and [code]1.0[/code] (inclusive). However, values outside this range are allowed and can be used to perform [i]extrapolation[/i]. If this is not desired, use [method clampf] on the result of this function.
## [codeblock]
## lerpf(0, 4, 0.75) # Returns 3.0
## [/codeblock]
## See also [method inverse_lerp] which performs the reverse of this operation. To perform eased interpolation with [method lerp], combine it with [method ease] or [method smoothstep].
func lerpf(from: float, to: float, weight: float) -> float:
	pass;

## Converts from linear energy to decibels (audio). Since volume is not normally linear, this can be used to implement volume sliders that behave as expected.
## [b]Example:[/b] Change the Master bus's volume through a [Slider] node, which ranges from [code]0.0[/code] to [code]1.0[/code]:
## [codeblock]
## AudioServer.set_bus_volume_db(AudioServer.get_bus_index("Master"), linear_to_db($Slider.value))
## [/codeblock]
func linear_to_db(lin: float) -> float:
	pass;

## Returns the [url=https://en.wikipedia.org/wiki/Natural_logarithm]natural logarithm[/url] of [param x] (base [url=https://en.wikipedia.org/wiki/E_(mathematical_constant)][i]e[/i][/url], with [i]e[/i] being approximately 2.71828). This is the amount of time needed to reach a certain level of continuous growth.
## [b]Note:[/b] This is not the same as the "log" function on most calculators, which uses a base 10 logarithm. To use base 10 logarithm, use [code]log(x) / log(10)[/code].
## [codeblock]
## log(10) # Returns 2.302585
## [/codeblock]
## [b]Note:[/b] The logarithm of [code]0[/code] returns [code]-inf[/code], while negative values return [code]-nan[/code].
func log(x: float) -> float:
	pass;

## Returns the maximum of the given numeric values. This function can take any number of arguments.
## [codeblock]
## max(1, 7, 3, -6, 5) # Returns 7
## [/codeblock]
## [b]Note:[/b] When using this on vectors it will [i]not[/i] perform component-wise maximum, and will pick the largest value when compared using [code]x < y[/code]. To perform component-wise maximum, use [method Vector2.max], [method Vector2i.max], [method Vector3.max], [method Vector3i.max], [method Vector4.max], and [method Vector4i.max].
vararg func max() -> Variant:
	pass;

## Returns the maximum of two [float] values.
## [codeblock]
## maxf(3.6, 24)   # Returns 24.0
## maxf(-3.99, -4) # Returns -3.99
## [/codeblock]
func maxf(a: float, b: float) -> float:
	pass;

## Returns the maximum of two [int] values.
## [codeblock]
## maxi(1, 2)   # Returns 2
## maxi(-3, -4) # Returns -3
## [/codeblock]
func maxi(a: int, b: int) -> int:
	pass;

## Returns the minimum of the given numeric values. This function can take any number of arguments.
## [codeblock]
## min(1, 7, 3, -6, 5) # Returns -6
## [/codeblock]
## [b]Note:[/b] When using this on vectors it will [i]not[/i] perform component-wise minimum, and will pick the smallest value when compared using [code]x < y[/code]. To perform component-wise minimum, use [method Vector2.min], [method Vector2i.min], [method Vector3.min], [method Vector3i.min], [method Vector4.min], and [method Vector4i.min].
vararg func min() -> Variant:
	pass;

## Returns the minimum of two [float] values.
## [codeblock]
## minf(3.6, 24)   # Returns 3.6
## minf(-3.99, -4) # Returns -4.0
## [/codeblock]
func minf(a: float, b: float) -> float:
	pass;

## Returns the minimum of two [int] values.
## [codeblock]
## mini(1, 2)   # Returns 1
## mini(-3, -4) # Returns -4
## [/codeblock]
func mini(a: int, b: int) -> int:
	pass;

## Moves [param from] toward [param to] by the [param delta] amount. Will not go past [param to].
## Use a negative [param delta] value to move away.
## [codeblock]
## move_toward(5, 10, 4)    # Returns 9
## move_toward(10, 5, 4)    # Returns 6
## move_toward(5, 10, 9)    # Returns 10
## move_toward(10, 5, -1.5) # Returns 11.5
## [/codeblock]
func move_toward(from: float, to: float, delta: float) -> float:
	pass;

## Returns the smallest integer power of 2 that is greater than or equal to [param value].
## [codeblock]
## nearest_po2(3) # Returns 4
## nearest_po2(4) # Returns 4
## nearest_po2(5) # Returns 8
## nearest_po2(0)  # Returns 0 (this may not be expected)
## nearest_po2(-1) # Returns 0 (this may not be expected)
## [/codeblock]
## [b]Warning:[/b] Due to its implementation, this method returns [code]0[/code] rather than [code]1[/code] for values less than or equal to [code]0[/code], with an exception for [param value] being the smallest negative 64-bit integer ([code]-9223372036854775808[/code]) in which case the [param value] is returned unchanged.
func nearest_po2(value: int) -> int:
	pass;

## Wraps [param value] between [code]0[/code] and the [param length]. If the limit is reached, the next value the function returns is decreased to the [code]0[/code] side or increased to the [param length] side (like a triangle wave). If [param length] is less than zero, it becomes positive.
## [codeblock]
## pingpong(-3.0, 3.0) # Returns 3.0
## pingpong(-2.0, 3.0) # Returns 2.0
## pingpong(-1.0, 3.0) # Returns 1.0
## pingpong(0.0, 3.0)  # Returns 0.0
## pingpong(1.0, 3.0)  # Returns 1.0
## pingpong(2.0, 3.0)  # Returns 2.0
## pingpong(3.0, 3.0)  # Returns 3.0
## pingpong(4.0, 3.0)  # Returns 2.0
## pingpong(5.0, 3.0)  # Returns 1.0
## pingpong(6.0, 3.0)  # Returns 0.0
## [/codeblock]
func pingpong(value: float, length: float) -> float:
	pass;

## Returns the integer modulus of [param x] divided by [param y] that wraps equally in positive and negative.
## [codeblock]
## print("#(i)  (i % 3)   (posmod(i, 3))")
## for i in range(-3, 4):
## print("%2d       %2d  | %2d" % [i, i % 3, posmod(i, 3)])
## [/codeblock]
## Prints:
## [codeblock lang=text]
## (i)  (i % 3)   (posmod(i, 3))
## -3        0  |  0
## -2       -2  |  1
## -1       -1  |  2
## 0        0  |  0
## 1        1  |  1
## 2        2  |  2
## 3        0  |  0
## [/codeblock]
func posmod(x: int, y: int) -> int:
	pass;

## Returns the result of [param base] raised to the power of [param exp].
## In GDScript, this is the equivalent of the [code]**[/code] operator.
## [codeblock]
## pow(2, 5)   # Returns 32.0
## pow(4, 1.5) # Returns 8.0
## [/codeblock]
func pow(base: float, exp: float) -> float:
	pass;

## Converts one or more arguments of any type to string in the best way possible and prints them to the console.
## [codeblocks]
## [gdscript]
## var a = [1, 2, 3]
## print("a", "b", a) # Prints "ab[1, 2, 3]"
## [/gdscript]
## [csharp]
## Godot.Collections.Array a = [1, 2, 3];
## GD.Print("a", "b", a); // Prints "ab[1, 2, 3]"
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] Consider using [method push_error] and [method push_warning] to print error and warning messages instead of [method print] or [method print_rich]. This distinguishes them from print messages used for debugging purposes, while also displaying a stack trace when an error or warning is printed. See also [member Engine.print_to_stdout] and [member ProjectSettings.application/run/disable_stdout].
vararg func print() -> void:
	pass;

## Converts one or more arguments of any type to string in the best way possible and prints them to the console.
## The following BBCode tags are supported: [code]b[/code], [code]i[/code], [code]u[/code], [code]s[/code], [code]indent[/code], [code]code[/code], [code]url[/code], [code]center[/code], [code]right[/code], [code]color[/code], [code]bgcolor[/code], [code]fgcolor[/code].
## URL tags only support URLs wrapped by a URL tag, not URLs with a different title.
## When printing to standard output, the supported subset of BBCode is converted to ANSI escape codes for the terminal emulator to display. Support for ANSI escape codes varies across terminal emulators, especially for italic and strikethrough. In standard output, [code]code[/code] is represented with faint text but without any font change. Unsupported tags are left as-is in standard output.
## [codeblocks]
## [gdscript skip-lint]
## print_rich("[color=green][b]Hello world![/b][/color]") # Prints "Hello world!", in green with a bold font.
## [/gdscript]
## [csharp skip-lint]
## GD.PrintRich("[color=green][b]Hello world![/b][/color]"); // Prints "Hello world!", in green with a bold font.
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] Consider using [method push_error] and [method push_warning] to print error and warning messages instead of [method print] or [method print_rich]. This distinguishes them from print messages used for debugging purposes, while also displaying a stack trace when an error or warning is printed.
## [b]Note:[/b] Output displayed in the editor supports clickable [code skip-lint][url=address]text[/url][/code] tags. The [code skip-lint][url][/code] tag's [code]address[/code] value is handled by [method OS.shell_open] when clicked.
vararg func print_rich() -> void:
	pass;

## If verbose mode is enabled ([method OS.is_stdout_verbose] returning [code]true[/code]), converts one or more arguments of any type to string in the best way possible and prints them to the console.
vararg func print_verbose() -> void:
	pass;

## Prints one or more arguments to strings in the best way possible to standard error line.
## [codeblocks]
## [gdscript]
## printerr("prints to stderr")
## [/gdscript]
## [csharp]
## GD.PrintErr("prints to stderr");
## [/csharp]
## [/codeblocks]
vararg func printerr() -> void:
	pass;

## Prints one or more arguments to strings in the best way possible to the OS terminal. Unlike [method print], no newline is automatically added at the end.
## [b]Note:[/b] The OS terminal is [i]not[/i] the same as the editor's Output dock. The output sent to the OS terminal can be seen when running Godot from a terminal. On Windows, this requires using the [code]console.exe[/code] executable.
## [codeblocks]
## [gdscript]
## # Prints "ABC" to terminal.
## printraw("A")
## printraw("B")
## printraw("C")
## [/gdscript]
## [csharp]
## // Prints "ABC" to terminal.
## GD.PrintRaw("A");
## GD.PrintRaw("B");
## GD.PrintRaw("C");
## [/csharp]
## [/codeblocks]
vararg func printraw() -> void:
	pass;

## Prints one or more arguments to the console with a space between each argument.
## [codeblocks]
## [gdscript]
## prints("A", "B", "C") # Prints "A B C"
## [/gdscript]
## [csharp]
## GD.PrintS("A", "B", "C"); // Prints "A B C"
## [/csharp]
## [/codeblocks]
vararg func prints() -> void:
	pass;

## Prints one or more arguments to the console with a tab between each argument.
## [codeblocks]
## [gdscript]
## printt("A", "B", "C") # Prints "A       B       C"
## [/gdscript]
## [csharp]
## GD.PrintT("A", "B", "C"); // Prints "A       B       C"
## [/csharp]
## [/codeblocks]
vararg func printt() -> void:
	pass;

## Pushes an error message to Godot's built-in debugger and to the OS terminal.
## [codeblocks]
## [gdscript]
## push_error("test error") # Prints "test error" to debugger and terminal as an error.
## [/gdscript]
## [csharp]
## GD.PushError("test error"); // Prints "test error" to debugger and terminal as an error.
## [/csharp]
## [/codeblocks]
## [b]Note:[/b] This function does not pause project execution. To print an error message and pause project execution in debug builds, use [code]assert(false, "test error")[/code] instead.
vararg func push_error() -> void:
	pass;

## Pushes a warning message to Godot's built-in debugger and to the OS terminal.
## [codeblocks]
## [gdscript]
## push_warning("test warning") # Prints "test warning" to debugger and terminal as a warning.
## [/gdscript]
## [csharp]
## GD.PushWarning("test warning"); // Prints "test warning" to debugger and terminal as a warning.
## [/csharp]
## [/codeblocks]
vararg func push_warning() -> void:
	pass;

## Converts an angle expressed in radians to degrees.
## [codeblock]
## rad_to_deg(0.523599) # Returns 30
## rad_to_deg(PI)       # Returns 180
## rad_to_deg(PI * 2)   # Returns 360
## [/codeblock]
func rad_to_deg(rad: float) -> float:
	pass;

## Given a [param seed], returns a [PackedInt64Array] of size [code]2[/code], where its first element is the randomized [int] value, and the second element is the same as [param seed]. Passing the same [param seed] consistently returns the same array.
## [b]Note:[/b] "Seed" here refers to the internal state of the pseudo random number generator, currently implemented as a 64 bit integer.
## [codeblock]
## var a = rand_from_seed(4)
## print(a[0]) # Prints 2879024997
## print(a[1]) # Prints 4
## [/codeblock]
func rand_from_seed(seed: int) -> PackedInt64Array:
	pass;

## Returns a random floating-point value between [code]0.0[/code] and [code]1.0[/code] (inclusive).
## [codeblocks]
## [gdscript]
## randf() # Returns e.g. 0.375671
## [/gdscript]
## [csharp]
## GD.Randf(); // Returns e.g. 0.375671
## [/csharp]
## [/codeblocks]
func randf() -> float:
	pass;

## Returns a random floating-point value between [param from] and [param to] (inclusive).
## [codeblocks]
## [gdscript]
## randf_range(0, 20.5) # Returns e.g. 7.45315
## randf_range(-10, 10) # Returns e.g. -3.844535
## [/gdscript]
## [csharp]
## GD.RandRange(0.0, 20.5);   // Returns e.g. 7.45315
## GD.RandRange(-10.0, 10.0); // Returns e.g. -3.844535
## [/csharp]
## [/codeblocks]
func randf_range(from: float, to: float) -> float:
	pass;

## Returns a [url=https://en.wikipedia.org/wiki/Normal_distribution]normally-distributed[/url], pseudo-random floating-point value from the specified [param mean] and a standard [param deviation]. This is also known as a Gaussian distribution.
## [b]Note:[/b] This method uses the [url=https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform]Box-Muller transform[/url] algorithm.
func randfn(mean: float, deviation: float) -> float:
	pass;

## Returns a random unsigned 32-bit integer. Use remainder to obtain a random value in the interval [code][0, N - 1][/code] (where N is smaller than 2^32).
## [codeblocks]
## [gdscript]
## randi()           # Returns random integer between 0 and 2^32 - 1
## randi() % 20      # Returns random integer between 0 and 19
## randi() % 100     # Returns random integer between 0 and 99
## randi() % 100 + 1 # Returns random integer between 1 and 100
## [/gdscript]
## [csharp]
## GD.Randi();           // Returns random integer between 0 and 2^32 - 1
## GD.Randi() % 20;      // Returns random integer between 0 and 19
## GD.Randi() % 100;     // Returns random integer between 0 and 99
## GD.Randi() % 100 + 1; // Returns random integer between 1 and 100
## [/csharp]
## [/codeblocks]
func randi() -> int:
	pass;

## Returns a random signed 32-bit integer between [param from] and [param to] (inclusive). If [param to] is lesser than [param from], they are swapped.
## [codeblocks]
## [gdscript]
## randi_range(0, 1)      # Returns either 0 or 1
## randi_range(-10, 1000) # Returns random integer between -10 and 1000
## [/gdscript]
## [csharp]
## GD.RandRange(0, 1);      // Returns either 0 or 1
## GD.RandRange(-10, 1000); // Returns random integer between -10 and 1000
## [/csharp]
## [/codeblocks]
func randi_range(from: int, to: int) -> int:
	pass;

## Randomizes the seed (or the internal state) of the random number generator. The current implementation uses a number based on the device's time.
## [b]Note:[/b] This function is called automatically when the project is run. If you need to fix the seed to have consistent, reproducible results, use [method seed] to initialize the random number generator.
func randomize() -> void:
	pass;

## Maps a [param value] from range [code][istart, istop][/code] to [code][ostart, ostop][/code]. See also [method lerp] and [method inverse_lerp]. If [param value] is outside [code][istart, istop][/code], then the resulting value will also be outside [code][ostart, ostop][/code]. If this is not desired, use [method clamp] on the result of this function.
## [codeblock]
## remap(75, 0, 100, -1, 1) # Returns 0.5
## [/codeblock]
## For complex use cases where multiple ranges are needed, consider using [Curve] or [Gradient] instead.
## [b]Note:[/b] If [code]istart == istop[/code], the return value is undefined (most likely NaN, INF, or -INF).
func remap(value: float, istart: float, istop: float, ostart: float, ostop: float) -> float:
	pass;

## Allocates a unique ID which can be used by the implementation to construct an RID. This is used mainly from native extensions to implement servers.
func rid_allocate_id() -> int:
	pass;

## Creates an RID from a [param base]. This is used mainly from native extensions to build servers.
func rid_from_int64(base: int) -> RID:
	pass;

## Rotates [param from] toward [param to] by the [param delta] amount. Will not go past [param to].
## Similar to [method move_toward], but interpolates correctly when the angles wrap around [constant @GDScript.TAU].
## If [param delta] is negative, this function will rotate away from [param to], toward the opposite angle, and will not go past the opposite angle.
func rotate_toward(from: float, to: float, delta: float) -> float:
	pass;

## Rounds [param x] to the nearest whole number, with halfway cases rounded away from 0. Supported types: [int], [float], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i].
## [codeblock]
## round(2.4) # Returns 2
## round(2.5) # Returns 3
## round(2.6) # Returns 3
## [/codeblock]
## See also [method floor], [method ceil], and [method snapped].
## [b]Note:[/b] For better type safety, use [method roundf], [method roundi], [method Vector2.round], [method Vector3.round], or [method Vector4.round].
func round(x: Variant) -> Variant:
	pass;

## Rounds [param x] to the nearest whole number, with halfway cases rounded away from 0.
## A type-safe version of [method round], returning a [float].
func roundf(x: float) -> float:
	pass;

## Rounds [param x] to the nearest whole number, with halfway cases rounded away from 0.
## A type-safe version of [method round], returning an [int].
func roundi(x: float) -> int:
	pass;

## Sets the seed for the random number generator to [param base]. Setting the seed manually can ensure consistent, repeatable results for most random functions.
## [codeblocks]
## [gdscript]
## var my_seed = "Godot Rocks".hash()
## seed(my_seed)
## var a = randf() + randi()
## seed(my_seed)
## var b = randf() + randi()
## # a and b are now identical
## [/gdscript]
## [csharp]
## ulong mySeed = (ulong)GD.Hash("Godot Rocks");
## GD.Seed(mySeed);
## var a = GD.Randf() + GD.Randi();
## GD.Seed(mySeed);
## var b = GD.Randf() + GD.Randi();
## // a and b are now identical
## [/csharp]
## [/codeblocks]
func seed(base: int) -> void:
	pass;

## Returns the same type of [Variant] as [param x], with [code]-1[/code] for negative values, [code]1[/code] for positive values, and [code]0[/code] for zeros. For [code]nan[/code] values it returns 0.
## Supported types: [int], [float], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i].
## [codeblock]
## sign(-6.0) # Returns -1
## sign(0.0)  # Returns 0
## sign(6.0)  # Returns 1
## sign(NAN)  # Returns 0
## sign(Vector3(-6.0, 0.0, 6.0)) # Returns (-1, 0, 1)
## [/codeblock]
## [b]Note:[/b] For better type safety, use [method signf], [method signi], [method Vector2.sign], [method Vector2i.sign], [method Vector3.sign], [method Vector3i.sign], [method Vector4.sign], or [method Vector4i.sign].
func sign(x: Variant) -> Variant:
	pass;

## Returns [code]-1.0[/code] if [param x] is negative, [code]1.0[/code] if [param x] is positive, and [code]0.0[/code] if [param x] is zero. For [code]nan[/code] values of [param x] it returns 0.0.
## [codeblock]
## signf(-6.5) # Returns -1.0
## signf(0.0)  # Returns 0.0
## signf(6.5)  # Returns 1.0
## signf(NAN)  # Returns 0.0
## [/codeblock]
func signf(x: float) -> float:
	pass;

## Returns [code]-1[/code] if [param x] is negative, [code]1[/code] if [param x] is positive, and [code]0[/code] if [param x] is zero.
## [codeblock]
## signi(-6) # Returns -1
## signi(0)  # Returns 0
## signi(6)  # Returns 1
## [/codeblock]
func signi(x: int) -> int:
	pass;

## Returns the sine of angle [param angle_rad] in radians.
## [codeblock]
## sin(0.523599)       # Returns 0.5
## sin(deg_to_rad(90)) # Returns 1.0
## [/codeblock]
func sin(angle_rad: float) -> float:
	pass;

## Returns the hyperbolic sine of [param x].
## [codeblock]
## var a = log(2.0) # Returns 0.693147
## sinh(a) # Returns 0.75
## [/codeblock]
func sinh(x: float) -> float:
	pass;

## Returns a smooth cubic Hermite interpolation between [code]0[/code] and [code]1[/code].
## For positive ranges (when [code]from <= to[/code]) the return value is [code]0[/code] when [code]x <= from[/code], and [code]1[/code] when [code]x >= to[/code]. If [param x] lies between [param from] and [param to], the return value follows an S-shaped curve that smoothly transitions from [code]0[/code] to [code]1[/code].
## For negative ranges (when [code]from > to[/code]) the function is mirrored and returns [code]1[/code] when [code]x <= to[/code] and [code]0[/code] when [code]x >= from[/code].
## This S-shaped curve is the cubic Hermite interpolator, given by [code]f(y) = 3*y^2 - 2*y^3[/code] where [code]y = (x-from) / (to-from)[/code].
## [codeblock]
## smoothstep(0, 2, -5.0) # Returns 0.0
## smoothstep(0, 2, 0.5) # Returns 0.15625
## smoothstep(0, 2, 1.0) # Returns 0.5
## smoothstep(0, 2, 2.0) # Returns 1.0
## [/codeblock]
## Compared to [method ease] with a curve value of [code]-1.6521[/code], [method smoothstep] returns the smoothest possible curve with no sudden changes in the derivative. If you need to perform more advanced transitions, use [Tween] or [AnimationPlayer].
## [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/smoothstep_ease_comparison.png]Comparison between smoothstep() and ease(x, -1.6521) return values[/url]
## [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/smoothstep_range.webp]Smoothstep() return values with positive, zero, and negative ranges[/url]
func smoothstep(from: float, to: float, x: float) -> float:
	pass;

## Returns the multiple of [param step] that is the closest to [param x]. This can also be used to round a floating-point number to an arbitrary number of decimals.
## The returned value is the same type of [Variant] as [param step]. Supported types: [int], [float], [Vector2], [Vector2i], [Vector3], [Vector3i], [Vector4], [Vector4i].
## [codeblock]
## snapped(100, 32)  # Returns 96
## snapped(3.14159, 0.01)  # Returns 3.14
## snapped(Vector2(34, 70), Vector2(8, 8))  # Returns (32, 72)
## [/codeblock]
## See also [method ceil], [method floor], and [method round].
## [b]Note:[/b] For better type safety, use [method snappedf], [method snappedi], [method Vector2.snapped], [method Vector2i.snapped], [method Vector3.snapped], [method Vector3i.snapped], [method Vector4.snapped], or [method Vector4i.snapped].
func snapped(x: Variant, step: Variant) -> Variant:
	pass;

## Returns the multiple of [param step] that is the closest to [param x]. This can also be used to round a floating-point number to an arbitrary number of decimals.
## A type-safe version of [method snapped], returning a [float].
## [codeblock]
## snappedf(32.0, 2.5)  # Returns 32.5
## snappedf(3.14159, 0.01)  # Returns 3.14
## [/codeblock]
func snappedf(x: float, step: float) -> float:
	pass;

## Returns the multiple of [param step] that is the closest to [param x].
## A type-safe version of [method snapped], returning an [int].
## [codeblock]
## snappedi(53, 16)  # Returns 48
## snappedi(4096, 100)  # Returns 4100
## [/codeblock]
func snappedi(x: float, step: int) -> int:
	pass;

## Returns the square root of [param x], where [param x] is a non-negative number.
## [codeblock]
## sqrt(9)     # Returns 3
## sqrt(10.24) # Returns 3.2
## sqrt(-1)    # Returns NaN
## [/codeblock]
## [b]Note:[/b] Negative values of [param x] return NaN ("Not a Number"). In C#, if you need negative inputs, use [code]System.Numerics.Complex[/code].
func sqrt(x: float) -> float:
	pass;

## Returns the position of the first non-zero digit, after the decimal point. Note that the maximum return value is 10, which is a design decision in the implementation.
## [codeblock]
## var n = step_decimals(5)       # n is 0
## n = step_decimals(1.0005)      # n is 4
## n = step_decimals(0.000000005) # n is 9
## [/codeblock]
func step_decimals(x: float) -> int:
	pass;

## Converts one or more arguments of any [Variant] type to a [String] in the best way possible.
## [codeblock]
## var a = [10, 20, 30]
## var b = str(a)
## print(len(a)) # Prints 3 (the number of elements in the array).
## print(len(b)) # Prints 12 (the length of the string "[10, 20, 30]").
## [/codeblock]
vararg func str() -> String:
	pass;

## Converts a formatted [param string] that was returned by [method var_to_str] to the original [Variant].
## [codeblocks]
## [gdscript]
## var data = '{ "a": 1, "b": 2 }' # data is a String
## var dict = str_to_var(data)     # dict is a Dictionary
## print(dict["a"])                # Prints 1
## [/gdscript]
## [csharp]
## string data = "{ \"a\": 1, \"b\": 2 }";           // data is a string
## var dict = GD.StrToVar(data).AsGodotDictionary(); // dict is a Dictionary
## GD.Print(dict["a"]);                              // Prints 1
## [/csharp]
## [/codeblocks]
func str_to_var(string: String) -> Variant:
	pass;

## Returns the tangent of angle [param angle_rad] in radians.
## [codeblock]
## tan(deg_to_rad(45)) # Returns 1
## [/codeblock]
func tan(angle_rad: float) -> float:
	pass;

## Returns the hyperbolic tangent of [param x].
## [codeblock]
## var a = log(2.0) # Returns 0.693147
## tanh(a)          # Returns 0.6
## [/codeblock]
func tanh(x: float) -> float:
	pass;

## Converts the given [param variant] to the given [param type], using the [enum Variant.Type] values. This method is generous with how it handles types, it can automatically convert between array types, convert numeric [String]s to [int], and converting most things to [String].
## If the type conversion cannot be done, this method will return the default value for that type, for example converting [Rect2] to [Vector2] will always return [constant Vector2.ZERO]. This method will never show error messages as long as [param type] is a valid Variant type.
## The returned value is a [Variant], but the data inside and its type will be the same as the requested type.
## [codeblock]
## type_convert("Hi!", TYPE_INT) # Returns 0
## type_convert("123", TYPE_INT) # Returns 123
## type_convert(123.4, TYPE_INT) # Returns 123
## type_convert(5, TYPE_VECTOR2) # Returns (0, 0)
## type_convert("Hi!", TYPE_NIL) # Returns null
## [/codeblock]
func type_convert(variant: Variant, type: int) -> Variant:
	pass;

## Returns a human-readable name of the given [param type], using the [enum Variant.Type] values.
## [codeblock]
## print(TYPE_INT) # Prints 2
## print(type_string(TYPE_INT)) # Prints "int"
## print(type_string(TYPE_STRING)) # Prints "String"
## [/codeblock]
## See also [method typeof].
func type_string(type: int) -> String:
	pass;

## Returns the internal type of the given [param variable], using the [enum Variant.Type] values.
## [codeblock]
## var json = JSON.new()
## json.parse('["a", "b", "c"]')
## var result = json.get_data()
## if result is Array:
## print(result[0]) # Prints "a"
## else:
## print("Unexpected result!")
## [/codeblock]
## See also [method type_string].
func typeof(variable: Variant) -> int:
	pass;

## Encodes a [Variant] value to a byte array, without encoding objects. Deserialization can be done with [method bytes_to_var].
## [b]Note:[/b] If you need object serialization, see [method var_to_bytes_with_objects].
## [b]Note:[/b] Encoding [Callable] is not supported and will result in an empty value, regardless of the data.
func var_to_bytes(variable: Variant) -> PackedByteArray:
	pass;

## Encodes a [Variant] value to a byte array. Encoding objects is allowed (and can potentially include executable code). Deserialization can be done with [method bytes_to_var_with_objects].
## [b]Note:[/b] Encoding [Callable] is not supported and will result in an empty value, regardless of the data.
func var_to_bytes_with_objects(variable: Variant) -> PackedByteArray:
	pass;

## Converts a [Variant] [param variable] to a formatted [String] that can then be parsed using [method str_to_var].
## [codeblocks]
## [gdscript]
## var a = { "a": 1, "b": 2 }
## print(var_to_str(a))
## [/gdscript]
## [csharp]
## var a = new Godot.Collections.Dictionary { ["a"] = 1, ["b"] = 2 };
## GD.Print(GD.VarToStr(a));
## [/csharp]
## [/codeblocks]
## Prints:
## [codeblock lang=text]
## {
## "a": 1,
## "b": 2
## }
## [/codeblock]
## [b]Note:[/b] Converting [Signal] or [Callable] is not supported and will result in an empty value for these types, regardless of their data.
func var_to_str(variable: Variant) -> String:
	pass;

## Returns a [WeakRef] instance holding a weak reference to [param obj]. Returns an empty [WeakRef] instance if [param obj] is [code]null[/code]. Prints an error and returns [code]null[/code] if [param obj] is neither [Object]-derived nor [code]null[/code].
## A weak reference to an object is not enough to keep the object alive: when the only remaining references to a referent are weak references, garbage collection is free to destroy the referent and reuse its memory for something else. However, until the object is actually destroyed the weak reference may return the object even if there are no strong references to it.
func weakref(obj: Variant) -> Variant:
	pass;

## Wraps the [Variant] [param value] between [param min] and [param max]. Can be used for creating loop-alike behavior or infinite surfaces.
## Variant types [int] and [float] are supported. If any of the arguments is [float] this function returns a [float], otherwise it returns an [int].
## [codeblock]
## var a = wrap(4, 5, 10)
## # a is 9 (int)
## var a = wrap(7, 5, 10)
## # a is 7 (int)
## var a = wrap(10.5, 5, 10)
## # a is 5.5 (float)
## [/codeblock]
func wrap(value: Variant, min: Variant, max: Variant) -> Variant:
	pass;

## Wraps the float [param value] between [param min] and [param max]. Can be used for creating loop-alike behavior or infinite surfaces.
## [codeblock]
## # Infinite loop between 5.0 and 9.9
## value = wrapf(value + 0.1, 5.0, 10.0)
## [/codeblock]
## [codeblock]
## # Infinite rotation (in radians)
## angle = wrapf(angle + 0.1, 0.0, TAU)
## [/codeblock]
## [codeblock]
## # Infinite rotation (in radians)
## angle = wrapf(angle + 0.1, -PI, PI)
## [/codeblock]
## [b]Note:[/b] If [param min] is [code]0[/code], this is equivalent to [method fposmod], so prefer using that instead.
## [method wrapf] is more flexible than using the [method fposmod] approach by giving the user control over the minimum value.
func wrapf(value: float, min: float, max: float) -> float:
	pass;

## Wraps the integer [param value] between [param min] and [param max]. Can be used for creating loop-alike behavior or infinite surfaces.
## [codeblock]
## # Infinite loop between 5 and 9
## frame = wrapi(frame + 1, 5, 10)
## [/codeblock]
## [codeblock]
## # result is -2
## var result = wrapi(-6, -5, -1)
## [/codeblock]
func wrapi(value: int, min: int, max: int) -> int:
	pass;


