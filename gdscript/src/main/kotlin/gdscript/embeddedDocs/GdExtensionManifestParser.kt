package gdscript.embeddedDocs

import com.intellij.platform.eel.fs.EelFiles
import org.jetbrains.annotations.ApiStatus
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.LinkOption
import java.nio.file.Path

/**
 * Reads a GDExtension manifest and resolves its documentation-bearing library.
 *
 * A manifest can contain these sections:
 * ```
 * [configuration]
 * entry_symbol = "example_library_init"
 * compatibility_minimum = "4.2"
 *
 * [libraries]
 * linux.debug.x86_64 = "res://bin/libexample.so"
 * macos.debug = "res://bin/libexample.framework"
 * ```
 * A `[libraries]` key maps a feature expression to a binary resource path.
 * The parser selects the key with the most matching tags. It prefers a debug key when two keys have equal tag counts.
 *
 * An `Info.plist` file stores the metadata for a macOS framework bundle.
 * Its `CFBundleExecutable` value names the real executable, which need not match the framework directory name.
 * This lookup exists because the basename guess failed on the real godot-cpp framework.
 */
object GdExtensionManifestParser {
    const val MAX_MANIFEST_SIZE_BYTES = 1024L * 1024L
    const val MAX_INFO_PLIST_SIZE_BYTES = 4L * 1024L * 1024L
    private val windowsDrivePath = Regex("^[A-Za-z]:.*")

    /** Holds the feature tags that the current Godot target supports. */
    data class ActiveFeatureTags(
        val platformName: String,
        val architecture: String,
        val debug: Boolean,
    ) {
        private val names: Set<String> = buildSet {
            add(platformName)
            add(architecture)
            if (debug) {
                add("debug")
                add("template_debug")
            }
        }

        /** Returns whether Godot's feature check accepts the tag. */
        fun contains(tag: String): Boolean = tag in names
    }

    /** A library feature expression such as `linux.debug.x86_64`. */
    @JvmInline
    value class LibraryFeatureKey(val value: String)

    /** A manifest resource path such as `res://bin/libexample.so`. */
    @JvmInline
    value class LibraryResourcePath(val value: String)

    data class Manifest(
        val entrySymbol: String?,
        val compatibilityMinimum: String?,
        val libraries: Map<LibraryFeatureKey, LibraryResourcePath>,
    )

    sealed interface Resolution {
        data class Success(val manifest: Manifest, val binary: Path, val libraryKey: LibraryFeatureKey) : Resolution

        data class Failure(val reason: Reason, val manifest: Manifest) : Resolution {
            enum class Reason {
                NO_MANIFEST_FILE,
                MANIFEST_UNREADABLE_OR_MALFORMED,
                MANIFEST_TOO_LARGE,
                NO_LIBRARIES_SECTION,
                NO_KEY_MATCHED_CURRENT_PLATFORM,
                ONLY_RELEASE_KEY_MATCHED,
                RESOLVED_PATH_DOES_NOT_EXIST,
                RESOLVED_PATH_IS_NOT_REGULAR_FILE,
                INVALID_LIBRARY_PATH,
                PATH_ESCAPES_PROJECT_ROOT,
                FRAMEWORK_HAS_NO_READABLE_INFO_PLIST,
                INFO_PLIST_TOO_LARGE,
                XCFRAMEWORK_UNSUPPORTED,
            }
        }
    }

    fun parsePath(manifestPath: Path, projectRoot: Path, features: ActiveFeatureTags): Resolution {
        val emptyManifest = Manifest(null, null, emptyMap())
        if (!Files.exists(manifestPath)) {
            return Resolution.Failure(Resolution.Failure.Reason.NO_MANIFEST_FILE, emptyManifest)
        }

        val text = try {
            if (!Files.isRegularFile(manifestPath) || !Files.isReadable(manifestPath)) {
                return Resolution.Failure(Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED, emptyManifest)
            }
            if (Files.size(manifestPath) > MAX_MANIFEST_SIZE_BYTES) {
                return Resolution.Failure(Resolution.Failure.Reason.MANIFEST_TOO_LARGE, emptyManifest)
            }
            EelFiles.readString(manifestPath)
        }
        catch (_: Exception) {
            return Resolution.Failure(Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED, emptyManifest)
        }
        return parseText(text, projectRoot, features)
    }

