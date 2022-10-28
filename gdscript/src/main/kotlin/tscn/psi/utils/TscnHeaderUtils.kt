package tscn.psi.utils

import com.intellij.psi.util.siblings
import tscn.psi.TscnDataLine
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

        return path;
    }

    /** Node header specific */
    fun getNodePath(element: TscnNodeHeader): String {
        val stub = element.stub
        if (stub != null) {
            return stub.nodePath()
        }

        val isRoot = element.parentPath == ".";

        return "${if (isRoot) "" else "${element.parentPath}/"}${element.name}"
    }

    fun isUniqueNameOwner(element: TscnNodeHeader): Boolean {
        val dataLines = element.siblings(forward = true, withSelf = false);

        return dataLines
            .filterIsInstance<TscnDataLine>()
            .find { it.dataLineNm.text == TscnParagraphUtils.UNIQUE_NAME_IN_OWNER }
            ?.dataLineValue
            ?.text?.trim { it == ' ' } == "true"; // TODO trim .. whitespace lexer
    }

}