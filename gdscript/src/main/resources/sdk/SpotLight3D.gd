extends Light3D
#brief A spotlight, such as a reflector spotlight or a lantern.
#desc A Spotlight is a type of [Light3D] node that emits lights in a specific direction, in the shape of a cone. The light is attenuated through the distance. This attenuation can be configured by changing the energy, radius and attenuation parameters of [Light3D].
class_name SpotLight3D


var shadow_bias: float;

#desc The spotlight's angle in degrees.
var spot_angle: float;

#desc The spotlight's angular attenuation curve.
var spot_angle_attenuation: float;

#desc The spotlight's light energy attenuation curve.
var spot_attenuation: float;

#desc The maximal range that can be reached by the spotlight. Note that the effectively lit area may appear to be smaller depending on the [member spot_attenuation] in use. No matter the [member spot_attenuation] in use, the light will never reach anything outside this range.
var spot_range: float;




