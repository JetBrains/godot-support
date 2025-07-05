package gdscript.utils

import gdscript.GdKeywords
import gdscript.psi.GdExpr

object GdExprUtil {

    fun List<GdExpr>.left(): String {
        return this.firstOrNull()?.returnType ?: GdKeywords.VARIANT
    }

    fun List<GdExpr>.right(): String {
        return this.getOrNull(1)?.returnType ?: GdKeywords.VARIANT
    }

}
