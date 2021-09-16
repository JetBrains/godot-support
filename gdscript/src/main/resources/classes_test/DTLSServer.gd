extends RefCounted
class_name DTLSServer


func setup(key: CryptoKey, certificate: X509Certificate, chain: X509Certificate) -> int:
    pass;
func take_connection(udp_peer: PacketPeerUDP) -> PacketPeerDTLS:
    pass;
