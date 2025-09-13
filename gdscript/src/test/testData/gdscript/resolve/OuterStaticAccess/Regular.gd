func _ready():
	Outer.s()                  # static OK
	Outer.STATIC_VAL           # static OK
	Outer.i()                  # non-static should be filtered out by static context
	Outer.instance_val         # non-static should be filtered out

	# Qualifier is `Outer` (for `Sub`) or `Outer.Sub` (for members)
	# When completing after `Outer.`, selecting `Sub` and then `Sub.` will set a class context.
	Outer.Sub.s_Sub()          # static OK
	Outer.Sub.inst_Sub         # non-static should be filtered out in static context
