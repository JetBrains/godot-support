package com.jetbrains.godot.gdscript.embeddedDocs

import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path

internal object GodotBinaryFromEnvironment {
  sealed interface Outcome {
    data object NotConfigured : Outcome
    data class Invalid(val message: String) : Outcome
    data class Ready(val binary: Path) : Outcome
  }

  fun resolve(
    rawValue: String?,
    isRegularFile: (Path) -> Boolean = { Files.isRegularFile(it) },
  ): Outcome {
    if (rawValue.isNullOrBlank()) return Outcome.NotConfigured

    val binary = try {
      Path.of(rawValue)
    }
    catch (_: InvalidPathException) {
      return Outcome.Invalid("GODOT_BINARY_PATH has an invalid path value: '$rawValue'.")
    }

    if (!isRegularFile(binary)) {
      return Outcome.Invalid("GODOT_BINARY_PATH must identify a regular file: '$rawValue'.")
    }

    return Outcome.Ready(binary)
  }
}
