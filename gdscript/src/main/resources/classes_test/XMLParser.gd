extends RefCounted
#brief Low-level class for creating parsers for [url=https://en.wikipedia.org/wiki/XML]XML[/url] files.
#desc This class can serve as base to make custom XML parsers. Since XML is a very flexible standard, this interface is low-level so it can be applied to any possible schema.
class_name XMLParser

#desc There's no node (no file or buffer opened).
const NODE_NONE = 0;

#desc Element (tag).
const NODE_ELEMENT = 1;

#desc End of element.
const NODE_ELEMENT_END = 2;

#desc Text node.
const NODE_TEXT = 3;

#desc Comment node.
const NODE_COMMENT = 4;

#desc CDATA content.
const NODE_CDATA = 5;

#desc Unknown node.
const NODE_UNKNOWN = 6;




#desc Gets the number of attributes in the current element.
func get_attribute_count() -> int:
	pass;

#desc Gets the name of the attribute specified by the [param idx] index.
func get_attribute_name(idx: int) -> String:
	pass;

#desc Gets the value of the attribute specified by the [param idx] index.
func get_attribute_value(idx: int) -> String:
	pass;

#desc Gets the current line in the parsed file, counting from 0.
func get_current_line() -> int:
	pass;

#desc Gets the value of a certain attribute of the current element by [param name]. This will raise an error if the element has no such attribute.
func get_named_attribute_value(name: String) -> String:
	pass;

#desc Gets the value of a certain attribute of the current element by [param name]. This will return an empty [String] if the attribute is not found.
func get_named_attribute_value_safe(name: String) -> String:
	pass;

#desc Gets the contents of a text node. This will raise an error in any other type of node.
func get_node_data() -> String:
	pass;

#desc Gets the name of the current element node. This will raise an error if the current node type is neither [constant NODE_ELEMENT] nor [constant NODE_ELEMENT_END].
func get_node_name() -> String:
	pass;

#desc Gets the byte offset of the current node since the beginning of the file or buffer.
func get_node_offset() -> int:
	pass;

#desc Gets the type of the current node. Compare with [enum NodeType] constants.
func get_node_type() -> int:
	pass;

#desc Check whether the current element has a certain attribute.
func has_attribute(name: String) -> bool:
	pass;

#desc Check whether the current element is empty (this only works for completely empty tags, e.g. [code]<element \>[/code]).
func is_empty() -> bool:
	pass;

#desc Opens an XML [param file] for parsing. This returns an error code.
func open(file: String) -> int:
	pass;

#desc Opens an XML raw [param buffer] for parsing. This returns an error code.
func open_buffer(buffer: PackedByteArray) -> int:
	pass;

#desc Reads the next node of the file. This returns an error code.
func read() -> int:
	pass;

#desc Moves the buffer cursor to a certain offset (since the beginning) and read the next node there. This returns an error code.
func seek(position: int) -> int:
	pass;

#desc Skips the current section. If the node contains other elements, they will be ignored and the cursor will go to the closing of the current element.
func skip_section() -> void:
	pass;


