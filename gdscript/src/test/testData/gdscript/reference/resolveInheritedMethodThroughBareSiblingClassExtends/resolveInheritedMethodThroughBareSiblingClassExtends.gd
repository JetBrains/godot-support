class_name ScatchMe
extends Node

class SuperInner:
	func foo() -> void:
		print_debug("Hey")

class Inner extends SuperInner:
	func bar():
		print_debug("hi")

func use_it():
	Inner.new().foo()
