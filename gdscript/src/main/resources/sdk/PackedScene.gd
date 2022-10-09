extends Resource
#brief An abstraction of a serialized scene.
#desc A simplified interface to a scene file. Provides access to operations and checks that can be performed on the scene resource itself.
#desc Can be used to save a node to a file. When saving, the node as well as all the nodes it owns get saved (see [member Node.owner] property).
#desc [b]Note:[/b] The node doesn't need to own itself.
#desc [b]Example of loading a saved scene:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc # Use load() instead of preload() if the path isn't known at compile-time.
#desc var scene = preload("res://scene.tscn").instantiate()
#desc # Add the node as a child of the node the script is attached to.
#desc add_child(scene)
#desc [/gdscript]
#desc [csharp]
#desc // C# has no preload, so you have to always use ResourceLoader.Load<PackedScene>().
#desc var scene = ResourceLoader.Load<PackedScene>("res://scene.tscn").Instantiate();
#desc // Add the node as a child of the node the script is attached to.
#desc AddChild(scene);
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Example of saving a node with different owners:[/b] The following example creates 3 objects: [Node2D] ([code]node[/code]), [RigidBody2D] ([code]body[/code]) and [CollisionObject2D] ([code]collision[/code]). [code]collision[/code] is a child of [code]body[/code] which is a child of [code]node[/code]. Only [code]body[/code] is owned by [code]node[/code] and [code]pack[/code] will therefore only save those two nodes, but not [code]collision[/code].
#desc [codeblocks]
#desc [gdscript]
#desc # Create the objects.
#desc var node = Node2D.new()
#desc var body = RigidBody2D.new()
#desc var collision = CollisionShape2D.new()
#desc 
#desc # Create the object hierarchy.
#desc body.add_child(collision)
#desc node.add_child(body)
#desc 
#desc # Change owner of `body`, but not of `collision`.
#desc body.owner = node
#desc var scene = PackedScene.new()
#desc 
#desc # Only `node` and `body` are now packed.
#desc var result = scene.pack(node)
#desc if result == OK:
#desc var error = ResourceSaver.save(scene, "res://path/name.tscn")  # Or "user://..."
#desc if error != OK:
#desc push_error("An error occurred while saving the scene to disk.")
#desc [/gdscript]
#desc [csharp]
#desc // Create the objects.
#desc var node = new Node2D();
#desc var body = new RigidBody2D();
#desc var collision = new CollisionShape2D();
#desc 
#desc // Create the object hierarchy.
#desc body.AddChild(collision);
#desc node.AddChild(body);
#desc 
#desc // Change owner of `body`, but not of `collision`.
#desc body.Owner = node;
#desc var scene = new PackedScene();
#desc 
#desc // Only `node` and `body` are now packed.
#desc Error result = scene.Pack(node);
#desc if (result == Error.Ok)
#desc {
#desc Error error = ResourceSaver.Save(scene, "res://path/name.tscn"); // Or "user://..."
#desc if (error != Error.Ok)
#desc {
#desc GD.PushError("An error occurred while saving the scene to disk.");
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name PackedScene

#desc If passed to [method instantiate], blocks edits to the scene state.
const GEN_EDIT_STATE_DISABLED = 0;

#desc If passed to [method instantiate], provides local scene resources to the local scene.
#desc [b]Note:[/b] Only available in editor builds.
const GEN_EDIT_STATE_INSTANCE = 1;

#desc If passed to [method instantiate], provides local scene resources to the local scene. Only the main scene should receive the main edit state.
#desc [b]Note:[/b] Only available in editor builds.
const GEN_EDIT_STATE_MAIN = 2;

#desc It's similar to [constant GEN_EDIT_STATE_MAIN], but for the case where the scene is being instantiated to be the base of another one.
#desc [b]Note:[/b] Only available in editor builds.
const GEN_EDIT_STATE_MAIN_INHERITED = 3;


#desc A dictionary representation of the scene contents.
#desc Available keys include "rnames" and "variants" for resources, "node_count", "nodes", "node_paths" for nodes, "editable_instances" for base scene children overrides, "conn_count" and "conns" for signal connections, and "version" for the format style of the PackedScene.
var _bundled: Dictionary;



#desc Returns [code]true[/code] if the scene file has nodes.
func can_instantiate() -> bool:
	pass;

#desc Returns the [code]SceneState[/code] representing the scene file contents.
func get_state() -> SceneState:
	pass;

#desc Instantiates the scene's node hierarchy. Triggers child scene instantiation(s). Triggers a [constant Node.NOTIFICATION_SCENE_INSTANTIATED] notification on the root node.
func instantiate(edit_state: int) -> Node:
	pass;

#desc Pack will ignore any sub-nodes not owned by given node. See [member Node.owner].
func pack(path: Node) -> int:
	pass;


