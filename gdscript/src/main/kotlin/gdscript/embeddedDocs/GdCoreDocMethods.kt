package gdscript.embeddedDocs

import com.intellij.openapi.util.Version

internal object GdCoreDocMethods {
    /** Entries have to be descending, because `first` represents a lower bound. Null lower bound covers every earlier version. */
    private val methods: List<Pair<Version?, GdBinaryDocMethod>> = listOf(
        Version(4, 8, 0) to GdZstdEmbeddedDocs,
        null to GdZlibEmbeddedDocs,
    )

    /** Returns binary methods with the version match first. The order matters: the caller tries them in list order. */
    fun toTry(version: Version): List<GdBinaryDocMethod> {
        // Prefer the version match, then try the other binary method.
        val preferred = methods.first { [lowerBound, _] -> lowerBound == null || version >= lowerBound }.second
        val remaining = methods.map { it.second }.filter { it != preferred }
        return buildList {
            add(preferred)
            addAll(remaining)
        }
    }
}
