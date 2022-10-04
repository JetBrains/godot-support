#brief Color in RGBA format using floats on the range of 0 to 1.
#desc A color represented by red, green, blue, and alpha (RGBA) components. The alpha component is often used for opacity. Values are in floating-point and usually range from 0 to 1. Some properties (such as CanvasItem.modulate) may accept values greater than 1 (overbright or HDR colors).
#desc You can also create a color from standardized color names by using the string constructor or directly using the color constants defined here. The standardized color set is based on the [url=https://en.wikipedia.org/wiki/X11_color_names]X11 color names[/url].
#desc If you want to supply values in a range of 0 to 255, you should use [method @GDScript.Color8].
#desc [b]Note:[/b] In a boolean context, a Color will evaluate to [code]false[/code] if it's equal to [code]Color(0, 0, 0, 1)[/code] (opaque black). Otherwise, a Color will always evaluate to [code]true[/code].
#desc [url=https://raw.githubusercontent.com/godotengine/godot-docs/master/img/color_constants.png]Color constants cheatsheet[/url]
class_name Color

#desc Alice blue color.
const ALICE_BLUE = Color(0.941176, 0.972549, 1, 1);

#desc Antique white color.
const ANTIQUE_WHITE = Color(0.980392, 0.921569, 0.843137, 1);

#desc Aqua color.
const AQUA = Color(0, 1, 1, 1);

#desc Aquamarine color.
const AQUAMARINE = Color(0.498039, 1, 0.831373, 1);

#desc Azure color.
const AZURE = Color(0.941176, 1, 1, 1);

#desc Beige color.
const BEIGE = Color(0.960784, 0.960784, 0.862745, 1);

#desc Bisque color.
const BISQUE = Color(1, 0.894118, 0.768627, 1);

#desc Black color.
const BLACK = Color(0, 0, 0, 1);

#desc Blanched almond color.
const BLANCHED_ALMOND = Color(1, 0.921569, 0.803922, 1);

#desc Blue color.
const BLUE = Color(0, 0, 1, 1);

#desc Blue violet color.
const BLUE_VIOLET = Color(0.541176, 0.168627, 0.886275, 1);

#desc Brown color.
const BROWN = Color(0.647059, 0.164706, 0.164706, 1);

#desc Burlywood color.
const BURLYWOOD = Color(0.870588, 0.721569, 0.529412, 1);

#desc Cadet blue color.
const CADET_BLUE = Color(0.372549, 0.619608, 0.627451, 1);

#desc Chartreuse color.
const CHARTREUSE = Color(0.498039, 1, 0, 1);

#desc Chocolate color.
const CHOCOLATE = Color(0.823529, 0.411765, 0.117647, 1);

#desc Coral color.
const CORAL = Color(1, 0.498039, 0.313726, 1);

#desc Cornflower blue color.
const CORNFLOWER_BLUE = Color(0.392157, 0.584314, 0.929412, 1);

#desc Cornsilk color.
const CORNSILK = Color(1, 0.972549, 0.862745, 1);

#desc Crimson color.
const CRIMSON = Color(0.862745, 0.0784314, 0.235294, 1);

#desc Cyan color.
const CYAN = Color(0, 1, 1, 1);

#desc Dark blue color.
const DARK_BLUE = Color(0, 0, 0.545098, 1);

#desc Dark cyan color.
const DARK_CYAN = Color(0, 0.545098, 0.545098, 1);

#desc Dark goldenrod color.
const DARK_GOLDENROD = Color(0.721569, 0.52549, 0.0431373, 1);

#desc Dark gray color.
const DARK_GRAY = Color(0.662745, 0.662745, 0.662745, 1);

#desc Dark green color.
const DARK_GREEN = Color(0, 0.392157, 0, 1);

#desc Dark khaki color.
const DARK_KHAKI = Color(0.741176, 0.717647, 0.419608, 1);

#desc Dark magenta color.
const DARK_MAGENTA = Color(0.545098, 0, 0.545098, 1);

#desc Dark olive green color.
const DARK_OLIVE_GREEN = Color(0.333333, 0.419608, 0.184314, 1);

