package gdscript.psi.utils

import com.intellij.psi.util.elementType
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdTypes

object PsiGdEnumUtil {

    fun fromString(data: String?): HashMap<String, Int> {
        val params = HashMap<String, Int>();
        if (data == null) {
            return params;
        }

        data.trim('{', '}').split(' ').forEach {
            val parts = it.split('=');
            if (parts.size == 2) {
                params[parts[0]] = parts[1].trim(',', ' ').toInt();
            }
        }

        return params;
    }

    fun values(enum: GdEnumDeclTl): HashMap<String, Int> {
        val stub = enum.stub
        if (stub != null) return stub.values();

        val values = HashMap<String, Int>();
        var i = 1;

        enum.enumValueList.forEach {
            // TODO zkontrolovat, co generuje Godot
            var value = i++;
            var child = it.lastChild;
            while (child != null) {
                if (child.elementType == GdTypes.NUMBER) {
                    value = child.text.toInt()
                    break;
                }
                child = child.prevSibling;
            }

            values[it.enumValueNmi.name] = value;
        }

        return values;
    }

}
