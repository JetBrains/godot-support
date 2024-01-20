package gdscript.model
import kotlinx.serialization.Serializable

// https://github.com/Kotlin/kotlinx.serialization/issues/993
@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
class GdHistory(
    val commit: GdCommitHistory,
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
class GdCommitHistory(
    val authored_date: String,
    val committed_date: String,
)
