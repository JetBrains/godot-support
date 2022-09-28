package gdscript.utils

import java.util.*

object StringUtils {

    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    private val snakeRegex = "_[a-zA-Z]".toRegex()

    fun String.camelToSnakeCase(): String {
        return camelRegex.replace(this) {
                "_${it.value}"
            }.lowercase(Locale.getDefault())
    }

    fun String.snakeToLowerCamelCase(): String {
        return snakeRegex.replace(this) {
            it.value.replace("_","")
                .uppercase(Locale.getDefault())
        }
    }

    fun String.snakeToUpperCamelCase(): String {
        return this.snakeToLowerCamelCase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

}