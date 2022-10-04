#brief Base class used for extending the [MultiplayerAPI].
#desc This class can be used to augment or replace the default [MultiplayerAPI] implementation via script or extensions.
#desc The following example augment the default implementation ([SceneMultiplayer]) by logging every RPC being made, and every object being configured for replication.
#desc [codeblocks]
#desc [gdscript]
#desc extends MultiplayerAPIExtension
#desc class_name LogMultiplayer
#desc 
#desc # We want to augment the default SceneMultiplayer.
#desc var base_multiplayer = SceneMultiplayer.new()
#desc 
#desc func _init():
#desc # Just passthourgh base signals (copied to var to avoid cyclic reference)
#desc var cts = connected_to_server
#desc var cf = connection_failed
#desc var pc = peer_connected
#desc var pd = peer_disconnected
#desc base_multiplayer.connected_to_server.connect(func(): cts.emit())
#desc base_multiplayer.connection_failed.connect(func(): cf.emit())
#desc base_multiplayer.peer_connected.connect(func(id): pc.emit(id))
#desc base_multiplayer.peer_disconnected.connect(func(id): pd.emit(id))
#desc 
#desc # Log RPC being made and forward it to the default multiplayer.
#desc func _rpc(peer: int, object: Object, method: StringName, args: Array) -> int: # Error
#desc print("Got RPC for %d: %s::%s(%s)" % [peer, object, method, args])
#desc return base_multiplayer.rpc(peer, object, method, args)
#desc 
#desc # Log configuration add. E.g. root path (nullptr, NodePath), replication (Node, Spawner|Synchronizer), custom.
#desc func _object_configuration_add(object, config: Variant) -> int: # Error
#desc if config is MultiplayerSynchronizer:
#desc print("Adding synchronization configuration for %s. Synchronizer: %s" % [object, config])
#desc elif config is MultiplayerSpawner:
#desc print("Adding node %s to the spawn list. Spawner: %s" % [object, config])
#desc return base_multiplayer.object_configuration_add(object, config)
#desc 
#desc # Log configuration remove. E.g. root path (nullptr, NodePath), replication (Node, Spawner|Synchronizer), custom.
#desc func _object_configuration_remove(object, config: Variant) -> int: # Error
#desc if config is MultiplayerSynchronizer:
#desc print("Removing synchronization configuration for %s. Synchronizer: %s" % [object, config])
#desc elif config is MultiplayerSpawner:
#desc print("Removing node %s from the spawn list. Spawner: %s" % [object, config])
#desc return base_multiplayer.object_configuration_remove(object, config)
#desc 
#desc # These can be optional, but in our case we want to augment SceneMultiplayer, so forward everything.
#desc func _set_multiplayer_peer(p_peer: MultiplayerPeer):
#desc base_multiplayer.multiplayer_peer = p_peer
#desc 
#desc func _get_multiplayer_peer() -> MultiplayerPeer:
#desc return base_multiplayer.multiplayer_peer
#desc 
#desc func _get_unique_id() -> int:
#desc return base_multiplayer.get_unique_id()
#desc 
#desc func _get_peer_ids() -> PackedInt32Array:
#desc return base_multiplayer.get_peers()
#desc [/gdscript]
#desc [/codeblocks]
#desc Then in your main scene or in an autoload call [method SceneTree.set_multiplayer] to start using your custom [MultiplayerAPI]:
#desc [codeblocks]
#desc [gdscript]
#desc # autoload.gd
#desc func _enter_tree():
#desc # Sets our custom multiplayer as the main one in SceneTree.
#desc get_tree().set_multiplayer(LogMultiplayer.new())
#desc [/gdscript]
#desc [/codeblocks]
#desc Native extensions can alternatively use the [method MultiplayerAPI.set_default_interface] method during initialization to configure themselves as the default implementation.
class_name MultiplayerAPIExtension




#desc Called when the [member MultiplayerAPI.multiplayer_peer] is retrieved.
virtual func _get_multiplayer_peer() -> MultiplayerPeer:
	pass;

#desc Callback for [method MultiplayerAPI.get_peers].
virtual const func _get_peer_ids() -> PackedInt32Array:
	pass;

#desc Callback for [method MultiplayerAPI.get_remote_sender_id].
virtual const func _get_remote_sender_id() -> int:
	pass;

#desc Callback for [method MultiplayerAPI.get_unique_id].
virtual const func _get_unique_id() -> int:
	pass;

#desc Callback for [method MultiplayerAPI.object_configuration_add].
virtual func _object_configuration_add(object: Object, configuration: Variant) -> int:
	pass;

#desc Callback for [method MultiplayerAPI.object_configuration_remove].
virtual func _object_configuration_remove(object: Object, configuration: Variant) -> int:
	pass;

#desc Callback for [method MultiplayerAPI.poll].
virtual func _poll() -> int:
	pass;

#desc Callback for [method MultiplayerAPI.rpc].
virtual func _rpc(peer: int, object: Object, method: StringName, args: Array) -> int:
	pass;

#desc Called when the [member MultiplayerAPI.multiplayer_peer] is set.
virtual func _set_multiplayer_peer(multiplayer_peer: MultiplayerPeer) -> void:
	pass;


