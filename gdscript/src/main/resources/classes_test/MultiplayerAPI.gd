#brief High-level multiplayer API interface.
#desc Base class for high-level multiplayer API implementations. See also [MultiplayerPeer].
#desc By default, [SceneTree] has a reference to an implementation of this class and uses it to provide multiplayer capabilities (i.e. RPCs) across the whole scene.
#desc It is possible to override the MultiplayerAPI instance used by specific tree branches by calling the [method SceneTree.set_multiplayer] method, effectively allowing to run both client and server in the same scene.
#desc It is also possible to extend or replace the default implementation via scripting or native extensions. See [MultiplayerAPIExtension] for details about extensions, [SceneMultiplayer] for the details about the default implementation.
class_name MultiplayerAPI

#desc Used with [method Node.rpc_config] to disable a method or property for all RPC calls, making it unavailable. Default for all methods.
const RPC_MODE_DISABLED = 0;

#desc Used with [method Node.rpc_config] to set a method to be callable remotely by any peer. Analogous to the [code]@rpc(any)[/code] annotation. Calls are accepted from all remote peers, no matter if they are node's authority or not.
const RPC_MODE_ANY_PEER = 1;

#desc Used with [method Node.rpc_config] to set a method to be callable remotely only by the current multiplayer authority (which is the server by default). Analogous to the [code]@rpc(authority)[/code] annotation. See [method Node.set_multiplayer_authority].
const RPC_MODE_AUTHORITY = 2;


#desc The peer object to handle the RPC system (effectively enabling networking when set). Depending on the peer itself, the MultiplayerAPI will become a network server (check with [method is_server]) and will set root node's network mode to authority, or it will become a regular client peer. All child nodes are set to inherit the network mode by default. Handling of networking-related events (connection, disconnection, new clients) is done by connecting to MultiplayerAPI's signals.
var multiplayer_peer: MultiplayerPeer;



#desc Returns a new instance of the default MultiplayerAPI.
static func create_default_interface() -> MultiplayerAPI:
	pass;

#desc Returns the default MultiplayerAPI implementation class name. This is usually [code]"SceneMultiplayer"[/code] when [SceneMultiplayer] is available. See [method set_default_interface].
static func get_default_interface() -> StringName:
	pass;

#desc Returns the peer IDs of all connected peers of this MultiplayerAPI's [member multiplayer_peer].
func get_peers() -> PackedInt32Array:
	pass;

#desc Returns the sender's peer ID for the RPC currently being executed.
#desc [b]Note:[/b] If not inside an RPC this method will return 0.
func get_remote_sender_id() -> int:
	pass;

#desc Returns the unique peer ID of this MultiplayerAPI's [member multiplayer_peer].
func get_unique_id() -> int:
	pass;

#desc Returns [code]true[/code] if there is a [member multiplayer_peer] set.
func has_multiplayer_peer() -> bool:
	pass;

#desc Returns [code]true[/code] if this MultiplayerAPI's [member multiplayer_peer] is valid and in server mode (listening for connections).
func is_server() -> bool:
	pass;

#desc Notifies the MultiplayerAPI of a new [param configuration] for the given [param object]. This method is used internally by [SceneTree] to configure the root path for this MultiplayerAPI (passing [code]null[/code] and a valid [NodePath] as [param configuration]). This method can be further used by MultiplayerAPI implementations to provide additional features, refer to specific implementation (e.g. [SceneMultiplayer]) for details on how they use it.
#desc [b]Note:[/b] This method is mostly relevant when extending or overriding the MultiplayerAPI behavior via [MultiplayerAPIExtension].
func object_configuration_add(object: Object, configuration: Variant) -> int:
	pass;

#desc Notifies the MultiplayerAPI to remove a [param configuration] for the given [param object]. This method is used internally by [SceneTree] to configure the root path for this MultiplayerAPI (passing [code]null[/code] and an empty [NodePath] as [param configuration]). This method can be further used by MultiplayerAPI implementations to provide additional features, refer to specific implementation (e.g. [SceneMultiplayer]) for details on how they use it.
#desc [b]Note:[/b] This method is mostly relevant when extending or overriding the MultiplayerAPI behavior via [MultiplayerAPIExtension].
func object_configuration_remove(object: Object, configuration: Variant) -> int:
	pass;

#desc Method used for polling the MultiplayerAPI. You only need to worry about this if you set [member SceneTree.multiplayer_poll] to [code]false[/code]. By default, [SceneTree] will poll its MultiplayerAPI(s) for you.
#desc [b]Note:[/b] This method results in RPCs being called, so they will be executed in the same context of this function (e.g. [code]_process[/code], [code]physics[/code], [Thread]).
func poll() -> int:
	pass;

#desc Sends an RPC to the target [param peer]. The given [param method] will be called on the remote [param object] with the provided [param arguments]. The RPC may also be called locally depending on the implementation and RPC configuration. See [method Node.rpc] and [method Node.rpc_config].
#desc [b]Note:[/b] Prefer using [method Node.rpc], [method Node.rpc_id], or [code]my_method.rpc(peer, arg1, arg2, ...)[/code] (in GDScript), since they are faster. This method is mostly useful in conjunction with [MultiplayerAPIExtension] when augmenting or replacing the multiplayer capabilities.
func rpc(peer: int, object: Object, method: StringName, arguments: Array) -> int:
	pass;

#desc Sets the default MultiplayerAPI implementation class. This method can be used by modules and extensions to configure which implementation will be used by [SceneTree] when the engine starts.
static func set_default_interface() -> void:
	pass;


