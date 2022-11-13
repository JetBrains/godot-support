package gdscript.model

data class BoolVal(var value: Boolean) {
    companion object {
        fun new(): BoolVal {
            return BoolVal(false)
        }
    }
}
