#brief An X509 certificate (e.g. for TLS).
#desc The X509Certificate class represents an X509 certificate. Certificates can be loaded and saved like any other [Resource].
#desc They can be used as the server certificate in [method StreamPeerTLS.accept_stream] (along with the proper [CryptoKey]), and to specify the only certificate that should be accepted when connecting to an TLS server via [method StreamPeerTLS.connect_to_stream].
class_name X509Certificate




#desc Loads a certificate from [param path] ("*.crt" file).
func load(path: String) -> int:
	pass;

#desc Saves a certificate to the given [param path] (should be a "*.crt" file).
func save(path: String) -> int:
	pass;


