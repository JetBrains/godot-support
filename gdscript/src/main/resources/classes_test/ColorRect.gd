extends Control
#brief Colored rectangle.
#desc Displays a rectangle filled with a solid [member color]. If you need to display the border alone, consider using [ReferenceRect] instead.
class_name ColorRect


#desc The fill color.
#desc [codeblocks]
#desc [gdscript]
#desc $ColorRect.color = Color(1, 0, 0, 1) # Set ColorRect's color to red.
#desc [/gdscript]
#desc [csharp]
#desc GetNode<ColorRect>("ColorRect").Color = new Color(1, 0, 0, 1); // Set ColorRect's color to red.
#desc [/csharp]
#desc [/codeblocks]
var color: Color;




