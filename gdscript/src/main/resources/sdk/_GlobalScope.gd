#brief Global scope constants and functions.
#desc A list of global scope enumerated constants and built-in functions. This is all that resides in the globals, constants regarding error codes, keycodes, property hints, etc.
#desc Singletons are also documented here, since they can be accessed from anywhere.
#desc For the entries related to GDScript which can be accessed in any script see [@GDScript].
class_name _GlobalScope

#desc Left side, usually used for [Control] or [StyleBox]-derived classes.
const SIDE_LEFT = 0;

#desc Top side, usually used for [Control] or [StyleBox]-derived classes.
const SIDE_TOP = 1;

#desc Right side, usually used for [Control] or [StyleBox]-derived classes.
const SIDE_RIGHT = 2;

#desc Bottom side, usually used for [Control] or [StyleBox]-derived classes.
const SIDE_BOTTOM = 3;

#desc Top-left corner.
const CORNER_TOP_LEFT = 0;

#desc Top-right corner.
const CORNER_TOP_RIGHT = 1;

#desc Bottom-right corner.
const CORNER_BOTTOM_RIGHT = 2;

#desc Bottom-left corner.
const CORNER_BOTTOM_LEFT = 3;

#desc General vertical alignment, usually used for [Separator], [ScrollBar], [Slider], etc.
const VERTICAL = 1;

#desc General horizontal alignment, usually used for [Separator], [ScrollBar], [Slider], etc.
const HORIZONTAL = 0;

const CLOCKWISE = 0;

const COUNTERCLOCKWISE = 1;

#desc Horizontal left alignment, usually for text-derived classes.
const HORIZONTAL_ALIGNMENT_LEFT = 0;

#desc Horizontal center alignment, usually for text-derived classes.
const HORIZONTAL_ALIGNMENT_CENTER = 1;

#desc Horizontal right alignment, usually for text-derived classes.
const HORIZONTAL_ALIGNMENT_RIGHT = 2;

#desc Expand row to fit width, usually for text-derived classes.
const HORIZONTAL_ALIGNMENT_FILL = 3;

#desc Vertical top alignment, usually for text-derived classes.
const VERTICAL_ALIGNMENT_TOP = 0;

#desc Vertical center alignment, usually for text-derived classes.
const VERTICAL_ALIGNMENT_CENTER = 1;

#desc Vertical bottom alignment, usually for text-derived classes.
const VERTICAL_ALIGNMENT_BOTTOM = 2;

#desc Expand rows to fit height, usually for text-derived classes.
const VERTICAL_ALIGNMENT_FILL = 3;

#desc Aligns the top of the inline object (e.g. image, table) to the position of the text specified by [code]INLINE_ALIGNMENT_TO_*[/code] constant.
const INLINE_ALIGNMENT_TOP_TO = 0;

#desc Aligns the center of the inline object (e.g. image, table) to the position of the text specified by [code]INLINE_ALIGNMENT_TO_*[/code] constant.
const INLINE_ALIGNMENT_CENTER_TO = 1;

#desc Aligns the bottom of the inline object (e.g. image, table) to the position of the text specified by [code]INLINE_ALIGNMENT_TO_*[/code] constant.
const INLINE_ALIGNMENT_BOTTOM_TO = 2;

#desc Aligns the position of the inline object (e.g. image, table) specified by [code]INLINE_ALIGNMENT_*_TO[/code] constant to the top of the text.
const INLINE_ALIGNMENT_TO_TOP = 0;

#desc Aligns the position of the inline object (e.g. image, table) specified by [code]INLINE_ALIGNMENT_*_TO[/code] constant to the center of the text.
const INLINE_ALIGNMENT_TO_CENTER = 4;

#desc Aligns the position of the inline object (e.g. image, table) specified by [code]INLINE_ALIGNMENT_*_TO[/code] constant to the baseline of the text.
const INLINE_ALIGNMENT_TO_BASELINE = 8;

#desc Aligns inline object (e.g. image, table) to the bottom of the text.
const INLINE_ALIGNMENT_TO_BOTTOM = 12;

#desc Aligns top of the inline object (e.g. image, table) to the top of the text. Equivalent to [code]INLINE_ALIGNMENT_TOP_TO | INLINE_ALIGNMENT_TO_TOP[/code].
const INLINE_ALIGNMENT_TOP = 0;

#desc Aligns center of the inline object (e.g. image, table) to the center of the text. Equivalent to [code]INLINE_ALIGNMENT_CENTER_TO | INLINE_ALIGNMENT_TO_CENTER[/code].
const INLINE_ALIGNMENT_CENTER = 5;

#desc Aligns bottom of the inline object (e.g. image, table) to the bottom of the text. Equivalent to [code]INLINE_ALIGNMENT_BOTTOM_TO | INLINE_ALIGNMENT_TO_BOTTOM[/code].
const INLINE_ALIGNMENT_BOTTOM = 14;

#desc A bit mask for [code]INLINE_ALIGNMENT_*_TO[/code] alignment constants.
const INLINE_ALIGNMENT_IMAGE_MASK = 3;

#desc A bit mask for [code]INLINE_ALIGNMENT_TO_*[/code] alignment constants.
const INLINE_ALIGNMENT_TEXT_MASK = 12;

#desc Enum value which doesn't correspond to any key. This is used to initialize [enum Key] properties with a generic state.
const KEY_NONE = 0;

#desc Keycodes with this bit applied are non-printable.
const KEY_SPECIAL = 4194304;

#desc Escape key.
const KEY_ESCAPE = 4194305;

#desc Tab key.
const KEY_TAB = 4194306;

#desc Shift + Tab key.
const KEY_BACKTAB = 4194307;

#desc Backspace key.
const KEY_BACKSPACE = 4194308;

#desc Return key (on the main keyboard).
const KEY_ENTER = 4194309;

#desc Enter key on the numeric keypad.
const KEY_KP_ENTER = 4194310;

#desc Insert key.
const KEY_INSERT = 4194311;

#desc Delete key.
const KEY_DELETE = 4194312;

#desc Pause key.
const KEY_PAUSE = 4194313;

#desc Print Screen key.
const KEY_PRINT = 4194314;

#desc System Request key.
const KEY_SYSREQ = 4194315;

#desc Clear key.
const KEY_CLEAR = 4194316;

#desc Home key.
const KEY_HOME = 4194317;

#desc End key.
const KEY_END = 4194318;

#desc Left arrow key.
const KEY_LEFT = 4194319;

#desc Up arrow key.
const KEY_UP = 4194320;

#desc Right arrow key.
const KEY_RIGHT = 4194321;

#desc Down arrow key.
const KEY_DOWN = 4194322;

#desc Page Up key.
const KEY_PAGEUP = 4194323;

#desc Page Down key.
const KEY_PAGEDOWN = 4194324;

#desc Shift key.
const KEY_SHIFT = 4194325;

#desc Control key.
const KEY_CTRL = 4194326;

#desc Meta key.
const KEY_META = 4194327;

#desc Alt key.
const KEY_ALT = 4194328;

#desc Caps Lock key.
const KEY_CAPSLOCK = 4194329;

#desc Num Lock key.
const KEY_NUMLOCK = 4194330;

#desc Scroll Lock key.
const KEY_SCROLLLOCK = 4194331;

#desc F1 key.
const KEY_F1 = 4194332;

#desc F2 key.
const KEY_F2 = 4194333;

#desc F3 key.
const KEY_F3 = 4194334;

#desc F4 key.
const KEY_F4 = 4194335;

#desc F5 key.
const KEY_F5 = 4194336;

#desc F6 key.
const KEY_F6 = 4194337;

#desc F7 key.
const KEY_F7 = 4194338;

#desc F8 key.
const KEY_F8 = 4194339;

#desc F9 key.
const KEY_F9 = 4194340;

#desc F10 key.
const KEY_F10 = 4194341;

#desc F11 key.
const KEY_F11 = 4194342;

#desc F12 key.
const KEY_F12 = 4194343;

#desc F13 key.
const KEY_F13 = 4194344;

#desc F14 key.
const KEY_F14 = 4194345;

#desc F15 key.
const KEY_F15 = 4194346;

#desc F16 key.
const KEY_F16 = 4194347;

#desc F17 key.
const KEY_F17 = 4194348;

#desc F18 key.
const KEY_F18 = 4194349;

#desc F19 key.
const KEY_F19 = 4194350;

#desc F20 key.
const KEY_F20 = 4194351;

#desc F21 key.
const KEY_F21 = 4194352;

#desc F22 key.
const KEY_F22 = 4194353;

#desc F23 key.
const KEY_F23 = 4194354;

#desc F24 key.
const KEY_F24 = 4194355;

#desc F25 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F25 = 4194356;

#desc F26 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F26 = 4194357;

#desc F27 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F27 = 4194358;

#desc F28 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F28 = 4194359;

#desc F29 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F29 = 4194360;

#desc F30 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F30 = 4194361;

#desc F31 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F31 = 4194362;

#desc F32 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F32 = 4194363;

#desc F33 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F33 = 4194364;

#desc F34 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F34 = 4194365;

#desc F35 key. Only supported on macOS and Linux due to a Windows limitation.
const KEY_F35 = 4194366;

#desc Multiply (*) key on the numeric keypad.
const KEY_KP_MULTIPLY = 4194433;

#desc Divide (/) key on the numeric keypad.
const KEY_KP_DIVIDE = 4194434;

#desc Subtract (-) key on the numeric keypad.
const KEY_KP_SUBTRACT = 4194435;

#desc Period (.) key on the numeric keypad.
const KEY_KP_PERIOD = 4194436;

#desc Add (+) key on the numeric keypad.
const KEY_KP_ADD = 4194437;

#desc Number 0 on the numeric keypad.
const KEY_KP_0 = 4194438;

#desc Number 1 on the numeric keypad.
const KEY_KP_1 = 4194439;

#desc Number 2 on the numeric keypad.
const KEY_KP_2 = 4194440;

#desc Number 3 on the numeric keypad.
const KEY_KP_3 = 4194441;

#desc Number 4 on the numeric keypad.
const KEY_KP_4 = 4194442;

#desc Number 5 on the numeric keypad.
const KEY_KP_5 = 4194443;

#desc Number 6 on the numeric keypad.
const KEY_KP_6 = 4194444;

#desc Number 7 on the numeric keypad.
const KEY_KP_7 = 4194445;

#desc Number 8 on the numeric keypad.
const KEY_KP_8 = 4194446;

#desc Number 9 on the numeric keypad.
const KEY_KP_9 = 4194447;

#desc Left Super key (Windows key).
const KEY_SUPER_L = 4194368;

#desc Right Super key (Windows key).
const KEY_SUPER_R = 4194369;

#desc Context menu key.
const KEY_MENU = 4194370;

#desc Left Hyper key.
const KEY_HYPER_L = 4194371;

#desc Right Hyper key.
const KEY_HYPER_R = 4194372;

#desc Help key.
const KEY_HELP = 4194373;

#desc Left Direction key.
const KEY_DIRECTION_L = 4194374;

#desc Right Direction key.
const KEY_DIRECTION_R = 4194375;

#desc Media back key. Not to be confused with the Back button on an Android device.
const KEY_BACK = 4194376;

#desc Media forward key.
const KEY_FORWARD = 4194377;

#desc Media stop key.
const KEY_STOP = 4194378;

#desc Media refresh key.
const KEY_REFRESH = 4194379;

#desc Volume down key.
const KEY_VOLUMEDOWN = 4194380;

#desc Mute volume key.
const KEY_VOLUMEMUTE = 4194381;

#desc Volume up key.
const KEY_VOLUMEUP = 4194382;

#desc Bass Boost key.
const KEY_BASSBOOST = 4194383;

#desc Bass up key.
const KEY_BASSUP = 4194384;

#desc Bass down key.
const KEY_BASSDOWN = 4194385;

#desc Treble up key.
const KEY_TREBLEUP = 4194386;

#desc Treble down key.
const KEY_TREBLEDOWN = 4194387;

#desc Media play key.
const KEY_MEDIAPLAY = 4194388;

#desc Media stop key.
const KEY_MEDIASTOP = 4194389;

#desc Previous song key.
const KEY_MEDIAPREVIOUS = 4194390;

#desc Next song key.
const KEY_MEDIANEXT = 4194391;

