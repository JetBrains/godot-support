class_name SuperBase


## Creates the base with a [param value].
func _init(value: int = 0) -> void:
	pass


## Greets [param who] the given number of [param times].
func greet(who: String, times: int = 1) -> String:
	return who.repeat(times)


## Describes the base.
func describe() -> String:
	return "base"
