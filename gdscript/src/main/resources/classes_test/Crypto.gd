#brief Access to advanced cryptographic functionalities.
#desc The Crypto class allows you to access some more advanced cryptographic functionalities in Godot.
#desc For now, this includes generating cryptographically secure random bytes, RSA keys and self-signed X509 certificates generation, asymmetric key encryption/decryption, and signing/verification.
#desc [codeblocks]
#desc [gdscript]
#desc extends Node
#desc var crypto = Crypto.new()
#desc var key = CryptoKey.new()
#desc var cert = X509Certificate.new()
#desc func _ready():
#desc # Generate new RSA key.
#desc key = crypto.generate_rsa(4096)
#desc # Generate new self-signed certificate with the given key.
#desc cert = crypto.generate_self_signed_certificate(key, "CN=mydomain.com,O=My Game Company,C=IT")
#desc # Save key and certificate in the user folder.
#desc key.save("user://generated.key")
#desc cert.save("user://generated.crt")
#desc # Encryption
#desc var data = "Some data"
#desc var encrypted = crypto.encrypt(key, data.to_utf8())
#desc # Decryption
#desc var decrypted = crypto.decrypt(key, encrypted)
#desc # Signing
#desc var signature = crypto.sign(HashingContext.HASH_SHA256, data.sha256_buffer(), key)
#desc # Verifying
#desc var verified = crypto.verify(HashingContext.HASH_SHA256, data.sha256_buffer(), signature, key)
#desc # Checks
#desc assert(verified)
#desc assert(data.to_utf8() == decrypted)
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc using System.Diagnostics;
#desc 
#desc public class CryptoNode : Node
#desc {
#desc public Crypto Crypto = new Crypto();
#desc public CryptoKey Key = new CryptoKey();
#desc public X509Certificate Cert = new X509Certificate();
#desc public override void _Ready()
#desc {
#desc // Generate new RSA key.
#desc Key = Crypto.GenerateRsa(4096);
#desc // Generate new self-signed certificate with the given key.
#desc Cert = Crypto.GenerateSelfSignedCertificate(Key, "CN=mydomain.com,O=My Game Company,C=IT");
#desc // Save key and certificate in the user folder.
#desc Key.Save("user://generated.key");
#desc Cert.Save("user://generated.crt");
#desc // Encryption
#desc string data = "Some data";
#desc byte[] encrypted = Crypto.Encrypt(Key, data.ToUTF8());
#desc // Decryption
#desc byte[] decrypted = Crypto.Decrypt(Key, encrypted);
#desc // Signing
#desc byte[] signature = Crypto.Sign(HashingContext.HashType.Sha256, Data.SHA256Buffer(), Key);
#desc // Verifying
#desc bool verified = Crypto.Verify(HashingContext.HashType.Sha256, Data.SHA256Buffer(), signature, Key);
#desc // Checks
#desc Debug.Assert(verified);
#desc Debug.Assert(data.ToUTF8() == decrypted);
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name Crypto




#desc Compares two [PackedByteArray]s for equality without leaking timing information in order to prevent timing attacks.
#desc See [url=https://paragonie.com/blog/2015/11/preventing-timing-attacks-on-string-comparison-with-double-hmac-strategy]this blog post[/url] for more information.
func constant_time_compare(trusted: PackedByteArray, received: PackedByteArray) -> bool:
	pass;

#desc Decrypt the given [param ciphertext] with the provided private [param key].
#desc [b]Note:[/b] The maximum size of accepted ciphertext is limited by the key size.
func decrypt(key: CryptoKey, ciphertext: PackedByteArray) -> PackedByteArray:
	pass;

#desc Encrypt the given [param plaintext] with the provided public [param key].
#desc [b]Note:[/b] The maximum size of accepted plaintext is limited by the key size.
func encrypt(key: CryptoKey, plaintext: PackedByteArray) -> PackedByteArray:
	pass;

#desc Generates a [PackedByteArray] of cryptographically secure random bytes with given [param size].
func generate_random_bytes(size: int) -> PackedByteArray:
	pass;

#desc Generates an RSA [CryptoKey] that can be used for creating self-signed certificates and passed to [method StreamPeerTLS.accept_stream].
func generate_rsa(size: int) -> CryptoKey:
	pass;

#desc Generates a self-signed [X509Certificate] from the given [CryptoKey] and [param issuer_name]. The certificate validity will be defined by [param not_before] and [param not_after] (first valid date and last valid date). The [param issuer_name] must contain at least "CN=" (common name, i.e. the domain name), "O=" (organization, i.e. your company name), "C=" (country, i.e. 2 lettered ISO-3166 code of the country the organization is based in).
#desc A small example to generate an RSA key and a X509 self-signed certificate.
#desc [codeblocks]
#desc [gdscript]
#desc var crypto = Crypto.new()
#desc # Generate 4096 bits RSA key.
#desc var key = crypto.generate_rsa(4096)
#desc # Generate self-signed certificate using the given key.
#desc var cert = crypto.generate_self_signed_certificate(key, "CN=example.com,O=A Game Company,C=IT")
#desc [/gdscript]
#desc [csharp]
#desc var crypto = new Crypto();
#desc // Generate 4096 bits RSA key.
#desc CryptoKey key = crypto.GenerateRsa(4096);
#desc // Generate self-signed certificate using the given key.
#desc X509Certificate cert = crypto.GenerateSelfSignedCertificate(key, "CN=mydomain.com,O=My Game Company,C=IT");
#desc [/csharp]
#desc [/codeblocks]
func generate_self_signed_certificate(key: CryptoKey, issuer_name: String, not_before: String, not_after: String) -> X509Certificate:
	pass;

#desc Generates an [url=https://en.wikipedia.org/wiki/HMAC]HMAC[/url] digest of [param msg] using [param key]. The [param hash_type] parameter is the hashing algorithm that is used for the inner and outer hashes.
#desc Currently, only [constant HashingContext.HASH_SHA256] and [constant HashingContext.HASH_SHA1] are supported.
func hmac_digest(hash_type: int, key: PackedByteArray, msg: PackedByteArray) -> PackedByteArray:
	pass;

#desc Sign a given [param hash] of type [param hash_type] with the provided private [param key].
func sign(hash_type: int, hash: PackedByteArray, key: CryptoKey) -> PackedByteArray:
	pass;

#desc Verify that a given [param signature] for [param hash] of type [param hash_type] against the provided public [param key].
func verify(hash_type: int, hash: PackedByteArray, signature: PackedByteArray, key: CryptoKey) -> bool:
	pass;