#desc Dark orange color.
const DARK_ORANGE = Color(1, 0.54902, 0, 1);

#desc Dark orchid color.
const DARK_ORCHID = Color(0.6, 0.196078, 0.8, 1);

#desc Dark red color.
const DARK_RED = Color(0.545098, 0, 0, 1);

#desc Dark salmon color.
const DARK_SALMON = Color(0.913725, 0.588235, 0.478431, 1);

#desc Dark sea green color.
const DARK_SEA_GREEN = Color(0.560784, 0.737255, 0.560784, 1);

#desc Dark slate blue color.
const DARK_SLATE_BLUE = Color(0.282353, 0.239216, 0.545098, 1);

#desc Dark slate gray color.
const DARK_SLATE_GRAY = Color(0.184314, 0.309804, 0.309804, 1);

#desc Dark turquoise color.
const DARK_TURQUOISE = Color(0, 0.807843, 0.819608, 1);

#desc Dark violet color.
const DARK_VIOLET = Color(0.580392, 0, 0.827451, 1);

#desc Deep pink color.
const DEEP_PINK = Color(1, 0.0784314, 0.576471, 1);

#desc Deep sky blue color.
const DEEP_SKY_BLUE = Color(0, 0.74902, 1, 1);

#desc Dim gray color.
const DIM_GRAY = Color(0.411765, 0.411765, 0.411765, 1);

#desc Dodger blue color.
const DODGER_BLUE = Color(0.117647, 0.564706, 1, 1);

#desc Firebrick color.
const FIREBRICK = Color(0.698039, 0.133333, 0.133333, 1);

#desc Floral white color.
const FLORAL_WHITE = Color(1, 0.980392, 0.941176, 1);

#desc Forest green color.
const FOREST_GREEN = Color(0.133333, 0.545098, 0.133333, 1);

#desc Fuchsia color.
const FUCHSIA = Color(1, 0, 1, 1);

#desc Gainsboro color.
const GAINSBORO = Color(0.862745, 0.862745, 0.862745, 1);

#desc Ghost white color.
const GHOST_WHITE = Color(0.972549, 0.972549, 1, 1);

#desc Gold color.
const GOLD = Color(1, 0.843137, 0, 1);

#desc Goldenrod color.
const GOLDENROD = Color(0.854902, 0.647059, 0.12549, 1);

#desc Gray color.
const GRAY = Color(0.745098, 0.745098, 0.745098, 1);

#desc Green color.
const GREEN = Color(0, 1, 0, 1);

#desc Green yellow color.
const GREEN_YELLOW = Color(0.678431, 1, 0.184314, 1);

#desc Honeydew color.
const HONEYDEW = Color(0.941176, 1, 0.941176, 1);

#desc Hot pink color.
const HOT_PINK = Color(1, 0.411765, 0.705882, 1);

#desc Indian red color.
const INDIAN_RED = Color(0.803922, 0.360784, 0.360784, 1);

#desc Indigo color.
const INDIGO = Color(0.294118, 0, 0.509804, 1);

#desc Ivory color.
const IVORY = Color(1, 1, 0.941176, 1);

#desc Khaki color.
const KHAKI = Color(0.941176, 0.901961, 0.54902, 1);

#desc Lavender color.
const LAVENDER = Color(0.901961, 0.901961, 0.980392, 1);

#desc Lavender blush color.
const LAVENDER_BLUSH = Color(1, 0.941176, 0.960784, 1);

#desc Lawn green color.
const LAWN_GREEN = Color(0.486275, 0.988235, 0, 1);

#desc Lemon chiffon color.
const LEMON_CHIFFON = Color(1, 0.980392, 0.803922, 1);

#desc Light blue color.
const LIGHT_BLUE = Color(0.678431, 0.847059, 0.901961, 1);

#desc Light coral color.
const LIGHT_CORAL = Color(0.941176, 0.501961, 0.501961, 1);

#desc Light cyan color.
const LIGHT_CYAN = Color(0.878431, 1, 1, 1);

