extends EditorResourcePicker
#brief Godot editor's control for selecting the [code]script[/code] property of a [Node].
#desc Similar to [EditorResourcePicker] this [Control] node is used in the editor's Inspector dock, but only to edit the [code]script[/code] property of a [Node]. Default options for creating new resources of all possible subtypes are replaced with dedicated buttons that open the "Attach Node Script" dialog. Can be used with [EditorInspectorPlugin] to recreate the same behavior.
#desc [b]Note:[/b] You must set the [member script_owner] for the custom context menu items to work.
class_name EditorScriptPicker


#desc The owner [Node] of the script property that holds the edited resource.
var script_owner: Node;




