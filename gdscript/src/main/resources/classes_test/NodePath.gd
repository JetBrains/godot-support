#brief Pre-parsed scene tree path.
#desc A pre-parsed relative or absolute path in a scene tree, for use with [method Node.get_node] and similar functions. It can reference a node, a resource within a node, or a property of a node or resource. For instance, [code]"Path2D/PathFollow2D/Sprite2D:texture:size"[/code] would refer to the [code]size[/code] property of the [code]texture[/code] resource on the node named [code]"Sprite2D"[/code] which is a child of the other named nodes in the path.
#desc You will usually just pass a string to [method Node.get_node] and it will be automatically converted, but you may occasionally want to parse a path ahead of time with [NodePath] or the literal syntax [code]^"path"[/code]. Exporting a [NodePath] variable will give you a node selection widget in the properties panel of the editor, which can often be useful.
#desc A [NodePath] is composed of a list of slash-separated node names (like a filesystem path) and an optional colon-separated list of "subnames" which can be resources or properties.
#desc Some examples of NodePaths include the following:
#desc [codeblock]
#desc # No leading slash means it is relative to the current node.
#desc ^"A" # Immediate child A
#desc ^"A/B" # A's child B
#desc ^"." # The current node.
#desc ^".." # The parent node.
#desc ^"../C" # A sibling node C.
#desc # A leading slash means it is absolute from the SceneTree.
#desc ^"/root" # Equivalent to get_tree().get_root().
#desc ^"/root/Main" # If your main scene's root node were named "Main".
#desc ^"/root/MyAutoload" # If you have an autoloaded node or scene.
#desc [/codeblock]
#desc See also [StringName], which is a similar concept for general-purpose string interning.
#desc [b]Note:[/b] In the editor, [NodePath] properties are automatically updated when moving, renaming or deleting a node in the scene tree, but they are never updated at runtime.
class_name NodePath



#desc Constructs an empty [NodePath].
func NodePath() -> NodePath:
	pass;

#desc Constructs a [NodePath] as a copy of the given [NodePath]. [code]NodePath("example")[/code] is equivalent to [code]^"example"[/code].
func NodePath() -> NodePath:
	pass;

#desc Creates a NodePath from a string, e.g. [code]"Path2D/PathFollow2D/Sprite2D:texture:size"[/code]. A path is absolute if it starts with a slash. Absolute paths are only valid in the global scene tree, not within individual scenes. In a relative path, [code]"."[/code] and [code]".."[/code] indicate the current node and its parent.
#desc The "subnames" optionally included after the path to the target node can point to resources or properties, and can also be nested.
#desc Examples of valid NodePaths (assuming that those nodes exist and have the referenced resources or properties):
#desc [codeblock]
#desc # Points to the Sprite2D node.
#desc "Path2D/PathFollow2D/Sprite2D"
#desc # Points to the Sprite2D node and its "texture" resource.
#desc # get_node() would retrieve "Sprite2D", while get_node_and_resource()
#desc # would retrieve both the Sprite2D node and the "texture" resource.
#desc "Path2D/PathFollow2D/Sprite2D:texture"
#desc # Points to the Sprite2D node and its "position" property.
#desc "Path2D/PathFollow2D/Sprite2D:position"
#desc # Points to the Sprite2D node and the "x" component of its "position" property.
#desc "Path2D/PathFollow2D/Sprite2D:position:x"
#desc # Absolute path (from "root")
#desc "/root/Level/Path2D"
#desc [/codeblock]
func NodePath() -> NodePath:
	pass;


#desc Returns a node path with a colon character ([code]:[/code]) prepended, transforming it to a pure property path with no node name (defaults to resolving from the current node).
#desc [codeblocks]
#desc [gdscript]
#desc # This will be parsed as a node path to the "x" property in the "position" node.
#desc var node_path = NodePath("position:x")
#desc # This will be parsed as a node path to the "x" component of the "position" property in the current node.
#desc var property_path = node_path.get_as_property_path()
#desc print(property_path) # :position:x
#desc [/gdscript]
#desc [csharp]
#desc // This will be parsed as a node path to the "x" property in the "position" node.
#desc var nodePath = new NodePath("position:x");
#desc // This will be parsed as a node path to the "x" component of the "position" property in the current node.
#desc NodePath propertyPath = nodePath.GetAsPropertyPath();
#desc GD.Print(propertyPath); // :position:x
#desc [/csharp]
#desc [/codeblocks]
func get_as_property_path() -> NodePath:
	pass;

