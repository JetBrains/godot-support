extends Node

func _ready() -> void:
	EventBus.connect("battle_state_changed", Callable(self, "_on_battle_state_changed"))

func _on_battle_state_changed() -> void:
	pass
