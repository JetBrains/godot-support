package gdscript.psi.utils

import com.intellij.psi.util.elementType
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdTypes

object PsiGdInheritanceUtil {

    fun isClassName(inheritance: GdInheritanceIdRef): Boolean {
        return inheritance.firstChild.elementType == GdTypes.IDENTIFIER
    }

}
