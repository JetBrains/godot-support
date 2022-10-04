#brief Registers a custom resource importer in the editor. Use the class to parse any file and import it as a new resource type.
#desc [EditorImportPlugin]s provide a way to extend the editor's resource import functionality. Use them to import resources from custom files or to provide alternatives to the editor's existing importers.
#desc EditorImportPlugins work by associating with specific file extensions and a resource type. See [method _get_recognized_extensions] and [method _get_resource_type]. They may optionally specify some import presets that affect the import process. EditorImportPlugins are responsible for creating the resources and saving them in the [code].godot/imported[/code] directory (see [member ProjectSettings.application/config/use_hidden_project_data_directory]).
#desc Below is an example EditorImportPlugin that imports a [Mesh] from a file with the extension ".special" or ".spec":
#desc [codeblocks]
#desc [gdscript]
#desc @tool
#desc extends EditorImportPlugin
#desc 
#desc func _get_importer_name():
#desc return "my.special.plugin"
#desc 
#desc func _get_visible_name():
#desc return "Special Mesh"
#desc 
#desc func _get_recognized_extensions():
#desc return ["special", "spec"]
#desc 
#desc func _get_save_extension():
#desc return "mesh"
#desc 
#desc func _get_resource_type():
#desc return "Mesh"
#desc 
#desc func _get_preset_count():
#desc return 1
#desc 
#desc func _get_preset_name(i):
#desc return "Default"
#desc 
#desc func _get_import_options(i):
#desc return [{"name": "my_option", "default_value": false}]
#desc 
#desc func _import(source_file, save_path, options, platform_variants, gen_files):
#desc var file = File.new()
#desc if file.open(source_file, File.READ) != OK:
#desc return FAILED
#desc var mesh = ArrayMesh.new()
#desc # Fill the Mesh with data read in "file", left as an exercise to the reader.
#desc 
#desc var filename = save_path + "." + _get_save_extension()
#desc return ResourceSaver.save(mesh, filename)
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc 
#desc public class MySpecialPlugin : EditorImportPlugin
#desc {
#desc public override String GetImporterName()
#desc {
#desc return "my.special.plugin";
#desc }
#desc 
#desc public override String GetVisibleName()
#desc {
#desc return "Special Mesh";
#desc }
#desc 
#desc public override Godot.Collections.Array GetRecognizedExtensions()
#desc {
#desc return new Godot.Collections.Array{"special", "spec"};
#desc }
#desc 
#desc public override String GetSaveExtension()
#desc {
#desc return "mesh";
#desc }
#desc 
#desc public override String GetResourceType()
#desc {
#desc return "Mesh";
#desc }
#desc 
#desc public override int GetPresetCount()
#desc {
#desc return 1;
#desc }
#desc 
#desc public override String GetPresetName(int i)
#desc {
#desc return "Default";
#desc }
#desc 
#desc public override Godot.Collections.Array GetImportOptions(int i)
#desc {
#desc return new Godot.Collections.Array{new Godot.Collections.Dictionary{{"name", "myOption"}, {"defaultValue", false}}};
#desc }
#desc 
#desc public override int Import(String sourceFile, String savePath, Godot.Collections.Dictionary options, Godot.Collections.Array platformVariants, Godot.Collections.Array genFiles)
#desc {
#desc var file = new File();
#desc if (file.Open(sourceFile, File.ModeFlags.Read) != Error.Ok)
#desc {
#desc return (int)Error.Failed;
#desc }
#desc 
#desc var mesh = new ArrayMesh();
#desc // Fill the Mesh with data read in "file", left as an exercise to the reader.
#desc String filename = savePath + "." + GetSaveExtension();
#desc return (int)ResourceSaver.Save(mesh, filename);
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc To use [EditorImportPlugin], register it using the [method EditorPlugin.add_import_plugin] method first.
class_name EditorImportPlugin




#desc Gets the options and default values for the preset at this index. Returns an Array of Dictionaries with the following keys: [code]name[/code], [code]default_value[/code], [code]property_hint[/code] (optional), [code]hint_string[/code] (optional), [code]usage[/code] (optional).
virtual const func _get_import_options(path: String, preset_index: int) -> Dictionary[]:
	pass;

#desc Gets the order of this importer to be run when importing resources. Importers with [i]lower[/i] import orders will be called first, and higher values will be called later. Use this to ensure the importer runs after the dependencies are already imported. The default import order is [code]0[/code] unless overridden by a specific importer. See [enum ResourceImporter.ImportOrder] for some predefined values.
virtual const func _get_import_order() -> int:
	pass;

#desc Gets the unique name of the importer.
virtual const func _get_importer_name() -> String:
	pass;

#desc This method can be overridden to hide specific import options if conditions are met. This is mainly useful for hiding options that depend on others if one of them is disabled. For example:
#desc [codeblocks]
#desc [gdscript]
#desc func _get_option_visibility(option, options):
#desc # Only show the lossy quality setting if the compression mode is set to "Lossy".
#desc if option == "compress/lossy_quality" and options.has("compress/mode"):
#desc return int(options["compress/mode"]) == COMPRESS_LOSSY # This is a constant that you set
#desc 
#desc return true
#desc [/gdscript]
#desc [csharp]
#desc public void GetOptionVisibility(string option, Godot.Collections.Dictionary options)
#desc {
#desc // Only show the lossy quality setting if the compression mode is set to "Lossy".
#desc if (option == "compress/lossyQuality" && options.Contains("compress/mode"))
#desc {
#desc return (int)options["compress/mode"] == COMPRESS_LOSSY; // This is a constant you set
#desc }
#desc 
#desc return true;
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Returns [code]true[/code] to make all options always visible.
virtual const func _get_option_visibility(path: String, option_name: StringName, options: Dictionary) -> bool:
	pass;

#desc Gets the number of initial presets defined by the plugin. Use [method _get_import_options] to get the default options for the preset and [method _get_preset_name] to get the name of the preset.
virtual const func _get_preset_count() -> int:
	pass;

#desc Gets the name of the options preset at this index.
virtual const func _get_preset_name() -> String:
	pass;

#desc Gets the priority of this plugin for the recognized extension. Higher priority plugins will be preferred. The default priority is [code]1.0[/code].
virtual const func _get_priority() -> float:
	pass;

#desc Gets the list of file extensions to associate with this loader (case-insensitive). e.g. [code]["obj"][/code].
virtual const func _get_recognized_extensions() -> PackedStringArray:
	pass;

#desc Gets the Godot resource type associated with this loader. e.g. [code]"Mesh"[/code] or [code]"Animation"[/code].
virtual const func _get_resource_type() -> String:
	pass;

#desc Gets the extension used to save this resource in the [code].godot/imported[/code] directory (see [member ProjectSettings.application/config/use_hidden_project_data_directory]).
virtual const func _get_save_extension() -> String:
	pass;

#desc Gets the name to display in the import window. You should choose this name as a continuation to "Import as", e.g. "Import as Special Mesh".
virtual const func _get_visible_name() -> String:
	pass;

#desc Imports [param source_file] into [param save_path] with the import [param options] specified. The [param platform_variants] and [param gen_files] arrays will be modified by this function.
#desc This method must be overridden to do the actual importing work. See this class' description for an example of overriding this method.
virtual const func _import(source_file: String, save_path: String, options: Dictionary, platform_variants: String[], gen_files: String[]) -> int:
	pass;


