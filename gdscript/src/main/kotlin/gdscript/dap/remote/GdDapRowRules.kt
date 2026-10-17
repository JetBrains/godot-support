package gdscript.dap.remote

/** The row names and roles supplied by Godot DAP. */
internal object GdDapRowRules {
    const val CATEGORY_TYPE: String = "Category"

    private val objectWrapper = Regex("<EncodedObjectAsID#-?\\d+>")

    fun isObjectWrapper(value: String): Boolean = objectWrapper.matches(value)

    /** Owners accept nonnegative numeric labels. Watches require the canonical index text. */
    fun arrayIndex(name: String, canonical: Boolean = false): Int? =
        name.toIntOrNull()?.takeIf { it >= 0 && (!canonical || it.toString() == name) }

    fun propertyName(category: String, label: String, watch: Boolean): String {
        if (category != "Members") return label
        val slash = label.indexOf('/')
        if (slash < 0) return label
        val prefix = label.substring(0, slash)
        return if (prefix.endsWith(".gd") || watch && (prefix.isEmpty() || "::" in prefix)) label.substring(slash + 1) else label
    }

    fun unambiguousNames(names: List<String?>): List<String?> {
        val ambiguous = names.filterNotNull().groupingBy { it }.eachCount().filterValues { it > 1 }.keys
        return names.map { it?.takeUnless { name -> name.isEmpty() || name in ambiguous } }
    }
}
