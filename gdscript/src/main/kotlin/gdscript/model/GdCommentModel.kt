package gdscript.model

import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import gdscript.psi.types.GdDocumented

data class GdCommentModel(
    var brief: String = "",
    var description: String = "",
    var tutorials: MutableList<GdTutorial> = mutableListOf(),
    var isDeprecated: Boolean = false,
    var isExperimental: Boolean = false,
) {

    constructor(dataStream: StubInputStream) : this(
        dataStream.readNameString() ?: "",
        dataStream.readNameString() ?: "",
        deserializeTutorials(dataStream.readNameString() ?: ""),
        dataStream.readBoolean(),
        dataStream.readBoolean(),
    )

    companion object {
        fun deserializeTutorials(hash: String): MutableList<GdTutorial> {
            return hash
                .split("\n")
                .mapNotNull {
                    val parts = it.split("_||_")
                    if (parts.size < 2) null
                    else GdTutorial(parts[0], parts[1])
                }
                .toMutableList()
        }

        fun serializeTutorials(tutorials: Collection<GdTutorial>): String {
            return tutorials
                .map { "${it.name}_||_${it.url}" }
                .joinToString { "\n" }
        }

        fun serializeDocumentation(element: GdDocumented, dataStream: StubOutputStream) {
            dataStream.writeName(element.brief())
            dataStream.writeName(element.description())
            dataStream.writeName(serializeTutorials(element.tutorials()))
            dataStream.writeBoolean(element.isDeprecated())
            dataStream.writeBoolean(element.isExperimental())
        }
    }

}

data class GdTutorial(
    var name: String = "",
    var url: String = "",
)
