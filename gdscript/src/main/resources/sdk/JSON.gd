extends RefCounted
#brief Helper class for creating and parsing JSON data.
#desc The [JSON] enables all data types to be converted to and from a JSON string. This useful for serializing data to save to a file or send over the network.
#desc [method stringify] is used to convert any data type into a JSON string.
#desc [method parse] is used to convert any existing JSON data into a [Variant] that can be used within Godot. If successfully parsed, use [member data] to retrieve the [Variant], and use [code]typeof[/code] to check if the Variant's type is what you expect. JSON Objects are converted into a [Dictionary], but JSON data can be used to store [Array]s, numbers, [String]s and even just a boolean.
#desc [b]Example[/b]
#desc [codeblock]
#desc var data_to_send = ["a", "b", "c"]
#desc var json_string = JSON.stringify(data_to_send)
#desc # Save data
#desc # ...
#desc # Retrieve data
#desc var error = json.parse(json_string)
#desc if error == OK:
#desc var data_received = json.data
#desc if typeof(data_received) == TYPE_ARRAY:
#desc print(data_received) # Prints array
#desc else:
#desc print("Unexpected data")
#desc else:
#desc print("JSON Parse Error: ", json.get_error_message(), " in ", json_string, " at line ", json.get_error_line())
#desc [/codeblock]
#desc Alternatively, you can parse string using the static [method parse_string] method, but it doesn't allow to handle errors.
#desc [codeblock]
#desc var data = JSON.parse_string(json_string) # Returns null if parsing failed.
#desc [/codeblock]
#desc [b]Note:[/b] Both parse methods do not fully comply with the JSON specification:
#desc - Trailing commas in arrays or objects are ignored, instead of causing a parser error.
#desc - New line and tab characters are accepted in string literals, and are treated like their corresponding escape sequences [code]\n[/code] and [code]\t[/code].
#desc - Numbers are parsed using [method String.to_float] which is generally more lax than the JSON specification.
#desc - Certain errors, such as invalid Unicode sequences, do not cause a parser error. Instead, the string is cleansed and an error is logged to the console.
class_name JSON


#desc Contains the parsed JSON data in [Variant] form.
var data: Variant;



#desc Returns [code]0[/code] if the last call to [method parse] was successful, or the line number where the parse failed.
func get_error_line() -> int:
	pass;

#desc Returns an empty string if the last call to [method parse] was successful, or the error message if it failed.
func get_error_message() -> String:
	pass;

#desc Attempts to parse the [param json_string] provided.
#desc Returns an [enum Error]. If the parse was successful, it returns [code]OK[/code] and the result can be retrieved using [member data]. If unsuccessful, use [method get_error_line] and [method get_error_message] for identifying the source of the failure.
#desc Non-static variant of [method parse_string], if you want custom error handling.
func parse(json_string: String) -> int:
	pass;

#desc Attempts to parse the [param json_string] provided and returns the parsed data. Returns [code]null[/code] if parse failed.
static func parse_string(json_string: String) -> Variant:
	pass;

#desc Converts a [Variant] var to JSON text and returns the result. Useful for serializing data to store or send over the network.
#desc [b]Note:[/b] The JSON specification does not define integer or float types, but only a [i]number[/i] type. Therefore, converting a Variant to JSON text will convert all numerical values to [float] types.
#desc [b]Note:[/b] If [param full_precision] is [code]true[/code], when stringifying floats, the unreliable digits are stringified in addition to the reliable digits to guarantee exact decoding.
#desc The [param indent] parameter controls if and how something is indented, the string used for this parameter will be used where there should be an indent in the output, even spaces like [code]"   "[/code] will work. [code]\t[/code] and [code]\n[/code] can also be used for a tab indent, or to make a newline for each indent respectively.
#desc [b]Example output:[/b]
#desc [codeblock]
#desc ## JSON.stringify(my_dictionary)
#desc {"name":"my_dictionary","version":"1.0.0","entities":[{"name":"entity_0","value":"value_0"},{"name":"entity_1","value":"value_1"}]}
#desc 
#desc ## JSON.stringify(my_dictionary, "\t")
#desc {
#desc "name": "my_dictionary",
#desc "version": "1.0.0",
#desc "entities": [
#desc {
#desc "name": "entity_0",
#desc "value": "value_0"
#desc },
#desc {
#desc "name": "entity_1",
#desc "value": "value_1"
#desc }
#desc ]
#desc }
#desc 
#desc ## JSON.stringify(my_dictionary, "...")
#desc {
#desc ..."name": "my_dictionary",
#desc ..."version": "1.0.0",
#desc ..."entities": [
#desc ......{
#desc ........."name": "entity_0",
#desc ........."value": "value_0"
#desc ......},
#desc ......{
#desc ........."name": "entity_1",
#desc ........."value": "value_1"
#desc ......}
#desc ...]
#desc }
#desc [/codeblock]
static func stringify(data: Variant, indent: String, sort_keys: bool, full_precision: bool) -> String:
	pass;


