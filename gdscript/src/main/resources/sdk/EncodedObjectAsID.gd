extends RefCounted
#brief Holds a reference to an [Object]'s instance ID.
#desc Utility class which holds a reference to the internal identifier of an [Object] instance, as given by [method Object.get_instance_id]. This ID can then be used to retrieve the object instance with [method @GlobalScope.instance_from_id].
#desc This class is used internally by the editor inspector and script debugger, but can also be used in plugins to pass and display objects as their IDs.
class_name EncodedObjectAsID


#desc The [Object] identifier stored in this [EncodedObjectAsID] instance. The object instance can be retrieved with [method @GlobalScope.instance_from_id].
var object_id: int;




