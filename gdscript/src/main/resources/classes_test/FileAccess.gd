#brief Type to handle file reading and writing operations.
#desc File type. This is used to permanently store data into the user device's file system and to read from it. This can be used to store game save data or player configuration files, for example.
#desc Here's a sample on how to write and read from a file:
#desc [codeblocks]
#desc [gdscript]
#desc func save(content):
#desc var file = FileAccess.open("user://save_game.dat", FileAccess.WRITE)
#desc file.store_string(content)
#desc 
#desc func load():
#desc var file = FileAccess.open("user://save_game.dat", FileAccess.READ)
#desc var content = file.get_as_text()
#desc return content
#desc [/gdscript]
#desc [csharp]
#desc public void Save(string content)
#desc {
#desc using var file = FileAccess.Open("user://save_game.dat", FileAccess.ModeFlags.Write);
#desc file.StoreString(content);
#desc }
#desc 
#desc public string Load()
#desc {
#desc using var file = FileAccess.Open("user://save_game.dat", FileAccess.ModeFlags.Read);
#desc string content = file.GetAsText();
#desc return content;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc In the example above, the file will be saved in the user data folder as specified in the [url=$DOCS_URL/tutorials/io/data_paths.html]Data paths[/url] documentation.
#desc There is no method to close a file in order to free it from use. Instead, [FileAccess] will close when it's freed, which happens when it goes out of scope or when it gets assigned with [code]null[/code]. In C# the reference must be disposed after we are done using it, this can be done with the [code]using[/code] statement or calling the [code]Dispose[/code] method directly.
#desc [codeblocks]
#desc [gdscript]
#desc var file = FileAccess.open("res://something") # File is opened and locked for use.
#desc file = null # File is closed.
#desc [/gdscript]
#desc [csharp]
#desc using var file = FileAccess.Open("res://something"); // File is opened and locked for use.
#desc // The using statement calls Dispose when going out of scope.
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] To access project resources once exported, it is recommended to use [ResourceLoader] instead of the [FileAccess] API, as some files are converted to engine-specific formats and their original source files might not be present in the exported PCK package.
#desc [b]Note:[/b] Files are automatically closed only if the process exits "normally" (such as by clicking the window manager's close button or pressing [b]Alt + F4[/b]). If you stop the project execution by pressing [b]F8[/b] while the project is running, the file won't be closed as the game process will be killed. You can work around this by calling [method flush] at regular intervals.
class_name FileAccess

#desc Opens the file for read operations. The cursor is positioned at the beginning of the file.
const READ = 1;

#desc Opens the file for write operations. The file is created if it does not exist, and truncated if it does.
const WRITE = 2;

#desc Opens the file for read and write operations. Does not truncate the file. The cursor is positioned at the beginning of the file.
const READ_WRITE = 3;

#desc Opens the file for read and write operations. The file is created if it does not exist, and truncated if it does. The cursor is positioned at the beginning of the file.
const WRITE_READ = 7;

#desc Uses the [url=https://fastlz.org/]FastLZ[/url] compression method.
const COMPRESSION_FASTLZ = 0;

#desc Uses the [url=https://en.wikipedia.org/wiki/DEFLATE]DEFLATE[/url] compression method.
const COMPRESSION_DEFLATE = 1;

#desc Uses the [url=https://facebook.github.io/zstd/]Zstandard[/url] compression method.
const COMPRESSION_ZSTD = 2;

#desc Uses the [url=https://www.gzip.org/]gzip[/url] compression method.
const COMPRESSION_GZIP = 3;


#desc If [code]true[/code], the file is read with big-endian [url=https://en.wikipedia.org/wiki/Endianness]endianness[/url]. If [code]false[/code], the file is read with little-endian endianness. If in doubt, leave this to [code]false[/code] as most files are written with little-endian endianness.
#desc [b]Note:[/b] [member big_endian] is only about the file format, not the CPU type. The CPU endianness doesn't affect the default endianness for files written.
#desc [b]Note:[/b] This is always reset to [code]false[/code] whenever you open the file. Therefore, you must set [member big_endian] [i]after[/i] opening the file, not before.
var big_endian: bool;



