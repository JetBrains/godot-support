#brief Context to compute cryptographic hashes over multiple iterations.
#desc The HashingContext class provides an interface for computing cryptographic hashes over multiple iterations. This is useful for example when computing hashes of big files (so you don't have to load them all in memory), network streams, and data streams in general (so you don't have to hold buffers).
#desc The [enum HashType] enum shows the supported hashing algorithms.
#desc [codeblocks]
#desc [gdscript]
#desc const CHUNK_SIZE = 102
#desc 
#desc func hash_file(path):
#desc var ctx = HashingContext.new()
#desc var file = File.new()
#desc # Start a SHA-256 context.
#desc ctx.start(HashingContext.HASH_SHA256)
#desc # Check that file exists.
#desc if not file.file_exists(path):
#desc return
#desc # Open the file to hash.
#desc file.open(path, File.READ)
#desc # Update the context after reading each chunk.
#desc while not file.eof_reached():
#desc ctx.update(file.get_buffer(CHUNK_SIZE))
#desc # Get the computed hash.
#desc var res = ctx.finish()
#desc # Print the result as hex string and array.
#desc printt(res.hex_encode(), Array(res))
#desc [/gdscript]
#desc [csharp]
#desc public const int ChunkSize = 1024;
#desc 
#desc public void HashFile(string path)
#desc {
#desc var ctx = new HashingContext();
#desc var file = new File();
#desc // Start a SHA-256 context.
#desc ctx.Start(HashingContext.HashType.Sha256);
#desc // Check that file exists.
#desc if (!file.FileExists(path))
#desc {
#desc return;
#desc }
#desc // Open the file to hash.
#desc file.Open(path, File.ModeFlags.Read);
#desc // Update the context after reading each chunk.
#desc while (!file.EofReached())
#desc {
#desc ctx.Update(file.GetBuffer(ChunkSize));
#desc }
#desc // Get the computed hash.
#desc byte[] res = ctx.Finish();
#desc // Print the result as hex string and array.
#desc 
#desc GD.PrintT(res.HexEncode(), res);
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name HashingContext

#desc Hashing algorithm: MD5.
const HASH_MD5 = 0;

#desc Hashing algorithm: SHA-1.
const HASH_SHA1 = 1;

#desc Hashing algorithm: SHA-256.
const HASH_SHA256 = 2;




#desc Closes the current context, and return the computed hash.
func finish() -> PackedByteArray:
	pass;

#desc Starts a new hash computation of the given [param type] (e.g. [constant HASH_SHA256] to start computation of a SHA-256).
func start(type: int) -> int:
	pass;

#desc Updates the computation with the given [param chunk] of data.
func update(chunk: PackedByteArray) -> int:
	pass;


