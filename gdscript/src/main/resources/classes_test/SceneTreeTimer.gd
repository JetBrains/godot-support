#brief One-shot timer.
#desc A one-shot timer managed by the scene tree, which emits [signal timeout] on completion. See also [method SceneTree.create_timer].
#desc As opposed to [Timer], it does not require the instantiation of a node. Commonly used to create a one-shot delay timer as in the following example:
#desc [codeblocks]
#desc [gdscript]
#desc func some_function():
#desc print("Timer started.")
#desc await get_tree().create_timer(1.0).timeout
#desc print("Timer ended.")
#desc [/gdscript]
#desc [csharp]
#desc public async void SomeFunction()
#desc {
#desc GD.Print("Timer started.");
#desc await ToSignal(GetTree().CreateTimer(1.0f), "timeout");
#desc GD.Print("Timer ended.");
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc The timer will be dereferenced after its time elapses. To preserve the timer, you can keep a reference to it. See [RefCounted].
class_name SceneTreeTimer


#desc The time remaining (in seconds).
var time_left: float;




