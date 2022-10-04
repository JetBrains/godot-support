#brief Creates packages that can be loaded into a running project.
#desc The [PCKPacker] is used to create packages that can be loaded into a running project using [method ProjectSettings.load_resource_pack].
#desc [codeblocks]
#desc [gdscript]
#desc var packer = PCKPacker.new()
#desc packer.pck_start("test.pck")
#desc packer.add_file("res://text.txt", "text.txt")
#desc packer.flush()
#desc [/gdscript]
#desc [csharp]
#desc var packer = new PCKPacker();
#desc packer.PckStart("test.pck");
#desc packer.AddFile("res://text.txt", "text.txt");
#desc packer.Flush();
#desc [/csharp]
#desc [/codeblocks]
#desc The above [PCKPacker] creates package [code]test.pck[/code], then adds a file named [code]text.txt[/code] at the root of the package.
class_name PCKPacker




#desc Adds the [param source_path] file to the current PCK package at the [param pck_path] internal path (should start with [code]res://[/code]).
func add_file(pck_path: String, source_path: String, encrypt: bool) -> int:
	pass;

#desc Writes the files specified using all [method add_file] calls since the last flush. If [param verbose] is [code]true[/code], a list of files added will be printed to the console for easier debugging.
func flush(verbose: bool) -> int:
	pass;

#desc Creates a new PCK file with the name [param pck_name]. The [code].pck[/code] file extension isn't added automatically, so it should be part of [param pck_name] (even though it's not required).
func pck_start(pck_name: String, alignment: int, key: String, encrypt_directory: bool) -> int:
	pass;


