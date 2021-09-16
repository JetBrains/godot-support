extends RefCounted
class_name Crypto


func constant_time_compare(trusted: PackedByteArray, received: PackedByteArray) -> bool:
    pass;
func decrypt(key: CryptoKey, ciphertext: PackedByteArray) -> PackedByteArray:
    pass;
func encrypt(key: CryptoKey, plaintext: PackedByteArray) -> PackedByteArray:
    pass;
func generate_random_bytes(size: int) -> PackedByteArray:
    pass;
func generate_rsa(size: int) -> CryptoKey:
    pass;
func generate_self_signed_certificate(key: CryptoKey, issuer_name: String, not_before: String, not_after: String) -> X509Certificate:
    pass;
func hmac_digest(hash_type: int, key: PackedByteArray, msg: PackedByteArray) -> PackedByteArray:
    pass;
func sign(hash_type: int, hash: PackedByteArray, key: CryptoKey) -> PackedByteArray:
    pass;
func verify(hash_type: int, hash: PackedByteArray, signature: PackedByteArray, key: CryptoKey) -> bool:
    pass;
