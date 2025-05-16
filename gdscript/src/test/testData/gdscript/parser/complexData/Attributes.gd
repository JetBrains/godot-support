func has_value():
	return _data.has and _data[blackboard_name].has(key) and _data[blackboard_name][key] != null
