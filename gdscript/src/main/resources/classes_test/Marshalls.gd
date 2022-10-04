#brief Data transformation (marshalling) and encoding helpers.
#desc Provides data transformation and encoding utility functions.
class_name Marshalls




#desc Returns a decoded [PackedByteArray] corresponding to the Base64-encoded string [param base64_str].
func base64_to_raw(base64_str: String) -> PackedByteArray:
	pass;

#desc Returns a decoded string corresponding to the Base64-encoded string [param base64_str].
func base64_to_utf8(base64_str: String) -> String:
	pass;

#desc Returns a decoded [Variant] corresponding to the Base64-encoded string [param base64_str]. If [param allow_objects] is [code]true[/code], decoding objects is allowed.
#desc [b]Warning:[/b] Deserialized objects can contain code which gets executed. Do not use this option if the serialized object comes from untrusted sources to avoid potential security threats such as remote code execution.
func base64_to_variant(base64_str: String, allow_objects: bool) -> Variant:
	pass;

#desc Returns a Base64-encoded string of a given [PackedByteArray].
func raw_to_base64(array: PackedByteArray) -> String:
	pass;

#desc Returns a Base64-encoded string of the UTF-8 string [param utf8_str].
func utf8_to_base64(utf8_str: String) -> String:
	pass;

#desc Returns a Base64-encoded string of the [Variant] [param variant]. If [param full_objects] is [code]true[/code], encoding objects is allowed (and can potentially include code).
func variant_to_base64(variant: Variant, full_objects: bool) -> String:
	pass;