#desc Light goldenrod color.
const LIGHT_GOLDENROD = Color(0.980392, 0.980392, 0.823529, 1);

#desc Light gray color.
const LIGHT_GRAY = Color(0.827451, 0.827451, 0.827451, 1);

#desc Light green color.
const LIGHT_GREEN = Color(0.564706, 0.933333, 0.564706, 1);

#desc Light pink color.
const LIGHT_PINK = Color(1, 0.713726, 0.756863, 1);

#desc Light salmon color.
const LIGHT_SALMON = Color(1, 0.627451, 0.478431, 1);

#desc Light sea green color.
const LIGHT_SEA_GREEN = Color(0.12549, 0.698039, 0.666667, 1);

#desc Light sky blue color.
const LIGHT_SKY_BLUE = Color(0.529412, 0.807843, 0.980392, 1);

#desc Light slate gray color.
const LIGHT_SLATE_GRAY = Color(0.466667, 0.533333, 0.6, 1);

#desc Light steel blue color.
const LIGHT_STEEL_BLUE = Color(0.690196, 0.768627, 0.870588, 1);

#desc Light yellow color.
const LIGHT_YELLOW = Color(1, 1, 0.878431, 1);

#desc Lime color.
const LIME = Color(0, 1, 0, 1);

#desc Lime green color.
const LIME_GREEN = Color(0.196078, 0.803922, 0.196078, 1);

#desc Linen color.
const LINEN = Color(0.980392, 0.941176, 0.901961, 1);

#desc Magenta color.
const MAGENTA = Color(1, 0, 1, 1);

#desc Maroon color.
const MAROON = Color(0.690196, 0.188235, 0.376471, 1);

#desc Medium aquamarine color.
const MEDIUM_AQUAMARINE = Color(0.4, 0.803922, 0.666667, 1);

#desc Medium blue color.
const MEDIUM_BLUE = Color(0, 0, 0.803922, 1);

#desc Medium orchid color.
const MEDIUM_ORCHID = Color(0.729412, 0.333333, 0.827451, 1);

#desc Medium purple color.
const MEDIUM_PURPLE = Color(0.576471, 0.439216, 0.858824, 1);

#desc Medium sea green color.
const MEDIUM_SEA_GREEN = Color(0.235294, 0.701961, 0.443137, 1);

#desc Medium slate blue color.
const MEDIUM_SLATE_BLUE = Color(0.482353, 0.407843, 0.933333, 1);

#desc Medium spring green color.
const MEDIUM_SPRING_GREEN = Color(0, 0.980392, 0.603922, 1);

#desc Medium turquoise color.
const MEDIUM_TURQUOISE = Color(0.282353, 0.819608, 0.8, 1);

#desc Medium violet red color.
const MEDIUM_VIOLET_RED = Color(0.780392, 0.0823529, 0.521569, 1);

#desc Midnight blue color.
const MIDNIGHT_BLUE = Color(0.0980392, 0.0980392, 0.439216, 1);

#desc Mint cream color.
const MINT_CREAM = Color(0.960784, 1, 0.980392, 1);

#desc Misty rose color.
const MISTY_ROSE = Color(1, 0.894118, 0.882353, 1);

#desc Moccasin color.
const MOCCASIN = Color(1, 0.894118, 0.709804, 1);

#desc Navajo white color.
const NAVAJO_WHITE = Color(1, 0.870588, 0.678431, 1);

#desc Navy blue color.
const NAVY_BLUE = Color(0, 0, 0.501961, 1);

#desc Old lace color.
const OLD_LACE = Color(0.992157, 0.960784, 0.901961, 1);

#desc Olive color.
const OLIVE = Color(0.501961, 0.501961, 0, 1);

#desc Olive drab color.
const OLIVE_DRAB = Color(0.419608, 0.556863, 0.137255, 1);

#desc Orange color.
const ORANGE = Color(1, 0.647059, 0, 1);

#desc Orange red color.
const ORANGE_RED = Color(1, 0.270588, 0, 1);

#desc Orchid color.
const ORCHID = Color(0.854902, 0.439216, 0.839216, 1);

