package gdscript.psi.utils

object GdExprUtil {

    fun typesMatch(a: String, b: String): Boolean {
        if (a == b) return true;
        // Array checks
        if (a.startsWith("Array") && b.startsWith("Array")) {
            return a.length <= 5 || b.length <= 5;
        }

        return false;
    }

}
