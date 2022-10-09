#brief Integer built-in type.
#desc Signed 64-bit integer type.
#desc It can take values in the interval [code][-2^63, 2^63 - 1][/code], i.e. [code][-9223372036854775808, 9223372036854775807][/code]. Exceeding those bounds will wrap around.
#desc [int] is a [Variant] type, and will thus be used when assigning an integer value to a [Variant]. It can also be enforced with the [code]: int[/code] type hint.
#desc [codeblocks]
#desc [gdscript]
#desc var my_variant = 0 # int, value 0.
#desc my_variant += 4.2 # float, value 4.2.
#desc var my_int: int = 1 # int, value 1.
#desc my_int = 4.2 # int, value 4, the right value is implicitly cast to int.
#desc my_int = int("6.7") # int, value 6, the String is explicitly cast with int.
#desc var max_int = 9223372036854775807
#desc print(max_int) # 9223372036854775807, OK.
#desc max_int += 1
#desc print(max_int) # -9223372036854775808, we overflowed and wrapped around.
#desc [/gdscript]
#desc [csharp]
#desc int myInt = (int)"6.7".ToFloat(); // int, value 6, the String is explicitly cast with int.
#desc // We have to use `long` here, because GDSript's `int`
#desc // is 64 bits long while C#'s `int` is only 32 bits.
#desc long maxInt = 9223372036854775807;
#desc GD.Print(maxInt); // 9223372036854775807, OK.
#desc maxInt++;
#desc GD.Print(maxInt); // -9223372036854775808, we overflowed and wrapped around.
#desc 
#desc // Alternatively, if we used C#'s 32-bit `int` type, the maximum value is much smaller:
#desc int halfInt = 2147483647;
#desc GD.Print(halfInt); // 2147483647, OK.
#desc halfInt++;
#desc GD.Print(halfInt); // -2147483648, we overflowed and wrapped around.
#desc [/csharp]
#desc [/codeblocks]
class_name int



#desc Constructs a default-initialized [int] set to [code]0[/code].
func int() -> int:
	pass;

#desc Constructs an [int] as a copy of the given [int].
func int(from: int) -> int:
	pass;

#desc Cast a [bool] value to an integer value, [code]int(true)[/code] will be equals to 1 and [code]int(false)[/code] will be equals to 0.
func int(from: bool) -> int:
	pass;

#desc Cast a float value to an integer value, this method simply removes the number fractions (i.e. rounds [param from] towards zero), so for example [code]int(2.7)[/code] will be equals to 2, [code]int(0.1)[/code] will be equals to 0 and [code]int(-2.7)[/code] will be equals to -2. This operation is also called truncation.
func int(from: float) -> int:
	pass;



