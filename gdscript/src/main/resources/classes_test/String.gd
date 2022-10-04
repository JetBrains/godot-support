#brief Built-in string class.
#desc This is the built-in string class (and the one used by GDScript). It supports Unicode and provides all necessary means for string handling. Strings are reference-counted and use a copy-on-write approach, so passing them around is cheap in resources.
class_name String



#desc Constructs an empty [String] ([code]""[/code]).
func String() -> String:
	pass;

#desc Constructs a [String] as a copy of the given [String].
func String() -> String:
	pass;

#desc Constructs a new String from the given [NodePath].
func String() -> String:
	pass;

#desc Constructs a new String from the given [StringName].
func String() -> String:
	pass;


#desc Returns [code]true[/code] if the string begins with the given string.
func begins_with() -> bool:
	pass;

#desc Returns an array containing the bigrams (pairs of consecutive letters) of this string.
#desc [codeblock]
#desc print("Bigrams".bigrams()) # Prints "[Bi, ig, gr, ra, am, ms]"
#desc [/codeblock]
func bigrams() -> PackedStringArray:
	pass;

#desc Converts a string containing a binary number into an integer. Binary strings can either be prefixed with [code]0b[/code] or not, and they can also start with a [code]-[/code] before the optional prefix.
#desc [codeblocks]
#desc [gdscript]
#desc print("0b101".bin_to_int()) # Prints "5".
#desc print("101".bin_to_int()) # Prints "5".
#desc [/gdscript]
#desc [csharp]
#desc GD.Print("0b101".BinToInt()); // Prints "5".
#desc GD.Print("101".BinToInt()); // Prints "5".
#desc [/csharp]
#desc [/codeblocks]
func bin_to_int() -> int:
	pass;

#desc Returns a copy of the string with special characters escaped using the C language standard.
func c_escape() -> String:
	pass;

#desc Returns a copy of the string with escaped characters replaced by their meanings. Supported escape sequences are [code]\'[/code], [code]\"[/code], [code]\?[/code], [code]\\[/code], [code]\a[/code], [code]\b[/code], [code]\f[/code], [code]\n[/code], [code]\r[/code], [code]\t[/code], [code]\v[/code].
#desc [b]Note:[/b] Unlike the GDScript parser, this method doesn't support the [code]\uXXXX[/code] escape sequence.
func c_unescape() -> String:
	pass;

#desc Changes the case of some letters. Replaces underscores with spaces, adds spaces before in-word uppercase characters, converts all letters to lowercase, then capitalizes the first letter and every letter following a space character. For [code]capitalize camelCase mixed_with_underscores[/code], it will return [code]Capitalize Camel Case Mixed With Underscores[/code].
func capitalize() -> String:
	pass;

#desc Performs a case-sensitive comparison to another string. Returns [code]-1[/code] if less than, [code]1[/code] if greater than, or [code]0[/code] if equal. "less than" or "greater than" are determined by the [url=https://en.wikipedia.org/wiki/List_of_Unicode_characters]Unicode code points[/url] of each string, which roughly matches the alphabetical order.
#desc [b]Behavior with different string lengths:[/b] Returns [code]1[/code] if the "base" string is longer than the [param to] string or [code]-1[/code] if the "base" string is shorter than the [param to] string. Keep in mind this length is determined by the number of Unicode codepoints, [i]not[/i] the actual visible characters.
#desc [b]Behavior with empty strings:[/b] Returns [code]-1[/code] if the "base" string is empty, [code]1[/code] if the [param to] string is empty or [code]0[/code] if both strings are empty.
#desc To get a boolean result from a string comparison, use the [code]==[/code] operator instead. See also [method nocasecmp_to] and [method naturalnocasecmp_to].
func casecmp_to() -> int:
	pass;

#desc Directly converts an decimal integer to a unicode character. Tables of these characters can be found in various locations, for example [url=https://unicodelookup.com/]here.[/url]
#desc [codeblock]
#desc print(String.chr(65)) # Prints "A"
#desc print(String.chr(129302)) # Prints "🤖" (robot face emoji)
#desc [/codeblock]
static func chr() -> String:
	pass;

