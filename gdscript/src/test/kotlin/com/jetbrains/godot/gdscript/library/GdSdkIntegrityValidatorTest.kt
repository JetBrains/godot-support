package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.util.Version
import com.intellij.testFramework.rules.TempDirectory
import gdscript.embeddedDocs.GdDocFormat
import gdscript.library.GdSdkIntegrityValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import kotlin.io.path.writeText

/**
 * Tests the core stamp, which records the executable that produced the core documentation.
 *
 * The stamp must refuse a stamp from another executable, another format version, and the legacy
 * plain-version stamp that the shipped doctool path wrote.
 */
@RunWith(JUnit4::class)
class GdSdkIntegrityValidatorTest {
    @JvmField
    @Rule
    val tempDir: TempDirectory = TempDirectory()

    private val version: Version = Version.parseVersion("4.5")!!

    @Test
    fun testCoreStampRecordsTheExecutableSizeAndTime() {
        val binary = binary("some bytes")
        Files.setLastModifiedTime(binary, FileTime.fromMillis(1_700_000_000_000))

        val stamp = GdSdkIntegrityValidator.coreStamp(version, binary)

        assertNotNull(stamp)
        assertEquals(GdDocFormat.VERSION, stamp!!.formatVersion)
        assertEquals(version.toString(), stamp.godotVersion)
        assertEquals(10L, stamp.binarySize)
        assertEquals(1_700_000_000_000, stamp.binaryModifiedMillis)
    }

    @Test
    fun testCoreStampIsNullForAMissingExecutable() {
        val missing = tempDir.rootPath.resolve("missing.bin")

        assertNull(GdSdkIntegrityValidator.coreStamp(version, missing))
    }

    @Test
    fun testStampAcceptsOnlyItsOwnExecutable() {
        val first = binary("first bytes")
        val second = binary("second executable bytes")
        val stampFile = stampFile()

        val firstStamp = GdSdkIntegrityValidator.coreStamp(version, first)!!
        val secondStamp = GdSdkIntegrityValidator.coreStamp(version, second)!!
        assertNotEquals(firstStamp, secondStamp)

        assertFalse("An absent stamp file cannot be valid.", GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, firstStamp))

        GdSdkIntegrityValidator.writeCoreStamp(stampFile, firstStamp)
        assertTrue(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, firstStamp))
        assertFalse(
            "A stamp of another executable must force a rewrite.",
            GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, secondStamp),
        )
    }

    @Test
    fun testStampRefusesAnotherFormatVersion() {
        val binary = binary("bytes")
        val stampFile = stampFile()
        val stamp = GdSdkIntegrityValidator.coreStamp(version, binary)!!
        GdSdkIntegrityValidator.writeCoreStamp(stampFile, stamp)

        val newerFormat = stamp.copy(formatVersion = stamp.formatVersion + 1)

        assertFalse(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, newerFormat))
    }

    @Test
    fun testStampRefusesTheLegacyPlainVersionStamp() {
        val binary = binary("bytes")
        val stampFile = stampFile()
        // The shipped doctool path wrote the version string alone.
        stampFile.writeText(version.toString())

        val stamp = GdSdkIntegrityValidator.coreStamp(version, binary)!!

        assertFalse(
            "A legacy stamp must force one rewrite, because it names no executable.",
            GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp),
        )
        GdSdkIntegrityValidator.writeCoreStamp(stampFile, stamp)
        assertTrue(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp))
    }

    private fun binary(content: String): Path =
        tempDir.newDirectoryPath().resolve("godot.bin").also { it.writeText(content) }

    private fun stampFile(): Path = tempDir.newDirectoryPath().resolve("stamp-4.5.txt")
}