#desc Pale goldenrod color.
const PALE_GOLDENROD = Color(0.933333, 0.909804, 0.666667, 1);

#desc Pale green color.
const PALE_GREEN = Color(0.596078, 0.984314, 0.596078, 1);

#desc Pale turquoise color.
const PALE_TURQUOISE = Color(0.686275, 0.933333, 0.933333, 1);

#desc Pale violet red color.
const PALE_VIOLET_RED = Color(0.858824, 0.439216, 0.576471, 1);

#desc Papaya whip color.
const PAPAYA_WHIP = Color(1, 0.937255, 0.835294, 1);

#desc Peach puff color.
const PEACH_PUFF = Color(1, 0.854902, 0.72549, 1);

#desc Peru color.
const PERU = Color(0.803922, 0.521569, 0.247059, 1);

#desc Pink color.
const PINK = Color(1, 0.752941, 0.796078, 1);

#desc Plum color.
const PLUM = Color(0.866667, 0.627451, 0.866667, 1);

#desc Powder blue color.
const POWDER_BLUE = Color(0.690196, 0.878431, 0.901961, 1);

#desc Purple color.
const PURPLE = Color(0.627451, 0.12549, 0.941176, 1);

#desc Rebecca purple color.
const REBECCA_PURPLE = Color(0.4, 0.2, 0.6, 1);

#desc Red color.
const RED = Color(1, 0, 0, 1);

#desc Rosy brown color.
const ROSY_BROWN = Color(0.737255, 0.560784, 0.560784, 1);

#desc Royal blue color.
const ROYAL_BLUE = Color(0.254902, 0.411765, 0.882353, 1);

#desc Saddle brown color.
const SADDLE_BROWN = Color(0.545098, 0.270588, 0.0745098, 1);

#desc Salmon color.
const SALMON = Color(0.980392, 0.501961, 0.447059, 1);

#desc Sandy brown color.
const SANDY_BROWN = Color(0.956863, 0.643137, 0.376471, 1);

#desc Sea green color.
const SEA_GREEN = Color(0.180392, 0.545098, 0.341176, 1);

#desc Seashell color.
const SEASHELL = Color(1, 0.960784, 0.933333, 1);

#desc Sienna color.
const SIENNA = Color(0.627451, 0.321569, 0.176471, 1);

#desc Silver color.
const SILVER = Color(0.752941, 0.752941, 0.752941, 1);

#desc Sky blue color.
const SKY_BLUE = Color(0.529412, 0.807843, 0.921569, 1);

#desc Slate blue color.
const SLATE_BLUE = Color(0.415686, 0.352941, 0.803922, 1);

#desc Slate gray color.
const SLATE_GRAY = Color(0.439216, 0.501961, 0.564706, 1);

#desc Snow color.
const SNOW = Color(1, 0.980392, 0.980392, 1);

#desc Spring green color.
const SPRING_GREEN = Color(0, 1, 0.498039, 1);

#desc Steel blue color.
const STEEL_BLUE = Color(0.27451, 0.509804, 0.705882, 1);

#desc Tan color.
const TAN = Color(0.823529, 0.705882, 0.54902, 1);

#desc Teal color.
const TEAL = Color(0, 0.501961, 0.501961, 1);

#desc Thistle color.
const THISTLE = Color(0.847059, 0.74902, 0.847059, 1);

#desc Tomato color.
const TOMATO = Color(1, 0.388235, 0.278431, 1);

#desc Transparent color (white with zero alpha).
const TRANSPARENT = Color(1, 1, 1, 0);

#desc Turquoise color.
const TURQUOISE = Color(0.25098, 0.878431, 0.815686, 1);

#desc Violet color.
const VIOLET = Color(0.933333, 0.509804, 0.933333, 1);

#desc Web gray color.
const WEB_GRAY = Color(0.501961, 0.501961, 0.501961, 1);

#desc Web green color.
const WEB_GREEN = Color(0, 0.501961, 0, 1);

#desc Web maroon color.
const WEB_MAROON = Color(0.501961, 0, 0, 1);

