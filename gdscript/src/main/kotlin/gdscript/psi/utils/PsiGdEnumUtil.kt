package gdscript.psi.utils

import com.intellij.psi.util.elementType
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdTypes

object PsiGdEnumUtil {

    fun fromString(data: String?): LinkedHashMap<String, Long> {
        val params: LinkedHashMap<String, Long> = java.util.LinkedHashMap()
        if (data.isNullOrBlank()) {
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

    fun values(enum: GdEnumDeclTl): LinkedHashMap<String, Long> {
        val stub = enum.stub
        if (stub != null) return stub.values()

        val values: LinkedHashMap<String, Long> = LinkedHashMap()
        var currentVal: Long = 0

        enum.enumValueList.forEach {
            var child = it.lastChild
            while (child != null) {
                if (child.elementType == GdTypes.NUMBER) {
                    currentVal = child.text.toLong()
                    break
                }
                child = child.prevSibling
            }

            values[it.enumValueNmi.name] = currentVal
            currentVal += 1
        }

        return values
    }

}
