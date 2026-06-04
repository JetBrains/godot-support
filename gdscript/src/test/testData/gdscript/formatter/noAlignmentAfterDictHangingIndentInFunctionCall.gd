func test_function(a, b = null, c = null):
	pass

func main():
	test_function({
	"a": "b",
	}, 5)

	test_function(1,
	              2,
	              3)