#desc Web purple color.
const WEB_PURPLE = Color(0.501961, 0, 0.501961, 1);

#desc Wheat color.
const WHEAT = Color(0.960784, 0.870588, 0.701961, 1);

#desc White color.
const WHITE = Color(1, 1, 1, 1);

#desc White smoke color.
const WHITE_SMOKE = Color(0.960784, 0.960784, 0.960784, 1);

#desc Yellow color.
const YELLOW = Color(1, 1, 0, 1);

#desc Yellow green color.
const YELLOW_GREEN = Color(0.603922, 0.803922, 0.196078, 1);


#desc The color's alpha component, typically on the range of 0 to 1. A value of 0 means that the color is fully transparent. A value of 1 means that the color is fully opaque.
var a: float;

#desc Wrapper for [member a] that uses the range 0 to 255 instead of 0 to 1.
var a8: int;

#desc The color's blue component, typically on the range of 0 to 1.
var b: float;

#desc Wrapper for [member b] that uses the range 0 to 255 instead of 0 to 1.
var b8: int;

#desc The color's green component, typically on the range of 0 to 1.
var g: float;

#desc Wrapper for [member g] that uses the range 0 to 255 instead of 0 to 1.
var g8: int;

#desc The HSV hue of this color, on the range 0 to 1.
var h: float;

#desc The color's red component, typically on the range of 0 to 1.
var r: float;

#desc Wrapper for [member r] that uses the range 0 to 255 instead of 0 to 1.
var r8: int;

#desc The HSV saturation of this color, on the range 0 to 1.
var s: float;

#desc The HSV value (brightness) of this color, on the range 0 to 1.
var v: float;


#desc Constructs a default-initialized [Color] with all components set to [code]0[/code].
func Color() -> Color:
	pass;

#desc Constructs a [Color] from an existing color, but with a custom alpha value.
#desc [codeblocks]
#desc [gdscript]
#desc var red = Color(Color.red, 0.2) # 20% opaque red.
#desc [/gdscript]
#desc [csharp]
#desc var red = new Color(Colors.Red, 0.2f); // 20% opaque red.
#desc [/csharp]
#desc [/codeblocks]
func Color(from: Color, alpha: float) -> Color:
	pass;

#desc Constructs a [Color] as a copy of the given [Color].
func Color(from: Color) -> Color:
	pass;

#desc Constructs a [Color] either from an HTML color code or from a standardized color name. Supported color names are the same as the constants.
func Color(code: String) -> Color:
	pass;

#desc Constructs a [Color] either from an HTML color code or from a standardized color name, with [param alpha] on the range of 0 to 1. Supported color names are the same as the constants.
func Color(code: String, alpha: float) -> Color:
	pass;

#desc Constructs a [Color] from RGB values, typically between 0 and 1. Alpha will be 1.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(0.2, 1.0, 0.7) # Similar to `Color8(51, 255, 178, 255)`
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(0.2f, 1.0f, 0.7f); // Similar to `Color.Color8(51, 255, 178, 255)`
#desc [/csharp]
#desc [/codeblocks]
func Color(r: float, g: float, b: float) -> Color:
	pass;

#desc Constructs a [Color] from RGBA values, typically between 0 and 1.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(0.2, 1.0, 0.7, 0.8) # Similar to `Color8(51, 255, 178, 204)`
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(0.2f, 1.0f, 0.7f, 0.8f); // Similar to `Color.Color8(51, 255, 178, 255, 204)`
#desc [/csharp]
#desc [/codeblocks]
func Color(r: float, g: float, b: float, a: float) -> Color:
	pass;


