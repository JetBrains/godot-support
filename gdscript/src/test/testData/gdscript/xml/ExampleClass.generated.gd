extends RefCounted
class_name ExampleClass

# Signals

signal counter_changed(new_value: int)

signal named_event(name: String, amount: float)

signal ready()


# Constants

const STATUS_IDLE = 0

const STATUS_RUNNING = 1

const STATUS_DONE = 42

const FLAG_NONE = 0

const FLAG_ALPHA = 1

const FLAG_BETA = 2

const FLAG_GAMMA = 4

const UNGROUPED_CONSTANT = 314


# Enums

enum Status {
	STATUS_IDLE = 0,
	STATUS_RUNNING = 1,
	STATUS_DONE = 42,
}

enum Flags {
	FLAG_NONE = 0,
	FLAG_ALPHA = 1,
	FLAG_BETA = 2,
	FLAG_GAMMA = 4,
}


# Properties

var counter: int: get = get_counter, set = set_counter

var label: String: get = get_label, set = set_label

var origin: Vector2: get = get_origin, set = set_origin

var status: int: get = get_status, set = set_status


# Methods

func add(a: int, b: int) -> int:
	pass

func apply_flags(flags: int) -> void:
	pass

func compute_with_defaults(a: int, b: int) -> int:
	pass

func get_hello() -> String:
	pass

static func get_version() -> String:
	pass

func log_varargs(...args: Array) -> void:
	pass

static func multiply(a: int, b: int) -> int:
	pass

func sum_varargs(first: float, ...args: Array) -> Variant:
	pass

func transform_point(point: Vector2, scale: float) -> Vector2:
	pass


# Getters and Setters

func get_counter() -> int:
	return counter

func set_counter(value: int) -> void:
	counter = value

func get_label() -> String:
	return label

func set_label(value: String) -> void:
	label = value

func get_origin() -> Vector2:
	return origin

func set_origin(value: Vector2) -> void:
	origin = value

func get_status() -> int:
	return status

func set_status(value: int) -> void:
	status = value

