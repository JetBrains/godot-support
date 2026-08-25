class_name TopLevel
extends Node

class Inner:
	static func factory_method() -> Inner:
		Inner.factory_method()
		TopLevel.factory_method
		return null