#desc Returns [code]true[/code] if the string contains the given string.
func contains() -> bool:
	pass;

#desc Returns the number of occurrences of substring [param what] between [param from] and [param to] positions. If [param from] and [param to] equals 0 the whole string will be used. If only [param to] equals 0 the remained substring will be used.
func count(what: String, from: int, to: int) -> int:
	pass;

#desc Returns the number of occurrences of substring [param what] (ignoring case) between [param from] and [param to] positions. If [param from] and [param to] equals 0 the whole string will be used. If only [param to] equals 0 the remained substring will be used.
func countn(what: String, from: int, to: int) -> int:
	pass;

#desc Returns a copy of the string with indentation (leading tabs and spaces) removed. See also [method indent] to add indentation.
func dedent() -> String:
	pass;

#desc Returns [code]true[/code] if the string ends with the given string.
func ends_with() -> bool:
	pass;

#desc Returns the index of the [b]first[/b] case-sensitive occurrence of the specified string in this instance, or [code]-1[/code]. Optionally, the starting search index can be specified, continuing to the end of the string.
#desc [b]Note:[/b] If you just want to know whether a string contains a substring, use the [code]in[/code] operator as follows:
#desc [codeblocks]
#desc [gdscript]
#desc print("i" in "team") # Will print `false`.
#desc [/gdscript]
#desc [csharp]
#desc // C# has no in operator, but we can use `Contains()`.
#desc GD.Print("team".Contains("i")); // Will print `false`.
#desc [/csharp]
#desc [/codeblocks]
func find(what: String, from: int) -> int:
	pass;

#desc Returns the index of the [b]first[/b] case-insensitive occurrence of the specified string in this instance, or [code]-1[/code]. Optionally, the starting search index can be specified, continuing to the end of the string.
func findn(what: String, from: int) -> int:
	pass;

#desc Formats the string by replacing all occurrences of [param placeholder] with the elements of [param values].
#desc [param values] can be a [Dictionary] or an [Array]. Any underscores in [param placeholder] will be replaced with the corresponding keys in advance. Array elements use their index as keys.
#desc [codeblock]
#desc # Prints: Waiting for Godot is a play by Samuel Beckett, and Godot Engine is named after it.
#desc var use_array_values = "Waiting for {0} is a play by {1}, and {0} Engine is named after it."
#desc print(use_array_values.format(["Godot", "Samuel Beckett"]))
#desc 
#desc # Prints: User 42 is Godot.
#desc print("User {id} is {name}.".format({"id": 42, "name": "Godot"}))
#desc [/codeblock]
#desc Some additional handling is performed when [param values] is an array. If [param placeholder] does not contain an underscore, the elements of the array will be used to replace one occurrence of the placeholder in turn; If an array element is another 2-element array, it'll be interpreted as a key-value pair.
#desc [codeblock]
#desc # Prints: User 42 is Godot.
#desc print("User {} is {}.".format([42, "Godot"], "{}"))
#desc print("User {id} is {name}.".format([["id", 42], ["name", "Godot"]]))
#desc [/codeblock]
func format(values: Variant, placeholder: String) -> String:
	pass;

#desc If the string is a valid file path, returns the base directory name.
func get_base_dir() -> String:
	pass;

#desc If the string is a valid file path, returns the full file path without the extension.
func get_basename() -> String:
	pass;

#desc Returns the extension without the leading period character ([code].[/code]) if the string is a valid file name or path. If the string does not contain an extension, returns an empty string instead.
#desc [codeblock]
#desc print("/path/to/file.txt".get_extension())  # "txt"
#desc print("file.txt".get_extension())  # "txt"
#desc print("file.sample.txt".get_extension())  # "txt"
#desc print(".txt".get_extension())  # "txt"
#desc print("file.txt.".get_extension())  # "" (empty string)
#desc print("file.txt..".get_extension())  # "" (empty string)
#desc print("txt".get_extension())  # "" (empty string)
#desc print("".get_extension())  # "" (empty string)
#desc [/codeblock]
func get_extension() -> String:
	pass;

