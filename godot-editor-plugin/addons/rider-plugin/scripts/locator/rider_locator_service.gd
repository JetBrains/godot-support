@tool
class_name RiderLocatorService

func get_installations() -> Array:
	var result: Array = RiderLocator.new().get_installations()
	return result
