#brief Dictionary type.
#desc Dictionary type. Associative container, which contains values referenced by unique keys. Dictionaries are composed of pairs of keys (which must be unique) and values. Dictionaries will preserve the insertion order when adding elements. In other programming languages, this data structure is sometimes referred to as a hash map or associative array.
#desc You can define a dictionary by placing a comma-separated list of [code]key: value[/code] pairs in curly braces [code]{}[/code].
#desc Erasing elements while iterating over them [b]is not supported[/b] and will result in undefined behavior.
#desc [b]Note:[/b] Dictionaries are always passed by reference. To get a copy of a dictionary which can be modified independently of the original dictionary, use [method duplicate].
#desc Creating a dictionary:
#desc [codeblocks]
#desc [gdscript]
#desc var my_dict = {} # Creates an empty dictionary.
#desc 
#desc var dict_variable_key = "Another key name"
#desc var dict_variable_value = "value2"
#desc var another_dict = {
#desc "Some key name": "value1",
#desc dict_variable_key: dict_variable_value,
#desc }
#desc 
#desc var points_dict = {"White": 50, "Yellow": 75, "Orange": 100}
#desc 
#desc # Alternative Lua-style syntax.
#desc # Doesn't require quotes around keys, but only string constants can be used as key names.
#desc # Additionally, key names must start with a letter or an underscore.
#desc # Here, `some_key` is a string literal, not a variable!
#desc another_dict = {
#desc some_key = 42,
#desc }
#desc [/gdscript]
#desc [csharp]
#desc var myDict = new Godot.Collections.Dictionary(); // Creates an empty dictionary.
#desc var pointsDict = new Godot.Collections.Dictionary
#desc {
#desc {"White", 50},
#desc {"Yellow", 75},
#desc {"Orange", 100}
#desc };
#desc [/csharp]
#desc [/codeblocks]
#desc You can access a dictionary's values by referencing the appropriate key. In the above example, [code]points_dict["White"][/code] will return [code]50[/code]. You can also write [code]points_dict.White[/code], which is equivalent. However, you'll have to use the bracket syntax if the key you're accessing the dictionary with isn't a fixed string (such as a number or variable).
#desc [codeblocks]
#desc [gdscript]
#desc export(String, "White", "Yellow", "Orange") var my_color
#desc var points_dict = {"White": 50, "Yellow": 75, "Orange": 100}
#desc func _ready():
#desc # We can't use dot syntax here as `my_color` is a variable.
#desc var points = points_dict[my_color]
#desc [/gdscript]
#desc [csharp]
#desc [Export(PropertyHint.Enum, "White,Yellow,Orange")]
#desc public string MyColor { get; set; }
#desc public Godot.Collections.Dictionary pointsDict = new Godot.Collections.Dictionary
#desc {
#desc {"White", 50},
#desc {"Yellow", 75},
#desc {"Orange", 100}
#desc };
#desc 
#desc public override void _Ready()
#desc {
#desc int points = (int)pointsDict[MyColor];
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc In the above code, [code]points[/code] will be assigned the value that is paired with the appropriate color selected in [code]my_color[/code].
#desc Dictionaries can contain more complex data:
#desc [codeblocks]
#desc [gdscript]
#desc my_dict = {"First Array": [1, 2, 3, 4]} # Assigns an Array to a String key.
#desc [/gdscript]
#desc [csharp]
#desc var myDict = new Godot.Collections.Dictionary
#desc {
#desc {"First Array", new Godot.Collections.Array{1, 2, 3, 4}}
#desc };
#desc [/csharp]
#desc [/codeblocks]
#desc To add a key to an existing dictionary, access it like an existing key and assign to it:
#desc [codeblocks]
#desc [gdscript]
#desc var points_dict = {"White": 50, "Yellow": 75, "Orange": 100}
#desc points_dict["Blue"] = 150 # Add "Blue" as a key and assign 150 as its value.
#desc [/gdscript]
#desc [csharp]
#desc var pointsDict = new Godot.Collections.Dictionary
#desc {
#desc {"White", 50},
#desc {"Yellow", 75},
#desc {"Orange", 100}
#desc };
#desc pointsDict["blue"] = 150; // Add "Blue" as a key and assign 150 as its value.
#desc [/csharp]
#desc [/codeblocks]
#desc Finally, dictionaries can contain different types of keys and values in the same dictionary:
#desc [codeblocks]
#desc [gdscript]
#desc # This is a valid dictionary.
#desc # To access the string "Nested value" below, use `my_dict.sub_dict.sub_key` or `my_dict["sub_dict"]["sub_key"]`.
#desc # Indexing styles can be mixed and matched depending on your needs.
#desc var my_dict = {
#desc "String Key": 5,
#desc 4: [1, 2, 3],
#desc 7: "Hello",
#desc "sub_dict": {"sub_key": "Nested value"},
#desc }
#desc [/gdscript]
#desc [csharp]
#desc // This is a valid dictionary.
#desc // To access the string "Nested value" below, use `((Godot.Collections.Dictionary)myDict["sub_dict"])["sub_key"]`.
#desc var myDict = new Godot.Collections.Dictionary {
#desc {"String Key", 5},
#desc {4, new Godot.Collections.Array{1,2,3}},
#desc {7, "Hello"},
#desc {"sub_dict", new Godot.Collections.Dictionary{{"sub_key", "Nested value"}}}
#desc };
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] Unlike [Array]s, you can't compare dictionaries directly:
#desc [codeblocks]
#desc [gdscript]
#desc var array1 = [1, 2, 3]
#desc var array2 = [1, 2, 3]
#desc 
#desc func compare_arrays():
#desc print(array1 == array2) # Will print true.
#desc 
#desc var dict1 = {"a": 1, "b": 2, "c": 3}
#desc var dict2 = {"a": 1, "b": 2, "c": 3}
#desc 
#desc func compare_dictionaries():
#desc print(dict1 == dict2) # Will NOT print true.
#desc [/gdscript]
#desc [csharp]
#desc // You have to use GD.Hash().
#desc 
#desc public Godot.Collections.Array array1 = new Godot.Collections.Array{1, 2, 3};
#desc public Godot.Collections.Array array2 = new Godot.Collections.Array{1, 2, 3};
#desc 
#desc public void CompareArrays()
#desc {
#desc GD.Print(array1 == array2); // Will print FALSE!!
#desc GD.Print(GD.Hash(array1) == GD.Hash(array2)); // Will print true.
#desc }
#desc 
#desc public Godot.Collections.Dictionary dict1 = new Godot.Collections.Dictionary{{"a", 1}, {"b", 2}, {"c", 3}};
#desc public Godot.Collections.Dictionary dict2 = new Godot.Collections.Dictionary{{"a", 1}, {"b", 2}, {"c", 3}};
#desc 
#desc public void CompareDictionaries()
#desc {
#desc GD.Print(dict1 == dict2); // Will NOT print true.
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc You need to first calculate the dictionary's hash with [method hash] before you can compare them:
#desc [codeblocks]
#desc [gdscript]
#desc var dict1 = {"a": 1, "b": 2, "c": 3}
#desc var dict2 = {"a": 1, "b": 2, "c": 3}
#desc 
#desc func compare_dictionaries():
#desc print(dict1.hash() == dict2.hash()) # Will print true.
#desc [/gdscript]
#desc [csharp]
#desc // You have to use GD.Hash().
#desc public Godot.Collections.Dictionary dict1 = new Godot.Collections.Dictionary{{"a", 1}, {"b", 2}, {"c", 3}};
#desc public Godot.Collections.Dictionary dict2 = new Godot.Collections.Dictionary{{"a", 1}, {"b", 2}, {"c", 3}};
#desc 
#desc public void CompareDictionaries()
#desc {
#desc GD.Print(GD.Hash(dict1) == GD.Hash(dict2)); // Will print true.
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] When declaring a dictionary with [code]const[/code], the dictionary itself can still be mutated by defining the values of individual keys. Using [code]const[/code] will only prevent assigning the constant with another value after it was initialized.
class_name Dictionary



