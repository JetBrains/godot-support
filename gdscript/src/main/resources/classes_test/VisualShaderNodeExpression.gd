#brief A custom visual shader graph expression written in Godot Shading Language.
#desc Custom Godot Shading Language expression, with a custom number of input and output ports.
#desc The provided code is directly injected into the graph's matching shader function ([code]vertex[/code], [code]fragment[/code], or [code]light[/code]), so it cannot be used to declare functions, varyings, uniforms, or global constants. See [VisualShaderNodeGlobalExpression] for such global definitions.
class_name VisualShaderNodeExpression


#desc An expression in Godot Shading Language, which will be injected at the start of the graph's matching shader function ([code]vertex[/code], [code]fragment[/code], or [code]light[/code]), and thus cannot be used to declare functions, varyings, uniforms, or global constants.
var expression: String;




