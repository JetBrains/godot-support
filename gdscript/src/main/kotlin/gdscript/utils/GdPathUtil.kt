package gdscript.utils

import org.jetbrains.annotations.ApiStatus
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.LinkOption
import java.nio.file.Path

@ApiStatus.Internal
object GdPathUtil {
    private val windowsDrivePath = Regex("^[A-Za-z]:.*")

    /**
     * Returns [candidate] when it resolves inside [root], even after following a symbolic link.
     * Returns null when it escapes, including when a link inside it points outside [root].
     */
    fun verifyContained(candidate: Path, root: Path): Path? {
        val normalizedRoot = root.toAbsolutePath().normalize()
        val normalizedCandidate = candidate.toAbsolutePath().normalize()
        if (!normalizedCandidate.startsWith(normalizedRoot)) return null

        val realRoot = try {
            normalizedRoot.toRealPath()
        } catch (_: Exception) {
            return null
        }
        var existingAncestor: Path? = normalizedCandidate
        while (existingAncestor != null && !Files.exists(existingAncestor, LinkOption.NOFOLLOW_LINKS)) {
            existingAncestor = existingAncestor.parent
        }
        val realAncestor = try {
            existingAncestor?.toRealPath()
        } catch (_: Exception) {
            return null
        }
        return normalizedCandidate.takeIf { realAncestor != null && realAncestor.startsWith(realRoot) }
    }

    /** Same as [verifyContained], but also resolves the path from [base] to the [value]. */
    fun resolveContained(base: Path, value: String): Path? {
        if (isAbsolutePath(value)) return null
        val normalizedBase = base.toAbsolutePath().normalize()
        return verifyContained(normalizedBase.resolve(value), normalizedBase)
    }

    /** Returns whether [value] parses as a path at all, regardless of where it points. */
    fun isValidPath(value: String): Boolean = try {
        Path.of(value)
        true
    } catch (_: InvalidPathException) {
        false
    }

    fun isAbsolutePath(value: String): Boolean =
        Path.of(value).isAbsolute || value.startsWith("//") || value.startsWith("\\") || windowsDrivePath.matches(value)
}