#desc Constructs an empty [Dictionary].
func Dictionary() -> Dictionary:
	pass;

#desc Constructs a [Dictionary] as a copy of the given [Dictionary].
func Dictionary() -> Dictionary:
	pass;


#desc Clear the dictionary, removing all key/value pairs.
func clear() -> void:
	pass;

#desc Creates a copy of the dictionary, and returns it. The [param deep] parameter causes inner dictionaries and arrays to be copied recursively, but does not apply to objects.
func duplicate() -> Dictionary:
	pass;

#desc Erase a dictionary key/value pair by key. Returns [code]true[/code] if the given key was present in the dictionary, [code]false[/code] otherwise.
#desc [b]Note:[/b] Don't erase elements while iterating over the dictionary. You can iterate over the [method keys] array instead.
func erase() -> bool:
	pass;

#desc Returns the first key whose associated value is equal to [param value], or [code]null[/code] if no such value is found.
#desc [b]Note:[/b] [code]null[/code] is also a valid key. If you have it in your [Dictionary], the [method find_key] method can give misleading results.
func find_key() -> Variant:
	pass;

#desc Returns the current value for the specified key in the [Dictionary]. If the key does not exist, the method returns the value of the optional default argument, or [code]null[/code] if it is omitted.
func get(key: Variant, default: Variant) -> Variant:
	pass;