#desc Media record key.
const KEY_MEDIARECORD = 4194392;

#desc Home page key.
const KEY_HOMEPAGE = 4194393;

#desc Favorites key.
const KEY_FAVORITES = 4194394;

#desc Search key.
const KEY_SEARCH = 4194395;

#desc Standby key.
const KEY_STANDBY = 4194396;

#desc Open URL / Launch Browser key.
const KEY_OPENURL = 4194397;

#desc Launch Mail key.
const KEY_LAUNCHMAIL = 4194398;

#desc Launch Media key.
const KEY_LAUNCHMEDIA = 4194399;

#desc Launch Shortcut 0 key.
const KEY_LAUNCH0 = 4194400;

#desc Launch Shortcut 1 key.
const KEY_LAUNCH1 = 4194401;

#desc Launch Shortcut 2 key.
const KEY_LAUNCH2 = 4194402;

#desc Launch Shortcut 3 key.
const KEY_LAUNCH3 = 4194403;

#desc Launch Shortcut 4 key.
const KEY_LAUNCH4 = 4194404;

#desc Launch Shortcut 5 key.
const KEY_LAUNCH5 = 4194405;

#desc Launch Shortcut 6 key.
const KEY_LAUNCH6 = 4194406;

#desc Launch Shortcut 7 key.
const KEY_LAUNCH7 = 4194407;

#desc Launch Shortcut 8 key.
const KEY_LAUNCH8 = 4194408;

#desc Launch Shortcut 9 key.
const KEY_LAUNCH9 = 4194409;

#desc Launch Shortcut A key.
const KEY_LAUNCHA = 4194410;

#desc Launch Shortcut B key.
const KEY_LAUNCHB = 4194411;

#desc Launch Shortcut C key.
const KEY_LAUNCHC = 4194412;

#desc Launch Shortcut D key.
const KEY_LAUNCHD = 4194413;

#desc Launch Shortcut E key.
const KEY_LAUNCHE = 4194414;

#desc Launch Shortcut F key.
const KEY_LAUNCHF = 4194415;

#desc Unknown key.
const KEY_UNKNOWN = 16777215;

#desc Space key.
const KEY_SPACE = 32;

#desc ! key.
const KEY_EXCLAM = 33;

#desc " key.
const KEY_QUOTEDBL = 34;

#desc # key.
const KEY_NUMBERSIGN = 35;

#desc $ key.
const KEY_DOLLAR = 36;

#desc % key.
const KEY_PERCENT = 37;

#desc & key.
const KEY_AMPERSAND = 38;

#desc ' key.
const KEY_APOSTROPHE = 39;

#desc ( key.
const KEY_PARENLEFT = 40;

#desc ) key.
const KEY_PARENRIGHT = 41;

#desc * key.
const KEY_ASTERISK = 42;

#desc + key.
const KEY_PLUS = 43;

#desc , key.
const KEY_COMMA = 44;

#desc - key.
const KEY_MINUS = 45;

#desc . key.
const KEY_PERIOD = 46;

#desc / key.
const KEY_SLASH = 47;

#desc Number 0.
const KEY_0 = 48;

#desc Number 1.
const KEY_1 = 49;

#desc Number 2.
const KEY_2 = 50;

#desc Number 3.
const KEY_3 = 51;

#desc Number 4.
const KEY_4 = 52;

#desc Number 5.
const KEY_5 = 53;

#desc Number 6.
const KEY_6 = 54;

#desc Number 7.
const KEY_7 = 55;

#desc Number 8.
const KEY_8 = 56;

#desc Number 9.
const KEY_9 = 57;

#desc : key.
const KEY_COLON = 58;

#desc ; key.
const KEY_SEMICOLON = 59;

#desc < key.
const KEY_LESS = 60;

#desc = key.
const KEY_EQUAL = 61;

#desc > key.
const KEY_GREATER = 62;

#desc ? key.
const KEY_QUESTION = 63;

#desc @ key.
const KEY_AT = 64;

#desc A key.
const KEY_A = 65;

#desc B key.
const KEY_B = 66;

#desc C key.
const KEY_C = 67;

#desc D key.
const KEY_D = 68;

#desc E key.
const KEY_E = 69;

#desc F key.
const KEY_F = 70;

#desc G key.
const KEY_G = 71;

#desc H key.
const KEY_H = 72;

#desc I key.
const KEY_I = 73;

#desc J key.
const KEY_J = 74;

#desc K key.
const KEY_K = 75;

#desc L key.
const KEY_L = 76;

#desc M key.
const KEY_M = 77;

#desc N key.
const KEY_N = 78;

#desc O key.
const KEY_O = 79;

#desc P key.
const KEY_P = 80;

#desc Q key.
const KEY_Q = 81;

#desc R key.
const KEY_R = 82;

#desc S key.
const KEY_S = 83;

#desc T key.
const KEY_T = 84;

#desc U key.
const KEY_U = 85;

#desc V key.
const KEY_V = 86;

#desc W key.
const KEY_W = 87;

#desc X key.
const KEY_X = 88;

#desc Y key.
const KEY_Y = 89;

#desc Z key.
const KEY_Z = 90;

#desc [ key.
const KEY_BRACKETLEFT = 91;

#desc \ key.
const KEY_BACKSLASH = 92;

#desc ] key.
const KEY_BRACKETRIGHT = 93;

#desc ^ key.
const KEY_ASCIICIRCUM = 94;

#desc _ key.
const KEY_UNDERSCORE = 95;

#desc ` key.
const KEY_QUOTELEFT = 96;

#desc { key.
const KEY_BRACELEFT = 123;

#desc | key.
const KEY_BAR = 124;

#desc } key.
const KEY_BRACERIGHT = 125;

#desc ~ key.
const KEY_ASCIITILDE = 126;

#desc Non-breakable space key.
const KEY_NOBREAKSPACE = 160;

#desc ¡ key.
const KEY_EXCLAMDOWN = 161;

#desc ¢ key.
const KEY_CENT = 162;

#desc £ key.
const KEY_STERLING = 163;

#desc ¤ key.
const KEY_CURRENCY = 164;

#desc ¥ key.
const KEY_YEN = 165;

#desc ¦ key.
const KEY_BROKENBAR = 166;

#desc § key.
const KEY_SECTION = 167;

#desc ¨ key.
const KEY_DIAERESIS = 168;

#desc © key.
const KEY_COPYRIGHT = 169;

#desc ª key.
const KEY_ORDFEMININE = 170;

#desc « key.
const KEY_GUILLEMOTLEFT = 171;

#desc ¬ key.
const KEY_NOTSIGN = 172;

#desc Soft hyphen key.
const KEY_HYPHEN = 173;

#desc ® key.
const KEY_REGISTERED = 174;

#desc ¯ key.
const KEY_MACRON = 175;

#desc ° key.
const KEY_DEGREE = 176;

#desc ± key.
const KEY_PLUSMINUS = 177;

#desc ² key.
const KEY_TWOSUPERIOR = 178;

#desc ³ key.
const KEY_THREESUPERIOR = 179;

#desc ´ key.
const KEY_ACUTE = 180;

#desc µ key.
const KEY_MU = 181;

#desc ¶ key.
const KEY_PARAGRAPH = 182;

#desc · key.
const KEY_PERIODCENTERED = 183;

#desc ¸ key.
const KEY_CEDILLA = 184;

#desc ¹ key.
const KEY_ONESUPERIOR = 185;

#desc º key.
const KEY_MASCULINE = 186;

#desc » key.
const KEY_GUILLEMOTRIGHT = 187;

#desc ¼ key.
const KEY_ONEQUARTER = 188;

#desc ½ key.
const KEY_ONEHALF = 189;

#desc ¾ key.
const KEY_THREEQUARTERS = 190;

#desc ¿ key.
const KEY_QUESTIONDOWN = 191;

#desc À key.
const KEY_AGRAVE = 192;

#desc Á key.
const KEY_AACUTE = 193;

#desc Â key.
const KEY_ACIRCUMFLEX = 194;

#desc Ã key.
const KEY_ATILDE = 195;

#desc Ä key.
const KEY_ADIAERESIS = 196;

#desc Å key.
const KEY_ARING = 197;

#desc Æ key.
const KEY_AE = 198;

#desc Ç key.
const KEY_CCEDILLA = 199;

#desc È key.
const KEY_EGRAVE = 200;

#desc É key.
const KEY_EACUTE = 201;

#desc Ê key.
const KEY_ECIRCUMFLEX = 202;

#desc Ë key.
const KEY_EDIAERESIS = 203;

#desc Ì key.
const KEY_IGRAVE = 204;

#desc Í key.
const KEY_IACUTE = 205;

#desc Î key.
const KEY_ICIRCUMFLEX = 206;

#desc Ï key.
const KEY_IDIAERESIS = 207;

#desc Ð key.
const KEY_ETH = 208;

#desc Ñ key.
const KEY_NTILDE = 209;

#desc Ò key.
const KEY_OGRAVE = 210;

#desc Ó key.
const KEY_OACUTE = 211;

#desc Ô key.
const KEY_OCIRCUMFLEX = 212;

#desc Õ key.
const KEY_OTILDE = 213;

#desc Ö key.
const KEY_ODIAERESIS = 214;

#desc × key.
const KEY_MULTIPLY = 215;

#desc Ø key.
const KEY_OOBLIQUE = 216;

#desc Ù key.
const KEY_UGRAVE = 217;

#desc Ú key.
const KEY_UACUTE = 218;

#desc Û key.
const KEY_UCIRCUMFLEX = 219;

#desc Ü key.
const KEY_UDIAERESIS = 220;

#desc Ý key.
const KEY_YACUTE = 221;

#desc Þ key.
const KEY_THORN = 222;

#desc ß key.
const KEY_SSHARP = 223;

#desc ÷ key.
const KEY_DIVISION = 247;

#desc ÿ key.
const KEY_YDIAERESIS = 255;

#desc Key Code mask.
const KEY_CODE_MASK = 8388607;

#desc Modifier key mask.
const KEY_MODIFIER_MASK = 532676608;

#desc Automatically remapped to [constant KEY_META] on macOS and [constant KEY_CTRL] on other platforms, this mask is never set in the actual events, and should be used for key mapping only.
const KEY_MASK_CMD_OR_CTRL = 16777216;

#desc Shift key mask.
const KEY_MASK_SHIFT = 33554432;

#desc Alt or Option (on macOS) key mask.
const KEY_MASK_ALT = 67108864;

#desc Command (on macOS) or Meta/Windows key mask.
const KEY_MASK_META = 134217728;

#desc Ctrl key mask.
const KEY_MASK_CTRL = 268435456;

#desc Keypad key mask.
const KEY_MASK_KPAD = 536870912;

#desc Group Switch key mask.
const KEY_MASK_GROUP_SWITCH = 1073741824;

#desc Enum value which doesn't correspond to any mouse button. This is used to initialize [enum MouseButton] properties with a generic state.
const MOUSE_BUTTON_NONE = 0;

#desc Left mouse button.
const MOUSE_BUTTON_LEFT = 1;

#desc Right mouse button.
const MOUSE_BUTTON_RIGHT = 2;

#desc Middle mouse button.
const MOUSE_BUTTON_MIDDLE = 3;

#desc Mouse wheel up.
const MOUSE_BUTTON_WHEEL_UP = 4;

#desc Mouse wheel down.
const MOUSE_BUTTON_WHEEL_DOWN = 5;

#desc Mouse wheel left button (only present on some mice).
const MOUSE_BUTTON_WHEEL_LEFT = 6;

#desc Mouse wheel right button (only present on some mice).
const MOUSE_BUTTON_WHEEL_RIGHT = 7;

#desc Extra mouse button 1 (only present on some mice).
const MOUSE_BUTTON_XBUTTON1 = 8;

#desc Extra mouse button 2 (only present on some mice).
const MOUSE_BUTTON_XBUTTON2 = 9;

#desc Left mouse button mask.
const MOUSE_BUTTON_MASK_LEFT = 1;

#desc Right mouse button mask.
const MOUSE_BUTTON_MASK_RIGHT = 2;

#desc Middle mouse button mask.
const MOUSE_BUTTON_MASK_MIDDLE = 4;

#desc Extra mouse button 1 mask.
const MOUSE_BUTTON_MASK_XBUTTON1 = 128;

