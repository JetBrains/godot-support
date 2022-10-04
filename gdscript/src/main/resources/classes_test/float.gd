#brief Float built-in type.
#desc The [float] built-in type is a 64-bit double-precision floating-point number, equivalent to [code]double[/code] in C++. This type has 14 reliable decimal digits of precision. The [float] type can be stored in [Variant], which is the generic type used by the engine. The maximum value of [float] is approximately [code]1.79769e308[/code], and the minimum is approximately [code]-1.79769e308[/code].
#desc Many methods and properties in the engine use 32-bit single-precision floating-point numbers instead, equivalent to [code]float[/code] in C++, which have 6 reliable decimal digits of precision. For data structures such as [Vector2] and [Vector3], Godot uses 32-bit floating-point numbers by default, but it can be changed to use 64-bit doubles if Godot is compiled with the [code]float=64[/code] option.
#desc Math done using the [float] type is not guaranteed to be exact or deterministic, and will often result in small errors. You should usually use the [method @GlobalScope.is_equal_approx] and [method @GlobalScope.is_zero_approx] methods instead of [code]==[/code] to compare [float] values for equality.
class_name float



#desc Constructs a default-initialized [float] set to [code]0.0[/code].
func float() -> float:
	pass;

#desc Constructs a [float] as a copy of the given [float].
func float() -> float:
	pass;

#desc Cast a [bool] value to a floating-point value, [code]float(true)[/code] will be equal to 1.0 and [code]float(false)[/code] will be equal to 0.0.
func float() -> float:
	pass;

#desc Cast an [int] value to a floating-point value, [code]float(1)[/code] will be equal to [code]1.0[/code].
func float() -> float:
	pass;



