var variable: set = set_var, get = get_var

static var static_variable: set = static_setter, get = static_getter

static var static_trying_non_static: set = set_var, get = get_var

var non_static_trying_static: set = static_setter, get = static_getter

func set_var(value):
	pass

func get_var():
	pass

static func static_setter(value):
	pass

static func static_getter():
	pass
