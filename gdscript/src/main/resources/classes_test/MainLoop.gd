#brief Abstract base class for the game's main loop.
#desc [MainLoop] is the abstract base class for a Godot project's game loop. It is inherited by [SceneTree], which is the default game loop implementation used in Godot projects, though it is also possible to write and use one's own [MainLoop] subclass instead of the scene tree.
#desc Upon the application start, a [MainLoop] implementation must be provided to the OS; otherwise, the application will exit. This happens automatically (and a [SceneTree] is created) unless a [MainLoop] [Script] is provided from the command line (with e.g. [code]godot -s my_loop.gd[/code] or the "Main Loop Type" project setting is overwritten.
#desc Here is an example script implementing a simple [MainLoop]:
#desc [codeblocks]
#desc [gdscript]
#desc class_name CustomMainLoop
#desc extends MainLoop
#desc 
#desc var time_elapsed = 0
#desc 
#desc func _initialize():
#desc print("Initialized:")
#desc print("  Starting time: %s" % str(time_elapsed))
#desc 
#desc func _process(delta):
#desc time_elapsed += delta
#desc # Return true to end the main loop.
#desc return Input.get_mouse_button_mask() != 0 || Input.is_key_pressed(KEY_ESCAPE)
#desc 
#desc func _finalize():
#desc print("Finalized:")
#desc print("  End time: %s" % str(time_elapsed))
#desc [/gdscript]
#desc [csharp]
#desc using Godot;
#desc using System;
#desc 
#desc public class CustomMainLoop : MainLoop
#desc {
#desc public float TimeElapsed = 0;
#desc 
#desc public override void _Initialize()
#desc {
#desc GD.Print("Initialized:");
#desc GD.Print($"  Starting Time: {TimeElapsed}");
#desc }
#desc 
#desc public override bool _Process(float delta)
#desc {
#desc TimeElapsed += delta;
#desc // Return true to end the main loop.
#desc return Input.GetMouseButtonMask() != 0 || Input.IsKeyPressed((int)KeyList.Escape);
#desc }
#desc 
#desc private void _Finalize()
#desc {
#desc GD.Print("Finalized:");
#desc GD.Print($"  End Time: {TimeElapsed}");
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name MainLoop

#desc Notification received from the OS when the application is exceeding its allocated memory.
#desc Specific to the iOS platform.
const NOTIFICATION_OS_MEMORY_WARNING = 2009;

#desc Notification received when translations may have changed. Can be triggered by the user changing the locale. Can be used to respond to language changes, for example to change the UI strings on the fly. Useful when working with the built-in translation support, like [method Object.tr].
const NOTIFICATION_TRANSLATION_CHANGED = 2010;

#desc Notification received from the OS when a request for "About" information is sent.
#desc Specific to the macOS platform.
const NOTIFICATION_WM_ABOUT = 2011;

#desc Notification received from Godot's crash handler when the engine is about to crash.
#desc Implemented on desktop platforms if the crash handler is enabled.
const NOTIFICATION_CRASH = 2012;

#desc Notification received from the OS when an update of the Input Method Engine occurs (e.g. change of IME cursor position or composition string).
#desc Specific to the macOS platform.
const NOTIFICATION_OS_IME_UPDATE = 2013;

#desc Notification received from the OS when the application is resumed.
#desc Specific to the Android platform.
const NOTIFICATION_APPLICATION_RESUMED = 2014;

#desc Notification received from the OS when the application is paused.
#desc Specific to the Android platform.
const NOTIFICATION_APPLICATION_PAUSED = 2015;

#desc Notification received from the OS when the application is focused, i.e. when changing the focus from the OS desktop or a thirdparty application to any open window of the Godot instance.
#desc Implemented on desktop platforms.
const NOTIFICATION_APPLICATION_FOCUS_IN = 2016;

#desc Notification received from the OS when the application is defocused, i.e. when changing the focus from any open window of the Godot instance to the OS desktop or a thirdparty application.
#desc Implemented on desktop platforms.
const NOTIFICATION_APPLICATION_FOCUS_OUT = 2017;

#desc Notification received when text server is changed.
const NOTIFICATION_TEXT_SERVER_CHANGED = 2018;




#desc Called before the program exits.
virtual func _finalize() -> void:
	pass;

#desc Called once during initialization.
virtual func _initialize() -> void:
	pass;

#desc Called each physics frame with the time since the last physics frame as argument ([param delta], in seconds). Equivalent to [method Node._physics_process].
#desc If implemented, the method must return a boolean value. [code]true[/code] ends the main loop, while [code]false[/code] lets it proceed to the next frame.
virtual func _physics_process() -> bool:
	pass;

#desc Called each process (idle) frame with the time since the last process frame as argument (in seconds). Equivalent to [method Node._process].
#desc If implemented, the method must return a boolean value. [code]true[/code] ends the main loop, while [code]false[/code] lets it proceed to the next frame.
virtual func _process() -> bool:
	pass;