#desc Extra mouse button 2 mask.
const MOUSE_BUTTON_MASK_XBUTTON2 = 256;

#desc An invalid game controller button.
const JOY_BUTTON_INVALID = -1;

#desc Game controller SDL button A. Corresponds to the bottom action button: Sony Cross, Xbox A, Nintendo B.
const JOY_BUTTON_A = 0;

#desc Game controller SDL button B. Corresponds to the right action button: Sony Circle, Xbox B, Nintendo A.
const JOY_BUTTON_B = 1;

#desc Game controller SDL button X. Corresponds to the left action button: Sony Square, Xbox X, Nintendo Y.
const JOY_BUTTON_X = 2;

#desc Game controller SDL button Y. Corresponds to the top action button: Sony Triangle, Xbox Y, Nintendo X.
const JOY_BUTTON_Y = 3;

#desc Game controller SDL back button. Corresponds to the Sony Select, Xbox Back, Nintendo - button.
const JOY_BUTTON_BACK = 4;

#desc Game controller SDL guide button. Corresponds to the Sony PS, Xbox Home button.
const JOY_BUTTON_GUIDE = 5;

#desc Game controller SDL start button. Corresponds to the Nintendo + button.
const JOY_BUTTON_START = 6;

#desc Game controller SDL left stick button. Corresponds to the Sony L3, Xbox L/LS button.
const JOY_BUTTON_LEFT_STICK = 7;

#desc Game controller SDL right stick button. Corresponds to the Sony R3, Xbox R/RS button.
const JOY_BUTTON_RIGHT_STICK = 8;

#desc Game controller SDL left shoulder button. Corresponds to the Sony L1, Xbox LB button.
const JOY_BUTTON_LEFT_SHOULDER = 9;

#desc Game controller SDL right shoulder button. Corresponds to the Sony R1, Xbox RB button.
const JOY_BUTTON_RIGHT_SHOULDER = 10;

#desc Game controller D-pad up button.
const JOY_BUTTON_DPAD_UP = 11;

#desc Game controller D-pad down button.
const JOY_BUTTON_DPAD_DOWN = 12;

#desc Game controller D-pad left button.
const JOY_BUTTON_DPAD_LEFT = 13;

#desc Game controller D-pad right button.
const JOY_BUTTON_DPAD_RIGHT = 14;

#desc Game controller SDL miscellaneous button. Corresponds to Xbox share button, PS5 microphone button, Nintendo capture button.
const JOY_BUTTON_MISC1 = 15;

#desc Game controller SDL paddle 1 button.
const JOY_BUTTON_PADDLE1 = 16;

#desc Game controller SDL paddle 2 button.
const JOY_BUTTON_PADDLE2 = 17;

#desc Game controller SDL paddle 3 button.
const JOY_BUTTON_PADDLE3 = 18;

#desc Game controller SDL paddle 4 button.
const JOY_BUTTON_PADDLE4 = 19;

#desc Game controller SDL touchpad button.
const JOY_BUTTON_TOUCHPAD = 20;

#desc The number of SDL game controller buttons.
const JOY_BUTTON_SDL_MAX = 21;

#desc The maximum number of game controller buttons supported by the engine. The actual limit may be lower on specific platforms:
#desc - Android: Up to 36 buttons.
#desc - Linux: Up to 80 buttons.
#desc - Windows and macOS: Up to 128 buttons.
const JOY_BUTTON_MAX = 128;

#desc An invalid game controller axis.
const JOY_AXIS_INVALID = -1;

#desc Game controller left joystick x-axis.
const JOY_AXIS_LEFT_X = 0;

#desc Game controller left joystick y-axis.
const JOY_AXIS_LEFT_Y = 1;

#desc Game controller right joystick x-axis.
const JOY_AXIS_RIGHT_X = 2;

#desc Game controller right joystick y-axis.
const JOY_AXIS_RIGHT_Y = 3;

#desc Game controller left trigger axis.
const JOY_AXIS_TRIGGER_LEFT = 4;

#desc Game controller right trigger axis.
const JOY_AXIS_TRIGGER_RIGHT = 5;

#desc The number of SDL game controller axes.
const JOY_AXIS_SDL_MAX = 6;

#desc The maximum number of game controller axes: OpenVR supports up to 5 Joysticks making a total of 10 axes.
const JOY_AXIS_MAX = 10;

#desc Enum value which doesn't correspond to any MIDI message. This is used to initialize [enum MIDIMessage] properties with a generic state.
const MIDI_MESSAGE_NONE = 0;

#desc MIDI note OFF message. See the documentation of [InputEventMIDI] for information of how to use MIDI inputs.
const MIDI_MESSAGE_NOTE_OFF = 8;

#desc MIDI note ON message. See the documentation of [InputEventMIDI] for information of how to use MIDI inputs.
const MIDI_MESSAGE_NOTE_ON = 9;

#desc MIDI aftertouch message. This message is most often sent by pressing down on the key after it "bottoms out".
const MIDI_MESSAGE_AFTERTOUCH = 10;

#desc MIDI control change message. This message is sent when a controller value changes. Controllers include devices such as pedals and levers.
const MIDI_MESSAGE_CONTROL_CHANGE = 11;

#desc MIDI program change message. This message sent when the program patch number changes.
const MIDI_MESSAGE_PROGRAM_CHANGE = 12;

#desc MIDI channel pressure message. This message is most often sent by pressing down on the key after it "bottoms out". This message is different from polyphonic after-touch as it indicates the highest pressure across all keys.
const MIDI_MESSAGE_CHANNEL_PRESSURE = 13;

#desc MIDI pitch bend message. This message is sent to indicate a change in the pitch bender (wheel or lever, typically).
const MIDI_MESSAGE_PITCH_BEND = 14;

#desc MIDI system exclusive message. This has behavior exclusive to the device you're receiving input from. Getting this data is not implemented in Godot.
const MIDI_MESSAGE_SYSTEM_EXCLUSIVE = 240;

#desc MIDI quarter frame message. Contains timing information that is used to synchronize MIDI devices. Getting this data is not implemented in Godot.
const MIDI_MESSAGE_QUARTER_FRAME = 241;

#desc MIDI song position pointer message. Gives the number of 16th notes since the start of the song. Getting this data is not implemented in Godot.
const MIDI_MESSAGE_SONG_POSITION_POINTER = 242;

#desc MIDI song select message. Specifies which sequence or song is to be played. Getting this data is not implemented in Godot.
const MIDI_MESSAGE_SONG_SELECT = 243;

#desc MIDI tune request message. Upon receiving a tune request, all analog synthesizers should tune their oscillators.
const MIDI_MESSAGE_TUNE_REQUEST = 246;

#desc MIDI timing clock message. Sent 24 times per quarter note when synchronization is required.
const MIDI_MESSAGE_TIMING_CLOCK = 248;

#desc MIDI start message. Start the current sequence playing. This message will be followed with Timing Clocks.
const MIDI_MESSAGE_START = 250;

#desc MIDI continue message. Continue at the point the sequence was stopped.
const MIDI_MESSAGE_CONTINUE = 251;

#desc MIDI stop message. Stop the current sequence.
const MIDI_MESSAGE_STOP = 252;

#desc MIDI active sensing message. This message is intended to be sent repeatedly to tell the receiver that a connection is alive.
const MIDI_MESSAGE_ACTIVE_SENSING = 254;

#desc MIDI system reset message. Reset all receivers in the system to power-up status. It should not be sent on power-up itself.
const MIDI_MESSAGE_SYSTEM_RESET = 255;

#desc Methods that return [enum Error] return [constant OK] when no error occurred. Note that many functions don't return an error code but will print error messages to standard output.
#desc Since [constant OK] has value 0, and all other failure codes are positive integers, it can also be used in boolean checks, e.g.:
#desc [codeblock]
#desc var err = method_that_returns_error()
#desc if err != OK:
#desc print("Failure!")
#desc # Or, equivalent:
#desc if err:
#desc print("Still failing!")
#desc [/codeblock]
const OK = 0;

#desc Generic error.
const FAILED = 1;

#desc Unavailable error.
const ERR_UNAVAILABLE = 2;

#desc Unconfigured error.
const ERR_UNCONFIGURED = 3;

#desc Unauthorized error.
const ERR_UNAUTHORIZED = 4;

#desc Parameter range error.
const ERR_PARAMETER_RANGE_ERROR = 5;

#desc Out of memory (OOM) error.
const ERR_OUT_OF_MEMORY = 6;

#desc File: Not found error.
const ERR_FILE_NOT_FOUND = 7;

#desc File: Bad drive error.
const ERR_FILE_BAD_DRIVE = 8;

#desc File: Bad path error.
const ERR_FILE_BAD_PATH = 9;

#desc File: No permission error.
const ERR_FILE_NO_PERMISSION = 10;

#desc File: Already in use error.
const ERR_FILE_ALREADY_IN_USE = 11;

#desc File: Can't open error.
const ERR_FILE_CANT_OPEN = 12;

#desc File: Can't write error.
const ERR_FILE_CANT_WRITE = 13;

#desc File: Can't read error.
const ERR_FILE_CANT_READ = 14;

#desc File: Unrecognized error.
const ERR_FILE_UNRECOGNIZED = 15;

#desc File: Corrupt error.
const ERR_FILE_CORRUPT = 16;

#desc File: Missing dependencies error.
const ERR_FILE_MISSING_DEPENDENCIES = 17;

#desc File: End of file (EOF) error.
const ERR_FILE_EOF = 18;

#desc Can't open error.
const ERR_CANT_OPEN = 19;

#desc Can't create error.
const ERR_CANT_CREATE = 20;

#desc Query failed error.
const ERR_QUERY_FAILED = 21;

#desc Already in use error.
const ERR_ALREADY_IN_USE = 22;

#desc Locked error.
const ERR_LOCKED = 23;

#desc Timeout error.
const ERR_TIMEOUT = 24;

#desc Can't connect error.
const ERR_CANT_CONNECT = 25;

#desc Can't resolve error.
const ERR_CANT_RESOLVE = 26;

#desc Connection error.
const ERR_CONNECTION_ERROR = 27;

#desc Can't acquire resource error.
const ERR_CANT_ACQUIRE_RESOURCE = 28;

#desc Can't fork process error.
const ERR_CANT_FORK = 29;

#desc Invalid data error.
const ERR_INVALID_DATA = 30;

#desc Invalid parameter error.
const ERR_INVALID_PARAMETER = 31;

#desc Already exists error.
const ERR_ALREADY_EXISTS = 32;

#desc Does not exist error.
const ERR_DOES_NOT_EXIST = 33;

#desc Database: Read error.
const ERR_DATABASE_CANT_READ = 34;

#desc Database: Write error.
const ERR_DATABASE_CANT_WRITE = 35;

#desc Compilation failed error.
const ERR_COMPILATION_FAILED = 36;

#desc Method not found error.
const ERR_METHOD_NOT_FOUND = 37;

#desc Linking failed error.
const ERR_LINK_FAILED = 38;

#desc Script failed error.
const ERR_SCRIPT_FAILED = 39;

#desc Cycling link (import cycle) error.
const ERR_CYCLIC_LINK = 40;

#desc Invalid declaration error.
const ERR_INVALID_DECLARATION = 41;

#desc Duplicate symbol error.
const ERR_DUPLICATE_SYMBOL = 42;

#desc Parse error.
const ERR_PARSE_ERROR = 43;

#desc Busy error.
const ERR_BUSY = 44;

#desc Skip error.
const ERR_SKIP = 45;

#desc Help error.
const ERR_HELP = 46;

#desc Bug error.
const ERR_BUG = 47;

#desc Printer on fire error. (This is an easter egg, no engine methods return this error code.)
const ERR_PRINTER_ON_FIRE = 48;

#desc No hint for the edited property.
const PROPERTY_HINT_NONE = 0;

#desc Hints that an integer or float property should be within a range specified via the hint string [code]"min,max"[/code] or [code]"min,max,step"[/code]. The hint string can optionally include [code]"or_greater"[/code] and/or [code]"or_less"[/code] to allow manual input going respectively above the max or below the min values. Example: [code]"-360,360,1,or_greater,or_less"[/code].
#desc Additionally, other keywords can be included: [code]"exp"[/code] for exponential range editing, [code]"radians"[/code] for editing radian angles in degrees, [code]"degrees"[/code] to hint at an angle and [code]"no_slider"[/code] to hide the slider.
const PROPERTY_HINT_RANGE = 1;

