extends RefCounted
#brief A wrapper class for web native JavaScript objects.
#desc JavaScriptObject is used to interact with JavaScript objects retrieved or created via [method JavaScriptBridge.get_interface], [method JavaScriptBridge.create_object], or [method JavaScriptBridge.create_callback].
#desc Example:
#desc [codeblock]
#desc extends Node
#desc 
#desc var _my_js_callback = JavaScriptBridge.create_callback(self, "myCallback") # This reference must be kept
#desc var console = JavaScriptBridge.get_interface("console")
#desc 
#desc func _init():
#desc var buf = JavaScriptBridge.create_object("ArrayBuffer", 10) # new ArrayBuffer(10)
#desc print(buf) # prints [JavaScriptObject:OBJECT_ID]
#desc var uint8arr = JavaScriptBridge.create_object("Uint8Array", buf) # new Uint8Array(buf)
#desc uint8arr[1] = 255
#desc prints(uint8arr[1], uint8arr.byteLength) # prints 255 10
#desc console.log(uint8arr) # prints in browser console "Uint8Array(10) [ 0, 255, 0, 0, 0, 0, 0, 0, 0, 0 ]"
#desc 
#desc # Equivalent of JavaScriptBridge: Array.from(uint8arr).forEach(myCallback)
#desc JavaScriptBridge.get_interface("Array").from(uint8arr).forEach(_my_js_callback)
#desc 
#desc func myCallback(args):
#desc # Will be called with the parameters passed to the "forEach" callback
#desc # [0, 0, [JavaScriptObject:1173]]
#desc # [255, 1, [JavaScriptObject:1173]]
#desc # ...
#desc # [0, 9, [JavaScriptObject:1180]]
#desc print(args)
#desc [/codeblock]
#desc [b]Note:[/b] Only available in the Web platform.
class_name JavaScriptObject





