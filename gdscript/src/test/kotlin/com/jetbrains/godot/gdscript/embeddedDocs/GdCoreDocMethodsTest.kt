package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.openapi.util.Version
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.embeddedDocs.GdCoreDocMethods
import gdscript.embeddedDocs.GdZlibEmbeddedDocs
import gdscript.embeddedDocs.GdZstdEmbeddedDocs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Documentation"])
class GdCoreDocMethodsTest {
    @Test
    fun testVersionOrdersBinaryMethods() {
        // Prefer the packing method for the SDK version.
        assertEquals(
            listOf(GdZstdEmbeddedDocs, GdZlibEmbeddedDocs),
            GdCoreDocMethods.toTry(Version(4, 8, 0)),
        )
        assertEquals(
            listOf(GdZlibEmbeddedDocs, GdZstdEmbeddedDocs),
            GdCoreDocMethods.toTry(Version(4, 5, 0)),
        )
    }
}