#desc Returns a new color resulting from blending this color over another. If the color is opaque, the result is also opaque. The second color may have a range of alpha values.
#desc [codeblocks]
#desc [gdscript]
#desc var bg = Color(0.0, 1.0, 0.0, 0.5) # Green with alpha of 50%
#desc var fg = Color(1.0, 0.0, 0.0, 0.5) # Red with alpha of 50%
#desc var blended_color = bg.blend(fg) # Brown with alpha of 75%
#desc [/gdscript]
#desc [csharp]
#desc var bg = new Color(0.0f, 1.0f, 0.0f, 0.5f); // Green with alpha of 50%
#desc var fg = new Color(1.0f, 0.0f, 0.0f, 0.5f); // Red with alpha of 50%
#desc Color blendedColor = bg.Blend(fg); // Brown with alpha of 75%
#desc [/csharp]
#desc [/codeblocks]
func blend(over: Color) -> Color:
	pass;

#desc Returns a new color with all components clamped between the components of [param min] and [param max], by running [method @GlobalScope.clamp] on each component.
func clamp(min: Color, max: Color) -> Color:
	pass;

#desc Returns a new color resulting from making this color darker by the specified percentage (ratio from 0 to 1).
#desc [codeblocks]
#desc [gdscript]
#desc var green = Color(0.0, 1.0, 0.0)
#desc var darkgreen = green.darkened(0.2) # 20% darker than regular green
#desc [/gdscript]
#desc [csharp]
#desc var green = new Color(0.0f, 1.0f, 0.0f);
#desc Color darkgreen = green.Darkened(0.2f); // 20% darker than regular green
#desc [/csharp]
#desc [/codeblocks]
func darkened(amount: float) -> Color:
	pass;

static func find_named_color(name: String) -> int:
	pass;

#desc Constructs a color from an [url=https://en.wikipedia.org/wiki/HSL_and_HSV]HSV profile[/url]. [param h] (hue), [param s] (saturation), and [param v] (value) are typically between 0 and 1.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color.from_hsv(0.58, 0.5, 0.79, 0.8)
#desc [/gdscript]
#desc [csharp]
#desc var color = Color.FromHsv(0.58f, 0.5f, 0.79f, 0.8f);
#desc [/csharp]
#desc [/codeblocks]
static func from_hsv(h: float, s: float, v: float, alpha: float) -> Color:
	pass;

#desc Constructs a color from an [url=https://bottosson.github.io/posts/colorpicker/]OK HSL profile[/url]. [param h] (hue), [param s] (saturation), and [param l] (lightness) are typically between 0 and 1.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color.from_ok_hsl(0.58, 0.5, 0.79, 0.8)
#desc [/gdscript]
#desc [csharp]
#desc var color = Color.FromOkHsl(0.58f, 0.5f, 0.79f, 0.8f);
#desc [/csharp]
#desc [/codeblocks]
static func from_ok_hsl(h: float, s: float, l: float, alpha: float) -> Color:
	pass;

static func from_rgbe9995(rgbe: int) -> Color:
	pass;

static func from_string(str: String, default: Color) -> Color:
	pass;

#desc Returns the luminance of the color in the [code][0.0, 1.0][/code] range.
#desc This is useful when determining light or dark color. Colors with a luminance smaller than 0.5 can be generally considered dark.
#desc [b]Note:[/b] [method get_luminance] relies on the colour being in the linear color space to return an accurate relative luminance value. If the color is in the sRGB color space, use [method srgb_to_linear] to convert it to the linear color space first.
func get_luminance() -> float:
	pass;

static func get_named_color(idx: int) -> Color:
	pass;

static func get_named_color_count() -> int:
	pass;

static func get_named_color_name(idx: int) -> String:
	pass;

static func hex(hex: int) -> Color:
	pass;

static func hex64(hex: int) -> Color:
	pass;

#desc Returns a new color from [param rgba], an HTML hexadecimal color string. [param rgba] is not case sensitive, and may be prefixed with a '#' character.
#desc [param rgba] must be a valid three-digit or six-digit hexadecimal color string, and may contain an alpha channel value. If [param rgba] does not contain an alpha channel value, an alpha channel value of 1.0 is applied.
#desc If [param rgba] is invalid a Color(0.0, 0.0, 0.0, 1.0) is returned.
#desc [b]Note:[/b] This method is not implemented in C#, but the same functionality is provided in the class constructor.
#desc [codeblocks]
#desc [gdscript]
#desc var green = Color.html("#00FF00FF") # set green to Color(0.0, 1.0, 0.0, 1.0)
#desc var blue = Color.html("#0000FF") # set blue to Color(0.0, 0.0, 1.0, 1.0)
#desc [/gdscript]
#desc [csharp]
#desc var green = new Color("#00FF00FF"); // set green to Color(0.0, 1.0, 0.0, 1.0)
#desc var blue = new Color("#0000FF"); // set blue to Color(0.0, 0.0, 1.0, 1.0)
#desc [/csharp]
#desc [/codeblocks]
static func html(rgba: String) -> Color:
	pass;

