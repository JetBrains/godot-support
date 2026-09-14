class_name YSort25D
extends Node

func sort() -> void:
	var nodes: Array = []
	nodes.sort_custom(Callable(Node25D, &"y_sort_slight_xz"))
