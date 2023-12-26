package common.util

object HashMapUtil {

    val VALUE_SEPARATOR = "||"
    val PARAM_SEPARATOR = "__||__"

    fun HashMap<String, String>.toSimpleParamString(): String {
        return this.map {
            "${it.key}${VALUE_SEPARATOR}${it.value}"
        }.joinToString(PARAM_SEPARATOR)
    }

    fun String.fromSimpleParamString(): HashMap<String, String> {
        val map = hashMapOf<String, String>()
        this.split(PARAM_SEPARATOR)
            .forEach {
                val vals = it.split(VALUE_SEPARATOR)
                map[vals[0]] = vals[1]
            }

        return map
    }

}