#desc Hints that an integer, float or string property is an enumerated value to pick in a list specified via a hint string.
#desc The hint string is a comma separated list of names such as [code]"Hello,Something,Else"[/code]. For integer and float properties, the first name in the list has value 0, the next 1, and so on. Explicit values can also be specified by appending [code]:integer[/code] to the name, e.g. [code]"Zero,One,Three:3,Four,Six:6"[/code].
const PROPERTY_HINT_ENUM = 2;

#desc Hints that a string property can be an enumerated value to pick in a list specified via a hint string such as [code]"Hello,Something,Else"[/code].
#desc Unlike [constant PROPERTY_HINT_ENUM] a property with this hint still accepts arbitrary values and can be empty. The list of values serves to suggest possible values.
const PROPERTY_HINT_ENUM_SUGGESTION = 3;

#desc Hints that a float property should be edited via an exponential easing function. The hint string can include [code]"attenuation"[/code] to flip the curve horizontally and/or [code]"positive_only"[/code] to exclude in/out easing and limit values to be greater than or equal to zero.
const PROPERTY_HINT_EXP_EASING = 4;

#desc Hints that a vector property should allow linking values (e.g. to edit both [code]x[/code] and [code]y[/code] together).
const PROPERTY_HINT_LINK = 5;

#desc Hints that an integer property is a bitmask with named bit flags. For example, to allow toggling bits 0, 1, 2 and 4, the hint could be something like [code]"Bit0,Bit1,Bit2,,Bit4"[/code].
const PROPERTY_HINT_FLAGS = 6;

#desc Hints that an integer property is a bitmask using the optionally named 2D render layers.
const PROPERTY_HINT_LAYERS_2D_RENDER = 7;

#desc Hints that an integer property is a bitmask using the optionally named 2D physics layers.
const PROPERTY_HINT_LAYERS_2D_PHYSICS = 8;

#desc Hints that an integer property is a bitmask using the optionally named 2D navigation layers.
const PROPERTY_HINT_LAYERS_2D_NAVIGATION = 9;

#desc Hints that an integer property is a bitmask using the optionally named 3D render layers.
const PROPERTY_HINT_LAYERS_3D_RENDER = 10;

#desc Hints that an integer property is a bitmask using the optionally named 3D physics layers.
const PROPERTY_HINT_LAYERS_3D_PHYSICS = 11;

#desc Hints that an integer property is a bitmask using the optionally named 3D navigation layers.
const PROPERTY_HINT_LAYERS_3D_NAVIGATION = 12;

#desc Hints that a string property is a path to a file. Editing it will show a file dialog for picking the path. The hint string can be a set of filters with wildcards like [code]"*.png,*.jpg"[/code].
const PROPERTY_HINT_FILE = 13;

#desc Hints that a string property is a path to a directory. Editing it will show a file dialog for picking the path.
const PROPERTY_HINT_DIR = 14;

#desc Hints that a string property is an absolute path to a file outside the project folder. Editing it will show a file dialog for picking the path. The hint string can be a set of filters with wildcards like [code]"*.png,*.jpg"[/code].
const PROPERTY_HINT_GLOBAL_FILE = 15;

#desc Hints that a string property is an absolute path to a directory outside the project folder. Editing it will show a file dialog for picking the path.
const PROPERTY_HINT_GLOBAL_DIR = 16;

#desc Hints that a property is an instance of a [Resource]-derived type, optionally specified via the hint string (e.g. [code]"Texture2D"[/code]). Editing it will show a popup menu of valid resource types to instantiate.
const PROPERTY_HINT_RESOURCE_TYPE = 17;

#desc Hints that a string property is text with line breaks. Editing it will show a text input field where line breaks can be typed.
const PROPERTY_HINT_MULTILINE_TEXT = 18;

#desc Hints that a string property is an [Expression].
const PROPERTY_HINT_EXPRESSION = 19;

#desc Hints that a string property should have a placeholder text visible on its input field, whenever the property is empty. The hint string is the placeholder text to use.
const PROPERTY_HINT_PLACEHOLDER_TEXT = 20;

#desc Hints that a color property should be edited without changing its alpha component, i.e. only R, G and B channels are edited.
const PROPERTY_HINT_COLOR_NO_ALPHA = 21;

#desc Hints that an image is compressed using lossy compression.
const PROPERTY_HINT_IMAGE_COMPRESS_LOSSY = 22;

#desc Hints that an image is compressed using lossless compression.
const PROPERTY_HINT_IMAGE_COMPRESS_LOSSLESS = 23;

const PROPERTY_HINT_OBJECT_ID = 24;

#desc Hint that a property represents a particular type. If a property is [constant TYPE_STRING], allows to set a type from the create dialog. If you need to create an [Array] to contain elements of a specific type, the [code]hint_string[/code] must encode nested types using [code]":"[/code] and [code]"/"[/code] for specifying [Resource] types. For instance:
#desc [codeblock]
#desc hint_string = "%s:" % [TYPE_INT] # Array of inteters.
#desc hint_string = "%s:%s:" % [TYPE_ARRAY, TYPE_REAL] # Two-dimensional array of floats.
#desc hint_string = "%s/%s:Resource" % [TYPE_OBJECT, TYPE_OBJECT] # Array of resources.
#desc hint_string = "%s:%s/%s:Resource" % [TYPE_ARRAY, TYPE_OBJECT, TYPE_OBJECT] # Two-dimensional array of resources.
#desc [/codeblock]
#desc [b]Note:[/b] The final colon is required to specify for properly detecting built-in types.
const PROPERTY_HINT_TYPE_STRING = 25;

const PROPERTY_HINT_NODE_PATH_TO_EDITED_NODE = 26;

const PROPERTY_HINT_METHOD_OF_VARIANT_TYPE = 27;

const PROPERTY_HINT_METHOD_OF_BASE_TYPE = 28;

const PROPERTY_HINT_METHOD_OF_INSTANCE = 29;

const PROPERTY_HINT_METHOD_OF_SCRIPT = 30;

const PROPERTY_HINT_PROPERTY_OF_VARIANT_TYPE = 31;

const PROPERTY_HINT_PROPERTY_OF_BASE_TYPE = 32;

const PROPERTY_HINT_PROPERTY_OF_INSTANCE = 33;

const PROPERTY_HINT_PROPERTY_OF_SCRIPT = 34;

const PROPERTY_HINT_OBJECT_TOO_BIG = 35;

const PROPERTY_HINT_NODE_PATH_VALID_TYPES = 36;

const PROPERTY_HINT_SAVE_FILE = 37;

const PROPERTY_HINT_GLOBAL_SAVE_FILE = 38;

const PROPERTY_HINT_INT_IS_OBJECTID = 39;

const PROPERTY_HINT_INT_IS_POINTER = 41;

const PROPERTY_HINT_ARRAY_TYPE = 40;

#desc Hints that a string property is a locale code. Editing it will show a locale dialog for picking language and country.
const PROPERTY_HINT_LOCALE_ID = 42;

#desc Hints that a dictionary property is string translation map. Dictionary keys are locale codes and, values are translated strings.
const PROPERTY_HINT_LOCALIZABLE_STRING = 43;

const PROPERTY_HINT_NODE_TYPE = 44;

#desc Hints that a quaternion property should disable the temporary euler editor.
const PROPERTY_HINT_HIDE_QUATERNION_EDIT = 45;

#desc Hints that a string property is a password, and every character is replaced with the secret character.
const PROPERTY_HINT_PASSWORD = 46;

const PROPERTY_HINT_MAX = 47;

const PROPERTY_USAGE_NONE = 0;

#desc The property is serialized and saved in the scene file (default).
const PROPERTY_USAGE_STORAGE = 2;

#desc The property is shown in the editor inspector (default).
const PROPERTY_USAGE_EDITOR = 4;

#desc The property can be checked in the editor inspector.
const PROPERTY_USAGE_CHECKABLE = 8;

#desc The property is checked in the editor inspector.
const PROPERTY_USAGE_CHECKED = 16;

#desc The property is a translatable string.
const PROPERTY_USAGE_INTERNATIONALIZED = 32;

#desc Used to group properties together in the editor. See [EditorInspector].
const PROPERTY_USAGE_GROUP = 64;

#desc Used to categorize properties together in the editor.
const PROPERTY_USAGE_CATEGORY = 128;

#desc Used to group properties together in the editor in a subgroup (under a group). See [EditorInspector].
const PROPERTY_USAGE_SUBGROUP = 256;

const PROPERTY_USAGE_CLASS_IS_BITFIELD = 512;

#desc The property does not save its state in [PackedScene].
const PROPERTY_USAGE_NO_INSTANCE_STATE = 1024;

#desc Editing the property prompts the user for restarting the editor.
const PROPERTY_USAGE_RESTART_IF_CHANGED = 2048;

#desc The property is a script variable which should be serialized and saved in the scene file.
const PROPERTY_USAGE_SCRIPT_VARIABLE = 4096;

const PROPERTY_USAGE_STORE_IF_NULL = 8192;

const PROPERTY_USAGE_ANIMATE_AS_TRIGGER = 16384;

const PROPERTY_USAGE_UPDATE_ALL_IF_MODIFIED = 32768;

const PROPERTY_USAGE_SCRIPT_DEFAULT_VALUE = 65536;

const PROPERTY_USAGE_CLASS_IS_ENUM = 131072;

const PROPERTY_USAGE_NIL_IS_VARIANT = 262144;

const PROPERTY_USAGE_INTERNAL = 524288;

const PROPERTY_USAGE_DO_NOT_SHARE_ON_DUPLICATE = 1048576;

const PROPERTY_USAGE_HIGH_END_GFX = 2097152;

const PROPERTY_USAGE_NODE_PATH_FROM_SCENE_ROOT = 4194304;

const PROPERTY_USAGE_RESOURCE_NOT_PERSISTENT = 8388608;

const PROPERTY_USAGE_KEYING_INCREMENTS = 16777216;

const PROPERTY_USAGE_DEFERRED_SET_RESOURCE = 33554432;

const PROPERTY_USAGE_EDITOR_INSTANTIATE_OBJECT = 67108864;

const PROPERTY_USAGE_EDITOR_BASIC_SETTING = 134217728;

#desc The property is read-only in the editor inspector.
const PROPERTY_USAGE_READ_ONLY = 268435456;

const PROPERTY_USAGE_ARRAY = 536870912;

#desc Default usage (storage, editor and network).
const PROPERTY_USAGE_DEFAULT = 6;

#desc Default usage for translatable strings (storage, editor, network and internationalized).
const PROPERTY_USAGE_DEFAULT_INTL = 38;

#desc Default usage but without showing the property in the editor (storage, network).
const PROPERTY_USAGE_NO_EDITOR = 2;

#desc Flag for a normal method.
const METHOD_FLAG_NORMAL = 1;

#desc Flag for an editor method.
const METHOD_FLAG_EDITOR = 2;

#desc Flag for a constant method.
const METHOD_FLAG_CONST = 4;

#desc Flag for a virtual method.
const METHOD_FLAG_VIRTUAL = 8;

const METHOD_FLAG_VARARG = 16;

const METHOD_FLAG_STATIC = 32;

#desc Used internally. Allows to not dump core virtuals such as [code]_notification[/code] to the JSON API.
const METHOD_FLAG_OBJECT_CORE = 64;

#desc Default method flags.
const METHOD_FLAGS_DEFAULT = 1;

#desc Variable is [code]null[/code].
const TYPE_NIL = 0;

#desc Variable is of type [bool].
const TYPE_BOOL = 1;

#desc Variable is of type [int].
const TYPE_INT = 2;

#desc Variable is of type [float] (real).
const TYPE_FLOAT = 3;

#desc Variable is of type [String].
const TYPE_STRING = 4;

#desc Variable is of type [Vector2].
const TYPE_VECTOR2 = 5;

#desc Variable is of type [Vector2i].
const TYPE_VECTOR2I = 6;

#desc Variable is of type [Rect2].
const TYPE_RECT2 = 7;

#desc Variable is of type [Rect2i].
const TYPE_RECT2I = 8;

