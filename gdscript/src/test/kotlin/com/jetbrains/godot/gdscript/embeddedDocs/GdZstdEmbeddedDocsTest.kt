package com.jetbrains.godot.gdscript.embeddedDocs

import com.github.luben.zstd.Zstd
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.embeddedDocs.GdCoreDocPipeline
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Documentation"])
class GdZstdEmbeddedDocsTest {
    @Test
    fun testExtractsClassDescriptionsFromAnEmbeddedFrame() {
        // Put a complete class set across a scan boundary, with unrelated bytes on both sides.
        val names = listOf("Object", "Node", "RefCounted", "@GDScript")
        val xml = names.joinToString("\n") {
            """<?xml version="1.0"?><class name="$it"><description>Full $it description.</description></class>"""
        }
        val binary = Files.createTempFile("gd-zstd-docs", ".bin")
        try {
            // Reject the unrelated frame before it can exceed the 64 MiB stream cap.
            val unrelated = Zstd.compress(ByteArray(64 * 1024 * 1024 + 1))
            Files.write(binary, unrelated + ByteArray(65534 - unrelated.size) + Zstd.compress(xml.toByteArray()) + ByteArray(100))
            val outcome = GdCoreDocPipeline.build(binary) { }
            val ready = assertInstanceOf(GdCoreDocPipeline.Outcome.Ready::class.java, outcome, outcome.toString())

            assertEquals(names.map { "$it.xml" }.sorted(), ready.files.map { it.fileName })
            assertTrue(ready.files.all { String(it.bytes).contains("<description>Full ") })
            assertNull(ready.recoveryReport)
            assertTrue(ready.sweepStale)
        }
        finally {
            Files.delete(binary)
        }
    }
}