#desc Returns [code]true[/code] if [param color] is a valid HTML hexadecimal color string. [param color] is not case sensitive, and may be prefixed with a '#' character.
#desc For a string to be valid it must be three-digit or six-digit hexadecimal, and may contain an alpha channel value.
#desc [codeblocks]
#desc [gdscript]
#desc var result = Color.html_is_valid("#55aaFF") # result is true
#desc result = Color.html_is_valid("#55AAFF20") # result is true
#desc result = Color.html_is_valid("55AAFF") # result is true
#desc result = Color.html_is_valid("#F2C") # result is true
#desc result = Color.html_is_valid("#AABBC) # result is false
#desc result = Color.html_is_valid("#55aaFF5") # result is false
#desc [/gdscript]
#desc [csharp]
#desc var result = Color.HtmlIsValid("#55AAFF"); // result is true
#desc result = Color.HtmlIsValid("#55AAFF20"); // result is true
#desc result = Color.HtmlIsValid("55AAFF); // result is true
#desc result = Color.HtmlIsValid("#F2C"); // result is true
#desc result = Color.HtmlIsValid("#AABBC"); // result is false
#desc result = Color.HtmlIsValid("#55aaFF5"); // result is false
#desc [/csharp]
#desc [/codeblocks]
static func html_is_valid(color: String) -> bool:
	pass;

#desc Returns the inverted color [code](1 - r, 1 - g, 1 - b, a)[/code].
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(0.3, 0.4, 0.9)
#desc var inverted_color = color.inverted() # Equivalent to `Color(0.7, 0.6, 0.1)`
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(0.3f, 0.4f, 0.9f);
#desc Color invertedColor = color.Inverted(); // Equivalent to `new Color(0.7f, 0.6f, 0.1f)`
#desc [/csharp]
#desc [/codeblocks]
func inverted() -> Color:
	pass;

#desc Returns [code]true[/code] if this color and [param to] are approximately equal, by running [method @GlobalScope.is_equal_approx] on each component.
func is_equal_approx(to: Color) -> bool:
	pass;

#desc Returns the linear interpolation with another color. The interpolation factor [param weight] is between 0 and 1.
#desc [codeblocks]
#desc [gdscript]
#desc var c1 = Color(1.0, 0.0, 0.0)
#desc var c2 = Color(0.0, 1.0, 0.0)
#desc var lerp_color = c1.lerp(c2, 0.5) # Equivalent to `Color(0.5, 0.5, 0.0)`
#desc [/gdscript]
#desc [csharp]
#desc var c1 = new Color(1.0f, 0.0f, 0.0f);
#desc var c2 = new Color(0.0f, 1.0f, 0.0f);
#desc Color lerpColor = c1.Lerp(c2, 0.5f); // Equivalent to `new Color(0.5f, 0.5f, 0.0f)`
#desc [/csharp]
#desc [/codeblocks]
func lerp(to: Color, weight: float) -> Color:
	pass;

#desc Returns a new color resulting from making this color lighter by the specified percentage (ratio from 0 to 1).
#desc [codeblocks]
#desc [gdscript]
#desc var green = Color(0.0, 1.0, 0.0)
#desc var lightgreen = green.lightened(0.2) # 20% lighter than regular green
#desc [/gdscript]
#desc [csharp]
#desc var green = new Color(0.0f, 1.0f, 0.0f);
#desc Color lightgreen = green.Lightened(0.2f); // 20% lighter than regular green
#desc [/csharp]
#desc [/codeblocks]
func lightened(amount: float) -> Color:
	pass;