#desc Variable is of type [Vector3].
const TYPE_VECTOR3 = 9;

#desc Variable is of type [Vector3i].
const TYPE_VECTOR3I = 10;

#desc Variable is of type [Transform2D].
const TYPE_TRANSFORM2D = 11;

const TYPE_VECTOR4 = 12;

const TYPE_VECTOR4I = 13;

#desc Variable is of type [Plane].
const TYPE_PLANE = 14;

#desc Variable is of type [Quaternion].
const TYPE_QUATERNION = 15;

#desc Variable is of type [AABB].
const TYPE_AABB = 16;

#desc Variable is of type [Basis].
const TYPE_BASIS = 17;

#desc Variable is of type [Transform3D].
const TYPE_TRANSFORM3D = 18;

const TYPE_PROJECTION = 19;

#desc Variable is of type [Color].
const TYPE_COLOR = 20;

#desc Variable is of type [StringName].
const TYPE_STRING_NAME = 21;

#desc Variable is of type [NodePath].
const TYPE_NODE_PATH = 22;

#desc Variable is of type [RID].
const TYPE_RID = 23;

#desc Variable is of type [Object].
const TYPE_OBJECT = 24;

#desc Variable is of type [Callable].
const TYPE_CALLABLE = 25;

#desc Variable is of type [Signal].
const TYPE_SIGNAL = 26;

#desc Variable is of type [Dictionary].
const TYPE_DICTIONARY = 27;

#desc Variable is of type [Array].
const TYPE_ARRAY = 28;

#desc Variable is of type [PackedByteArray].
const TYPE_PACKED_BYTE_ARRAY = 29;

#desc Variable is of type [PackedInt32Array].
const TYPE_PACKED_INT32_ARRAY = 30;

#desc Variable is of type [PackedInt64Array].
const TYPE_PACKED_INT64_ARRAY = 31;

#desc Variable is of type [PackedFloat32Array].
const TYPE_PACKED_FLOAT32_ARRAY = 32;

#desc Variable is of type [PackedFloat64Array].
const TYPE_PACKED_FLOAT64_ARRAY = 33;

#desc Variable is of type [PackedStringArray].
const TYPE_PACKED_STRING_ARRAY = 34;

#desc Variable is of type [PackedVector2Array].
const TYPE_PACKED_VECTOR2_ARRAY = 35;

#desc Variable is of type [PackedVector3Array].
const TYPE_PACKED_VECTOR3_ARRAY = 36;

#desc Variable is of type [PackedColorArray].
const TYPE_PACKED_COLOR_ARRAY = 37;

#desc Represents the size of the [enum Variant.Type] enum.
const TYPE_MAX = 38;

#desc Equality operator ([code]==[/code]).
const OP_EQUAL = 0;

#desc Inequality operator ([code]!=[/code]).
const OP_NOT_EQUAL = 1;

#desc Less than operator ([code]<[/code]).
const OP_LESS = 2;

#desc Less than or equal operator ([code]<=[/code]).
const OP_LESS_EQUAL = 3;

#desc Greater than operator ([code]>[/code]).
const OP_GREATER = 4;

#desc Greater than or equal operator ([code]>=[/code]).
const OP_GREATER_EQUAL = 5;

#desc Addition operator ([code]+[/code]).
const OP_ADD = 6;

#desc Subtraction operator ([code]-[/code]).
const OP_SUBTRACT = 7;

#desc Multiplication operator ([code]*[/code]).
const OP_MULTIPLY = 8;

#desc Division operator ([code]/[/code]).
const OP_DIVIDE = 9;

#desc Unary negation operator ([code]-[/code]).
const OP_NEGATE = 10;

#desc Unary plus operator ([code]+[/code]).
const OP_POSITIVE = 11;

#desc Remainder/modulo operator ([code]%[/code]).
const OP_MODULE = 12;

#desc Power operator ([code]**[/code]).
const OP_POWER = 13;

#desc Left shift operator ([code]<<[/code]).
const OP_SHIFT_LEFT = 14;

#desc Right shift operator ([code]>>[/code]).
const OP_SHIFT_RIGHT = 15;

#desc Bitwise AND operator ([code]&[/code]).
const OP_BIT_AND = 16;

#desc Bitwise OR operator ([code]|[/code]).
const OP_BIT_OR = 17;

#desc Bitwise XOR operator ([code]^[/code]).
const OP_BIT_XOR = 18;

#desc Bitwise NOT operator ([code]~[/code]).
const OP_BIT_NEGATE = 19;

#desc Logical AND operator ([code]and[/code] or [code]&&[/code]).
const OP_AND = 20;

#desc Logical OR operator ([code]or[/code] or [code]||[/code]).
const OP_OR = 21;

#desc Logical XOR operator (not implemented in GDScript).
const OP_XOR = 22;

#desc Logical NOT operator ([code]not[/code] or [code]![/code]).
const OP_NOT = 23;

#desc Logical IN operator ([code]in[/code]).
const OP_IN = 24;

#desc Represents the size of the [enum Variant.Operator] enum.
const OP_MAX = 25;


#desc The [AudioServer] singleton.
var AudioServer: AudioServer;

#desc The [CameraServer] singleton.
var CameraServer: CameraServer;

#desc The [ClassDB] singleton.
var ClassDB: ClassDB;

#desc The [DisplayServer] singleton.
var DisplayServer: DisplayServer;

#desc The [Engine] singleton.
var Engine: Engine;

#desc The [EngineDebugger] singleton.
var EngineDebugger: EngineDebugger;

#desc The [Geometry2D] singleton.
var Geometry2D: Geometry2D;

#desc The [Geometry3D] singleton.
var Geometry3D: Geometry3D;

#desc The [GodotSharp] singleton.
var GodotSharp: GodotSharp;

#desc The [IP] singleton.
var IP: IP;

#desc The [Input] singleton.
var Input: Input;

#desc The [InputMap] singleton.
var InputMap: InputMap;

#desc The [JavaClassWrapper] singleton.
#desc [b]Note:[/b] Only implemented on Android.
var JavaClassWrapper: JavaClassWrapper;

#desc The [JavaScriptBridge] singleton.
#desc [b]Note:[/b] Only implemented on the Web platform.
var JavaScriptBridge: JavaScriptBridge;

#desc The [Marshalls] singleton.
var Marshalls: Marshalls;

var NativeExtensionManager: NativeExtensionManager;

#desc The [NavigationMeshGenerator] singleton.
var NavigationMeshGenerator: NavigationMeshGenerator;

#desc The [NavigationServer2D] singleton.
var NavigationServer2D: NavigationServer2D;

#desc The [NavigationServer2D] singleton.
var NavigationServer3D: NavigationServer3D;

#desc The [OS] singleton.
var OS: OS;

#desc The [Performance] singleton.
var Performance: Performance;

#desc The [PhysicsServer2D] singleton.
var PhysicsServer2D: PhysicsServer2D;

#desc The [PhysicsServer2DManager] singleton.
var PhysicsServer2DManager: PhysicsServer2DManager;

#desc The [PhysicsServer3D] singleton.
var PhysicsServer3D: PhysicsServer3D;

#desc The [PhysicsServer3DManager] singleton.
var PhysicsServer3DManager: PhysicsServer3DManager;

#desc The [ProjectSettings] singleton.
var ProjectSettings: ProjectSettings;

#desc The [RenderingServer] singleton.
var RenderingServer: RenderingServer;

#desc The [ResourceLoader] singleton.
var ResourceLoader: ResourceLoader;

#desc The [ResourceSaver] singleton.
var ResourceSaver: ResourceSaver;

#desc The [ResourceUID] singleton.
var ResourceUID: ResourceUID;

#desc The [TextServerManager] singleton.
var TextServerManager: TextServerManager;

#desc The [ThemeDB] singleton.
var ThemeDB: ThemeDB;

#desc The [Time] singleton.
var Time: Time;

#desc The [TranslationServer] singleton.
var TranslationServer: TranslationServer;

#desc The [WorkerThreadPool] singleton.
var WorkerThreadPool: WorkerThreadPool;

#desc The [XRServer] singleton.
var XRServer: XRServer;



#desc Returns the absolute value of a [Variant] parameter [param x] (i.e. non-negative value). Variant types [int], [float] (real), [Vector2], [Vector2i], [Vector3] and [Vector3i] are supported.
#desc [codeblock]
#desc var a = abs(-1)
#desc # a is 1
#desc 
#desc var b = abs(-1.2)
#desc # b is 1.2
#desc 
#desc var c = abs(Vector2(-3.5, -4))
#desc # c is (3.5, 4)
#desc 
#desc var d = abs(Vector2i(-5, -6))
#desc # d is (5, 6)
#desc 
#desc var e = abs(Vector3(-7, 8.5, -3.8))
#desc # e is (7, 8.5, 3.8)
#desc 
#desc var f = abs(Vector3i(-7, -8, -9))
#desc # f is (7, 8, 9)
#desc [/codeblock]
func abs(x: Variant) -> Variant:
	pass;

#desc Returns the absolute value of float parameter [param x] (i.e. positive value).
#desc [codeblock]
#desc # a is 1.2
#desc var a = absf(-1.2)
#desc [/codeblock]
func absf(x: float) -> float:
	pass;

#desc Returns the absolute value of int parameter [param x] (i.e. positive value).
#desc [codeblock]
#desc # a is 1
#desc var a = absi(-1)
#desc [/codeblock]
func absi(x: int) -> int:
	pass;

#desc Returns the arc cosine of [param x] in radians. Use to get the angle of cosine [param x]. [param x] must be between [code]-1.0[/code] and [code]1.0[/code] (inclusive), otherwise, [method acos] will return [constant @GDScript.NAN].
#desc [codeblock]
#desc # c is 0.523599 or 30 degrees if converted with rad_to_deg(c)
#desc var c = acos(0.866025)
#desc [/codeblock]
func acos(x: float) -> float:
	pass;

#desc Returns the arc sine of [param x] in radians. Use to get the angle of sine [param x]. [param x] must be between [code]-1.0[/code] and [code]1.0[/code] (inclusive), otherwise, [method asin] will return [constant @GDScript.NAN].
#desc [codeblock]
#desc # s is 0.523599 or 30 degrees if converted with rad_to_deg(s)
#desc var s = asin(0.5)
#desc [/codeblock]
func asin(x: float) -> float:
	pass;

#desc Returns the arc tangent of [param x] in radians. Use it to get the angle from an angle's tangent in trigonometry.
#desc The method cannot know in which quadrant the angle should fall. See [method atan2] if you have both [code]y[/code] and [code]x[/code].
#desc [codeblock]
#desc var a = atan(0.5) # a is 0.463648
#desc [/codeblock]
#desc If [param x] is between [code]-PI / 2[/code] and [code]PI / 2[/code] (inclusive), [code]atan(tan(x))[/code] is equal to [param x].
func atan(x: float) -> float:
	pass;

#desc Returns the arc tangent of [code]y/x[/code] in radians. Use to get the angle of tangent [code]y/x[/code]. To compute the value, the method takes into account the sign of both arguments in order to determine the quadrant.
#desc Important note: The Y coordinate comes first, by convention.
#desc [codeblock]
#desc var a = atan2(0, -1) # a is 3.141593
#desc [/codeblock]
func atan2(y: float, x: float) -> float:
	pass;

#desc Returns the point at the given [param t] on a one-dimnesional [url=https://en.wikipedia.org/wiki/B%C3%A9zier_curve]Bezier curve[/url] defined by the given [param control_1], [param control_2], and [param end] points.
func bezier_interpolate(start: float, control_1: float, control_2: float, end: float, t: float) -> float:
	pass;

#desc Decodes a byte array back to a [Variant] value, without decoding objects.
#desc [b]Note:[/b] If you need object deserialization, see [method bytes_to_var_with_objects].
func bytes_to_var(bytes: PackedByteArray) -> Variant:
	pass;

#desc Decodes a byte array back to a [Variant] value. Decoding objects is allowed.
#desc [b]Warning:[/b] Deserialized object can contain code which gets executed. Do not use this option if the serialized object comes from untrusted sources to avoid potential security threats (remote code execution).
func bytes_to_var_with_objects(bytes: PackedByteArray) -> Variant:
	pass;

