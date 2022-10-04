#brief Base script that can be used to add extension functions to the editor.
#desc Scripts extending this class and implementing its [method _run] method can be executed from the Script Editor's [b]File > Run[/b] menu option (or by pressing [kbd]Ctrl + Shift + X[/kbd]) while the editor is running. This is useful for adding custom in-editor functionality to Godot. For more complex additions, consider using [EditorPlugin]s instead.
#desc [b]Note:[/b] Extending scripts need to have [code]tool[/code] mode enabled.
#desc [b]Example script:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc @tool
#desc extends EditorScript
#desc 
#desc func _run():
#desc print("Hello from the Godot Editor!")
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc 
#desc [Tool]
#desc public class HelloEditor : EditorScript
#desc {
#desc public override void _Run()
#desc {
#desc GD.Print("Hello from the Godot Editor!");
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] The script is run in the Editor context, which means the output is visible in the console window started with the Editor (stdout) instead of the usual Godot [b]Output[/b] dock.
class_name EditorScript




#desc This method is executed by the Editor when [b]File > Run[/b] is used.
virtual func _run() -> void:
	pass;

#desc Adds [param node] as a child of the root node in the editor context.
#desc [b]Warning:[/b] The implementation of this method is currently disabled.
func add_root_node() -> void:
	pass;

#desc Returns the [EditorInterface] singleton instance.
func get_editor_interface() -> EditorInterface:
	pass;

#desc Returns the Editor's currently active scene.
func get_scene() -> Node:
	pass;


