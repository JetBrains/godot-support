package gdscript.sdk.xml

object GdNameSanitizer {

    fun sanitizeClassName(name: String): String =
        if (name.startsWith("@")) "_" + name.substring(1) else name
}
