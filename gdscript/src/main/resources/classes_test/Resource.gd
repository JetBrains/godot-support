#brief Base class for all resources.
#desc Resource is the base class for all Godot-specific resource types, serving primarily as data containers. Since they inherit from [RefCounted], resources are reference-counted and freed when no longer in use. They are also cached once loaded from disk, so that any further attempts to load a resource from a given path will return the same reference (all this in contrast to a [Node], which is not reference-counted and can be instantiated from disk as many times as desired). Resources can be saved externally on disk or bundled into another object, such as a [Node] or another resource.
#desc [b]Note:[/b] In C#, resources will not be freed instantly after they are no longer in use. Instead, garbage collection will run periodically and will free resources that are no longer in use. This means that unused resources will linger on for a while before being removed.
class_name Resource


#desc If [code]true[/code], the resource will be made unique in each instance of its local scene. It can thus be modified in a scene instance without impacting other instances of that same scene.
var resource_local_to_scene: bool;

#desc The name of the resource. This is an optional identifier. If [member resource_name] is not empty, its value will be displayed to represent the current resource in the editor inspector. For built-in scripts, the [member resource_name] will be displayed as the tab name in the script editor.
var resource_name: String;

#desc The path to the resource. In case it has its own file, it will return its filepath. If it's tied to the scene, it will return the scene's path, followed by the resource's index.
var resource_path: String;



virtual func _get_rid() -> RID:
	pass;

#desc Duplicates the resource, returning a new resource with the exported members copied. [b]Note:[/b] To duplicate the resource the constructor is called without arguments. This method will error when the constructor doesn't have default values.
#desc By default, sub-resources are shared between resource copies for efficiency. This can be changed by passing [code]true[/code] to the [param subresources] argument which will copy the subresources.
#desc [b]Note:[/b] If [param subresources] is [code]true[/code], this method will only perform a shallow copy. Nested resources within subresources will not be duplicated and will still be shared.
#desc [b]Note:[/b] When duplicating a resource, only [code]export[/code]ed properties are copied. Other properties will be set to their default value in the new resource.
func duplicate() -> Resource:
	pass;

#desc Emits the [signal changed] signal.
#desc If external objects which depend on this resource should be updated, this method must be called manually whenever the state of this resource has changed (such as modification of properties).
#desc The method is equivalent to:
#desc [codeblock]
#desc emit_signal("changed")
#desc [/codeblock]
#desc [b]Note:[/b] This method is called automatically for built-in resources.
func emit_changed() -> void:
	pass;

#desc If [member resource_local_to_scene] is enabled and the resource was loaded from a [PackedScene] instantiation, returns the local scene where this resource's unique copy is in use. Otherwise, returns [code]null[/code].
func get_local_scene() -> Node:
	pass;

#desc Returns the RID of the resource (or an empty RID). Many resources (such as [Texture2D], [Mesh], etc) are high-level abstractions of resources stored in a server, so this function will return the original RID.
func get_rid() -> RID:
	pass;

#desc This method is called when a resource with [member resource_local_to_scene] enabled is loaded from a [PackedScene] instantiation. Its behavior can be customized by connecting [signal setup_local_to_scene_requested] from script.
#desc For most resources, this method performs no base logic. [ViewportTexture] performs custom logic to properly set the proxy texture and flags in the local viewport.
func setup_local_to_scene() -> void:
	pass;

#desc Sets the path of the resource, potentially overriding an existing cache entry for this path. This differs from setting [member resource_path], as the latter would error out if another resource was already cached for the given path.
func take_over_path() -> void:
	pass;


