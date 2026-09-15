package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.openapi.application.PathManager
import gdscript.embeddedDocs.GdExtensionManifestParser
import gdscript.embeddedDocs.GdMacBundleUtils
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeNoException
import org.junit.Test
import org.junit.jupiter.api.fail
import java.nio.file.Files
import java.nio.file.Path

class GdExtensionManifestParserTest {
    private val linuxDebug = GdExtensionManifestParser.ActiveFeatureTags("linux", "x86_64", debug = true)
    private val temporaryRoots = mutableListOf<Path>()

    @After
    fun removeTemporaryRoots() {
        for (root in temporaryRoots.asReversed()) {
            Files.walk(root).use { paths ->
                paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
            }
        }
    }

    @Test
    fun longestMatchingKeyWins() {
        val root = tempRoot()
        Files.createFile(root.resolve("short.so"))
        Files.createFile(root.resolve("long.so"))
        val result = parse(
            root, """
      [configuration]
      entry_symbol="init"
      compatibility_minimum="4.1"
      [libraries]
      linux.debug="res://short.so"
      linux.debug.x86_64="res://long.so"
    """.trimIndent()
        )
        assertEquals(root.resolve("long.so"), result.assertSuccess().binary)
    }

    @Test
    fun debugKeyWinsEqualTagCount() {
        val root = tempRoot()
        Files.createFile(root.resolve("architecture.so"))
        val debugBinary = Files.createFile(root.resolve("debug.so"))
        val result = parse(
            root, """
      [libraries]
      linux.x86_64="res://architecture.so"
      linux.debug="res://debug.so"
    """.trimIndent()
        )
        assertEquals(debugBinary, result.assertSuccess().binary)
    }

    @Test
    fun keyWithoutArchitectureMatches() {
        val root = tempRoot()
        Files.createFile(root.resolve("lib.so"))
        val result = parse(root, "[libraries]\nlinux.debug=\"res://lib.so\"")
        result.assertSuccess()
    }