#desc If the string is a valid file path, returns the filename.
func get_file() -> String:
	pass;

#desc Splits a string using a [param delimiter] and returns a substring at index [param slice]. Returns an empty string if the index doesn't exist.
#desc This is a more performant alternative to [method split] for cases when you need only one element from the array at a fixed index.
#desc Example:
#desc [codeblock]
#desc print("i/am/example/string".get_slice("/", 2)) # Prints 'example'.
#desc [/codeblock]
func get_slice(delimiter: String, slice: int) -> String:
	pass;

#desc Splits a string using a [param delimiter] and returns a number of slices.
func get_slice_count() -> int:
	pass;

#desc Splits a string using a Unicode character with code [param delimiter] and returns a substring at index [param slice]. Returns an empty string if the index doesn't exist.
#desc This is a more performant alternative to [method split] for cases when you need only one element from the array at a fixed index.
func get_slicec(delimiter: int, slice: int) -> String:
	pass;

#desc Returns the 32-bit hash value representing the string's contents.
#desc [b]Note:[/b] [String]s with equal content will always produce identical hash values. However, the reverse is not true. Returning identical hash values does [i]not[/i] imply the strings are equal, because different strings can have identical hash values due to hash collisions.
func hash() -> int:
	pass;

#desc Converts a string containing a hexadecimal number into an integer. Hexadecimal strings can either be prefixed with [code]0x[/code] or not, and they can also start with a [code]-[/code] before the optional prefix.
#desc [codeblocks]
#desc [gdscript]
#desc print("0xff".hex_to_int()) # Prints "255".
#desc print("ab".hex_to_int()) # Prints "171".
#desc [/gdscript]
#desc [csharp]
#desc GD.Print("0xff".HexToInt()); // Prints "255".
#desc GD.Print("ab".HexToInt()); // Prints "171".
#desc [/csharp]
#desc [/codeblocks]
func hex_to_int() -> int:
	pass;

#desc Converts an integer representing a number of bytes into a human-readable form.
#desc Note that this output is in [url=https://en.wikipedia.org/wiki/Binary_prefix#IEC_prefixes]IEC prefix format[/url], and includes [code]B[/code], [code]KiB[/code], [code]MiB[/code], [code]GiB[/code], [code]TiB[/code], [code]PiB[/code], and [code]EiB[/code].
static func humanize_size() -> String:
	pass;

#desc Returns a copy of the string with lines indented with [param prefix].
#desc For example, the string can be indented with two tabs using [code]"\t\t"[/code], or four spaces using [code]"    "[/code]. The prefix can be any string so it can also be used to comment out strings with e.g. [code]"# "[/code]. See also [method dedent] to remove indentation.
#desc [b]Note:[/b] Empty lines are kept empty.
func indent() -> String:
	pass;

#desc Returns a copy of the string with the substring [param what] inserted at the given [param position].
func insert(position: int, what: String) -> String:
	pass;

#desc Returns [code]true[/code] if the string is a path to a file or directory and its starting point is explicitly defined. This includes [code]res://[/code], [code]user://[/code], [code]C:\[/code], [code]/[/code], etc.
func is_absolute_path() -> bool:
	pass;

#desc Returns [code]true[/code] if the length of the string equals [code]0[/code].
func is_empty() -> bool:
	pass;

#desc Returns [code]true[/code] if the string is a path to a file or directory and its starting point is implicitly defined within the context it is being used. The starting point may refer to the current directory ([code]./[/code]), or the current [Node].
func is_relative_path() -> bool:
	pass;

#desc Returns [code]true[/code] if this string is a subsequence of the given string.
func is_subsequence_of() -> bool:
	pass;

#desc Returns [code]true[/code] if this string is a subsequence of the given string, without considering case.
func is_subsequence_ofn() -> bool:
	pass;

