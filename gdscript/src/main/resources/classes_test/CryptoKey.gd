extends Resource
#brief A cryptographic key (RSA).
#desc The CryptoKey class represents a cryptographic key. Keys can be loaded and saved like any other [Resource].
#desc They can be used to generate a self-signed [X509Certificate] via [method Crypto.generate_self_signed_certificate] and as private key in [method StreamPeerTLS.accept_stream] along with the appropriate certificate.
class_name CryptoKey




#desc Returns [code]true[/code] if this CryptoKey only has the public part, and not the private one.
func is_public_only() -> bool:
	pass;

#desc Loads a key from [param path]. If [param public_only] is [code]true[/code], only the public key will be loaded.
#desc [b]Note:[/b] [param path] should be a "*.pub" file if [param public_only] is [code]true[/code], a "*.key" file otherwise.
func load(path: String, public_only: bool) -> int:
	pass;

#desc Loads a key from the given [param string_key]. If [param public_only] is [code]true[/code], only the public key will be loaded.
func load_from_string(string_key: String, public_only: bool) -> int:
	pass;

#desc Saves a key to the given [param path]. If [param public_only] is [code]true[/code], only the public key will be saved.
#desc [b]Note:[/b] [param path] should be a "*.pub" file if [param public_only] is [code]true[/code], a "*.key" file otherwise.
func save(path: String, public_only: bool) -> int:
	pass;

#desc Returns a string containing the key in PEM format. If [param public_only] is [code]true[/code], only the public key will be included.
func save_to_string(public_only: bool) -> String:
	pass;


