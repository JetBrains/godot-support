package gdscript.library

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

object GdExtensionFile {

    private val LIB_LINE = Regex("""^\s*[\w.]+\s*=\s*"([^"]+)"\s*$""")

    fun referencedLibraries(gdextensionFile: Path): List<Path> {
        if (!gdextensionFile.exists()) return emptyList()
        val content = gdextensionFile.readText()
        var inLibraries = false
        return content.lineSequence().mapNotNull { raw ->
            val line = raw.trim()
            if (line.startsWith("[")) {
                inLibraries = line == "[libraries]"
                return@mapNotNull null
            }
            if (!inLibraries || line.isEmpty() || line.startsWith(";") || line.startsWith("#")) return@mapNotNull null
            LIB_LINE.matchEntire(line)?.groupValues?.get(1)
        }.map { rel ->
            val cleaned = rel.removePrefix("res://").removePrefix("./")
            gdextensionFile.parent.resolve(cleaned).normalize()
        }.toList()
    }
}
