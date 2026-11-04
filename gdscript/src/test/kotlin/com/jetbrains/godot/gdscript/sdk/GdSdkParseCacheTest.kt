package com.jetbrains.godot.gdscript.sdk

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.polySymbols.sdk.GdSdkParseCache
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * Tests that the parse cache does not remember a read failure.
 *
 * The cache keys a parsed class by URL, and it resolves the URL again inside the cached closure.
 * A source that is unreadable now can be readable later, so the entry must go away at once.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
@TestApplication
class GdSdkParseCacheTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/highlighting")

    private val classXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\" inherits=\"Object\"></class>"

    @Test
    fun testDoesNotRememberAReadFailure() {
        val cache = GdSdkParseCache.getInstance(gd.project)

        // The URL of a file that exists for a moment. The fake file keeps the URL after the file is gone.
        val created = gd.fixture.addFileToProject("parseCache/Node.xml", classXml).virtualFile
        val url = created.url
        WriteCommandAction.runWriteCommandAction(gd.project) { created.delete(this) }

        val reference = UrlOnlyFile(url, "Node.xml")

        // The URL resolves to nothing, so the parse reports a read failure.
        assertNull(cache.getOrParseClassData(reference))

        // The same URL resolves again. The cache must parse the file instead of serving the failure.
        gd.fixture.addFileToProject("parseCache/Node.xml", classXml)

        val parsed = cache.getOrParseClassData(reference)
        assertNotNull(parsed, "The cache remembered the read failure.")
        assertEquals("Node", parsed!!.name)
    }

    @Test
    fun testParsesAFileThatTheUrlResolves() {
        val cache = GdSdkParseCache.getInstance(gd.project)
        val file = gd.fixture.addFileToProject("parseCacheGood/Node.xml", classXml).virtualFile

        assertEquals("Node", cache.getOrParseClassData(file)?.name)
        assertEquals("Node", cache.getOrParseClassData(file)?.name, "The second call must return the same data.")
    }

    /** Carries a URL without a backing file. The cache reads only the validity and the URL of its argument. */
    private class UrlOnlyFile(private val fileUrl: String, name: String) : LightVirtualFile(name) {
        override fun getUrl(): String = fileUrl
    }
}