    @Test
    fun unsatisfiedFeatureRejectsKey() {
        val result = parse(tempRoot(), "[libraries]\nlinux.debug.threads=\"res://lib.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.NO_KEY_MATCHED_CURRENT_PLATFORM, result.assertFailure().reason)
    }

    @Test
    fun releaseOnlyReportsMissingDocumentation() {
        val result = parse(tempRoot(), "[libraries]\nlinux.template_release=\"res://lib.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.ONLY_RELEASE_KEY_MATCHED, result.assertFailure().reason)
    }

    @Test
    fun resolvesResourcePathAgainstProjectRoot() {
        val root = tempRoot()
        Files.createDirectories(root.resolve("bin"))
        val binary = Files.createFile(root.resolve("bin/lib.so"))
        val result = parse(root, "[libraries]\nlinux.debug=\"res://bin/lib.so\"")
        assertEquals(binary, result.assertSuccess().binary)
    }

    @Test
    fun rejectsParentPathEscape() {
        val root = tempRoot()
        Files.createFile(root.parent.resolve("outside-${root.fileName}.so"))
            .also { temporaryRoots.add(it) }
        val result = parse(root, "[libraries]\nlinux.debug=\"res://../outside-${root.fileName}.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, result.assertFailure().reason)
    }

    @Test
    fun rejectsSymlinkPathEscape() {
        val root = tempRoot()
        val outside = tempRoot()
        Files.createFile(outside.resolve("outside.so"))
        try {
            Files.createSymbolicLink(root.resolve("linked"), outside)
        } catch (exception: Exception) {
            assumeNoException(exception)
        }
        val result = parse(root, "[libraries]\nlinux.debug=\"res://linked/outside.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, result.assertFailure().reason)
    }

    // These strings have no `res://` prefix, so this also exercises the local-path (`./...`) branch.
    @Test
    fun rejectsPlatformIndependentAbsolutePathForms() {
        val root = tempRoot()
        for (path in listOf("/outside.so", "C:\\outside.dll", "\\\\server\\share\\outside.dll")) {
            val result = parse(root, "[libraries]\nlinux.debug=\"$path\"")
            assertEquals(path, GdExtensionManifestParser.Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, result.assertFailure().reason)
        }
    }

    // A local path (e.g. `./lib.so`) is resolved against the manifest file's own directory, not the project root, unlike a `res://` path
    @Test
    fun localPathResolvesRelativeToManifestDirectoryNotProjectRoot() {
        val root = tempRoot()
        val sub = Files.createDirectories(root.resolve("sub"))
        Files.createFile(root.resolve("lib.so")) // decoy: a `res://lib.so` reference would pick this file instead
        val binary = Files.createFile(sub.resolve("lib.so"))
        val manifestPath = sub.resolve("plugin.gdextension")
        val result = parse(root, "[libraries]\nlinux.debug=\"./lib.so\"", manifestPath = manifestPath)
        assertEquals(binary, result.assertSuccess().binary)
    }

    @Test
    fun localPathResolvesNestedSubdirectoryRelativeToManifest() {
        val root = tempRoot()
        val sub = Files.createDirectories(root.resolve("sub"))
        Files.createDirectories(sub.resolve("bin"))
        val binary = Files.createFile(sub.resolve("bin/lib.so"))
        val manifestPath = sub.resolve("plugin.gdextension")
        val result = parse(root, "[libraries]\nlinux.debug=\"./bin/lib.so\"", manifestPath = manifestPath)
        assertEquals(binary, result.assertSuccess().binary)
    }

    @Test
    fun localPathCanTraverseUpWithinProjectRoot() {
        val root = tempRoot()
        val sub = Files.createDirectories(root.resolve("sub"))
        val binary = Files.createFile(root.resolve("lib.so"))
        val manifestPath = sub.resolve("plugin.gdextension")
        val result = parse(root, "[libraries]\nlinux.debug=\"../lib.so\"", manifestPath = manifestPath)
        assertEquals(binary, result.assertSuccess().binary)
    }

    @Test
    fun localPathRejectsParentPathEscape() {
        val root = tempRoot()
        val sub = Files.createDirectories(root.resolve("sub"))
        Files.createFile(root.parent.resolve("outside-${root.fileName}.so"))
            .also { temporaryRoots.add(it) }
        val manifestPath = sub.resolve("plugin.gdextension")
        val result = parse(
            root, "[libraries]\nlinux.debug=\"../../outside-${root.fileName}.so\"",
            manifestPath = manifestPath,
        )
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, result.assertFailure().reason)
    }

    @Test
    fun localPathRejectsSymlinkPathEscape() {
        val root = tempRoot()
        val sub = Files.createDirectories(root.resolve("sub"))
        val outside = tempRoot()
        Files.createFile(outside.resolve("outside.so"))
        try {
            Files.createSymbolicLink(sub.resolve("linked"), outside)
        } catch (exception: Exception) {
            assumeNoException(exception)
        }
        val manifestPath = sub.resolve("plugin.gdextension")
        val result = parse(root, "[libraries]\nlinux.debug=\"./linked/outside.so\"", manifestPath = manifestPath)
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, result.assertFailure().reason)
    }

    @Test
    fun localPathInvalidPathIsReported() {
        val result = parse(tempRoot(), "[libraries]\nlinux.debug=\"./invalid\u0000path.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.INVALID_LIBRARY_PATH, result.assertFailure().reason)
    }

    // This test proves that a generated Info.plist selects an executable whose name differs from the framework name.
    @Test
    fun resolvesFrameworkUsingBundleExecutable() {
        val root = tempRoot()
        val framework = Files.createDirectories(root.resolve("lib.framework"))
        Files.createDirectories(framework.resolve("Resources"))
        Files.writeString(framework.resolve("Resources/Info.plist"), plist("actual-name"))
        val binary = Files.createFile(framework.resolve("actual-name"))
        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())
        assertTrue(result.toString(), result is GdExtensionManifestParser.Resolution.Success)
        assertEquals(binary, result.assertSuccess().binary)
    }

    // This test pins the real godot-cpp plist format, unlike the first test, which uses a generated plist.
    @Test
    fun resolvesFrameworkUsingGodotCppInfoPlist() {
        val root = tempRoot()
        val framework = Files.createDirectories(root.resolve("lib.framework"))
        val resources = Files.createDirectories(framework.resolve("Resources"))
        val fixture = testDataPath("gdscript/embeddedDocs/godot-cpp-Info.plist")
        Files.copy(fixture, resources.resolve("Info.plist"))
        val binary = Files.createFile(framework.resolve("libgdexample.template_debug"))

        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())

        assertTrue(result.toString(), result is GdExtensionManifestParser.Resolution.Success)
        assertEquals(binary, result.assertSuccess().binary)
    }

    @Test
    fun resolvesFrameworkWithoutInfoPlist() {
        val root = tempRoot()
        val framework = Files.createDirectories(root.resolve("lib.framework"))
        val binary = Files.createFile(framework.resolve("lib"))

        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())

        assertEquals(binary, result.assertSuccess().binary)
    }

