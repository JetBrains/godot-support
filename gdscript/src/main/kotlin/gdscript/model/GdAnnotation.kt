package gdscript.model

data class GdAnnotation(val variadic: Boolean, val required: Int, val parameters: LinkedHashMap<String, String>) {
}