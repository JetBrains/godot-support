## Plugin settings:

- In order to dedent on backspace instead of deleting a line, you can change editor's settings under:

IntelliJ: Editor -> General -> Smart Keys -> Unindent on Backspace  
Rider: Editor -> General -> Typing Assistance -> Unindent on Backspace  
... or just search for "Unindent"

- By default, few of annotators are off due dynamics of Godot and GdScript, you can change it in settings, but then
  it's required to be thorough when specifying types
- Warning that variable is not typed is disabled by default, but I recommend to opt-in (types can be added via alt+enter)
- Completion settings: Editor -> General -> Code Completion -> Parameter Info
    - Try turning on: `Show parameter name hints on completion` which invokes hints after auto-completion
