class_name <info descr="null"><symbolName descr="GD_CLASS_TYPE">HighlightTarget</symbolName></info>

static var <info descr="null"><symbolName descr="GD_MEMBER">static_prop</symbolName></info> := 1
var <info descr="null"><symbolName descr="GD_MEMBER">instance_prop</symbolName></info> := 2

static func <info descr="null"><symbolName descr="GD_STATIC_METHOD_CALL">static_method</symbolName></info>():
	pass

func <info descr="null"><symbolName descr="GD_METHOD_CALL">instance_method</symbolName></info>():
	pass

class <info descr="null"><symbolName descr="GD_CLASS_TYPE">Inner</symbolName></info>:
	pass

func <info descr="null"><symbolName descr="GD_METHOD_CALL">use</symbolName></info>():
	<symbolName descr="GD_STATIC_METHOD_CALL">static_method</symbolName>()
	<symbolName descr="GD_METHOD_CALL">instance_method</symbolName>()
	<symbolName descr="GD_MEMBER">static_prop</symbolName>
	<symbolName descr="GD_MEMBER">instance_prop</symbolName>
	<symbolName descr="GD_CLASS_TYPE">Inner</symbolName>
