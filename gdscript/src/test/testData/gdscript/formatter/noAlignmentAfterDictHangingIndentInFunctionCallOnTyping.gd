class Mock:
	static func app(_x, _y = false): return null


var UserHandler = null
var handler = Mock.app([
	["/", UserHandler],<caret>
], true)