#desc Returns all paths concatenated with a slash character ([code]/[/code]) as separator without subnames.
func get_concatenated_names() -> StringName:
	pass;

#desc Returns all subnames concatenated with a colon character ([code]:[/code]) as separator, i.e. the right side of the first colon in a node path.
#desc [codeblocks]
#desc [gdscript]
#desc var nodepath = NodePath("Path2D/PathFollow2D/Sprite2D:texture:load_path")
#desc print(nodepath.get_concatenated_subnames()) # texture:load_path
#desc [/gdscript]
#desc [csharp]
#desc var nodepath = new NodePath("Path2D/PathFollow2D/Sprite2D:texture:load_path");
#desc GD.Print(nodepath.GetConcatenatedSubnames()); // texture:load_path
#desc [/csharp]
#desc [/codeblocks]
func get_concatenated_subnames() -> StringName:
	pass;

#desc Gets the node name indicated by [param idx] (0 to [method get_name_count] - 1).
#desc [codeblocks]
#desc [gdscript]
#desc var node_path = NodePath("Path2D/PathFollow2D/Sprite2D")
#desc print(node_path.get_name(0)) # Path2D
#desc print(node_path.get_name(1)) # PathFollow2D
#desc print(node_path.get_name(2)) # Sprite
#desc [/gdscript]
#desc [csharp]
#desc var nodePath = new NodePath("Path2D/PathFollow2D/Sprite2D");
#desc GD.Print(nodePath.GetName(0)); // Path2D
#desc GD.Print(nodePath.GetName(1)); // PathFollow2D
#desc GD.Print(nodePath.GetName(2)); // Sprite
#desc [/csharp]
#desc [/codeblocks]
func get_name() -> StringName:
	pass;

#desc Gets the number of node names which make up the path. Subnames (see [method get_subname_count]) are not included.
#desc For example, [code]"Path2D/PathFollow2D/Sprite2D"[/code] has 3 names.
func get_name_count() -> int:
	pass;

#desc Gets the resource or property name indicated by [param idx] (0 to [method get_subname_count]).
#desc [codeblocks]
#desc [gdscript]
#desc var node_path = NodePath("Path2D/PathFollow2D/Sprite2D:texture:load_path")
#desc print(node_path.get_subname(0)) # texture
#desc print(node_path.get_subname(1)) # load_path
#desc [/gdscript]
#desc [csharp]
#desc var nodePath = new NodePath("Path2D/PathFollow2D/Sprite2D:texture:load_path");
#desc GD.Print(nodePath.GetSubname(0)); // texture
#desc GD.Print(nodePath.GetSubname(1)); // load_path
#desc [/csharp]
#desc [/codeblocks]
func get_subname() -> StringName:
	pass;

#desc Gets the number of resource or property names ("subnames") in the path. Each subname is listed after a colon character ([code]:[/code]) in the node path.
#desc For example, [code]"Path2D/PathFollow2D/Sprite2D:texture:load_path"[/code] has 2 subnames.
func get_subname_count() -> int:
	pass;

#desc Returns the 32-bit hash value representing the [NodePath]'s contents.
func hash() -> int:
	pass;

#desc Returns [code]true[/code] if the node path is absolute (as opposed to relative), which means that it starts with a slash character ([code]/[/code]). Absolute node paths can be used to access the root node ([code]"/root"[/code]) or autoloads (e.g. [code]"/global"[/code] if a "global" autoload was registered).
func is_absolute() -> bool:
	pass;

#desc Returns [code]true[/code] if the node path is empty.
func is_empty() -> bool:
	pass;


