#brief Used to create an HMAC for a message using a key.
#desc The HMACContext class is useful for advanced HMAC use cases, such as streaming the message as it supports creating the message over time rather than providing it all at once.
#desc [codeblocks]
#desc [gdscript]
#desc extends Node
#desc var ctx = HMACContext.new()
#desc 
#desc func _ready():
#desc var key = "supersecret".to_utf8()
#desc var err = ctx.start(HashingContext.HASH_SHA256, key)
#desc assert(err == OK)
#desc var msg1 = "this is ".to_utf8()
#desc var msg2 = "super duper secret".to_utf8()
#desc err = ctx.update(msg1)
#desc assert(err == OK)
#desc err = ctx.update(msg2)
#desc assert(err == OK)
#desc var hmac = ctx.finish()
#desc print(hmac.hex_encode())
#desc 
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc using System.Diagnostics;
#desc 
#desc public class CryptoNode : Node
#desc {
#desc private HMACContext ctx = new HMACContext();
#desc 
#desc public override void _Ready()
#desc {
#desc byte[] key = "supersecret".ToUTF8();
#desc Error err = ctx.Start(HashingContext.HashType.Sha256, key);
#desc Debug.Assert(err == Error.Ok);
#desc byte[] msg1 = "this is ".ToUTF8();
#desc byte[] msg2 = "super duper secret".ToUTF8();
#desc err = ctx.Update(msg1);
#desc Debug.Assert(err == Error.Ok);
#desc err = ctx.Update(msg2);
#desc Debug.Assert(err == Error.Ok);
#desc byte[] hmac = ctx.Finish();
#desc GD.Print(hmac.HexEncode());
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name HMACContext




#desc Returns the resulting HMAC. If the HMAC failed, an empty [PackedByteArray] is returned.
func finish() -> PackedByteArray:
	pass;

#desc Initializes the HMACContext. This method cannot be called again on the same HMACContext until [method finish] has been called.
func start(hash_type: int, key: PackedByteArray) -> int:
	pass;

#desc Updates the message to be HMACed. This can be called multiple times before [method finish] is called to append [param data] to the message, but cannot be called until [method start] has been called.
func update() -> int:
	pass;