#desc Rounds [param x] upward (towards positive infinity), returning the smallest whole number that is not less than [param x]. Supported types: [int], [float], [Vector2], [Vector3], [Vector4].
#desc [codeblock]
#desc var i = ceil(1.45) # i is 2.0
#desc i = ceil(1.001)    # i is 2.0
#desc [/codeblock]
#desc See also [method floor], [method round], and [method snapped].
#desc [b]Note:[/b] For better type safety, you can use [method ceilf], [method ceili], [method Vector2.ceil], [method Vector3.ceil] or [method Vector4.ceil] instead.
func ceil(x: Variant) -> Variant:
	pass;

#desc Rounds [param x] upward (towards positive infinity), returning the smallest whole number that is not less than [param x].
#desc A type-safe version of [method ceil], specialzied in floats.
func ceilf(x: float) -> float:
	pass;

#desc Rounds [param x] upward (towards positive infinity), returning the smallest whole number that is not less than [param x].
#desc A type-safe version of [method ceil] that returns integer.
func ceili(x: float) -> int:
	pass;

#desc Clamps the [Variant] [param value] and returns a value not less than [param min] and not more than [param max]. Variant types [int], [float] (real), [Vector2], [Vector2i], [Vector3] and [Vector3i] are supported.
#desc [codeblock]
#desc var a = clamp(-10, -1, 5)
#desc # a is -1
#desc 
#desc var b = clamp(8.1, 0.9, 5.5)
#desc # b is 5.5
#desc 
#desc var c = clamp(Vector2(-3.5, -4), Vector2(-3.2, -2), Vector2(2, 6.5))
#desc # c is (-3.2, -2)
#desc 
#desc var d = clamp(Vector2i(7, 8), Vector2i(-3, -2), Vector2i(2, 6))
#desc # d is (2, 6)
#desc 
#desc var e = clamp(Vector3(-7, 8.5, -3.8), Vector3(-3, -2, 5.4), Vector3(-2, 6, -4.1))
#desc # e is (-3, -2, 5.4)
#desc 
#desc var f = clamp(Vector3i(-7, -8, -9), Vector3i(-1, 2, 3), Vector3i(-4, -5, -6))
#desc # f is (-4, -5, -6)
#desc [/codeblock]
func clamp(value: Variant, min: Variant, max: Variant) -> Variant:
	pass;

#desc Clamps the float [param value] and returns a value not less than [param min] and not more than [param max].
#desc [codeblock]
#desc var speed = 42.1
#desc # a is 20.0
#desc var a = clampf(speed, 1.0, 20.0)
#desc 
#desc speed = -10.0
#desc # a is -1.0
#desc a = clampf(speed, -1.0, 1.0)
#desc [/codeblock]
func clampf(value: float, min: float, max: float) -> float:
	pass;

#desc Clamps the integer [param value] and returns a value not less than [param min] and not more than [param max].
#desc [codeblock]
#desc var speed = 42
#desc # a is 20
#desc var a = clampi(speed, 1, 20)
#desc 
#desc speed = -10
#desc # a is -1
#desc a = clampi(speed, -1, 1)
#desc [/codeblock]
func clampi(value: int, min: int, max: int) -> int:
	pass;

#desc Returns the cosine of angle [param angle_rad] in radians.
#desc [codeblock]
#desc cos(PI * 2)         # Returns 1.0
#desc cos(PI)             # Returns -1.0
#desc cos(deg_to_rad(90)) # Returns 0.0
#desc [/codeblock]
func cos(angle_rad: float) -> float:
	pass;

#desc Returns the hyperbolic cosine of [param x] in radians.
#desc [codeblock]
#desc # Prints 1.543081
#desc print(cosh(1))
#desc [/codeblock]
func cosh(x: float) -> float:
	pass;

#desc Cubic interpolates between two values by the factor defined in [param weight] with pre and post values.
func cubic_interpolate(from: float, to: float, pre: float, post: float, weight: float) -> float:
	pass;

#desc Cubic interpolates between two rotation values with shortest path by the factor defined in [param weight] with pre and post values. See also [method lerp_angle].
func cubic_interpolate_angle(from: float, to: float, pre: float, post: float, weight: float) -> float:
	pass;

#desc Cubic interpolates between two rotation values with shortest path by the factor defined in [param weight] with pre and post values. See also [method lerp_angle].
#desc It can perform smoother interpolation than [code]cubic_interpolate()[/code] by the time values.
func cubic_interpolate_angle_in_time(from: float, to: float, pre: float, post: float, weight: float, to_t: float, pre_t: float, post_t: float) -> float:
	pass;

#desc Cubic interpolates between two values by the factor defined in [param weight] with pre and post values.
#desc It can perform smoother interpolation than [code]cubic_interpolate()[/code] by the time values.
func cubic_interpolate_in_time(from: float, to: float, pre: float, post: float, weight: float, to_t: float, pre_t: float, post_t: float) -> float:
	pass;

#desc Converts from decibels to linear energy (audio).
func db_to_linear(db: float) -> float:
	pass;

#desc Converts an angle expressed in degrees to radians.
#desc [codeblock]
#desc # r is 3.141593
#desc var r = deg_to_rad(180)
#desc [/codeblock]
func deg_to_rad(deg: float) -> float:
	pass;

#desc Returns an "eased" value of [param x] based on an easing function defined with [param curve]. This easing function is based on an exponent. The [param curve] can be any floating-point number, with specific values leading to the following behaviors:
#desc [codeblock]
#desc - Lower than -1.0 (exclusive): Ease in-out
#desc - 1.0: Linear
#desc - Between -1.0 and 0.0 (exclusive): Ease out-in
#desc - 0.0: Constant
#desc - Between 0.0 to 1.0 (exclusive): Ease out
#desc - 1.0: Linear
#desc - Greater than 1.0 (exclusive): Ease in
#desc [/codeblock]
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/ease_cheatsheet.png]ease() curve values cheatsheet[/url]
#desc See also [method smoothstep]. If you need to perform more advanced transitions, use [method Tween.interpolate_value].
func ease(x: float, curve: float) -> float:
	pass;

#desc Returns a human-readable name for the given error code.
func error_string(error: int) -> String:
	pass;

#desc The natural exponential function. It raises the mathematical constant [b]e[/b] to the power of [param x] and returns it.
#desc [b]e[/b] has an approximate value of 2.71828, and can be obtained with [code]exp(1)[/code].
#desc For exponents to other bases use the method [method pow].
#desc [codeblock]
#desc var a = exp(2) # Approximately 7.39
#desc [/codeblock]
func exp(x: float) -> float:
	pass;

#desc Rounds [param x] downward (towards negative infinity), returning the largest whole number that is not more than [param x]. Supported types: [int], [float], [Vector2], [Vector3], [Vector4].
#desc [codeblock]
#desc # a is 2.0
#desc var a = floor(2.99)
#desc # a is -3.0
#desc a = floor(-2.99)
#desc [/codeblock]
#desc See also [method ceil], [method round], and [method snapped].
#desc [b]Note:[/b] For better type safety, you can use [method floorf], [method floori], [method Vector2.floor], [method Vector3.floor] or [method Vector4.floor] instead.
func floor(x: Variant) -> Variant:
	pass;

#desc Rounds [param x] downward (towards negative infinity), returning the largest whole number that is not more than [param x].
#desc A type-safe version of [method floor], specialzied in floats.
func floorf(x: float) -> float:
	pass;

#desc Rounds [param x] downward (towards negative infinity), returning the largest whole number that is not more than [param x].
#desc Equivalent of doing [code]int(x)[/code].
func floori(x: float) -> int:
	pass;

#desc Returns the floating-point remainder of [code]x/y[/code], keeping the sign of [param x].
#desc [codeblock]
#desc # Remainder is 1.5
#desc var remainder = fmod(7, 5.5)
#desc [/codeblock]
#desc For the integer remainder operation, use the [code]%[/code] operator.
func fmod(x: float, y: float) -> float:
	pass;

#desc Returns the floating-point modulus of [code]x/y[/code] that wraps equally in positive and negative.
#desc [codeblock]
#desc for i in 7:
#desc var x = 0.5 * i - 1.5
#desc print("%4.1f %4.1f %4.1f" % [x, fmod(x, 1.5), fposmod(x, 1.5)])
#desc [/codeblock]
#desc Produces:
#desc [codeblock]
#desc -1.5 -0.0  0.0
#desc -1.0 -1.0  0.5
#desc -0.5 -0.5  1.0
#desc 0.0  0.0  0.0
#desc 0.5  0.5  0.5
#desc 1.0  1.0  1.0
#desc 1.5  0.0  0.0
#desc [/codeblock]
func fposmod(x: float, y: float) -> float:
	pass;

#desc Returns the integer hash of the variable passed.
#desc [codeblock]
#desc print(hash("a")) # Prints 177670
#desc [/codeblock]
func hash(variable: Variant) -> int:
	pass;

#desc Returns the Object that corresponds to [param instance_id]. All Objects have a unique instance ID.
#desc [codeblock]
#desc var foo = "bar"
#desc func _ready():
#desc var id = get_instance_id()
#desc var inst = instance_from_id(id)
#desc print(inst.foo) # Prints bar
#desc [/codeblock]
func instance_from_id(instance_id: int) -> Object:
	pass;

#desc Returns an interpolation or extrapolation factor considering the range specified in [param from] and [param to], and the interpolated value specified in [param weight]. The returned value will be between [code]0.0[/code] and [code]1.0[/code] if [param weight] is between [param from] and [param to] (inclusive). If [param weight] is located outside this range, then an extrapolation factor will be returned (return value lower than [code]0.0[/code] or greater than [code]1.0[/code]). Use [method clamp] on the result of [method inverse_lerp] if this is not desired.
#desc [codeblock]
#desc # The interpolation ratio in the `lerp()` call below is 0.75.
#desc var middle = lerp(20, 30, 0.75)
#desc # `middle` is now 27.5.
#desc # Now, we pretend to have forgotten the original ratio and want to get it back.
#desc var ratio = inverse_lerp(20, 30, 27.5)
#desc # `ratio` is now 0.75.
#desc [/codeblock]
#desc See also [method lerp] which performs the reverse of this operation, and [method remap] to map a continuous series of values to another.
func inverse_lerp(from: float, to: float, weight: float) -> float:
	pass;

#desc Returns [code]true[/code] if [param a] and [param b] are approximately equal to each other.
#desc Here, approximately equal means that [param a] and [param b] are within a small internal epsilon of each other, which scales with the magnitude of the numbers.
#desc Infinity values of the same sign are considered equal.
func is_equal_approx(a: float, b: float) -> bool:
	pass;

#desc Returns whether [param x] is an infinity value (either positive infinity or negative infinity).
func is_inf(x: float) -> bool:
	pass;

#desc Returns [code]true[/code] if the Object that corresponds to [param id] is a valid object (e.g. has not been deleted from memory). All Objects have a unique instance ID.
func is_instance_id_valid(id: int) -> bool:
	pass;

#desc Returns whether [param instance] is a valid object (e.g. has not been deleted from memory).
func is_instance_valid(instance: Variant) -> bool:
	pass;

#desc Returns whether [param x] is a NaN ("Not a Number" or invalid) value.
func is_nan(x: float) -> bool:
	pass;

#desc Returns [code]true[/code] if [param x] is zero or almost zero.
#desc This method is faster than using [method is_equal_approx] with one value as zero.
func is_zero_approx(x: float) -> bool:
	pass;

#desc Linearly interpolates between two values by the factor defined in [param weight]. To perform interpolation, [param weight] should be between [code]0.0[/code] and [code]1.0[/code] (inclusive). However, values outside this range are allowed and can be used to perform [i]extrapolation[/i]. Use [method clamp] on the result of [method lerp] if this is not desired.
#desc Both [param from] and [param to] must have matching types. Supported types: [float], [Vector2], [Vector3], [Vector4], [Color], [Quaternion], [Basis].
#desc [codeblock]
#desc lerp(0, 4, 0.75) # Returns 3.0
#desc [/codeblock]
#desc See also [method inverse_lerp] which performs the reverse of this operation. To perform eased interpolation with [method lerp], combine it with [method ease] or [method smoothstep]. See also [method remap] to map a continuous series of values to another.
#desc [b]Note:[/b] For better type safety, you can use [method lerpf], [method Vector2.lerp], [method Vector3.lerp], [method Vector4.lerp], [method Color.lerp], [method Quaternion.slerp] or [method Basis.slerp] instead.
func lerp(from: Variant, to: Variant, weight: Variant) -> Variant:
	pass;

