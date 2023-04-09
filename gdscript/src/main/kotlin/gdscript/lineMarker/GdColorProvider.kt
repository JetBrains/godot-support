package gdscript.lineMarker

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.psi.GdArgList
import gdscript.psi.GdCallEx
import gdscript.psi.GdElementFactory
import gdscript.psi.GdLiteralEx
import gdscript.psi.utils.GdClassMemberUtil
import java.awt.Color

class GdColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element !is GdCallEx) return null

        try {
            val text = element.expr.text
            val parameters = element.argList?.argExprList ?: emptyList()
            parameters.forEach {
                if (it.expr !is GdLiteralEx) return null
            }

            if (text == "Color") {
                // Empty constructor defaults to Color.BLACK Color(0, 0, 0, 1)
                if (parameters.isEmpty()) return Color.BLACK
                if (parameters.first().returnType == GdKeywords.FLOAT) {
                    if (parameters.size <= 2) return null
                    return Color(
                        parameters[0].text.toFloat(),
                        parameters[1].text.toFloat(),
                        parameters[2].text.toFloat(),
                        parameters.getOrNull(3)?.text?.toFloat() ?: 1f,
                    )
                }
            } else if (text == "Color8") {
                if (parameters.size <= 2) return null
                return Color(
                    parameters[0].text.toInt(),
                    parameters[1].text.toInt(),
                    parameters[2].text.toInt(),
                    parameters.getOrNull(3)?.text?.toInt() ?: 255,
                )
            } else if ((text == "html" || text == "html_is_valid" || text == "hex") && GdClassMemberUtil.calledUpon(element)?.text == "Color") {
                var value = parameters.firstOrNull()?.text ?: "000"
                var alpha = "ff"
                if (text.startsWith("html")) {
                    value = value.trim('"').removePrefix("#")
                    if (value.length > 6) {
                        alpha = value.substring(6)
                        value = value.substring(0, 6)
                    }
                } else {
                    value = value.removePrefix("0x")
                    if (value.length > 6) {
                        alpha = value.substring(0, 2)
                        value = value.substring(2)
                    }
                }
                val intVal = Integer.decode("#$value")
                return Color(intVal shr 16 and 0xFF, intVal shr 8 and 0xFF, intVal and 0xFF, Integer.decode("#$alpha") and 0xFF)
            }
        } catch (e: Exception) {
            return null
        }

        return null
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        if (element !is GdCallEx) return

        val text = element.expr.text
        val toReplace = PsiTreeUtil.getChildOfType(element, GdArgList::class.java)

        val exprValue = when (text) {
            "Color" -> {
                if (color == Color.BLACK) {
                    toReplace?.delete()
                    return
                }

                val alpha = if (color.alpha == 255) "" else ", ${color.alpha / 255f}"
                "Color(${color.red / 255f}, ${color.green / 255f}, ${color.blue / 255f}$alpha)"
            }
            "Color8" -> {
                val alpha = if (color.alpha == 255) "" else ", ${color.alpha}"
                "Color(${color.red}, ${color.green}, ${color.blue}$alpha)"
            }
            "html", "html_is_valid", "hex" -> {
                if (GdClassMemberUtil.calledUpon(element)?.text == "Color") {
                    val value = if (text == "hex") {
                        // Color.hex(0xbbefd2a4)
                        "0x${Integer.toHexString(color.rgb).trimStart('#')}"
                    } else {
                        // Required manually as color.rgb puts alpha at first instead of last
                        var hex = Integer.toHexString((color.red shl 24) + (color.green shl 16) + (color.blue shl 8) + color.alpha)
                        if (hex.length % 2 == 1) hex = "0$hex"
                        "\"#$hex\""
                    }
                    "Color.$text($value)"
                } else null
            }
            else -> null
        } ?: return

        val arguments = PsiTreeUtil.findChildOfType(
            GdElementFactory.callExpr(element.project, exprValue), GdArgList::class.java
        ) ?: return

        if (toReplace != null) {
            toReplace.replace(arguments)
            return
        }

        element.addBefore(arguments, element.lastChild)
    }

}
