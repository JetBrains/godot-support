func foo(a, b, c):
	return null


var xs = [  # foo
	1,
	[  # bar
		"a",
		"b",
		foo(true,  # baz
				false,
				null)
	],
	2
]
