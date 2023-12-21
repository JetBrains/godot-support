package gdscript.model

data class GdCommentModel(
    var brief: String = "",
    var description: String = "",
    var tutorials: MutableList<GdTutorial> = mutableListOf(),
    var isDeprecated: Boolean = false,
    var isExperimental: Boolean = false,
) {
}

data class GdTutorial(
    var name: String = "",
    var url: String = "",
)
