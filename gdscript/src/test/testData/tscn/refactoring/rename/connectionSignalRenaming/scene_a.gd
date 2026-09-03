extends Panel

func _on_player_died() -> void:
	get_tree().change_scene_to_file("res://scene_b.tscn")
