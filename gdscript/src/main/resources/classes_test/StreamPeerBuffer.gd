#brief Data buffer stream peer.
#desc Data buffer stream peer that uses a byte array as the stream. This object can be used to handle binary data from network sessions. To handle binary data stored in files, [FileAccess] can be used directly.
#desc A [StreamPeerBuffer] object keeps an internal cursor which is the offset in bytes to the start of the buffer. Get and put operations are performed at the cursor position and will move the cursor accordingly.
class_name StreamPeerBuffer


#desc The underlying data buffer. Setting this value resets the cursor.
var data_array: PackedByteArray;



#desc Clears the [member data_array] and resets the cursor.
func clear() -> void:
	pass;

#desc Returns a new [StreamPeerBuffer] with the same [member data_array] content.
func duplicate() -> StreamPeerBuffer:
	pass;

#desc Returns the current cursor position.
func get_position() -> int:
	pass;

#desc Returns the size of [member data_array].
func get_size() -> int:
	pass;

#desc Resizes the [member data_array]. This [i]doesn't[/i] update the cursor.
func resize() -> void:
	pass;

#desc Moves the cursor to the specified position. [param position] must be a valid index of [member data_array].
func seek() -> void:
	pass;