    fun parseText(text: String, projectRoot: Path, features: ActiveFeatureTags): Resolution {
        val manifest = parseManifest(text)
            ?: return Resolution.Failure(
                Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED,
                Manifest(null, null, emptyMap()),
            )
        return resolve(manifest, projectRoot, features)
    }

    private fun resolve(manifest: Manifest, projectRoot: Path, features: ActiveFeatureTags): Resolution {
        if (manifest.libraries.isEmpty()) {
            return Resolution.Failure(Resolution.Failure.Reason.NO_LIBRARIES_SECTION, manifest)
        }

        var selected: Map.Entry<LibraryFeatureKey, LibraryResourcePath>? = null
        var selectedTagCount = -1
        var selectedHasDebug = false
        for (entry in manifest.libraries.entries) {
            val tags = tags(entry.key)
            if (!tags.all(features::contains)) continue

            val hasDebug = tags.any(::isDebugTag)
            if (tags.size > selectedTagCount || tags.size == selectedTagCount && hasDebug && !selectedHasDebug) {
                selected = entry
                selectedTagCount = tags.size
                selectedHasDebug = hasDebug
            }
        }

        val chosen = selected
        if (chosen == null) {
            val releaseMatched = manifest.libraries.keys.any { key ->
                val tags = tags(key)
                tags.any { it == "release" || it == "template_release" } &&
                    tags.filterNot { it == "release" || it == "template_release" }.all(features::contains)
            }
            val reason = if (releaseMatched) {
                Resolution.Failure.Reason.ONLY_RELEASE_KEY_MATCHED
            }
            else {
                Resolution.Failure.Reason.NO_KEY_MATCHED_CURRENT_PLATFORM
            }
            return Resolution.Failure(reason, manifest)
        }

        val resourcePath = chosen.value.value.removePrefix("res://")
        if (!isValidPath(resourcePath)) {
            return Resolution.Failure(Resolution.Failure.Reason.INVALID_LIBRARY_PATH, manifest)
        }
        val path = resolveContained(projectRoot, resourcePath)
            ?: return Resolution.Failure(Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, manifest)
        if (features.platformName == "macos" || features.platformName == "ios") {
            if (path.fileName.toString().endsWith(".xcframework")) {
                return Resolution.Failure(Resolution.Failure.Reason.XCFRAMEWORK_UNSUPPORTED, manifest)
            }
            if (Files.isDirectory(path)) {
                val plist = resolveContained(path, "Resources/Info.plist")
                    ?: return Resolution.Failure(Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, manifest)
                val executable = when (val result = readBundleExecutable(plist)) {
                    is BundleExecutableResult.Found -> result.value
                    BundleExecutableResult.TooLarge -> {
                        return Resolution.Failure(Resolution.Failure.Reason.INFO_PLIST_TOO_LARGE, manifest)
                    }
                    BundleExecutableResult.Unreadable -> {
                        return Resolution.Failure(Resolution.Failure.Reason.FRAMEWORK_HAS_NO_READABLE_INFO_PLIST, manifest)
                    }
                }
                if (!isValidPath(executable)) {
                    return Resolution.Failure(Resolution.Failure.Reason.INVALID_LIBRARY_PATH, manifest)
                }
                val binary = resolveContained(path, executable)
                    ?: return Resolution.Failure(Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, manifest)
                if (!Files.exists(binary)) {
                    return Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_DOES_NOT_EXIST, manifest)
                }
                if (!Files.isRegularFile(binary)) {
                    return Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_IS_NOT_REGULAR_FILE, manifest)
                }
                return Resolution.Success(manifest, binary, chosen.key)
            }
        }
        if (!Files.exists(path)) {
            return Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_DOES_NOT_EXIST, manifest)
        }
        if (!Files.isRegularFile(path)) {
            return Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_IS_NOT_REGULAR_FILE, manifest)
        }
        return Resolution.Success(manifest, path, chosen.key)
    }

    /**
     * Resolves [value] under [base] and returns null when the result escapes [base].
     *
     * The check also follows a symbolic link, so a link out of [base] returns null too.
     */
    @ApiStatus.Internal
    fun resolveContained(base: Path, value: String): Path? {
        if (isAbsolutePath(value)) return null

        val normalizedBase = base.toAbsolutePath().normalize()
        val candidate = normalizedBase.resolve(value).normalize()
        if (!candidate.startsWith(normalizedBase)) return null

        val realBase = try {
            normalizedBase.toRealPath()
        } catch (_: Exception) {
            return null
        }
        var existingAncestor: Path? = candidate
        while (existingAncestor != null && !Files.exists(existingAncestor, LinkOption.NOFOLLOW_LINKS)) {
            existingAncestor = existingAncestor.parent
        }
        val realAncestor = try {
            existingAncestor?.toRealPath()
        } catch (_: Exception) {
            return null
        }
        return candidate.takeIf { realAncestor != null && realAncestor.startsWith(realBase) }
    }

    @ApiStatus.Internal
    fun isValidPath(value: String): Boolean = try {
        Path.of(value)
        true
    } catch (_: InvalidPathException) {
        false
    }

    private fun isAbsolutePath(value: String): Boolean =
        Path.of(value).isAbsolute || value.startsWith("//") || value.startsWith("\\") || windowsDrivePath.matches(value)

    private fun isDebugTag(tag: String): Boolean = tag == "debug" || tag == "template_debug"

    private fun tags(key: LibraryFeatureKey): List<String> = key.value.split('.').map(String::trim)

    private fun parseManifest(text: String): Manifest? {
        val sections = LinkedHashMap<String, LinkedHashMap<String, String>>()
        var section: LinkedHashMap<String, String>? = null
        for (line in text.lineSequence()) {
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith(";") || trimmed.startsWith("#")) continue
            if (trimmed.startsWith('[') && trimmed.endsWith(']')) {
                val sectionName = trimmed.substring(1, trimmed.length - 1).trim()
                if (sectionName.isEmpty()) return null
                section = sections.getOrPut(sectionName) { LinkedHashMap() }
                continue
            }
            val separator = trimmed.indexOf('=')
            if (separator <= 0 || section == null) return null
            val key = trimmed.substring(0, separator).trim()
            val rawValue = trimmed.substring(separator + 1).trim()
            if (key.isEmpty() || rawValue.length == 1 && rawValue[0] == '"' || rawValue.startsWith('"') != rawValue.endsWith('"')) return null
            section[key] = unquote(rawValue)
        }
        val configuration = sections["configuration"]
        return Manifest(
            configuration?.get("entry_symbol"),
            configuration?.get("compatibility_minimum"),
            sections["libraries"]?.entries?.associate { (key, value) ->
                LibraryFeatureKey(key) to LibraryResourcePath(value)
            } ?: emptyMap()
        )
    }

    private fun unquote(value: String): String {
        if (value.length < 2 || value.first() != '"' || value.last() != '"') return value
        return value.substring(1, value.length - 1)
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
    }

    @ApiStatus.Internal
    sealed interface BundleExecutableResult {
        data class Found(val value: String) : BundleExecutableResult
        data object TooLarge : BundleExecutableResult
        data object Unreadable : BundleExecutableResult
    }

    /** Reads `CFBundleExecutable` from an Apple property list, which names the real executable in a bundle. */
    @ApiStatus.Internal
    fun readBundleExecutable(plist: Path): BundleExecutableResult {
        if (!Files.isRegularFile(plist) || !Files.isReadable(plist)) return BundleExecutableResult.Unreadable
        return try {
            if (Files.size(plist) > MAX_INFO_PLIST_SIZE_BYTES) return BundleExecutableResult.TooLarge
            val document = newHardenedDocumentBuilderFactory().newDocumentBuilder().parse(plist.toFile())
            val keys = document.getElementsByTagName("key")
            for (index in 0 until keys.length) {
                if (keys.item(index).textContent == "CFBundleExecutable") {
                    var value = keys.item(index).nextSibling
                    while (value != null && value.nodeType != org.w3c.dom.Node.ELEMENT_NODE) value = value.nextSibling
                    if (value != null && value.nodeName == "string") return BundleExecutableResult.Found(value.textContent)
                }
            }
            BundleExecutableResult.Unreadable
        } catch (_: Exception) {
            BundleExecutableResult.Unreadable
        }
    }
}
