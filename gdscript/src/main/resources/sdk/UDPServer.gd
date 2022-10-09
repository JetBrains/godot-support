extends RefCounted
#brief Helper class to implement a UDP server.
#desc A simple server that opens a UDP socket and returns connected [PacketPeerUDP] upon receiving new packets. See also [method PacketPeerUDP.connect_to_host].
#desc After starting the server ([method listen]), you will need to [method poll] it at regular intervals (e.g. inside [method Node._process]) for it to process new packets, delivering them to the appropriate [PacketPeerUDP], and taking new connections.
#desc Below a small example of how it can be used:
#desc [codeblocks]
#desc [gdscript]
#desc class_name Server
#desc extends Node
#desc 
#desc var server := UDPServer.new()
#desc var peers = []
#desc 
#desc func _ready():
#desc server.listen(4242)
#desc 
#desc func _process(delta):
#desc server.poll() # Important!
#desc if server.is_connection_available():
#desc var peer : PacketPeerUDP = server.take_connection()
#desc var packet = peer.get_packet()
#desc print("Accepted peer: %s:%s" % [peer.get_packet_ip(), peer.get_packet_port()])
#desc print("Received data: %s" % [packet.get_string_from_utf8()])
#desc # Reply so it knows we received the message.
#desc peer.put_packet(packet)
#desc # Keep a reference so we can keep contacting the remote peer.
#desc peers.append(peer)
#desc 
#desc for i in range(0, peers.size()):
#desc pass # Do something with the connected peers.
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc using System.Collections.Generic;
#desc 
#desc public class Server : Node
#desc {
#desc public UDPServer Server = new UDPServer();
#desc public List<PacketPeerUDP> Peers = new List<PacketPeerUDP>();
#desc 
#desc public override void _Ready()
#desc {
#desc Server.Listen(4242);
#desc }
#desc 
#desc public override void _Process(float delta)
#desc {
#desc Server.Poll(); // Important!
#desc if (Server.IsConnectionAvailable())
#desc {
#desc PacketPeerUDP peer = Server.TakeConnection();
#desc byte[] packet = peer.GetPacket();
#desc GD.Print($"Accepted Peer: {peer.GetPacketIp()}:{peer.GetPacketPort()}");
#desc GD.Print($"Received Data: {packet.GetStringFromUTF8()}");
#desc // Reply so it knows we received the message.
#desc peer.PutPacket(packet);
#desc // Keep a reference so we can keep contacting the remote peer.
#desc Peers.Add(peer);
#desc }
#desc foreach (var peer in Peers)
#desc {
#desc // Do something with the peers.
#desc }
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [codeblocks]
#desc [gdscript]
#desc class_name Client
#desc extends Node
#desc 
#desc var udp := PacketPeerUDP.new()
#desc var connected = false
#desc 
#desc func _ready():
#desc udp.connect_to_host("127.0.0.1", 4242)
#desc 
#desc func _process(delta):
#desc if !connected:
#desc # Try to contact server
#desc udp.put_packet("The answer is... 42!".to_utf8())
#desc if udp.get_available_packet_count() > 0:
#desc print("Connected: %s" % udp.get_packet().get_string_from_utf8())
#desc connected = true
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc 
#desc public class Client : Node
#desc {
#desc public PacketPeerUDP Udp = new PacketPeerUDP();
#desc public bool Connected = false;
#desc 
#desc public override void _Ready()
#desc {
#desc Udp.ConnectToHost("127.0.0.1", 4242);
#desc }
#desc 
#desc public override void _Process(float delta)
#desc {
#desc if (!Connected)
#desc {
#desc // Try to contact server
#desc Udp.PutPacket("The Answer Is..42!".ToUTF8());
#desc }
#desc if (Udp.GetAvailablePacketCount() > 0)
#desc {
#desc GD.Print($"Connected: {Udp.GetPacket().GetStringFromUTF8()}");
#desc Connected = true;
#desc }
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name UDPServer


#desc Define the maximum number of pending connections, during [method poll], any new pending connection exceeding that value will be automatically dropped. Setting this value to [code]0[/code] effectively prevents any new pending connection to be accepted (e.g. when all your players have connected).
var max_pending_connections: int;



#desc Returns the local port this server is listening to.
func get_local_port() -> int:
	pass;

#desc Returns [code]true[/code] if a packet with a new address/port combination was received on the socket.
func is_connection_available() -> bool:
	pass;

#desc Returns [code]true[/code] if the socket is open and listening on a port.
func is_listening() -> bool:
	pass;

#desc Starts the server by opening a UDP socket listening on the given [param port]. You can optionally specify a [param bind_address] to only listen for packets sent to that address. See also [method PacketPeerUDP.bind].
func listen(port: int, bind_address: String) -> int:
	pass;

#desc Call this method at regular intervals (e.g. inside [method Node._process]) to process new packets. And packet from known address/port pair will be delivered to the appropriate [PacketPeerUDP], any packet received from an unknown address/port pair will be added as a pending connection (see [method is_connection_available], [method take_connection]). The maximum number of pending connection is defined via [member max_pending_connections].
func poll() -> int:
	pass;

#desc Stops the server, closing the UDP socket if open. Will close all connected [PacketPeerUDP] accepted via [method take_connection] (remote peers will not be notified).
func stop() -> void:
	pass;

#desc Returns the first pending connection (connected to the appropriate address/port). Will return [code]null[/code] if no new connection is available. See also [method is_connection_available], [method PacketPeerUDP.connect_to_host].
func take_connection() -> PacketPeerUDP:
	pass;