#desc Returns [code]true[/code] if the file cursor has already read past the end of the file.
#desc [b]Note:[/b] [code]eof_reached() == false[/code] cannot be used to check whether there is more data available. To loop while there is more data available, use:
#desc [codeblocks]
#desc [gdscript]
#desc while file.get_position() < file.get_length():
#desc # Read data
#desc [/gdscript]
#desc [csharp]
#desc while (file.GetPosition() < file.GetLength())
#desc {
#desc // Read data
#desc }
#desc [/csharp]
#desc [/codeblocks]
func eof_reached() -> bool:
	pass;

#desc Returns [code]true[/code] if the file exists in the given path.
#desc [b]Note:[/b] Many resources types are imported (e.g. textures or sound files), and their source asset will not be included in the exported game, as only the imported version is used. See [method ResourceLoader.exists] for an alternative approach that takes resource remapping into account.
#desc For a non-static, relative equivalent, use [method DirAccess.file_exists].
static func file_exists(path: String) -> bool:
	pass;

#desc Writes the file's buffer to disk. Flushing is automatically performed when the file is closed. This means you don't need to call [method flush] manually before closing a file. Still, calling [method flush] can be used to ensure the data is safe even if the project crashes instead of being closed gracefully.
#desc [b]Note:[/b] Only call [method flush] when you actually need it. Otherwise, it will decrease performance due to constant disk writes.
func flush() -> void:
	pass;

#desc Returns the next 16 bits from the file as an integer. See [method store_16] for details on what values can be stored and retrieved this way.
func get_16() -> int:
	pass;

#desc Returns the next 32 bits from the file as an integer. See [method store_32] for details on what values can be stored and retrieved this way.
func get_32() -> int:
	pass;

#desc Returns the next 64 bits from the file as an integer. See [method store_64] for details on what values can be stored and retrieved this way.
func get_64() -> int:
	pass;

#desc Returns the next 8 bits from the file as an integer. See [method store_8] for details on what values can be stored and retrieved this way.
func get_8() -> int:
	pass;

#desc Returns the whole file as a [String]. Text is interpreted as being UTF-8 encoded.
#desc If [param skip_cr] is [code]true[/code], carriage return characters ([code]\r[/code], CR) will be ignored when parsing the UTF-8, so that only line feed characters ([code]\n[/code], LF) represent a new line (Unix convention).
func get_as_text(skip_cr: bool) -> String:
	pass;

#desc Returns next [param length] bytes of the file as a [PackedByteArray].
func get_buffer(length: int) -> PackedByteArray:
	pass;

#desc Returns the next value of the file in CSV (Comma-Separated Values) format. You can pass a different delimiter [param delim] to use other than the default [code]","[/code] (comma). This delimiter must be one-character long, and cannot be a double quotation mark.
#desc Text is interpreted as being UTF-8 encoded. Text values must be enclosed in double quotes if they include the delimiter character. Double quotes within a text value can be escaped by doubling their occurrence.
#desc For example, the following CSV lines are valid and will be properly parsed as two strings each:
#desc [codeblock]
#desc Alice,"Hello, Bob!"
#desc Bob,Alice! What a surprise!
#desc Alice,"I thought you'd reply with ""Hello, world""."
#desc [/codeblock]
#desc Note how the second line can omit the enclosing quotes as it does not include the delimiter. However it [i]could[/i] very well use quotes, it was only written without for demonstration purposes. The third line must use [code]""[/code] for each quotation mark that needs to be interpreted as such instead of the end of a text value.
func get_csv_line(delim: String) -> PackedStringArray:
	pass;

#desc Returns the next 64 bits from the file as a floating-point number.
func get_double() -> float:
	pass;

#desc Returns the last error that happened when trying to perform operations. Compare with the [code]ERR_FILE_*[/code] constants from [enum Error].
func get_error() -> int:
	pass;

#desc Returns the next 32 bits from the file as a floating-point number.
func get_float() -> float:
	pass;

#desc Returns the size of the file in bytes.
func get_length() -> int:
	pass;

#desc Returns the next line of the file as a [String].
#desc Text is interpreted as being UTF-8 encoded.
func get_line() -> String:
	pass;

#desc Returns an MD5 String representing the file at the given path or an empty [String] on failure.
static func get_md5(path: String) -> String:
	pass;

