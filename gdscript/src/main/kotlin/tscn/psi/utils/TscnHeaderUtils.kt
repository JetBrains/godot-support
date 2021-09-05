package tscn.psi.utils

import tscn.psi.TscnHeaderValue
import tscn.psi.TscnNodeHeader

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

    fun getNodePath(element: TscnNodeHeader): String {
        val stub = element.stub
        if (stub != null) {
            return stub.nodePath()
        }

        val isRoot = element.parentPath == ".";

        return "${if (isRoot) "" else "${element.parentPath}/"}${element.name}"
    }

}