#desc Returns [code]true[/code] if this string is free from characters that aren't allowed in file names, those being:
#desc [code]: / \ ? * " | % < >[/code]
func is_valid_filename() -> bool:
	pass;

#desc Returns [code]true[/code] if this string contains a valid float. This is inclusive of integers, and also supports exponents:
#desc [codeblock]
#desc print("1.7".is_valid_float()) # Prints "true"
#desc print("24".is_valid_float()) # Prints "true"
#desc print("7e3".is_valid_float()) # Prints "true"
#desc print("Hello".is_valid_float()) # Prints "false"
#desc [/codeblock]
func is_valid_float() -> bool:
	pass;

#desc Returns [code]true[/code] if this string contains a valid hexadecimal number. If [param with_prefix] is [code]true[/code], then a validity of the hexadecimal number is determined by [code]0x[/code] prefix, for instance: [code]0xDEADC0DE[/code].
func is_valid_hex_number() -> bool:
	pass;

#desc Returns [code]true[/code] if this string contains a valid color in hexadecimal HTML notation. Other HTML notations such as named colors or [code]hsl()[/code] colors aren't considered valid by this method and will return [code]false[/code].
func is_valid_html_color() -> bool:
	pass;

#desc Returns [code]true[/code] if this string is a valid identifier. A valid identifier may contain only letters, digits and underscores ([code]_[/code]) and the first character may not be a digit.
#desc [codeblock]
#desc print("good_ident_1".is_valid_identifier()) # Prints "true"
#desc print("1st_bad_ident".is_valid_identifier()) # Prints "false"
#desc print("bad_ident_#2".is_valid_identifier()) # Prints "false"
#desc [/codeblock]
func is_valid_identifier() -> bool:
	pass;

#desc Returns [code]true[/code] if this string contains a valid integer.
#desc [codeblock]
#desc print("7".is_valid_int()) # Prints "true"
#desc print("14.6".is_valid_int()) # Prints "false"
#desc print("L".is_valid_int()) # Prints "false"
#desc print("+3".is_valid_int()) # Prints "true"
#desc print("-12".is_valid_int()) # Prints "true"
#desc [/codeblock]
func is_valid_int() -> bool:
	pass;

#desc Returns [code]true[/code] if this string contains only a well-formatted IPv4 or IPv6 address. This method considers [url=https://en.wikipedia.org/wiki/Reserved_IP_addresses]reserved IP addresses[/url] such as [code]0.0.0.0[/code] as valid.
func is_valid_ip_address() -> bool:
	pass;

#desc Returns a [String] which is the concatenation of the [param parts]. The separator between elements is the string providing this method.
#desc Example:
#desc [codeblocks]
#desc [gdscript]
#desc print(", ".join(["One", "Two", "Three", "Four"]))
#desc [/gdscript]
#desc [csharp]
#desc GD.Print(String.Join(",", new string[] {"One", "Two", "Three", "Four"}));
#desc [/csharp]
#desc [/codeblocks]
func join() -> String:
	pass;

#desc Returns a copy of the string with special characters escaped using the JSON standard.
func json_escape() -> String:
	pass;

#desc Returns a number of characters from the left of the string. If negative [param length] is used, the characters are counted downwards from [String]'s length.
#desc Examples:
#desc [codeblock]
#desc print("sample text".left(3)) #prints "sam"
#desc print("sample text".left(-3)) #prints "sample t"
#desc [/codeblock]
func left() -> String:
	pass;

#desc Returns the number of characters in the string.
func length() -> int:
	pass;

#desc Formats a string to be at least [param min_length] long by adding [param character]s to the left of the string.
func lpad(min_length: int, character: String) -> String:
	pass;

#desc Returns a copy of the string with characters removed from the left. The [param chars] argument is a string specifying the set of characters to be removed.
#desc [b]Note:[/b] The [param chars] is not a prefix. See [method trim_prefix] method that will remove a single prefix string rather than a set of characters.
func lstrip() -> String:
	pass;

