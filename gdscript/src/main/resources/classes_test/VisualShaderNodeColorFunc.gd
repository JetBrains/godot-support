#brief A [Color] function to be used within the visual shader graph.
#desc Accept a [Color] to the input port and transform it according to [member function].
class_name VisualShaderNodeColorFunc

#desc Converts the color to grayscale using the following formula:
#desc [codeblock]
#desc vec3 c = input;
#desc float max1 = max(c.r, c.g);
#desc float max2 = max(max1, c.b);
#desc float max3 = max(max1, max2);
#desc return vec3(max3, max3, max3);
#desc [/codeblock]
const FUNC_GRAYSCALE = 0;

#desc Converts HSV vector to RGB equivalent.
const FUNC_HSV2RGB = 1;

#desc Converts RGB vector to HSV equivalent.
const FUNC_RGB2HSV = 2;

#desc Applies sepia tone effect using the following formula:
#desc [codeblock]
#desc vec3 c = input;
#desc float r = (c.r * 0.393) + (c.g * 0.769) + (c.b * 0.189);
#desc float g = (c.r * 0.349) + (c.g * 0.686) + (c.b * 0.168);
#desc float b = (c.r * 0.272) + (c.g * 0.534) + (c.b * 0.131);
#desc return vec3(r, g, b);
#desc [/codeblock]
const FUNC_SEPIA = 3;

#desc Represents the size of the [enum Function] enum.
const FUNC_MAX = 4;


#desc A function to be applied to the input color. See [enum Function] for options.
var function: int;