#desc Returns [code]true[/code] if the dictionary has a given key.
#desc [b]Note:[/b] This is equivalent to using the [code]in[/code] operator as follows:
#desc [codeblocks]
#desc [gdscript]
#desc # Will evaluate to `true`.
#desc if "godot" in {"godot": "engine"}:
#desc pass
#desc [/gdscript]
#desc [csharp]
#desc // You have to use Contains() here as an alternative to GDScript's `in` operator.
#desc if (new Godot.Collections.Dictionary{{"godot", "engine"}}.Contains("godot"))
#desc {
#desc // I am executed.
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc This method (like the [code]in[/code] operator) will evaluate to [code]true[/code] as long as the key exists, even if the associated value is [code]null[/code].
func has() -> bool:
	pass;

#desc Returns [code]true[/code] if the dictionary has all the keys in the given array.
func has_all() -> bool:
	pass;

#desc Returns a hashed 32-bit integer value representing the dictionary contents. This can be used to compare dictionaries by value:
#desc [codeblocks]
#desc [gdscript]
#desc var dict1 = {0: 10}
#desc var dict2 = {0: 10}
#desc # The line below prints `true`, whereas it would have printed `false` if both variables were compared directly.
#desc print(dict1.hash() == dict2.hash())
#desc [/gdscript]
#desc [csharp]
#desc var dict1 = new Godot.Collections.Dictionary{{0, 10}};
#desc var dict2 = new Godot.Collections.Dictionary{{0, 10}};
#desc // The line below prints `true`, whereas it would have printed `false` if both variables were compared directly.
#desc // Dictionary has no Hash() method. Use GD.Hash() instead.
#desc GD.Print(GD.Hash(dict1) == GD.Hash(dict2));
#desc [/csharp]
#desc [/codeblocks]
#desc [b]Note:[/b] Dictionaries with the same keys/values but in a different order will have a different hash.
#desc [b]Note:[/b] Dictionaries with equal content will always produce identical hash values. However, the reverse is not true. Returning identical hash values does [i]not[/i] imply the dictionaries are equal, because different dictionaries can have identical hash values due to hash collisions.
func hash() -> int:
	pass;

#desc Returns [code]true[/code] if the dictionary is empty.
func is_empty() -> bool:
	pass;

#desc Returns the list of keys in the [Dictionary].
func keys() -> Array:
	pass;

#desc Adds elements from [param dictionary] to this [Dictionary]. By default, duplicate keys will not be copied over, unless [param overwrite] is [code]true[/code].
func merge(dictionary: Dictionary, overwrite: bool) -> void:
	pass;

#desc Returns the number of keys in the dictionary.
func size() -> int:
	pass;

#desc Returns the list of values in the [Dictionary].
func values() -> Array:
	pass;