#desc Does a simple case-sensitive expression match, where [code]"*"[/code] matches zero or more arbitrary characters and [code]"?"[/code] matches any single character except a period ([code]"."[/code]). An empty string or empty expression always evaluates to [code]false[/code].
func match() -> bool:
	pass;

#desc Does a simple case-insensitive expression match, where [code]"*"[/code] matches zero or more arbitrary characters and [code]"?"[/code] matches any single character except a period ([code]"."[/code]). An empty string or empty expression always evaluates to [code]false[/code].
func matchn() -> bool:
	pass;

#desc Returns the MD5 hash of the string as an array of bytes.
func md5_buffer() -> PackedByteArray:
	pass;

#desc Returns the MD5 hash of the string as a string.
func md5_text() -> String:
	pass;

#desc Performs a case-insensitive [i]natural order[/i] comparison to another string. Returns [code]-1[/code] if less than, [code]1[/code] if greater than, or [code]0[/code] if equal. "less than" or "greater than" are determined by the [url=https://en.wikipedia.org/wiki/List_of_Unicode_characters]Unicode code points[/url] of each string, which roughly matches the alphabetical order. Internally, lowercase characters will be converted to uppercase during the comparison.
#desc When used for sorting, natural order comparison will order suites of numbers as expected by most people. If you sort the numbers from 1 to 10 using natural order, you will get [code][1, 2, 3, ...][/code] instead of [code][1, 10, 2, 3, ...][/code].
#desc [b]Behavior with different string lengths:[/b] Returns [code]1[/code] if the "base" string is longer than the [param to] string or [code]-1[/code] if the "base" string is shorter than the [param to] string. Keep in mind this length is determined by the number of Unicode codepoints, [i]not[/i] the actual visible characters.
#desc [b]Behavior with empty strings:[/b] Returns [code]-1[/code] if the "base" string is empty, [code]1[/code] if the [param to] string is empty or [code]0[/code] if both strings are empty.
#desc To get a boolean result from a string comparison, use the [code]==[/code] operator instead. See also [method nocasecmp_to] and [method casecmp_to].
func naturalnocasecmp_to() -> int:
	pass;

#desc Performs a case-insensitive comparison to another string. Returns [code]-1[/code] if less than, [code]1[/code] if greater than, or [code]0[/code] if equal. "less than" or "greater than" are determined by the [url=https://en.wikipedia.org/wiki/List_of_Unicode_characters]Unicode code points[/url] of each string, which roughly matches the alphabetical order. Internally, lowercase characters will be converted to uppercase during the comparison.
#desc [b]Behavior with different string lengths:[/b] Returns [code]1[/code] if the "base" string is longer than the [param to] string or [code]-1[/code] if the "base" string is shorter than the [param to] string. Keep in mind this length is determined by the number of Unicode codepoints, [i]not[/i] the actual visible characters.
#desc [b]Behavior with empty strings:[/b] Returns [code]-1[/code] if the "base" string is empty, [code]1[/code] if the [param to] string is empty or [code]0[/code] if both strings are empty.
#desc To get a boolean result from a string comparison, use the [code]==[/code] operator instead. See also [method casecmp_to] and [method naturalnocasecmp_to].
func nocasecmp_to() -> int:
	pass;

#desc Converts a [float] to a string representation of a decimal number.
#desc The number of decimal places can be specified with [param decimals]. If [param decimals] is [code]-1[/code] (default), decimal places will be automatically adjusted so that the string representation has 14 significant digits (counting both digits to the left and the right of the decimal point).
#desc Trailing zeros are not included in the string. The last digit will be rounded and not truncated.
#desc Some examples:
#desc [codeblock]
#desc String.num(3.141593)     # "3.141593"
#desc String.num(3.141593, 3)  # "3.142"
#desc String.num(3.14159300)   # "3.141593", no trailing zeros.
#desc # Last digit will be rounded up here, which reduces total digit count since
#desc # trailing zeros are removed:
#desc String.num(42.129999, 5) # "42.13"
#desc # If `decimals` is not specified, the total number of significant digits is 14:
#desc String.num(-0.0000012345432123454321)     # "-0.00000123454321"
#desc String.num(-10000.0000012345432123454321) # "-10000.0000012345"
#desc [/codeblock]
static func num(number: float, decimals: int) -> String:
	pass;

