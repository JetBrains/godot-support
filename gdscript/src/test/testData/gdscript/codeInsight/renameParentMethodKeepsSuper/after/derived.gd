class_name SuperDerived
extends SuperBase


func _init(value: int = 0) -> void:
	super(value)


func greet(who: String, times: int = 1) -> String:
	return super(who, times)


func describe() -> String:
	var own := super()
	return own + super.describe_base()
