#brief Server that manages all translations.
#desc Server that manages all translations. Translations can be set to it and removed from it.
class_name TranslationServer


#desc If [code]true[/code], enables the use of pseudolocalization. See [member ProjectSettings.internationalization/pseudolocalization/use_pseudolocalization] for details.
var pseudolocalization_enabled: bool;



#desc Adds a [Translation] resource.
func add_translation(translation: Translation) -> void:
	pass;

#desc Clears the server from all translations.
func clear() -> void:
	pass;

#desc Compares two locales and return similarity score between [code]0[/code](no match) and [code]10[/code](full match).
func compare_locales(locale_a: String, locale_b: String) -> int:
	pass;

#desc Returns array of known country codes.
func get_all_countries() -> PackedStringArray:
	pass;

#desc Returns array of known language codes.
func get_all_languages() -> PackedStringArray:
	pass;

#desc Returns array of known script codes.
func get_all_scripts() -> PackedStringArray:
	pass;

#desc Returns readable country name for the [param country] code.
func get_country_name(country: String) -> String:
	pass;

#desc Returns readable language name for the [param language] code.
func get_language_name(language: String) -> String:
	pass;

#desc Returns an array of all loaded locales of the project.
func get_loaded_locales() -> PackedStringArray:
	pass;

#desc Returns the current locale of the project.
#desc See also [method OS.get_locale] and [method OS.get_locale_language] to query the locale of the user system.
func get_locale() -> String:
	pass;

#desc Returns a locale's language and its variant (e.g. [code]"en_US"[/code] would return [code]"English (United States)"[/code]).
func get_locale_name(locale: String) -> String:
	pass;

#desc Returns readable script name for the [param script] code.
func get_script_name(script: String) -> String:
	pass;

#desc Returns the current locale of the editor.
#desc [b]Note:[/b] When called from an exported project returns the same value as [method get_locale].
func get_tool_locale() -> String:
	pass;

#desc Returns the [Translation] instance based on the [param locale] passed in.
#desc It will return [code]null[/code] if there is no [Translation] instance that matches the [param locale].
func get_translation_object(locale: String) -> Translation:
	pass;

#desc Returns the pseudolocalized string based on the [param message] passed in.
func pseudolocalize(message: StringName) -> StringName:
	pass;

#desc Reparses the pseudolocalization options and reloads the translation.
func reload_pseudolocalization() -> void:
	pass;

#desc Removes the given translation from the server.
func remove_translation(translation: Translation) -> void:
	pass;

#desc Sets the locale of the project. The [param locale] string will be standardized to match known locales (e.g. [code]en-US[/code] would be matched to [code]en_US[/code]).
#desc If translations have been loaded beforehand for the new locale, they will be applied.
func set_locale(locale: String) -> void:
	pass;

#desc Returns [param locale] string standardized to match known locales (e.g. [code]en-US[/code] would be matched to [code]en_US[/code]).
func standardize_locale(locale: String) -> String:
	pass;

#desc Returns the current locale's translation for the given message (key) and context.
func translate(message: StringName, context: StringName) -> StringName:
	pass;

#desc Returns the current locale's translation for the given message (key), plural_message and context.
#desc The number [param n] is the number or quantity of the plural object. It will be used to guide the translation system to fetch the correct plural form for the selected language.
func translate_plural(message: StringName, plural_message: StringName, n: int, context: StringName) -> StringName:
	pass;