#desc Converts a signed [int] to a string representation of a number.
static func num_int64(number: int, base: int, capitalize_hex: bool) -> String:
	pass;

static func num_scientific() -> String:
	pass;

#desc Converts a unsigned [int] to a string representation of a number.
static func num_uint64(number: int, base: int, capitalize_hex: bool) -> String:
	pass;

#desc Formats a number to have an exact number of [param digits] after the decimal point.
func pad_decimals() -> String:
	pass;

#desc Formats a number to have an exact number of [param digits] before the decimal point.
func pad_zeros() -> String:
	pass;

#desc If the string is a path, this concatenates [param file] at the end of the string as a subpath. E.g. [code]"this/is".path_join("path") == "this/is/path"[/code].
func path_join() -> String:
	pass;

#desc Returns original string repeated a number of times. The number of repetitions is given by the argument.
func repeat() -> String:
	pass;

#desc Replaces occurrences of a case-sensitive substring with the given one inside the string.
func replace(what: String, forwhat: String) -> String:
	pass;

#desc Replaces occurrences of a case-insensitive substring with the given one inside the string.
func replacen(what: String, forwhat: String) -> String:
	pass;

#desc Returns the index of the [b]last[/b] case-sensitive occurrence of the specified string in this instance, or [code]-1[/code]. Optionally, the starting search index can be specified, continuing to the beginning of the string.
func rfind(what: String, from: int) -> int:
	pass;

#desc Returns the index of the [b]last[/b] case-insensitive occurrence of the specified string in this instance, or [code]-1[/code]. Optionally, the starting search index can be specified, continuing to the beginning of the string.
func rfindn(what: String, from: int) -> int:
	pass;

#desc Returns a number of characters from the right of the string. If negative [param length] is used, the characters are counted downwards from [String]'s length.
#desc Examples:
#desc [codeblock]
#desc print("sample text".right(3)) #prints "ext"
#desc print("sample text".right(-3)) #prints "ple text"
#desc [/codeblock]
func right() -> String:
	pass;

#desc Formats a string to be at least [param min_length] long by adding [param character]s to the right of the string.
func rpad(min_length: int, character: String) -> String:
	pass;

#desc Splits the string by a [param delimiter] string and returns an array of the substrings, starting from right.
#desc The splits in the returned array are sorted in the same order as the original string, from left to right.
#desc If [param allow_empty] is [code]true[/code], and there are two adjacent delimiters in the string, it will add an empty string to the array of substrings at this position.
#desc If [param maxsplit] is specified, it defines the number of splits to do from the right up to [param maxsplit]. The default value of 0 means that all items are split, thus giving the same result as [method split].
#desc Example:
#desc [codeblocks]
#desc [gdscript]
#desc var some_string = "One,Two,Three,Four"
#desc var some_array = some_string.rsplit(",", true, 1)
#desc print(some_array.size()) # Prints 2
#desc print(some_array[0]) # Prints "One,Two,Three"
#desc print(some_array[1]) # Prints "Four"
#desc [/gdscript]
#desc [csharp]
#desc // There is no Rsplit.
#desc [/csharp]
#desc [/codeblocks]
func rsplit(delimiter: String, allow_empty: bool, maxsplit: int) -> PackedStringArray:
	pass;

#desc Returns a copy of the string with characters removed from the right. The [param chars] argument is a string specifying the set of characters to be removed.
#desc [b]Note:[/b] The [param chars] is not a suffix. See [method trim_suffix] method that will remove a single suffix string rather than a set of characters.
func rstrip() -> String:
	pass;

#desc Returns the SHA-1 hash of the string as an array of bytes.
func sha1_buffer() -> PackedByteArray:
	pass;

