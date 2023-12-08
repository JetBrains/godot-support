package gdscript.psi.utils

import com.intellij.psi.util.elementType
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdTypes

object PsiGdEnumUtil {

    fun fromString(data: String?): HashMap<String, Long> {
        val params = HashMap<String, Long>()
        if (data == null) {
            return params
        }

        data.trim('{', '}').split(' ').forEach {
            val parts = it.split('=')
            if (parts.size == 2) {
                params[parts[0]] = parts[1].trim(',', ' ').toLong()
            }
        }

        return params
    }

    fun values(enum: GdEnumDeclTl): HashMap<String, Long> {
        val stub = enum.stub
        if (stub != null) return stub.values()

        val values = HashMap<String, Long>()
        var currentVal: Long = 0

        enum.enumValueList.forEach {
            currentVal += 1
            var child = it.lastChild
            while (child != null) {
                if (child.elementType == GdTypes.NUMBER) {
                    currentVal = child.text.toLong()
                    break
                }
                child = child.prevSibling;
            }

            values[it.enumValueNmi.name] = currentVal
        }

        return values
    }

}