#desc Returns the color converted to the [url=https://en.wikipedia.org/wiki/SRGB]sRGB[/url] color space. This assumes the original color is in the linear color space. See also [method srgb_to_linear] which performs the opposite operation.
func linear_to_srgb() -> Color:
	pass;

#desc Returns the color converted to the linear color space. This assumes the original color is in the sRGB color space. See also [method linear_to_srgb] which performs the opposite operation.
func srgb_to_linear() -> Color:
	pass;

#desc Returns the color converted to a 32-bit integer in ABGR format (each byte represents a color channel). ABGR is the reversed version of the default format.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 0.5, 0.2)
#desc print(color.to_abgr32()) # Prints 4281565439
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1.0f, 0.5f, 0.2f);
#desc GD.Print(color.ToAbgr32()); // Prints 4281565439
#desc [/csharp]
#desc [/codeblocks]
func to_abgr32() -> int:
	pass;

#desc Returns the color converted to a 64-bit integer in ABGR format (each word represents a color channel). ABGR is the reversed version of the default format.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 0.5, 0.2)
#desc print(color.to_abgr64()) # Prints -225178692812801
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1.0f, 0.5f, 0.2f);
#desc GD.Print(color.ToAbgr64()); // Prints -225178692812801
#desc [/csharp]
#desc [/codeblocks]
func to_abgr64() -> int:
	pass;

#desc Returns the color converted to a 32-bit integer in ARGB format (each byte represents a color channel). ARGB is more compatible with DirectX.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 0.5, 0.2)
#desc print(color.to_argb32()) # Prints 4294934323
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1.0f, 0.5f, 0.2f);
#desc GD.Print(color.ToArgb32()); // Prints 4294934323
#desc [/csharp]
#desc [/codeblocks]
func to_argb32() -> int:
	pass;

#desc Returns the color converted to a 64-bit integer in ARGB format (each word represents a color channel). ARGB is more compatible with DirectX.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 0.5, 0.2)
#desc print(color.to_argb64()) # Prints -2147470541
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1.0f, 0.5f, 0.2f);
#desc GD.Print(color.ToArgb64()); // Prints -2147470541
#desc [/csharp]
#desc [/codeblocks]
func to_argb64() -> int:
	pass;

#desc Returns the color converted to an HTML hexadecimal color string in RGBA format (ex: [code]ff34f822[/code]).
#desc Setting [param with_alpha] to [code]false[/code] excludes alpha from the hexadecimal string (and uses RGB instead of RGBA format).
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 1, 1, 0.5)
#desc var with_alpha = color.to_html() # Returns "ffffff7f"
#desc var without_alpha = color.to_html(false) # Returns "ffffff"
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1, 1, 1, 0.5f);
#desc String withAlpha = color.ToHtml(); // Returns "ffffff7f"
#desc String withoutAlpha = color.ToHtml(false); // Returns "ffffff"
#desc [/csharp]
#desc [/codeblocks]
func to_html(with_alpha: bool) -> String:
	pass;

#desc Returns the color converted to a 32-bit integer in RGBA format (each byte represents a color channel). RGBA is Godot's default format.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 0.5, 0.2)
#desc print(color.to_rgba32()) # Prints 4286526463
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1, 0.5f, 0.2f);
#desc GD.Print(color.ToRgba32()); // Prints 4286526463
#desc [/csharp]
#desc [/codeblocks]
func to_rgba32() -> int:
	pass;

#desc Returns the color converted to a 64-bit integer in RGBA format (each word represents a color channel). RGBA is Godot's default format.
#desc [codeblocks]
#desc [gdscript]
#desc var color = Color(1, 0.5, 0.2)
#desc print(color.to_rgba64()) # Prints -140736629309441
#desc [/gdscript]
#desc [csharp]
#desc var color = new Color(1, 0.5f, 0.2f);
#desc GD.Print(color.ToRgba64()); // Prints -140736629309441
#desc [/csharp]
#desc [/codeblocks]
func to_rgba64() -> int:
	pass;


