@rpc("authority", "call_remote", "reliable")
func single() -> void:
	pass

@rpc("authority", "call_remote", "reliable")
@rpc("authority", "call_remote", "reliable")
func multiple() -> void:
	pass


@export
@onready
var variable