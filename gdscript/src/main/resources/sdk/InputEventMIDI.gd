extends InputEvent
#brief Input event for MIDI inputs.
#desc InputEventMIDI allows receiving input events from MIDI devices such as a piano. MIDI stands for Musical Instrument Digital Interface.
#desc MIDI signals can be sent over a 5-pin MIDI connector or over USB, if your device supports both be sure to check the settings in the device to see which output it's using.
#desc To receive input events from MIDI devices, you need to call [method OS.open_midi_inputs]. You can check which devices are detected using [method OS.get_connected_midi_inputs].
#desc [codeblocks]
#desc [gdscript]
#desc func _ready():
#desc OS.open_midi_inputs()
#desc print(OS.get_connected_midi_inputs())
#desc 
#desc func _input(input_event):
#desc if input_event is InputEventMIDI:
#desc _print_midi_info(input_event)
#desc 
#desc func _print_midi_info(midi_event: InputEventMIDI):
#desc print(midi_event)
#desc print("Channel " + str(midi_event.channel))
#desc print("Message " + str(midi_event.message))
#desc print("Pitch " + str(midi_event.pitch))
#desc print("Velocity " + str(midi_event.velocity))
#desc print("Instrument " + str(midi_event.instrument))
#desc print("Pressure " + str(midi_event.pressure))
#desc print("Controller number: " + str(midi_event.controller_number))
#desc print("Controller value: " + str(midi_event.controller_value))
#desc [/gdscript]
#desc [csharp]
#desc public override void _Ready()
#desc {
#desc OS.OpenMidiInputs();
#desc GD.Print(OS.GetConnectedMidiInputs());
#desc }
#desc 
#desc public override void _Input(InputEvent inputEvent)
#desc {
#desc if (inputEvent is InputEventMIDI midiEvent)
#desc {
#desc PrintMIDIInfo(midiEvent);
#desc }
#desc }
#desc 
#desc private void PrintMIDIInfo(InputEventMIDI midiEvent)
#desc {
#desc GD.Print(midiEvent);
#desc GD.Print("Channel " + midiEvent.Channel);
#desc GD.Print("Message " + midiEvent.Message);
#desc GD.Print("Pitch " + midiEvent.Pitch);
#desc GD.Print("Velocity " + midiEvent.Velocity);
#desc GD.Print("Instrument " + midiEvent.Instrument);
#desc GD.Print("Pressure " + midiEvent.Pressure);
#desc GD.Print("Controller number: " + midiEvent.ControllerNumber);
#desc GD.Print("Controller value: " + midiEvent.ControllerValue);
#desc }
#desc [/csharp]
#desc [/codeblocks]
#desc Note that Godot does not currently support MIDI output, so there is no way to emit MIDI signals from Godot. Only MIDI input works.
class_name InputEventMIDI


#desc The MIDI channel of this input event. There are 16 channels, so this value ranges from 0 to 15. MIDI channel 9 is reserved for the use with percussion instruments, the rest of the channels are for non-percussion instruments.
var channel: int;

#desc If the message is [code]MIDI_MESSAGE_CONTROL_CHANGE[/code], this indicates the controller number, otherwise this is zero. Controllers include devices such as pedals and levers.
var controller_number: int;

#desc If the message is [code]MIDI_MESSAGE_CONTROL_CHANGE[/code], this indicates the controller value, otherwise this is zero. Controllers include devices such as pedals and levers.
var controller_value: int;

#desc The instrument of this input event. This value ranges from 0 to 127. Refer to the instrument list on the General MIDI wikipedia article to see a list of instruments, except that this value is 0-index, so subtract one from every number on that chart. A standard piano will have an instrument number of 0.
var instrument: int;

#desc Returns a value indicating the type of message for this MIDI signal. This is a member of the [enum @GlobalScope.MIDIMessage] enum.
#desc For MIDI messages between 0x80 and 0xEF, only the left half of the bits are returned as this value, as the other part is the channel (ex: 0x94 becomes 0x9). For MIDI messages from 0xF0 to 0xFF, the value is returned as-is.
#desc Notes will return [code]MIDI_MESSAGE_NOTE_ON[/code] when activated, but they might not always return [code]MIDI_MESSAGE_NOTE_OFF[/code] when deactivated, therefore your code should treat the input as stopped if some period of time has passed.
#desc For more information, see the MIDI message status byte list chart linked above.
var message: int;

#desc The pitch index number of this MIDI signal. This value ranges from 0 to 127. On a piano, middle C is 60, and A440 is 69, see the "MIDI note" column of the piano key frequency chart on Wikipedia for more information.
var pitch: int;

#desc The pressure of the MIDI signal. This value ranges from 0 to 127. For many devices, this value is always zero.
var pressure: int;

#desc The velocity of the MIDI signal. This value ranges from 0 to 127. For a piano, this corresponds to how quickly the key was pressed, and is rarely above about 110 in practice.
var velocity: int;




