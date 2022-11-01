package tscn.psi.utils

import com.intellij.psi.util.PsiTreeUtil
import tscn.psi.TscnDataLine
import tscn.psi.TscnHeaderValue
import tscn.psi.TscnParagraph

/**
 * Common utils for header lines
 */
object TscnHeaderUtils {

    /** Header keys */
    val HL_ID = "id";
    val HL_NAME = "name";
    val HL_TYPE = "type";
    val HL_PATH = "path";
    val HL_PARENT = "parent";

    /** Data line keys */
    val DL_UNIQUE = "unique_name_in_owner";
    val DL_SCRIPT = "script";

    /**
     * Gets a value from header line parameter [node name="Outer"] without quotes
     * or empty string
     */
    fun getValue(values: List<TscnHeaderValue>, key: String): String {
        val value = values.find {
            it.headerValueNm.text == key
        }?.headerValueVal?.text ?: "";

        return value.trim { it == '"' };
    }

    /**
     * Gets a value from data line (below header)
     * or empty string
     */
    fun getDataValue(element: TscnParagraph, key: String): String {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(element, TscnDataLine::class.java).find {
            it.dataLineNm.text == key
        }?.dataLineValue?.text?.trim(' ') ?: "";
    }

}
