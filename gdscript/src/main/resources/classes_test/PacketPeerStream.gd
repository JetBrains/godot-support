extends PacketPeer
#brief Wrapper to use a PacketPeer over a StreamPeer.
#desc PacketStreamPeer provides a wrapper for working using packets over a stream. This allows for using packet based code with StreamPeers. PacketPeerStream implements a custom protocol over the StreamPeer, so the user should not read or write to the wrapped StreamPeer directly.
#desc [b]Note:[/b] When exporting to Android, make sure to enable the [code]INTERNET[/code] permission in the Android export preset before exporting the project or using one-click deploy. Otherwise, network communication of any kind will be blocked by Android.
class_name PacketPeerStream


var input_buffer_max_size: int;

var output_buffer_max_size: int;

#desc The wrapped [StreamPeer] object.
var stream_peer: StreamPeer;




