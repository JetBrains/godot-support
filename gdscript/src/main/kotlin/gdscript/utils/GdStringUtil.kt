package gdscript.utils

/**
 * Removes the quote characters around a GDScript string literal.
 * This also strips the `&` prefix of a StringName literal.
 */
fun String.unquote(): String = trim('"', '\'', '&')
