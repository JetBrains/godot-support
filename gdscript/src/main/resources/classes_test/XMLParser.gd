extends RefCounted
class_name XMLParser

const NODE_NONE = 0;
const NODE_ELEMENT = 1;
const NODE_ELEMENT_END = 2;
const NODE_TEXT = 3;
const NODE_COMMENT = 4;
const NODE_CDATA = 5;
const NODE_UNKNOWN = 6;


func get_attribute_count() -> int:
    pass;

func get_attribute_name(idx: int) -> String:
    pass;

func get_attribute_value(idx: int) -> String:
    pass;

func get_current_line() -> int:
    pass;

func get_named_attribute_value(name: String) -> String:
    pass;

func get_named_attribute_value_safe(name: String) -> String:
    pass;

func get_node_data() -> String:
    pass;

func get_node_name() -> String:
    pass;

func get_node_offset() -> int:
    pass;

func get_node_type() -> int:
    pass;

func has_attribute(name: String) -> bool:
    pass;

func is_empty() -> bool:
    pass;

func open(file: String) -> int:
    pass;

func open_buffer(buffer: PackedByteArray) -> int:
    pass;

func read() -> int:
    pass;

func seek(position: int) -> int:
    pass;

func skip_section() -> void:
    pass;

