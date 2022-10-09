extends RefCounted
#brief Plugin for adding custom parsers to extract strings that are to be translated from custom files (.csv, .json etc.).
#desc [EditorTranslationParserPlugin] is invoked when a file is being parsed to extract strings that require translation. To define the parsing and string extraction logic, override the [method _parse_file] method in script.
#desc Add the extracted strings to argument [code]msgids[/code] or [code]msgids_context_plural[/code] if context or plural is used.
#desc When adding to [code]msgids_context_plural[/code], you must add the data using the format [code]["A", "B", "C"][/code], where [code]A[/code] represents the extracted string, [code]B[/code] represents the context, and [code]C[/code] represents the plural version of the extracted string. If you want to add only context but not plural, put [code]""[/code] for the plural slot. The idea is the same if you only want to add plural but not context. See the code below for concrete examples.
#desc The extracted strings will be written into a POT file selected by user under "POT Generation" in "Localization" tab in "Project Settings" menu.
#desc Below shows an example of a custom parser that extracts strings from a CSV file to write into a POT.
#desc [codeblocks]
#desc [gdscript]
#desc @tool
#desc extends EditorTranslationParserPlugin
#desc 
#desc func _parse_file(path, msgids, msgids_context_plural):
#desc var file = File.new()
#desc file.open(path, File.READ)
#desc var text = file.get_as_text()
#desc var split_strs = text.split(",", false)
#desc for s in split_strs:
#desc msgids.append(s)
#desc #print("Extracted string: " + s)
#desc 
#desc func _get_recognized_extensions():
#desc return ["csv"]
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc 
#desc [Tool]
#desc public class CustomParser : EditorTranslationParserPlugin
#desc {
#desc public override void ParseFile(string path, Godot.Collections.Array msgids, Godot.Collections.Array msgidsContextPlural)
#desc {
#desc var file = new File();
#desc file.Open(path, File.ModeFlags.Read);
#desc string text = file.GetAsText();
#desc string[] splitStrs = text.Split(",", false);
#desc foreach (var s in splitStrs)
#desc {
#desc msgids.Add(s);
#desc //GD.Print("Extracted string: " + s)
#desc }
#desc }
#desc 
#desc public override Godot.Collections.Array GetRecognizedExtensions()
#desc {
#desc return new Godot.Collections.Array{"csv"};
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc To add a translatable string associated with context or plural, add it to [code]msgids_context_plural[/code]:
#desc [codeblocks]
#desc [gdscript]
#desc # This will add a message with msgid "Test 1", msgctxt "context", and msgid_plural "test 1 plurals".
#desc msgids_context_plural.append(["Test 1", "context", "test 1 plurals"])
#desc # This will add a message with msgid "A test without context" and msgid_plural "plurals".
#desc msgids_context_plural.append(["A test without context", "", "plurals"])
#desc # This will add a message with msgid "Only with context" and msgctxt "a friendly context".
#desc msgids_context_plural.append(["Only with context", "a friendly context", ""])
#desc [/gdscript]
#desc [csharp]
#desc // This will add a message with msgid "Test 1", msgctxt "context", and msgid_plural "test 1 plurals".
#desc msgidsContextPlural.Add(new Godot.Collections.Array{"Test 1", "context", "test 1 Plurals"});
#desc // This will add a message with msgid "A test without context" and msgid_plural "plurals".
#desc msgidsContextPlural.Add(new Godot.Collections.Array{"A test without context", "", "plurals"});
#desc // This will add a message with msgid "Only with context" and msgctxt "a friendly context".
#desc msgidsContextPlural.Add(new Godot.Collections.Array{"Only with context", "a friendly context", ""});
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] If you override parsing logic for standard script types (GDScript, C#, etc.), it would be better to load the [code]path[/code] argument using [method ResourceLoader.load]. This is because built-in scripts are loaded as [Resource] type, not [FileAccess] type.
#desc For example:
#desc [codeblocks]
#desc [gdscript]
#desc func _parse_file(path, msgids, msgids_context_plural):
#desc var res = ResourceLoader.load(path, "Script")
#desc var text = res.source_code
#desc # Parsing logic.
#desc 
#desc func _get_recognized_extensions():
#desc return ["gd"]
#desc [/gdscript]
#desc [csharp]
#desc public override void ParseFile(string path, Godot.Collections.Array msgids, Godot.Collections.Array msgidsContextPlural)
#desc {
#desc var res = ResourceLoader.Load<Script>(path, "Script");
#desc string text = res.SourceCode;
#desc // Parsing logic.
#desc }
#desc 
#desc public override Godot.Collections.Array GetRecognizedExtensions()
#desc {
#desc return new Godot.Collections.Array{"gd"};
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc To use [EditorTranslationParserPlugin], register it using the [method EditorPlugin.add_translation_parser_plugin] method first.
class_name EditorTranslationParserPlugin




#desc Gets the list of file extensions to associate with this parser, e.g. [code]["csv"][/code].
func _get_recognized_extensions() -> PackedStringArray:
	pass;

#desc Override this method to define a custom parsing logic to extract the translatable strings.
func _parse_file(path: String, msgids: String[], msgids_context_plural: Array[]) -> void:
	pass;


