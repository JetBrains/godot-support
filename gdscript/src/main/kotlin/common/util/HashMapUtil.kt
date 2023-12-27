package common.util

object HashMapUtil {

    val VALUE_SEPARATOR = "||"
    val PARAM_SEPARATOR = "__||__"

    fun LinkedHashMap<String, String>.toSimpleParamString(): String {
        return this.map {
            "${it.key}${VALUE_SEPARATOR}${it.value}"
        }.joinToString(PARAM_SEPARATOR)
    }

    fun String.fromSimpleParamString(): LinkedHashMap<String, String> {
        val map = linkedMapOf<String, String>()
        if (this.isBlank()) return map

        this.split(PARAM_SEPARATOR)
            .forEach {
                val vals = it.split(VALUE_SEPARATOR)
                map[vals[0]] = vals[1]
            }

        return map
    }

}
