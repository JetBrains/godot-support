#brief RemoteTransform3D pushes its own [Transform3D] to another [Node3D] derived Node in the scene.
#desc RemoteTransform3D pushes its own [Transform3D] to another [Node3D] derived Node (called the remote node) in the scene.
#desc It can be set to update another Node's position, rotation and/or scale. It can use either global or local coordinates.
class_name RemoteTransform3D


#desc The [NodePath] to the remote node, relative to the RemoteTransform3D's position in the scene.
var remote_path: NodePath;

#desc If [code]true[/code], the remote node's position is updated.
var update_position: bool;

#desc If [code]true[/code], the remote node's rotation is updated.
var update_rotation: bool;

#desc If [code]true[/code], the remote node's scale is updated.
var update_scale: bool;

#desc If [code]true[/code], global coordinates are used. If [code]false[/code], local coordinates are used.
var use_global_coordinates: bool;



#desc [RemoteTransform3D] caches the remote node. It may not notice if the remote node disappears; [method force_update_cache] forces it to update the cache again.
func force_update_cache() -> void:
	pass;