#desc Returns the last time the [param file] was modified in Unix timestamp format or returns a [String] "ERROR IN [code]file[/code]". This Unix timestamp can be converted to another format using the [Time] singleton.
static func get_modified_time(file: String) -> int:
	pass;

#desc Returns the result of the last [method open] call in the current thread.
static func get_open_error() -> int:
	pass;

#desc Returns a [String] saved in Pascal format from the file.
#desc Text is interpreted as being UTF-8 encoded.
func get_pascal_string() -> String:
	pass;

#desc Returns the path as a [String] for the current open file.
func get_path() -> String:
	pass;

#desc Returns the absolute path as a [String] for the current open file.
func get_path_absolute() -> String:
	pass;

#desc Returns the file cursor's position.
func get_position() -> int:
	pass;

#desc Returns the next bits from the file as a floating-point number.
func get_real() -> float:
	pass;

#desc Returns a SHA-256 [String] representing the file at the given path or an empty [String] on failure.
static func get_sha256(path: String) -> String:
	pass;

#desc Returns the next [Variant] value from the file. If [param allow_objects] is [code]true[/code], decoding objects is allowed.
#desc [b]Warning:[/b] Deserialized objects can contain code which gets executed. Do not use this option if the serialized object comes from untrusted sources to avoid potential security threats such as remote code execution.
func get_var(allow_objects: bool) -> Variant:
	pass;

#desc Returns [code]true[/code] if the file is currently opened.
func is_open() -> bool:
	pass;

#desc Creates a new [FileAccess] object and opens the file for writing or reading, depending on the flags.
#desc Returns [code]null[/code] if opening the file failed. You can use [method get_open_error] to check the error that occurred.
static func open(path: String, flags: int) -> FileAccess:
	pass;

#desc Creates a new [FileAccess] object and opens a compressed file for reading or writing.
#desc [b]Note:[/b] [method open_compressed] can only read files that were saved by Godot, not third-party compression formats. See [url=https://github.com/godotengine/godot/issues/28999]GitHub issue #28999[/url] for a workaround.
#desc Returns [code]null[/code] if opening the file failed. You can use [method get_open_error] to check the error that occurred.
static func open_compressed(path: String, mode_flags: int, compression_mode: int) -> FileAccess:
	pass;

#desc Creates a new [FileAccess] object and opens an encrypted file in write or read mode. You need to pass a binary key to encrypt/decrypt it.
#desc [b]Note:[/b] The provided key must be 32 bytes long.
#desc Returns [code]null[/code] if opening the file failed. You can use [method get_open_error] to check the error that occurred.
static func open_encrypted(path: String, mode_flags: int, key: PackedByteArray) -> FileAccess:
	pass;

#desc Creates a new [FileAccess] object and opens an encrypted file in write or read mode. You need to pass a password to encrypt/decrypt it.
#desc Returns [code]null[/code] if opening the file failed. You can use [method get_open_error] to check the error that occurred.
static func open_encrypted_with_pass(path: String, mode_flags: int, pass: String) -> FileAccess:
	pass;

#desc Changes the file reading/writing cursor to the specified position (in bytes from the beginning of the file).
func seek(position: int) -> void:
	pass;

#desc Changes the file reading/writing cursor to the specified position (in bytes from the end of the file).
#desc [b]Note:[/b] This is an offset, so you should use negative numbers or the cursor will be at the end of the file.
func seek_end(position: int) -> void:
	pass;

