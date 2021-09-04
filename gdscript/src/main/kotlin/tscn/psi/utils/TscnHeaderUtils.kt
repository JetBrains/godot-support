package tscn.psi.utils

import tscn.psi.TscnHeaderValue

object TscnHeaderUtils {

    fun getValue(values: List<TscnHeaderValue>, key: String): String {
        val value = values.find {
            it.headerValueNm.text == key
        }?.headerValueVal?.text ?: "";

        return value.trim { it == '"' };
    }

    fun getPath(values: List<TscnHeaderValue>): String {
        val path = getValue(values, "path");

        return path.removePrefix("res://");
    }

}