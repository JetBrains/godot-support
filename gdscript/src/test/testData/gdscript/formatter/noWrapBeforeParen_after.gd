var record = ""
var comments = []
var some_really_long_array_variable_name = [""]


func foo():
	if true:
		if true:
			if comments:
				for comment in comments:
					record += "    <comment attribute_name=\"attribute_value\" " + " ".join(
							some_really_long_array_variable_name) + "/>\n"
