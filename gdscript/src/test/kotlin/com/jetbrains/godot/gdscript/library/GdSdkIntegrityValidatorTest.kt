package com.jetbrains.godot.gdscript.library

import com.intellij.openapi.util.Version
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.embeddedDocs.GdDocFormat
import gdscript.library.GdSdkIntegrityValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

/**
 * Tests the core stamp, which records the executable that produced the core documentation.
 *
 * The stamp must refuse a stamp from another executable, another format version, and the legacy
 * plain-version stamp that the shipped doctool path wrote.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
class GdSdkIntegrityValidatorTest {
    @TempDir
    lateinit var tempDir: Path

    private val version: Version = Version.parseVersion("4.5")!!

    private var directoryCounter = 0

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
        val missing = tempDir.resolve("missing.bin")

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

        assertFalse(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, firstStamp), "An absent stamp file cannot be valid.")

        GdSdkIntegrityValidator.writeCoreStamp(stampFile, firstStamp)
        assertTrue(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, firstStamp))
        assertFalse(
            GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, secondStamp),
            "A stamp of another executable must force a rewrite.",
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
            GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp),
            "A legacy stamp must force one rewrite, because it names no executable.",
        )
        GdSdkIntegrityValidator.writeCoreStamp(stampFile, stamp)
        assertTrue(GdSdkIntegrityValidator.hasValidCoreStamp(stampFile, stamp))
    }

    private fun binary(content: String): Path =
        newDirectory().resolve("godot.bin").also { it.writeText(content) }

    private fun stampFile(): Path = newDirectory().resolve("stamp-4.5.txt")

    private fun newDirectory(): Path =
        tempDir.resolve("dir-${directoryCounter++}").also { it.createDirectories() }
}
