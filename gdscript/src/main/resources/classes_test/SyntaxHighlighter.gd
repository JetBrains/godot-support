#brief Base Syntax highlighter resource for [TextEdit].
#desc Base syntax highlighter resource all syntax highlighters extend from, provides syntax highlighting data to [TextEdit].
#desc The associated [TextEdit] node will call into the [SyntaxHighlighter] on a as needed basis.
#desc [b]Note:[/b] Each Syntax highlighter instance should not be shared across multiple [TextEdit] nodes.
class_name SyntaxHighlighter




#desc Virtual method which can be overridden to clear any local caches.
virtual func _clear_highlighting_cache() -> void:
	pass;

#desc Virtual method which can be overridden to return syntax highlighting data.
#desc See [method get_line_syntax_highlighting] for more details.
virtual const func _get_line_syntax_highlighting() -> Dictionary:
	pass;

#desc Virtual method which can be overridden to update any local caches.
virtual func _update_cache() -> void:
	pass;

#desc Clears all cached syntax highlighting data.
#desc Then calls overridable method [method _clear_highlighting_cache].
func clear_highlighting_cache() -> void:
	pass;

#desc Returns syntax highlighting data for a single line. If the line is not cached, calls [method _get_line_syntax_highlighting] to calculate the data.
#desc The return [Dictionary] is column number to [Dictionary]. The column number notes the start of a region, the region will end if another region is found, or at the end of the line. The nested [Dictionary] contains the data for that region, currently only the key "color" is supported.
#desc [b]Example return:[/b]
#desc [codeblock]
#desc var color_map = {
#desc 0: {
#desc "color": Color(1, 0, 0)
#desc },
#desc 5: {
#desc "color": Color(0, 1, 0)
#desc }
#desc }
#desc [/codeblock]
#desc This will color columns 0-4 red, and columns 5-eol in green.
func get_line_syntax_highlighting() -> Dictionary:
	pass;

#desc Returns the associated [TextEdit] node.
func get_text_edit() -> TextEdit:
	pass;

#desc Clears then updates the [SyntaxHighlighter] caches. Override [method _update_cache] for a callback.
#desc [b]Note:[/b] This is called automatically when the associated [TextEdit] node, updates its own cache.
func update_cache() -> void:
	pass;


