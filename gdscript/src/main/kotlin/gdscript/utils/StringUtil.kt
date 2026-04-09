package gdscript.utils

import com.intellij.polySymbols.utils.NameCaseUtils
import gdscript.GdKeywords

object StringUtil {

    fun String.camelToSnakeCase(): String = NameCaseUtils.toSnakeCase(this)
    fun String.snakeToPascalCase(): String = NameCaseUtils.toPascalCase(this)

    fun List<String>.filterGdTscn(): Array<String> {
        return this.filter { it.endsWith(".gd") || it.endsWith(".tscn") }.toTypedArray()
    }

    fun String.parseFromSquare(): String {
        val start = this.indexOf('[')
        val end = this.indexOf(']', start)

        if (start < 0 || end < 1) return ""
        return this.substring(start + 1, end)
    }

    fun String.isDynamicType(): Boolean {
        return this.isBlank() || this == GdKeywords.VARIANT
    }

}
