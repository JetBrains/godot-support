#brief A class that stores an expression you can execute.
#desc An expression can be made of any arithmetic operation, built-in math function call, method call of a passed instance, or built-in type construction call.
#desc An example expression text using the built-in math functions could be [code]sqrt(pow(3, 2) + pow(4, 2))[/code].
#desc In the following example we use a [LineEdit] node to write our expression and show the result.
#desc [codeblocks]
#desc [gdscript]
#desc var expression = Expression.new()
#desc 
#desc func _ready():
#desc $LineEdit.connect("text_submitted", self, "_on_text_submitted")
#desc 
#desc func _on_text_submitted(command):
#desc var error = expression.parse(command)
#desc if error != OK:
#desc print(expression.get_error_text())
#desc return
#desc var result = expression.execute()
#desc if not expression.has_execute_failed():
#desc $LineEdit.text = str(result)
#desc [/gdscript]
#desc [csharp]
#desc public Expression expression = new Expression();
#desc 
#desc public override void _Ready()
#desc {
#desc GetNode("LineEdit").Connect("text_submitted", this, nameof(OnTextEntered));
#desc }
#desc 
#desc private void OnTextEntered(string command)
#desc {
#desc Error error = expression.Parse(command);
#desc if (error != Error.Ok)
#desc {
#desc GD.Print(expression.GetErrorText());
#desc return;
#desc }
#desc object result = expression.Execute();
#desc if (!expression.HasExecuteFailed())
#desc {
#desc GetNode<LineEdit>("LineEdit").Text = result.ToString();
#desc }
#desc }
#desc [/csharp]
#desc [/codeblocks]
class_name Expression




#desc Executes the expression that was previously parsed by [method parse] and returns the result. Before you use the returned object, you should check if the method failed by calling [method has_execute_failed].
#desc If you defined input variables in [method parse], you can specify their values in the inputs array, in the same order.
func execute(inputs: Array, base_instance: Object, show_error: bool, const_calls_only: bool) -> Variant:
	pass;

#desc Returns the error text if [method parse] has failed.
func get_error_text() -> String:
	pass;

#desc Returns [code]true[/code] if [method execute] has failed.
func has_execute_failed() -> bool:
	pass;

#desc Parses the expression and returns an [enum Error] code.
#desc You can optionally specify names of variables that may appear in the expression with [param input_names], so that you can bind them when it gets executed.
func parse(expression: String, input_names: PackedStringArray) -> int:
	pass;