    @Test
    fun oversizedInfoPlistIsReportedBeforeRead() {
        val root = tempRoot()
        val framework = Files.createDirectories(root.resolve("lib.framework"))
        val resources = Files.createDirectories(framework.resolve("Resources"))
        Files.write(
            resources.resolve("Info.plist"),
            ByteArray(GdMacBundleUtils.MAX_INFO_PLIST_SIZE_BYTES.toInt() + 1),
        )

        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())

        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.INFO_PLIST_TOO_LARGE, result.assertFailure().reason)
    }

    @Test
    fun rejectsFrameworkExecutableEscape() {
        val root = tempRoot()
        val framework = Files.createDirectories(root.resolve("lib.framework"))
        Files.createDirectories(framework.resolve("Resources"))
        Files.writeString(framework.resolve("Resources/Info.plist"), plist("../outside"))
        Files.createFile(root.resolve("outside"))
        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.PATH_ESCAPES_PROJECT_ROOT, result.assertFailure().reason)
    }

    @Test
    fun systemEntityDoesNotReadLocalFile() {
        val root = tempRoot()
        val framework = Files.createDirectories(root.resolve("lib.framework"))
        Files.createDirectories(framework.resolve("Resources"))
        val secret = Files.writeString(root.resolve("secret.txt"), "actual-name")
        Files.createFile(framework.resolve("actual-name"))
        Files.writeString(
            framework.resolve("Resources/Info.plist"), """
      <?xml version="1.0"?>
      <!DOCTYPE plist [<!ENTITY leaked SYSTEM "${secret.toUri()}">]>
      <plist><dict><key>CFBundleExecutable</key><string>&leaked;</string></dict></plist>
    """.trimIndent()
        )

        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())
        assertEquals(
            GdExtensionManifestParser.Resolution.Failure.Reason.RESOLVED_PATH_IS_NOT_REGULAR_FILE,
            result.assertFailure().reason,
        )
    }

    @Test
    fun missingFrameworkPlistIsReported() {
        val root = tempRoot()
        Files.createDirectories(root.resolve("lib.framework"))
        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.framework\"", macosFeatures())
        assertEquals(
            GdExtensionManifestParser.Resolution.Failure.Reason.FRAMEWORK_HAS_NO_READABLE_INFO_PLIST,
            result.assertFailure().reason,
        )
    }

    // An .xcframework contains platform and architecture variants. One plist cannot select a single executable, so version 1 rejects it.
    @Test
    fun xcframeworkIsUnsupported() {
        val root = tempRoot()
        Files.createDirectories(root.resolve("lib.xcframework"))
        val result = parse(root, "[libraries]\nmacos.debug=\"res://lib.xcframework\"", macosFeatures())
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.XCFRAMEWORK_UNSUPPORTED, result.assertFailure().reason)
    }

    @Test
    fun missingManifestIsReported() {
        val root = tempRoot()
        val result = GdExtensionManifestParser.parsePath(root.resolve("missing.gdextension"), root, linuxDebug)
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.NO_MANIFEST_FILE, result.assertFailure().reason)
    }

    @Test
    fun malformedManifestIsReported() {
        val root = tempRoot()
        val manifest = Files.writeString(root.resolve("broken.gdextension"), "[libraries\nlinux.debug=\"res://lib.so\"")
        val result = GdExtensionManifestParser.parsePath(manifest, root, linuxDebug)
        assertEquals(
            GdExtensionManifestParser.Resolution.Failure.Reason.MANIFEST_UNREADABLE_OR_MALFORMED,
            result.assertFailure().reason,
        )
    }

    @Test
    fun oversizedManifestIsReportedBeforeRead() {
        val root = tempRoot()
        val manifest = Files.write(
            root.resolve("large.gdextension"),
            ByteArray(GdExtensionManifestParser.MAX_MANIFEST_SIZE_BYTES.toInt() + 1),
        )
        val result = GdExtensionManifestParser.parsePath(manifest, root, linuxDebug)
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.MANIFEST_TOO_LARGE, result.assertFailure().reason)
    }

    @Test
    fun noLibrariesSectionIsReported() {
        val result = parse(tempRoot(), "[configuration]\nentry_symbol=\"init\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.NO_LIBRARIES_SECTION, result.assertFailure().reason)
    }

    @Test
    fun missingResolvedPathIsReported() {
        val result = parse(tempRoot(), "[libraries]\nlinux.debug=\"res://missing.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.RESOLVED_PATH_DOES_NOT_EXIST, result.assertFailure().reason)
    }

    @Test
    fun invalidLibraryPathIsReported() {
        val result = parse(tempRoot(), "[libraries]\nlinux.debug=\"res://invalid\u0000path.so\"")
        assertEquals(GdExtensionManifestParser.Resolution.Failure.Reason.INVALID_LIBRARY_PATH, result.assertFailure().reason)
    }

    @Test
    fun projectRootIsNotAResolvedLibrary() {
        val result = parse(tempRoot(), "[libraries]\nlinux.debug=\"res://\"")
        assertEquals(
            GdExtensionManifestParser.Resolution.Failure.Reason.RESOLVED_PATH_IS_NOT_REGULAR_FILE,
            result.assertFailure().reason,
        )
    }

    private fun GdExtensionManifestParser.Resolution.assertSuccess(): GdExtensionManifestParser.Resolution.Success =
        (this as? GdExtensionManifestParser.Resolution.Success) ?: fail("Expected $this to be a Success!")

    private fun GdExtensionManifestParser.Resolution.assertFailure(): GdExtensionManifestParser.Resolution.Failure =
        (this as? GdExtensionManifestParser.Resolution.Failure) ?: fail("Expected $this to be a Failure!")

    private fun tempRoot(): Path = Files.createTempDirectory("gdextension").also(temporaryRoots::add)

    private fun testDataPath(relativePath: String): Path {
        val home = PathManager.getHomeDirFor(javaClass)
        if (home != null) return home.resolve("dotnet/Plugins/godot-support/gdscript/src/test/testData").resolve(relativePath)
        return PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test/testData").resolve(relativePath)
    }

    // manifestPath does not need to exist on disk: parseText never reads the manifest file itself, it only
    // reads manifestPath.parent to resolve a local (non-`res://`) library path.
    private fun parse(
        root: Path,
        text: String,
        features: GdExtensionManifestParser.ActiveFeatureTags = linuxDebug,
        manifestPath: Path = root.resolve("plugin.gdextension"),
    ) = GdExtensionManifestParser.parseText(text, manifestPath, root, features)

    private fun macosFeatures() = GdExtensionManifestParser.ActiveFeatureTags("macos", "x86_64", debug = true)

    private fun plist(executable: String) = """
    <?xml version="1.0" encoding="UTF-8"?>
    <plist><dict><key>CFBundleExecutable</key><string>$executable</string></dict></plist>
  """.trimIndent()
}