#desc Returns the SHA-1 hash of the string as a string.
func sha1_text() -> String:
	pass;

#desc Returns the SHA-256 hash of the string as an array of bytes.
func sha256_buffer() -> PackedByteArray:
	pass;

#desc Returns the SHA-256 hash of the string as a string.
func sha256_text() -> String:
	pass;

#desc Returns the similarity index ([url=https://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient]Sorensen-Dice coefficient[/url]) of this string compared to another. A result of 1.0 means totally similar, while 0.0 means totally dissimilar.
#desc [codeblock]
#desc print("ABC123".similarity("ABC123")) # Prints "1"
#desc print("ABC123".similarity("XYZ456")) # Prints "0"
#desc print("ABC123".similarity("123ABC")) # Prints "0.8"
#desc print("ABC123".similarity("abc123")) # Prints "0.4"
#desc [/codeblock]
func similarity() -> float:
	pass;

#desc Returns a simplified canonical path.
func simplify_path() -> String:
	pass;

#desc Splits the string by a [param delimiter] string and returns an array of the substrings. The [param delimiter] can be of any length.
#desc If [param allow_empty] is [code]true[/code], and there are two adjacent delimiters in the string, it will add an empty string to the array of substrings at this position.
#desc If [param maxsplit] is specified, it defines the number of splits to do from the left up to [param maxsplit]. The default value of [code]0[/code] means that all items are split.
#desc If you need only one element from the array at a specific index, [method get_slice] is a more performant option.
#desc Example:
#desc [codeblocks]
#desc [gdscript]
#desc var some_string = "One,Two,Three,Four"
#desc var some_array = some_string.split(",", true, 1)
#desc print(some_array.size()) # Prints 2
#desc print(some_array[0]) # Prints "Four"
#desc print(some_array[1]) # Prints "Three,Two,One"
#desc [/gdscript]
#desc [csharp]
#desc var someString = "One,Two,Three,Four";
#desc var someArray = someString.Split(",", true); // This is as close as it gets to Godots API.
#desc GD.Print(someArray[0]); // Prints "Four"
#desc GD.Print(someArray[1]); // Prints "Three,Two,One"
#desc [/csharp]
#desc [/codeblocks]
#desc If you need to split strings with more complex rules, use the [RegEx] class instead.
func split(delimiter: String, allow_empty: bool, maxsplit: int) -> PackedStringArray:
	pass;

#desc Splits the string in floats by using a delimiter string and returns an array of the substrings.
#desc For example, [code]"1,2.5,3"[/code] will return [code][1,2.5,3][/code] if split by [code]","[/code].
#desc If [param allow_empty] is [code]true[/code], and there are two adjacent delimiters in the string, it will add an empty string to the array of substrings at this position.
func split_floats(delimiter: String, allow_empty: bool) -> PackedFloat32Array:
	pass;

#desc Returns a copy of the string stripped of any non-printable character (including tabulations, spaces and line breaks) at the beginning and the end. The optional arguments are used to toggle stripping on the left and right edges respectively.
func strip_edges(left: bool, right: bool) -> String:
	pass;

#desc Returns a copy of the string stripped of any escape character. These include all non-printable control characters of the first page of the ASCII table (< 32), such as tabulation ([code]\t[/code] in C) and newline ([code]\n[/code] and [code]\r[/code]) characters, but not spaces.
func strip_escapes() -> String:
	pass;

#desc Returns part of the string from the position [param from] with length [param len]. Argument [param len] is optional and using [code]-1[/code] will return remaining characters from given position.
func substr(from: int, len: int) -> String:
	pass;

#desc Converts the String (which is a character array) to ASCII/Latin-1 encoded [PackedByteArray] (which is an array of bytes). The conversion is faster compared to [method to_utf8_buffer], as this method assumes that all the characters in the String are ASCII/Latin-1 characters, unsupported characters are replaced with spaces.
func to_ascii_buffer() -> PackedByteArray:
	pass;

