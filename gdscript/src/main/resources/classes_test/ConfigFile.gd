#brief Helper class to handle INI-style files.
#desc This helper class can be used to store [Variant] values on the filesystem using INI-style formatting. The stored values are identified by a section and a key:
#desc [codeblock]
#desc [section]
#desc some_key=42
#desc string_example="Hello World3D!"
#desc a_vector=Vector3(1, 0, 2)
#desc [/codeblock]
#desc The stored data can be saved to or parsed from a file, though ConfigFile objects can also be used directly without accessing the filesystem.
#desc The following example shows how to create a simple [ConfigFile] and save it on disc:
#desc [codeblocks]
#desc [gdscript]
#desc # Create new ConfigFile object.
#desc var config = ConfigFile.new()
#desc 
#desc # Store some values.
#desc config.set_value("Player1", "player_name", "Steve")
#desc config.set_value("Player1", "best_score", 10)
#desc config.set_value("Player2", "player_name", "V3geta")
#desc config.set_value("Player2", "best_score", 9001)
#desc 
#desc # Save it to a file (overwrite if already exists).
#desc config.save("user://scores.cfg")
#desc [/gdscript]
#desc [csharp]
#desc // Create new ConfigFile object.
#desc var config = new ConfigFile();
#desc 
#desc // Store some values.
#desc config.SetValue("Player1", "player_name", "Steve");
#desc config.SetValue("Player1", "best_score", 10);
#desc config.SetValue("Player2", "player_name", "V3geta");
#desc config.SetValue("Player2", "best_score", 9001);
#desc 
#desc // Save it to a file (overwrite if already exists).
#desc config.Save("user://scores.cfg");
#desc [/csharp]
#desc [/codeblocks]
#desc This example shows how the above file could be loaded:
#desc [codeblocks]
#desc [gdscript]
#desc var score_data = {}
#desc var config = ConfigFile.new()
#desc 
#desc # Load data from a file.
#desc var err = config.load("user://scores.cfg")
#desc 
#desc # If the file didn't load, ignore it.
#desc if err != OK:
#desc return
#desc 
#desc # Iterate over all sections.
#desc for player in config.get_sections():
#desc # Fetch the data for each section.
#desc var player_name = config.get_value(player, "player_name")
#desc var player_score = config.get_value(player, "best_score")
#desc score_data[player_name] = player_score
#desc [/gdscript]
#desc [csharp]
#desc var score_data = new Godot.Collections.Dictionary();
#desc var config = new ConfigFile();
#desc 
#desc // Load data from a file.
#desc Error err = config.Load("user://scores.cfg");
#desc 
#desc // If the file didn't load, ignore it.
#desc if (err != Error.Ok)
#desc {
#desc return;
#desc }
#desc 
#desc // Iterate over all sections.
#desc foreach (String player in config.GetSections())
#desc {
#desc // Fetch the data for each section.
#desc var player_name = (String)config.GetValue(player, "player_name");
#desc var player_score = (int)config.GetValue(player, "best_score");
#desc score_data[player_name] = player_score;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Any operation that mutates the ConfigFile such as [method set_value], [method clear], or [method erase_section], only changes what is loaded in memory. If you want to write the change to a file, you have to save the changes with [method save], [method save_encrypted], or [method save_encrypted_pass].
#desc Keep in mind that section and property names can't contain spaces. Anything after a space will be ignored on save and on load.
#desc ConfigFiles can also contain manually written comment lines starting with a semicolon ([code];[/code]). Those lines will be ignored when parsing the file. Note that comments will be lost when saving the ConfigFile. This can still be useful for dedicated server configuration files, which are typically never overwritten without explicit user action.
#desc [b]Note:[/b] The file extension given to a ConfigFile does not have any impact on its formatting or behavior. By convention, the [code].cfg[/code] extension is used here, but any other extension such as [code].ini[/code] is also valid. Since neither [code].cfg[/code] nor [code].ini[/code] are standardized, Godot's ConfigFile formatting may differ from files written by other programs.
class_name ConfigFile




#desc Removes the entire contents of the config.
func clear() -> void:
	pass;

#desc Obtain the text version of this config file (the same text that would be written to a file).
func encode_to_text() -> String:
	pass;

#desc Deletes the specified section along with all the key-value pairs inside. Raises an error if the section does not exist.
func erase_section(section: String) -> void:
	pass;

#desc Deletes the specified key in a section. Raises an error if either the section or the key do not exist.
func erase_section_key(section: String, key: String) -> void:
	pass;

#desc Returns an array of all defined key identifiers in the specified section. Raises an error and returns an empty array if the section does not exist.
func get_section_keys(section: String) -> PackedStringArray:
	pass;

#desc Returns an array of all defined section identifiers.
func get_sections() -> PackedStringArray:
	pass;

#desc Returns the current value for the specified section and key. If either the section or the key do not exist, the method returns the fallback [param default] value. If [param default] is not specified or set to [code]null[/code], an error is also raised.
func get_value(section: String, key: String, default: Variant) -> Variant:
	pass;

#desc Returns [code]true[/code] if the specified section exists.
func has_section(section: String) -> bool:
	pass;

#desc Returns [code]true[/code] if the specified section-key pair exists.
func has_section_key(section: String, key: String) -> bool:
	pass;

#desc Loads the config file specified as a parameter. The file's contents are parsed and loaded in the [ConfigFile] object which the method was called on.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func load(path: String) -> int:
	pass;

#desc Loads the encrypted config file specified as a parameter, using the provided [param key] to decrypt it. The file's contents are parsed and loaded in the [ConfigFile] object which the method was called on.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func load_encrypted(path: String, key: PackedByteArray) -> int:
	pass;

#desc Loads the encrypted config file specified as a parameter, using the provided [param password] to decrypt it. The file's contents are parsed and loaded in the [ConfigFile] object which the method was called on.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func load_encrypted_pass(path: String, password: String) -> int:
	pass;

#desc Parses the passed string as the contents of a config file. The string is parsed and loaded in the ConfigFile object which the method was called on.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func parse(data: String) -> int:
	pass;

#desc Saves the contents of the [ConfigFile] object to the file specified as a parameter. The output file uses an INI-style structure.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func save(path: String) -> int:
	pass;

#desc Saves the contents of the [ConfigFile] object to the AES-256 encrypted file specified as a parameter, using the provided [param key] to encrypt it. The output file uses an INI-style structure.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func save_encrypted(path: String, key: PackedByteArray) -> int:
	pass;

#desc Saves the contents of the [ConfigFile] object to the AES-256 encrypted file specified as a parameter, using the provided [param password] to encrypt it. The output file uses an INI-style structure.
#desc Returns one of the [enum Error] code constants ([code]OK[/code] on success).
func save_encrypted_pass(path: String, password: String) -> int:
	pass;

#desc Assigns a value to the specified key of the specified section. If either the section or the key do not exist, they are created. Passing a [code]null[/code] value deletes the specified key if it exists, and deletes the section if it ends up empty once the key has been removed.
func set_value(section: String, key: String, value: Variant) -> void:
	pass;


