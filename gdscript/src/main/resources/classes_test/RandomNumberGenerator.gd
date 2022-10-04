extends RefCounted
#brief A class for generating pseudo-random numbers.
#desc RandomNumberGenerator is a class for generating pseudo-random numbers. It currently uses [url=https://www.pcg-random.org/]PCG32[/url].
#desc [b]Note:[/b] The underlying algorithm is an implementation detail. As a result, it should not be depended upon for reproducible random streams across Godot versions.
#desc To generate a random float number (within a given range) based on a time-dependant seed:
#desc [codeblock]
#desc var rng = RandomNumberGenerator.new()
#desc func _ready():
#desc rng.randomize()
#desc var my_random_number = rng.randf_range(-10.0, 10.0)
#desc [/codeblock]
#desc [b]Note:[/b] The default values of [member seed] and [member state] properties are pseudo-random, and changes when calling [method randomize]. The [code]0[/code] value documented here is a placeholder, and not the actual default seed.
class_name RandomNumberGenerator


#desc Initializes the random number generator state based on the given seed value. A given seed will give a reproducible sequence of pseudo-random numbers.
#desc [b]Note:[/b] The RNG does not have an avalanche effect, and can output similar random streams given similar seeds. Consider using a hash function to improve your seed quality if they're sourced externally.
#desc [b]Note:[/b] Setting this property produces a side effect of changing the internal [member state], so make sure to initialize the seed [i]before[/i] modifying the [member state]:
#desc [codeblock]
#desc var rng = RandomNumberGenerator.new()
#desc rng.seed = hash("Godot")
#desc rng.state = 100 # Restore to some previously saved state.
#desc [/codeblock]
var seed: int;

#desc The current state of the random number generator. Save and restore this property to restore the generator to a previous state:
#desc [codeblock]
#desc var rng = RandomNumberGenerator.new()
#desc print(rng.randf())
#desc var saved_state = rng.state # Store current state.
#desc print(rng.randf()) # Advance internal state.
#desc rng.state = saved_state # Restore the state.
#desc print(rng.randf()) # Prints the same value as in previous.
#desc [/codeblock]
#desc [b]Note:[/b] Do not set state to arbitrary values, since the random number generator requires the state to have certain qualities to behave properly. It should only be set to values that came from the state property itself. To initialize the random number generator with arbitrary input, use [member seed] instead.
var state: int;



#desc Returns a pseudo-random float between [code]0.0[/code] and [code]1.0[/code] (inclusive).
func randf() -> float:
	pass;

#desc Returns a pseudo-random float between [param from] and [param to] (inclusive).
func randf_range(from: float, to: float) -> float:
	pass;

#desc Returns a [url=https://en.wikipedia.org/wiki/Normal_distribution]normally-distributed[/url] pseudo-random number, using Box-Muller transform with the specified [param mean] and a standard [param deviation]. This is also called Gaussian distribution.
func randfn(mean: float, deviation: float) -> float:
	pass;

#desc Returns a pseudo-random 32-bit unsigned integer between [code]0[/code] and [code]4294967295[/code] (inclusive).
func randi() -> int:
	pass;

#desc Returns a pseudo-random 32-bit signed integer between [param from] and [param to] (inclusive).
func randi_range(from: int, to: int) -> int:
	pass;

#desc Setups a time-based seed to for this [RandomNumberGenerator] instance. Unlike the [@GlobalScope] random number generation functions, different [RandomNumberGenerator] instances can use different seeds.
func randomize() -> void:
	pass;


