#brief Input event type for actions.
#desc Contains a generic action which can be targeted from several types of inputs. Actions can be created from the [b]Input Map[/b] tab in the [b]Project > Project Settings[/b] menu. See [method Node._input].
class_name InputEventAction


#desc The action's name. Actions are accessed via this [String].
var action: StringName;

#desc If [code]true[/code], the action's state is pressed. If [code]false[/code], the action's state is released.
var pressed: bool;

#desc The action's strength between 0 and 1. This value is considered as equal to 0 if pressed is [code]false[/code]. The event strength allows faking analog joypad motion events, by specifying how strongly the joypad axis is bent or pressed.
var strength: float;




