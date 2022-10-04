#brief Parent class for camera settings.
#desc Controls camera-specific attributes such as depth of field and exposure override.
#desc When used in a [WorldEnvironment] it provides default settings for exposure, auto-exposure, and depth of field that will be used by all cameras without their own [CameraAttributes], including the editor camera. When used in a [Camera3D] it will override any [CameraAttributes] set in the [WorldEnvironment]. When used in [VoxelGI] or [LightmapGI], only the exposure settings will be used.
#desc See also [Environment] for general 3D environment settings.
#desc This is a pure virtual class that is inherited by [CameraAttributesPhysical] and [CameraAttributesPractical].
class_name CameraAttributes


#desc If [code]true[/code], enables the tonemapping auto exposure mode of the scene renderer. If [code]true[/code], the renderer will automatically determine the exposure setting to adapt to the scene's illumination and the observed light.
var auto_exposure_enabled: bool;

#desc The scale of the auto exposure effect. Affects the intensity of auto exposure.
var auto_exposure_scale: float;

#desc The speed of the auto exposure effect. Affects the time needed for the camera to perform auto exposure.
var auto_exposure_speed: float;

#desc Multiplier for the exposure amount. A higher value results in a brighter image.
var exposure_multiplier: float;

#desc Sensitivity of camera sensors, measured in ISO. A higher sensitivity results in a brighter image. Only available when [member ProjectSettings.rendering/lights_and_shadows/use_physical_light_units] is enabled. When [member auto_exposure_enabled] this can be used as a method of exposure compensation, doubling the value will increase the exposure value (measured in EV100) by 1 stop.
var exposure_sensitivity: float;




