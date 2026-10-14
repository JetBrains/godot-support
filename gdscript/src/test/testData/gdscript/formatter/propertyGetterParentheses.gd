var multiline:
	get():
		return 1
var single_line:
	get(): return 2
var spaced:
	get ():
		return 3
var spaced_inside:
	get( ):
		return 4
var setter_spaced:
	set (value):
		pass
var setter_spaced_inside:
	set( value ):
		pass
var setter_typed:
	set( value: int ):
		pass
static var x: Signal:
	get():
		return Signal()
