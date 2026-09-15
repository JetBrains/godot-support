package gdscript.embeddedDocs

import com.intellij.platform.eel.fs.EelFiles
import gdscript.utils.GdPathUtil
import java.nio.file.Files
import java.nio.file.Path

/**
 * Reads a GDExtension manifest and resolves its documentation-bearing library.
 *
 * A [Manifest] can contain these sections:
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
 * On macOS, a `[libraries]` entry can name a `.framework` bundle instead of a plain binary.
 * [GdMacBundleUtils] resolves the real executable inside it.
 */
object GdExtensionManifestParser {
    const val MAX_MANIFEST_SIZE_BYTES: Long = 1024L * 1024L

    /** Holds the feature tags that the current Godot target supports. */
    data class ActiveFeatureTags(
        val platformName: String,
        val architecture: String,
        val debug: Boolean,
    ) {
        private val names: Set<String> = buildSet {
            add(platformName)
            add(architecture)
            // TODO: Maybe other things possible?
            add("single")
            // The editor always runs with the "editor" tag active, together with "debug" (an editor build always
            // has debugging features): https://docs.godotengine.org/en/stable/tutorials/export/feature_tags.html.
            add("editor")
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

    /** A manifest resource path, either `res://bin/libexample.so` or `./libexample.so`. */
    @JvmInline
    value class LibraryResourcePath(val value: String)

    data class Manifest(
        val entrySymbol: String?,
        val compatibilityMinimum: String?,
        val libraries: Map<LibraryFeatureKey, LibraryResourcePath>,
        val path: Path,
    )

    sealed interface Resolution {
        data class Success(val manifest: Manifest, val binary: Path, val libraryKey: LibraryFeatureKey) : Resolution

        data class Failure(val reason: Reason) : Resolution {
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
        if (!Files.exists(manifestPath)) {
            return Resolution.Failure(Resolution.Failure.Reason.NO_MANIFEST_FILE)
        }

        val text = try {
            if (!Files.isRegularFile(manifestPath) || !Files.isReadable(manifestPath)) {
                return Resolution.Failure(Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED)
            }
            if (Files.size(manifestPath) > MAX_MANIFEST_SIZE_BYTES) {
                return Resolution.Failure(Resolution.Failure.Reason.MANIFEST_TOO_LARGE)
            }
            EelFiles.readString(manifestPath)
        } catch (_: Exception) {
            return Resolution.Failure(Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED)
        }
        return parseText(text, manifestPath, projectRoot, features)
    }

    fun parseText(text: String, manifestPath: Path, projectRoot: Path, features: ActiveFeatureTags): Resolution {
        val manifest = parseManifest(text, manifestPath)
            ?: return Resolution.Failure(
                Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED,
            )
        return resolve(manifest, projectRoot, features)
    }

    private fun parseManifest(text: String, manifestPath: Path): Manifest? {
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
            // A value must be either fully unquoted or a well-formed "" quoted string
            if (key.isEmpty() || rawValue.length == 1 && rawValue[0] == '"' || rawValue.startsWith('"') != rawValue.endsWith('"')) return null
            section[key] = unquote(rawValue)
        }
        val configuration = sections["configuration"]
        return Manifest(
            configuration?.get("entry_symbol"),
            configuration?.get("compatibility_minimum"),
            sections["libraries"]?.entries?.associate { (key, value) ->
                LibraryFeatureKey(key) to LibraryResourcePath(value)
            } ?: emptyMap(),
            manifestPath
        )
    }


    private fun resolve(manifest: Manifest, projectRoot: Path, features: ActiveFeatureTags): Resolution {
        if (manifest.libraries.isEmpty()) {
            return Resolution.Failure(Resolution.Failure.Reason.NO_LIBRARIES_SECTION)
        }

        // Pick the most specific option
        val candidates = manifest.libraries.entries.map { LibraryCandidate(it, tags(it.key)) }
        val chosen = candidates
            .filter { it.tags.all(features::contains) }
            .maxWithOrNull(
                compareBy(
                    // Prefer one with most tags, because it is more specific match
                    { it.tags.size },
                    // Then prefer editor (Photon has docs in editor, not debug)
                    { it.tags.any { tag -> tag == "editor" } },
                    // Then prefer debug (Rider addon has docs in debug)
                    { it.tags.any { tag -> tag == "debug" || tag == "template_debug" } },
                ),
            )
            ?.entry

        if (chosen == null) {
            // Nothing qualified above. Check whether a release-only key would otherwise have matched, so the
            // reported failure can distinguish "this build is a release build the editor cannot use" from a
            // platform/architecture mismatch.
            val releaseMatched = candidates.any { (_, tags) ->
                tags.any { it == "release" || it == "template_release" } &&
                    tags.filterNot { it == "release" || it == "template_release" }.all(features::contains)
            }
            val reason = if (releaseMatched) {
                Resolution.Failure.Reason.ONLY_RELEASE_KEY_MATCHED
            } else {
                Resolution.Failure.Reason.NO_KEY_MATCHED_CURRENT_PLATFORM
            }
            return Resolution.Failure(reason)
        }

        val resPath = chosen.value.value

        // A `res://` path is project-root-relative by definition, so it is built directly under the root.
        // A plain relative path is instead resolved against the manifest file's own directory and then must be
        // verified to still land inside the project root, to guard against a `..` or symlink escape.
        val path = if (resPath.startsWith(RESOURCE_PREFIX)) {
            val cleanResPath = resPath.removePrefix(RESOURCE_PREFIX)
            if (!GdPathUtil.isValidPath(cleanResPath)) {
                return Resolution.Failure(Resolution.Failure.Reason.INVALID_LIBRARY_PATH)
            }
            GdPathUtil.resolveContained(projectRoot, cleanResPath)
        } else {
            if (!GdPathUtil.isValidPath(resPath)) {
                return Resolution.Failure(Resolution.Failure.Reason.INVALID_LIBRARY_PATH)
            }
            // A path such as `C:\outside.dll` is not absolute by Java's rules on Linux or macOS, so reject it.
            if (GdPathUtil.isAbsolutePath(resPath)) {
                return Resolution.Failure(Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT)
            }
            GdPathUtil.verifyContained(manifest.path.parent.resolve(resPath), projectRoot)
        } ?: return Resolution.Failure(Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT)

        // On macOS/iOS the resolved path can name a `.framework` bundle (a directory) instead of a plain
        // binary. An `.xcframework` bundle covers several architectures and is not supported here. Otherwise,
        // look inside the bundle for the real executable named by its Info.plist.
        // CFBundleExecutable (`.framework`) is default in godot-cpp sample, not the `.dylib`
        if (features.platformName == "macos" || features.platformName == "ios") {
            if (GdMacBundleUtils.isXcframework(path)) {
                return Resolution.Failure(Resolution.Failure.Reason.XCFRAMEWORK_UNSUPPORTED)
            }
            if (Files.isDirectory(path)) {
                return when (val result = GdMacBundleUtils.resolveFrameworkExecutable(path)) {
                    is GdMacBundleUtils.BundleResolution.Found -> Resolution.Success(manifest, result.executable, chosen.key)
                    GdMacBundleUtils.BundleResolution.InfoPlistUnreadable ->
                        Resolution.Failure(Resolution.Failure.Reason.FRAMEWORK_HAS_NO_READABLE_INFO_PLIST)

                    GdMacBundleUtils.BundleResolution.InfoPlistTooLarge ->
                        Resolution.Failure(Resolution.Failure.Reason.INFO_PLIST_TOO_LARGE)

                    GdMacBundleUtils.BundleResolution.ExecutableNameInvalid ->
                        Resolution.Failure(Resolution.Failure.Reason.INVALID_LIBRARY_PATH)

                    GdMacBundleUtils.BundleResolution.ExecutableUnreachable ->
                        Resolution.Failure(Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT)

                    GdMacBundleUtils.BundleResolution.ExecutableDoesNotExist ->
                        Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_DOES_NOT_EXIST)

                    GdMacBundleUtils.BundleResolution.ExecutableIsNotRegularFile ->
                        Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_IS_NOT_REGULAR_FILE)
                }
            }
        }
        if (!Files.exists(path)) {
            return Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_DOES_NOT_EXIST)
        }
        if (!Files.isRegularFile(path)) {
            return Resolution.Failure(Resolution.Failure.Reason.RESOLVED_PATH_IS_NOT_REGULAR_FILE)
        }
        return Resolution.Success(manifest, path, chosen.key)
    }

    private data class LibraryCandidate(
        val entry: Map.Entry<LibraryFeatureKey, LibraryResourcePath>,
        val tags: List<String>,
    )

    private fun tags(key: LibraryFeatureKey): List<String> = key.value.split('.').map(String::trim)

    private fun unquote(value: String): String {
        if (value.length < 2 || value.first() != '"' || value.last() != '"') return value
        return value.substring(1, value.length - 1)
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
    }

}

private val RESOURCE_PREFIX = "res://"