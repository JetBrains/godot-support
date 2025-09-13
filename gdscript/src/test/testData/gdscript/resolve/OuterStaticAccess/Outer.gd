class_name Outer

static var STATIC_VAL := 1
var instance_val := 2
var names: Array[String] = []

static func s() -> void:
	pass

func i() -> void:
	pass

class Sub:
    static func s_Sub(): pass
    var inst_Sub := 42