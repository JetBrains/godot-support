## Class docstring.
## Second line of class docstring.
class_name CTestClass

## This is a brief for var x.
var x: int

# This is a comment that shouldn't contribute to documentation. '''
##
##	This is a brief.
##
##	This is a description.
##	It has multiple lines.
##
##	@tutorial: Tutorial URL
##	@tutorial(Tutorial Name 2): Tutorial URL 2
##	@deprecated: Use something else.
##	@experimental: This is experimental.
func documented_func():
	## This is a comment, not documentation for y.

	var y := 1

	##This is a brief for var z.
	##
	## This is a description for var z.
	var z := 1

	## Another comment
	y += z

	return y

## This is a comment.

## @brief: This is a static function.
## @experimental
static func static_func():
	pass
