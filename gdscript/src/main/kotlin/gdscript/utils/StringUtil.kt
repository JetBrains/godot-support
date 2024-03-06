package gdscript.utils

import gdscript.GdKeywords
import java.util.*

object StringUtil {

    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    private val snakeRegex = "([_ ])[a-zA-Z]".toRegex()

    fun String.camelToSnakeCase(): String {
        var toLower = true
        val parsed = this.map {
            if (toLower) {
                toLower = it == it.uppercaseChar()
                it.lowercase()
            } else {
                it.toString()
            }
        }.joinToString("")

        return camelRegex.replace(parsed) {
                "_${it.value}"
            }.lowercase(Locale.getDefault())
    }

    fun String.snakeToCamelCase(): String {
        return snakeRegex.replace(this) {
            it.value.replace("_","")
                .uppercase(Locale.getDefault())
        }.replace(" ", "")
    }

    fun String.snakeToPascalCase(): String {
        return this
            .replace("-", "_")
            .snakeToCamelCase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun List<String>.filterGdTscn(): Array<String> {
        return this.filter { it.endsWith(".gd") || it.endsWith(".tscn") }.toTypedArray()
    }

    fun String.parseFromSquare(): String {
        val start = this.indexOf('[');
        val end = this.indexOf(']', start);

        if (start < 0 || end < 1) return "";
        return this.substring(start + 1, end);
    }

    fun String.isDynamicType(): Boolean {
        return this.isBlank() || this == GdKeywords.VARIANT
    }

}
