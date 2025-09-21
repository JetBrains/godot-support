package tscn.psi.utils

import tscn.psi.TscnDataLine
import tscn.psi.TscnParagraph

object TscnParagraphUtil {
    /**
     * Get the [TscnDataLine] in the specified [TscnParagraph] that assigns the specified [fieldName], or `null` if no
     * such line exists.
     *
     * @param paragraph The [TscnParagraph] in which to search for the specified field.
     * @param fieldName The name of the field being set by the [TscnDataLine].
     *
     * @return A [TscnDataLine] that sets the specified field, or `null` if no such line exists.
     */
    fun getDataLine(paragraph: TscnParagraph, fieldName: String): TscnDataLine? {
        return paragraph.dataLineList.firstOrNull { it.dataLineHeader.text == fieldName }
    }
}
