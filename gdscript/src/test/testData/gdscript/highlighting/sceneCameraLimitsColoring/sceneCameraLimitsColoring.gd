extends <info descr="null"><symbolName descr="GD_ENGINE_TYPE">Node2D</symbolName></info>

const <symbolName descr="GD_MEMBER">LIMIT_LEFT</symbolName> = -315
const <symbolName descr="GD_MEMBER">LIMIT_TOP</symbolName> = -250
const <symbolName descr="GD_MEMBER">LIMIT_RIGHT</symbolName> = 955
const <symbolName descr="GD_MEMBER">LIMIT_BOTTOM</symbolName> = 690


func <info descr="null"><symbolName descr="GD_METHOD_CALL">_ready</symbolName></info>():
	for <symbolName descr="GD_MEMBER">child</symbolName> in <symbolName descr="GD_METHOD_CALL">get_children</symbolName>():
		if <symbolName descr="GD_MEMBER">child</symbolName> is <info descr="null"><symbolName descr="GD_CLASS_TYPE">Player</symbolName></info>:
			var <symbolName descr="GD_MEMBER">camera</symbolName>: <info descr="null"><symbolName descr="GD_ENGINE_TYPE">Camera2D</symbolName></info> = <symbolName descr="GD_MEMBER">child</symbolName>.<symbolName descr="GD_METHOD_CALL">get_node</symbolName>(^"Camera")
			<symbolName descr="GD_MEMBER">camera</symbolName>.<symbolName descr="GD_MEMBER">limit_left</symbolName> = <symbolName descr="GD_MEMBER">LIMIT_LEFT</symbolName>
			<symbolName descr="GD_MEMBER">camera</symbolName>.<symbolName descr="GD_MEMBER">limit_top</symbolName> = <symbolName descr="GD_MEMBER">LIMIT_TOP</symbolName>
			<symbolName descr="GD_MEMBER">camera</symbolName>.<symbolName descr="GD_MEMBER">limit_right</symbolName> = <symbolName descr="GD_MEMBER">LIMIT_RIGHT</symbolName>
			<symbolName descr="GD_MEMBER">camera</symbolName>.<symbolName descr="GD_MEMBER">limit_bottom</symbolName> = <symbolName descr="GD_MEMBER">LIMIT_BOTTOM</symbolName>
