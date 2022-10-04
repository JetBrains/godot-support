extends StreamPeer
#brief Stream peer handling GZIP and deflate compression/decompresison.
#desc This class allows to compress or decompress data using GZIP/deflate in a streaming fashion. This is particularly useful when compressing or decompressing files that has to be sent through the network without having to allocate them all in memory.
#desc After starting the stream via [method start_compression] (or [method start_decompression]), calling [method StreamPeer.put_partial_data] on this stream will compress (or decompress) the data, writing it to the internal buffer. Calling [method StreamPeer.get_available_bytes] will return the pending bytes in the internal buffer, and [method StreamPeer.get_partial_data] will retrieve the compressed (or decompressed) bytes from it. When the stream is over, you must call [method finish] to ensure the internal buffer is properly flushed (make sure to call [method StreamPeer.get_available_bytes] on last time to check if more data needs to be read after that).
class_name StreamPeerGZIP




#desc Clears this stream, resetting the internal state.
func clear() -> void:
	pass;

#desc Finalizes the stream, compressing or decompressing any buffered chunk left.
func finish() -> int:
	pass;

#desc Start the stream in compression mode with the given [param buffer_size], if [param use_deflate] is [code]true[/code] uses deflate instead of GZIP.
func start_compression(use_deflate: bool, buffer_size: int) -> int:
	pass;

#desc Start the stream in decompression mode with the given [param buffer_size], if [param use_deflate] is [code]true[/code] uses deflate instead of GZIP.
func start_decompression(use_deflate: bool, buffer_size: int) -> int:
	pass;


