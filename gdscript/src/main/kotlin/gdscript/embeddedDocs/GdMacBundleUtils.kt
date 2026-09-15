package gdscript.embeddedDocs

import gdscript.utils.GdPathUtil
import org.jetbrains.annotations.ApiStatus
import java.nio.file.Files
import java.nio.file.Path

/**
 * Resolves the real executable inside a macOS `.app` or `.framework` bundle.
 *
 * An `Info.plist` file stores the metadata for a bundle. Its `CFBundleExecutable` value names the
 * real executable, which need not match the bundle's directory name. This lookup exists because the
 * basename guess failed on the real godot-cpp framework.
 */
@ApiStatus.Internal
object GdMacBundleUtils {
    const val MAX_INFO_PLIST_SIZE_BYTES: Long = 4L * 1024L * 1024L

    sealed interface BundleResolution {
        data class Found(val executable: Path) : BundleResolution
        data object InfoPlistUnreadable : BundleResolution
        data object InfoPlistTooLarge : BundleResolution
        data object ExecutableNameInvalid : BundleResolution
        data object ExecutableUnreachable : BundleResolution
        data object ExecutableDoesNotExist : BundleResolution
        data object ExecutableIsNotRegularFile : BundleResolution
    }

    /** Returns whether [path] names an `.xcframework`, which the plugin does not support. */
    fun isXcframework(path: Path): Boolean = path.fileName.toString().endsWith(".xcframework")

    /** Resolves the executable inside an `.app` bundle, at `Contents/MacOS/<CFBundleExecutable>`. */
    fun resolveAppExecutable(appPath: Path): BundleResolution =
        resolveBundleExecutable(appPath, infoPlistRelativePath = "Contents/Info.plist", executableRelativeDir = "Contents/MacOS")

    /** Resolves the executable in the framework root. The property list name takes priority over the folder name. */
    fun resolveFrameworkExecutable(frameworkPath: Path): BundleResolution {
        val fromPlist = resolveBundleExecutable(frameworkPath, infoPlistRelativePath = "Resources/Info.plist", executableRelativeDir = "")
        if (fromPlist is BundleResolution.Found) return fromPlist
        // A godot-cpp SCons build creates a framework with no property list and names the binary after the folder.
        // Godot loads such a framework from the same path, so the plugin also uses it.
        val name = frameworkPath.fileName?.toString()?.removeSuffix(".framework") ?: return fromPlist
        if (!GdPathUtil.isValidPath(name)) return fromPlist
        val executable = GdPathUtil.resolveContained(frameworkPath, name) ?: return fromPlist
        return if (Files.isRegularFile(executable)) BundleResolution.Found(executable) else fromPlist
    }

    private fun resolveBundleExecutable(
        bundlePath: Path,
        infoPlistRelativePath: String,
        executableRelativeDir: String,
    ): BundleResolution {
        val plist = GdPathUtil.resolveContained(bundlePath, infoPlistRelativePath)
            ?: return BundleResolution.InfoPlistUnreadable

        val executableName = when (val result = readBundleExecutable(plist)) {
            is ReadResult.Found -> result.value
            ReadResult.TooLarge -> return BundleResolution.InfoPlistTooLarge
            ReadResult.Unreadable -> return BundleResolution.InfoPlistUnreadable
        }
        if (!GdPathUtil.isValidPath(executableName)) return BundleResolution.ExecutableNameInvalid

        val executableRelativePath = if (executableRelativeDir.isEmpty()) executableName else "$executableRelativeDir/$executableName"
        val executable = GdPathUtil.resolveContained(bundlePath, executableRelativePath)
            ?: return BundleResolution.ExecutableUnreachable
        if (!Files.exists(executable)) return BundleResolution.ExecutableDoesNotExist
        if (!Files.isRegularFile(executable)) return BundleResolution.ExecutableIsNotRegularFile
        return BundleResolution.Found(executable)
    }

    private sealed interface ReadResult {
        data class Found(val value: String) : ReadResult
        data object TooLarge : ReadResult
        data object Unreadable : ReadResult
    }

    /** Reads `CFBundleExecutable` from an Apple property list, which names the real executable in a bundle. */
    private fun readBundleExecutable(plist: Path): ReadResult {
        if (!Files.isRegularFile(plist) || !Files.isReadable(plist)) return ReadResult.Unreadable
        return try {
            if (Files.size(plist) > MAX_INFO_PLIST_SIZE_BYTES) return ReadResult.TooLarge
            @Suppress("IO_FILE_USAGE")
            val document = newHardenedDocumentBuilderFactory().newDocumentBuilder().parse(plist.toFile())
            val keys = document.getElementsByTagName("key")
            for (index in 0 until keys.length) {
                if (keys.item(index).textContent == "CFBundleExecutable") {
                    var value = keys.item(index).nextSibling
                    while (value != null && value.nodeType != org.w3c.dom.Node.ELEMENT_NODE) value = value.nextSibling
                    if (value != null && value.nodeName == "string") return ReadResult.Found(value.textContent)
                }
            }
            ReadResult.Unreadable
        } catch (_: Exception) {
            ReadResult.Unreadable
        }
    }
}
