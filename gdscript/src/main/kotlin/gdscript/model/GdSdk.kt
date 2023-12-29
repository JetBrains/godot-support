package gdscript.model

import kotlinx.serialization.Serializable

// https://github.com/Kotlin/kotlinx.serialization/issues/993
@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
class GdSdk(val id: String, val name: String, val type: String, val path: String, val mode: String)