#desc Linearly interpolates between two angles (in radians) by a normalized value.
#desc Similar to [method lerp], but interpolates correctly when the angles wrap around [constant @GDScript.TAU]. To perform eased interpolation with [method lerp_angle], combine it with [method ease] or [method smoothstep].
#desc [codeblock]
#desc extends Sprite
#desc var elapsed = 0.0
#desc func _process(delta):
#desc var min_angle = deg_to_rad(0.0)
#desc var max_angle = deg_to_rad(90.0)
#desc rotation = lerp_angle(min_angle, max_angle, elapsed)
#desc elapsed += delta
#desc [/codeblock]
#desc [b]Note:[/b] This method lerps through the shortest path between [param from] and [param to]. However, when these two angles are approximately [code]PI + k * TAU[/code] apart for any integer [code]k[/code], it's not obvious which way they lerp due to floating-point precision errors. For example, [code]lerp_angle(0, PI, weight)[/code] lerps counter-clockwise, while [code]lerp_angle(0, PI + 5 * TAU, weight)[/code] lerps clockwise.
func lerp_angle(from: float, to: float, weight: float) -> float:
	pass;

#desc Linearly interpolates between two values by the factor defined in [param weight]. To perform interpolation, [param weight] should be between [code]0.0[/code] and [code]1.0[/code] (inclusive). However, values outside this range are allowed and can be used to perform [i]extrapolation[/i].
#desc [codeblock]
#desc lerp(0, 4, 0.75) # Returns 3.0
#desc [/codeblock]
#desc See also [method inverse_lerp] which performs the reverse of this operation. To perform eased interpolation with [method lerp], combine it with [method ease] or [method smoothstep].
func lerpf(from: float, to: float, weight: float) -> float:
	pass;

#desc Converts from linear energy to decibels (audio). This can be used to implement volume sliders that behave as expected (since volume isn't linear). Example:
#desc [codeblock]
#desc # "Slider" refers to a node that inherits Range such as HSlider or VSlider.
#desc # Its range must be configured to go from 0 to 1.
#desc # Change the bus name if you'd like to change the volume of a specific bus only.
#desc AudioServer.set_bus_volume_db(AudioServer.get_bus_index("Master"), linear_to_db($Slider.value))
#desc [/codeblock]
func linear_to_db(lin: float) -> float:
	pass;

#desc Natural logarithm. The amount of time needed to reach a certain level of continuous growth.
#desc [b]Note:[/b] This is not the same as the "log" function on most calculators, which uses a base 10 logarithm.
#desc [codeblock]
#desc log(10) # Returns 2.302585
#desc [/codeblock]
#desc [b]Note:[/b] The logarithm of [code]0[/code] returns [code]-inf[/code], while negative values return [code]-nan[/code].
func log(x: float) -> float:
	pass;

#desc Returns the maximum of the given values. This method can take any number of arguments.
#desc [codeblock]
#desc max(1, 7, 3, -6, 5) # Returns 7
#desc [/codeblock]
vararg func max() -> Variant:
	pass;

#desc Returns the maximum of two float values.
#desc [codeblock]
#desc maxf(3.6, 24) # Returns 24.0
#desc maxf(-3.99, -4) # Returns -3.99
#desc [/codeblock]
func maxf(a: float, b: float) -> float:
	pass;

#desc Returns the maximum of two int values.
#desc [codeblock]
#desc maxi(1, 2) # Returns 2
#desc maxi(-3, -4) # Returns -3
#desc [/codeblock]
func maxi(a: int, b: int) -> int:
	pass;

#desc Returns the minimum of the given values. This method can take any number of arguments.
#desc [codeblock]
#desc min(1, 7, 3, -6, 5) # Returns -6
#desc [/codeblock]
vararg func min() -> Variant:
	pass;

#desc Returns the minimum of two float values.
#desc [codeblock]
#desc minf(3.6, 24) # Returns 3.6
#desc minf(-3.99, -4) # Returns -4.0
#desc [/codeblock]
func minf(a: float, b: float) -> float:
	pass;

#desc Returns the minimum of two int values.
#desc [codeblock]
#desc mini(1, 2) # Returns 1
#desc mini(-3, -4) # Returns -4
#desc [/codeblock]
func mini(a: int, b: int) -> int:
	pass;

#desc Moves [param from] toward [param to] by the [param delta] value.
#desc Use a negative [param delta] value to move away.
#desc [codeblock]
#desc move_toward(5, 10, 4) # Returns 9
#desc move_toward(10, 5, 4) # Returns 6
#desc move_toward(10, 5, -1.5) # Returns 11.5
#desc [/codeblock]
func move_toward(from: float, to: float, delta: float) -> float:
	pass;

#desc Returns the nearest equal or larger power of 2 for integer [param value].
#desc In other words, returns the smallest value [code]a[/code] where [code]a = pow(2, n)[/code] such that [code]value <= a[/code] for some non-negative integer [code]n[/code].
#desc [codeblock]
#desc nearest_po2(3) # Returns 4
#desc nearest_po2(4) # Returns 4
#desc nearest_po2(5) # Returns 8
#desc 
#desc nearest_po2(0) # Returns 0 (this may not be what you expect)
#desc nearest_po2(-1) # Returns 0 (this may not be what you expect)
#desc [/codeblock]
#desc [b]Warning:[/b] Due to the way it is implemented, this function returns [code]0[/code] rather than [code]1[/code] for non-positive values of [param value] (in reality, 1 is the smallest integer power of 2).
func nearest_po2(value: int) -> int:
	pass;

#desc Returns the [param value] wrapped between [code]0[/code] and the [param length]. If the limit is reached, the next value the function returned is decreased to the [code]0[/code] side or increased to the [param length] side (like a triangle wave). If [param length] is less than zero, it becomes positive.
#desc [codeblock]
#desc pingpong(-3.0, 3.0) # Returns 3
#desc pingpong(-2.0, 3.0) # Returns 2
#desc pingpong(-1.0, 3.0) # Returns 1
#desc pingpong(0.0, 3.0) # Returns 0
#desc pingpong(1.0, 3.0) # Returns 1
#desc pingpong(2.0, 3.0) # Returns 2
#desc pingpong(3.0, 3.0) # Returns 3
#desc pingpong(4.0, 3.0) # Returns 2
#desc pingpong(5.0, 3.0) # Returns 1
#desc pingpong(6.0, 3.0) # Returns 0
#desc [/codeblock]
func pingpong(value: float, length: float) -> float:
	pass;

#desc Returns the integer modulus of [code]x/y[/code] that wraps equally in positive and negative.
#desc [codeblock]
#desc for i in range(-3, 4):
#desc print("%2d %2d %2d" % [i, i % 3, posmod(i, 3)])
#desc [/codeblock]
#desc Produces:
#desc [codeblock]
#desc -3  0  0
#desc -2 -2  1
#desc -1 -1  2
#desc 0  0  0
#desc 1  1  1
#desc 2  2  2
#desc 3  0  0
#desc [/codeblock]
func posmod(x: int, y: int) -> int:
	pass;

#desc Returns the result of [param base] raised to the power of [param exp].
#desc [codeblock]
#desc pow(2, 5) # Returns 32
#desc [/codeblock]
func pow(base: float, exp: float) -> float:
	pass;

#desc Converts one or more arguments of any type to string in the best way possible and prints them to the console.
#desc [codeblock]
#desc var a = [1, 2, 3]
#desc print("a", "b", a) # Prints ab[1, 2, 3]
#desc [/codeblock]
#desc [b]Note:[/b] Consider using [method push_error] and [method push_warning] to print error and warning messages instead of [method print]. This distinguishes them from print messages used for debugging purposes, while also displaying a stack trace when an error or warning is printed.
vararg func print() -> void:
	pass;

#desc Converts one or more arguments of any type to string in the best way possible and prints them to the console. The following BBCode tags are supported: b, i, u, s, indent, code, url, center, right, color, bgcolor, fgcolor. Color tags only support named colors such as [code]red[/code], [i]not[/i] hexadecimal color codes. Unsupported tags will be left as-is in standard output.
#desc When printing to standard output, the supported subset of BBCode is converted to ANSI escape codes for the terminal emulator to display. Displaying ANSI escape codes is currently only supported on Linux and macOS. Support for ANSI escape codes may vary across terminal emulators, especially for italic and strikethrough.
#desc [codeblock]
#desc print_rich("[code][b]Hello world![/b][/code]") # Prints out: [b]Hello world![/b]
#desc [/codeblock]
#desc [b]Note:[/b] Consider using [method push_error] and [method push_warning] to print error and warning messages instead of [method print] or [method print_rich]. This distinguishes them from print messages used for debugging purposes, while also displaying a stack trace when an error or warning is printed.
vararg func print_rich() -> void:
	pass;

#desc If verbose mode is enabled ([method OS.is_stdout_verbose] returning [code]true[/code]), converts one or more arguments of any type to string in the best way possible and prints them to the console.
vararg func print_verbose() -> void:
	pass;

#desc Prints one or more arguments to strings in the best way possible to standard error line.
#desc [codeblock]
#desc printerr("prints to stderr")
#desc [/codeblock]
vararg func printerr() -> void:
	pass;

#desc Prints one or more arguments to strings in the best way possible to console. No newline is added at the end.
#desc [codeblock]
#desc printraw("A")
#desc printraw("B")
#desc # Prints AB
#desc [/codeblock]
#desc [b]Note:[/b] Due to limitations with Godot's built-in console, this only prints to the terminal. If you need to print in the editor, use another method, such as [method print].
vararg func printraw() -> void:
	pass;

#desc Prints one or more arguments to the console with a space between each argument.
#desc [codeblock]
#desc prints("A", "B", "C") # Prints A B C
#desc [/codeblock]
vararg func prints() -> void:
	pass;

#desc Prints one or more arguments to the console with a tab between each argument.
#desc [codeblock]
#desc printt("A", "B", "C") # Prints A       B       C
#desc [/codeblock]
vararg func printt() -> void:
	pass;

#desc Pushes an error message to Godot's built-in debugger and to the OS terminal.
#desc [codeblock]
#desc push_error("test error") # Prints "test error" to debugger and terminal as error call
#desc [/codeblock]
#desc [b]Note:[/b] Errors printed this way will not pause project execution. To print an error message and pause project execution in debug builds, use [code]assert(false, "test error")[/code] instead.
vararg func push_error() -> void:
	pass;

#desc Pushes a warning message to Godot's built-in debugger and to the OS terminal.
#desc [codeblock]
#desc push_warning("test warning") # Prints "test warning" to debugger and terminal as warning call
#desc [/codeblock]
vararg func push_warning() -> void:
	pass;

#desc Converts an angle expressed in radians to degrees.
#desc [codeblock]
#desc rad_to_deg(0.523599) # Returns 30
#desc [/codeblock]
func rad_to_deg(rad: float) -> float:
	pass;

#desc Random from seed: pass a [param seed], and an array with both number and new seed is returned. "Seed" here refers to the internal state of the pseudo random number generator. The internal state of the current implementation is 64 bits.
func rand_from_seed(seed: int) -> PackedInt64Array:
	pass;

#desc Returns a random floating point value between [code]0.0[/code] and [code]1.0[/code] (inclusive).
#desc [codeblock]
#desc randf() # Returns e.g. 0.375671
#desc [/codeblock]
func randf() -> float:
	pass;

#desc Returns a random floating point value on the interval between [param from] and [param to] (inclusive).
#desc [codeblock]
#desc prints(randf_range(-10, 10), randf_range(-10, 10)) # Prints e.g. -3.844535 7.45315
#desc [/codeblock]
func randf_range(from: float, to: float) -> float:
	pass;

#desc Returns a normally-distributed pseudo-random floating point value using Box-Muller transform with the specified [param mean] and a standard [param deviation]. This is also called Gaussian distribution.
func randfn(mean: float, deviation: float) -> float:
	pass;

