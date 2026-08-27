package gdscript.library

import org.jetbrains.annotations.ApiStatus

/**
 * A singleton registered in the running Godot instance, as reported by `gdscript/scripts/dump_singletons.gd`.
 *
 * @param apiType the `ClassDB.APIType` of [className]: 0 = core, 1 = editor, 2 = extension, 3 = editor_extension,
 * -1 if the engine did not report it.
 */
@ApiStatus.Internal
data class GdSingletonInfo(val name: String, val className: String, val apiType: Int) {
    val isFromGdExtension: Boolean get() = apiType >= API_TYPE_EXTENSION

    companion object {
        const val API_TYPE_EXTENSION: Int = 2

        const val LINE_PREFIX: String = "SINGLETON|"

        /**
         * Parses a single `SINGLETON|<name>|<class>|<api>` line.
         *
         * Godot's stdout also carries a version banner and possible extension error spam, so anything that does not
         * match the expected shape is skipped instead of failing the whole dump.
         */
        fun parseLine(line: String): GdSingletonInfo? {
            val trimmed = line.trim()
            if (!trimmed.startsWith(LINE_PREFIX)) return null

            val parts = trimmed.removePrefix(LINE_PREFIX).split('|')
            if (parts.size < 3) return null

            val name = parts[0].trim()
            val className = parts[1].trim()
            if (name.isEmpty() || className.isEmpty()) return null

            return GdSingletonInfo(name, className, parts[2].trim().toIntOrNull() ?: -1)
        }
    }
}