#desc Stores an integer as 16 bits in the file.
#desc [b]Note:[/b] The [param value] should lie in the interval [code][0, 2^16 - 1][/code]. Any other value will overflow and wrap around.
#desc To store a signed integer, use [method store_64] or store a signed integer from the interval [code][-2^15, 2^15 - 1][/code] (i.e. keeping one bit for the signedness) and compute its sign manually when reading. For example:
#desc [codeblocks]
#desc [gdscript]
#desc const MAX_15B = 1 << 15
#desc const MAX_16B = 1 << 16
#desc 
#desc func unsigned16_to_signed(unsigned):
#desc return (unsigned + MAX_15B) % MAX_16B - MAX_15B
#desc 
#desc func _ready():
#desc var f = FileAccess.open("user://file.dat", FileAccess.WRITE_READ)
#desc f.store_16(-42) # This wraps around and stores 65494 (2^16 - 42).
#desc f.store_16(121) # In bounds, will store 121.
#desc f.seek(0) # Go back to start to read the stored value.
#desc var read1 = f.get_16() # 65494
#desc var read2 = f.get_16() # 121
#desc var converted1 = unsigned16_to_signed(read1) # -42
#desc var converted2 = unsigned16_to_signed(read2) # 121
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc using var f = FileAccess.Open("user://file.dat", FileAccess.ModeFlags.WriteRead);
#desc f.Store16(unchecked((ushort)-42)); // This wraps around and stores 65494 (2^16 - 42).
#desc f.Store16(121); // In bounds, will store 121.
#desc f.Seek(0); // Go back to start to read the stored value.
#desc ushort read1 = f.Get16(); // 65494
#desc ushort read2 = f.Get16(); // 121
#desc short converted1 = BitConverter.ToInt16(BitConverter.GetBytes(read1), 0); // -42
#desc short converted2 = BitConverter.ToInt16(BitConverter.GetBytes(read2), 0); // 121
#desc }
#desc [/csharp]
#desc [/codeblocks]
func store_16(value: int) -> void:
	pass;

#desc Stores an integer as 32 bits in the file.
#desc [b]Note:[/b] The [param value] should lie in the interval [code][0, 2^32 - 1][/code]. Any other value will overflow and wrap around.
#desc To store a signed integer, use [method store_64], or convert it manually (see [method store_16] for an example).
func store_32(value: int) -> void:
	pass;

#desc Stores an integer as 64 bits in the file.
#desc [b]Note:[/b] The [param value] must lie in the interval [code][-2^63, 2^63 - 1][/code] (i.e. be a valid [int] value).
func store_64(value: int) -> void:
	pass;

#desc Stores an integer as 8 bits in the file.
#desc [b]Note:[/b] The [param value] should lie in the interval [code][0, 255][/code]. Any other value will overflow and wrap around.
#desc To store a signed integer, use [method store_64], or convert it manually (see [method store_16] for an example).
func store_8(value: int) -> void:
	pass;

#desc Stores the given array of bytes in the file.
func store_buffer(buffer: PackedByteArray) -> void:
	pass;

#desc Store the given [PackedStringArray] in the file as a line formatted in the CSV (Comma-Separated Values) format. You can pass a different delimiter [param delim] to use other than the default [code]","[/code] (comma). This delimiter must be one-character long.
#desc Text will be encoded as UTF-8.
func store_csv_line(values: PackedStringArray, delim: String) -> void:
	pass;

#desc Stores a floating-point number as 64 bits in the file.
func store_double(value: float) -> void:
	pass;

#desc Stores a floating-point number as 32 bits in the file.
func store_float(value: float) -> void:
	pass;

#desc Appends [param line] to the file followed by a line return character ([code]\n[/code]), encoding the text as UTF-8.
func store_line(line: String) -> void:
	pass;

#desc Stores the given [String] as a line in the file in Pascal format (i.e. also store the length of the string).
#desc Text will be encoded as UTF-8.
func store_pascal_string(string: String) -> void:
	pass;

#desc Stores a floating-point number in the file.
func store_real(value: float) -> void:
	pass;

#desc Appends [param string] to the file without a line return, encoding the text as UTF-8.
#desc [b]Note:[/b] This method is intended to be used to write text files. The string is stored as a UTF-8 encoded buffer without string length or terminating zero, which means that it can't be loaded back easily. If you want to store a retrievable string in a binary file, consider using [method store_pascal_string] instead. For retrieving strings from a text file, you can use [code]get_buffer(length).get_string_from_utf8()[/code] (if you know the length) or [method get_as_text].
func store_string(string: String) -> void:
	pass;

#desc Stores any Variant value in the file. If [param full_objects] is [code]true[/code], encoding objects is allowed (and can potentially include code).
#desc [b]Note:[/b] Not all properties are included. Only properties that are configured with the [constant PROPERTY_USAGE_STORAGE] flag set will be serialized. You can add a new usage flag to a property by overriding the [method Object._get_property_list] method in your class. You can also check how property usage is configured by calling [method Object._get_property_list]. See [enum PropertyUsageFlags] for the possible usage flags.
func store_var(value: Variant, full_objects: bool) -> void:
	pass;