#desc Returns a random unsigned 32-bit integer. Use remainder to obtain a random value in the interval [code][0, N - 1][/code] (where N is smaller than 2^32).
#desc [codeblock]
#desc randi()           # Returns random integer between 0 and 2^32 - 1
#desc randi() % 20      # Returns random integer between 0 and 19
#desc randi() % 100     # Returns random integer between 0 and 99
#desc randi() % 100 + 1 # Returns random integer between 1 and 100
#desc [/codeblock]
func randi() -> int:
	pass;

#desc Returns a random signed 32-bit integer between [param from] and [param to] (inclusive). If [param to] is lesser than [param from], they are swapped.
#desc [codeblock]
#desc print(randi_range(0, 1)) # Prints 0 or 1
#desc print(randi_range(-10, 1000)) # Prints any number from -10 to 1000
#desc [/codeblock]
func randi_range(from: int, to: int) -> int:
	pass;

#desc Randomizes the seed (or the internal state) of the random number generator. Current implementation reseeds using a number based on time.
#desc [b]Note:[/b] This method is called automatically when the project is run. If you need to fix the seed to have reproducible results, use [method seed] to initialize the random number generator.
func randomize() -> void:
	pass;

#desc Maps a [param value] from range [code][istart, istop][/code] to [code][ostart, ostop][/code]. See also [method lerp] and [method inverse_lerp]. If [param value] is outside [code][istart, istop][/code], then the resulting value will also be outside [code][ostart, ostop][/code]. Use [method clamp] on the result of [method remap] if this is not desired.
#desc [codeblock]
#desc remap(75, 0, 100, -1, 1) # Returns 0.5
#desc [/codeblock]
#desc For complex use cases where you need multiple ranges, consider using [Curve] or [Gradient] instead.
func remap(value: float, istart: float, istop: float, ostart: float, ostop: float) -> float:
	pass;

#desc Allocate a unique ID which can be used by the implementation to construct a RID. This is used mainly from native extensions to implement servers.
func rid_allocate_id() -> int:
	pass;

#desc Create a RID from an int64. This is used mainly from native extensions to build servers.
func rid_from_int64(base: int) -> RID:
	pass;

#desc Rounds [param x] to the nearest whole number, with halfway cases rounded away from zero. Supported types: [int], [float], [Vector2], [Vector3], [Vector4].
#desc [codeblock]
#desc round(2.4) # Returns 2
#desc round(2.5) # Returns 3
#desc round(2.6) # Returns 3
#desc [/codeblock]
#desc See also [method floor], [method ceil], and [method snapped].
#desc [b]Note:[/b] For better type safety, you can use [method roundf], [method roundi], [method Vector2.round], [method Vector3.round] or [method Vector4.round] instead.
func round(x: Variant) -> Variant:
	pass;

#desc Rounds [param x] to the nearest whole number, with halfway cases rounded away from zero.
#desc A type-safe version of [method round], specialzied in floats.
func roundf(x: float) -> float:
	pass;

#desc Rounds [param x] to the nearest whole number, with halfway cases rounded away from zero.
#desc A type-safe version of [method round] that returns integer.
func roundi(x: float) -> int:
	pass;

#desc Sets seed for the random number generator.
#desc [codeblock]
#desc var my_seed = "Godot Rocks"
#desc seed(my_seed.hash())
#desc [/codeblock]
func seed(base: int) -> void:
	pass;

#desc Returns the sign of [param x] as same type of [Variant] as [param x] with each component being -1, 0 and 1 for each negative, zero and positive values respectivelu. Variant types [int], [float] (real), [Vector2], [Vector2i], [Vector3] and [Vector3i] are supported.
#desc [codeblock]
#desc sign(-6.0) # Returns -1
#desc sign(0.0)  # Returns 0
#desc sign(6.0)  # Returns 1
#desc 
#desc sign(Vector3(-6.0, 0.0, 6.0) # Returns (-1, 0, 1)
#desc [/codeblock]
func sign(x: Variant) -> Variant:
	pass;

#desc Returns the sign of [param x] as a float: -1.0 or 1.0. Returns 0.0 if [param x] is 0.
#desc [codeblock]
#desc sign(-6.0) # Returns -1.0
#desc sign(0.0)  # Returns 0.0
#desc sign(6.0)  # Returns 1.0
#desc [/codeblock]
func signf(x: float) -> float:
	pass;

#desc Returns the sign of [param x] as an integer: -1 or 1. Returns 0 if [param x] is 0.
#desc [codeblock]
#desc sign(-6) # Returns -1
#desc sign(0)  # Returns 0
#desc sign(6)  # Returns 1
#desc [/codeblock]
func signi(x: int) -> int:
	pass;

#desc Returns the sine of angle [param angle_rad] in radians.
#desc [codeblock]
#desc sin(0.523599)       # Returns 0.5
#desc sin(deg_to_rad(90)) # Returns 1.0
#desc [/codeblock]
func sin(angle_rad: float) -> float:
	pass;

#desc Returns the hyperbolic sine of [param x].
#desc [codeblock]
#desc var a = log(2.0) # Returns 0.693147
#desc sinh(a) # Returns 0.75
#desc [/codeblock]
func sinh(x: float) -> float:
	pass;

#desc Returns the result of smoothly interpolating the value of [param x] between [code]0[/code] and [code]1[/code], based on the where [param x] lies with respect to the edges [param from] and [param to].
#desc The return value is [code]0[/code] if [code]x <= from[/code], and [code]1[/code] if [code]x >= to[/code]. If [param x] lies between [param from] and [param to], the returned value follows an S-shaped curve that maps [param x] between [code]0[/code] and [code]1[/code].
#desc This S-shaped curve is the cubic Hermite interpolator, given by [code]f(y) = 3*y^2 - 2*y^3[/code] where [code]y = (x-from) / (to-from)[/code].
#desc [codeblock]
#desc smoothstep(0, 2, -5.0) # Returns 0.0
#desc smoothstep(0, 2, 0.5) # Returns 0.15625
#desc smoothstep(0, 2, 1.0) # Returns 0.5
#desc smoothstep(0, 2, 2.0) # Returns 1.0
#desc [/codeblock]
#desc Compared to [method ease] with a curve value of [code]-1.6521[/code], [method smoothstep] returns the smoothest possible curve with no sudden changes in the derivative. If you need to perform more advanced transitions, use [Tween] or [AnimationPlayer].
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/smoothstep_ease_comparison.png]Comparison between smoothstep() and ease(x, -1.6521) return values[/url]
func smoothstep(from: float, to: float, x: float) -> float:
	pass;

#desc Snaps float value [param x] to a given [param step]. This can also be used to round a floating point number to an arbitrary number of decimals.
#desc [codeblock]
#desc snapped(100, 32) # Returns 96
#desc snapped(3.14159, 0.01) # Returns 3.14
#desc [/codeblock]
#desc See also [method ceil], [method floor], and [method round].
func snapped(x: float, step: float) -> float:
	pass;

#desc Returns the square root of [param x], where [param x] is a non-negative number.
#desc [codeblock]
#desc sqrt(9) # Returns 3
#desc [/codeblock]
#desc [b]Note:[/b] Negative values of [param x] return NaN. If you need negative inputs, use [code]System.Numerics.Complex[/code] in C#.
func sqrt(x: float) -> float:
	pass;

#desc Returns the position of the first non-zero digit, after the decimal point. Note that the maximum return value is 10, which is a design decision in the implementation.
#desc [codeblock]
#desc # n is 0
#desc var n = step_decimals(5)
#desc # n is 4
#desc n = step_decimals(1.0005)
#desc # n is 9
#desc n = step_decimals(0.000000005)
#desc [/codeblock]
func step_decimals(x: float) -> int:
	pass;

#desc Converts one or more arguments of any type to string in the best way possible.
vararg func str() -> String:
	pass;

#desc Converts a formatted [param string] that was returned by [method var_to_str] to the original value.
#desc [codeblock]
#desc var a = '{ "a": 1, "b": 2 }'
#desc var b = str_to_var(a)
#desc print(b["a"]) # Prints 1
#desc [/codeblock]
func str_to_var(string: String) -> Variant:
	pass;

#desc Returns the tangent of angle [param angle_rad] in radians.
#desc [codeblock]
#desc tan(deg_to_rad(45)) # Returns 1
#desc [/codeblock]
func tan(angle_rad: float) -> float:
	pass;

#desc Returns the hyperbolic tangent of [param x].
#desc [codeblock]
#desc var a = log(2.0) # Returns 0.693147
#desc tanh(a)      # Returns 0.6
#desc [/codeblock]
func tanh(x: float) -> float:
	pass;

#desc Returns the internal type of the given Variant object, using the [enum Variant.Type] values.
#desc [codeblock]
#desc var json = JSON.new()
#desc json.parse('["a", "b", "c"]')
#desc var result = json.get_data()
#desc if typeof(result) == TYPE_ARRAY:
#desc print(result[0]) # Prints a
#desc else:
#desc print("Unexpected result")
#desc [/codeblock]
func typeof(variable: Variant) -> int:
	pass;

#desc Encodes a [Variant] value to a byte array, without encoding objects. Deserialization can be done with [method bytes_to_var].
#desc [b]Note:[/b] If you need object serialization, see [method var_to_bytes_with_objects].
func var_to_bytes(variable: Variant) -> PackedByteArray:
	pass;

#desc Encodes a [Variant] value to a byte array. Encoding objects is allowed (and can potentially include code). Deserialization can be done with [method bytes_to_var_with_objects].
func var_to_bytes_with_objects(variable: Variant) -> PackedByteArray:
	pass;

#desc Converts a Variant [param variable] to a formatted string that can later be parsed using [method str_to_var].
#desc [codeblock]
#desc a = { "a": 1, "b": 2 }
#desc print(var_to_str(a))
#desc [/codeblock]
#desc prints
#desc [codeblock]
#desc {
#desc "a": 1,
#desc "b": 2
#desc }
#desc [/codeblock]
func var_to_str(variable: Variant) -> String:
	pass;

#desc Returns a weak reference to an object, or [code]null[/code] if the argument is invalid.
#desc A weak reference to an object is not enough to keep the object alive: when the only remaining references to a referent are weak references, garbage collection is free to destroy the referent and reuse its memory for something else. However, until the object is actually destroyed the weak reference may return the object even if there are no strong references to it.
func weakref(obj: Variant) -> Variant:
	pass;

#desc Wraps the [Variant] [param value] between [param min] and [param max].
#desc Usable for creating loop-alike behavior or infinite surfaces.
#desc Variant types [int] and [float] (real) are supported. If any of the argument is [float] the result will be [float], otherwise it is [int].
#desc [codeblock]
#desc var a = wrap(4, 5, 10)
#desc # a is 9 (int)
#desc 
#desc var a = wrap(7, 5, 10)
#desc # a is 7 (int)
#desc 
#desc var a = wrap(10.5, 5, 10)
#desc # a is 5.5 (float)
#desc [/codeblock]
func wrap(value: Variant, min: Variant, max: Variant) -> Variant:
	pass;

#desc Wraps float [param value] between [param min] and [param max].
#desc Usable for creating loop-alike behavior or infinite surfaces.
#desc [codeblock]
#desc # Infinite loop between 5.0 and 9.9
#desc value = wrapf(value + 0.1, 5.0, 10.0)
#desc [/codeblock]
#desc [codeblock]
#desc # Infinite rotation (in radians)
#desc angle = wrapf(angle + 0.1, 0.0, TAU)
#desc [/codeblock]
#desc [codeblock]
#desc # Infinite rotation (in radians)
#desc angle = wrapf(angle + 0.1, -PI, PI)
#desc [/codeblock]
#desc [b]Note:[/b] If [param min] is [code]0[/code], this is equivalent to [method fposmod], so prefer using that instead.
#desc [code]wrapf[/code] is more flexible than using the [method fposmod] approach by giving the user control over the minimum value.
func wrapf(value: float, min: float, max: float) -> float:
	pass;

#desc Wraps integer [param value] between [param min] and [param max].
#desc Usable for creating loop-alike behavior or infinite surfaces.
#desc [codeblock]
#desc # Infinite loop between 5 and 9
#desc frame = wrapi(frame + 1, 5, 10)
#desc [/codeblock]
#desc [codeblock]
#desc # result is -2
#desc var result = wrapi(-6, -5, -1)
#desc [/codeblock]
func wrapi(value: int, min: int, max: int) -> int:
	pass;