#desc Returns the string converted to [code]camelCase[/code].
func to_camel_case() -> String:
	pass;

#desc Converts a string containing a decimal number into a [code]float[/code]. The method will stop on the first non-number character except the first [code].[/code] (decimal point), and [code]e[/code] which is used for exponential.
#desc [codeblock]
#desc print("12.3".to_float()) # 12.3
#desc print("1.2.3".to_float()) # 1.2
#desc print("12ab3".to_float()) # 12
#desc print("1e3".to_float()) # 1000
#desc [/codeblock]
func to_float() -> float:
	pass;

#desc Converts a string containing an integer number into an [code]int[/code]. The method will remove any non-number character and stop if it encounters a [code].[/code].
#desc [codeblock]
#desc print("123".to_int()) # 123
#desc print("a1b2c3".to_int()) # 123
#desc print("1.2.3".to_int()) # 1
#desc [/codeblock]
func to_int() -> int:
	pass;

#desc Returns the string converted to lowercase.
func to_lower() -> String:
	pass;

#desc Returns the string converted to [code]PascalCase[/code].
func to_pascal_case() -> String:
	pass;

#desc Returns the string converted to [code]snake_case[/code].
func to_snake_case() -> String:
	pass;

#desc Returns the string converted to uppercase.
func to_upper() -> String:
	pass;

#desc Converts the String (which is an array of characters) to UTF-16 encoded [PackedByteArray] (which is an array of bytes).
func to_utf16_buffer() -> PackedByteArray:
	pass;

#desc Converts the String (which is an array of characters) to UTF-32 encoded [PackedByteArray] (which is an array of bytes).
func to_utf32_buffer() -> PackedByteArray:
	pass;

#desc Converts the String (which is an array of characters) to UTF-8 encode [PackedByteArray] (which is an array of bytes). The conversion is a bit slower than [method to_ascii_buffer], but supports all UTF-8 characters. Therefore, you should prefer this function over [method to_ascii_buffer].
func to_utf8_buffer() -> PackedByteArray:
	pass;

#desc Removes a given string from the start if it starts with it or leaves the string unchanged.
func trim_prefix() -> String:
	pass;

#desc Removes a given string from the end if it ends with it or leaves the string unchanged.
func trim_suffix() -> String:
	pass;

#desc Returns the character code at position [param at].
func unicode_at() -> int:
	pass;

#desc Decodes a string in URL encoded format. This is meant to decode parameters in a URL when receiving an HTTP request.
#desc [codeblocks]
#desc [gdscript]
#desc print("https://example.org/?escaped=" + "Godot%20Engine%3A%27docs%27".uri_decode())
#desc [/gdscript]
#desc [csharp]
#desc GD.Print("https://example.org/?escaped=" + "Godot%20Engine%3a%27Docs%27".URIDecode());
#desc [/csharp]
#desc [/codeblocks]
func uri_decode() -> String:
	pass;

#desc Encodes a string to URL friendly format. This is meant to encode parameters in a URL when sending an HTTP request.
#desc [codeblocks]
#desc [gdscript]
#desc print("https://example.org/?escaped=" + "Godot Engine:'docs'".uri_encode())
#desc [/gdscript]
#desc [csharp]
#desc GD.Print("https://example.org/?escaped=" + "Godot Engine:'docs'".URIEncode());
#desc [/csharp]
#desc [/codeblocks]
func uri_encode() -> String:
	pass;

#desc Removes any characters from the string that are prohibited in [Node] names ([code].[/code] [code]:[/code] [code]@[/code] [code]/[/code] [code]"[/code]).
func validate_node_name() -> String:
	pass;

#desc Returns a copy of the string with special characters escaped using the XML standard. If [param escape_quotes] is [code]true[/code], the single quote ([code]'[/code]) and double quote ([code]"[/code]) characters are also escaped.
func xml_escape() -> String:
	pass;

#desc Returns a copy of the string with escaped characters replaced by their meanings according to the XML standard.
func xml_unescape() -> String:
	pass;


