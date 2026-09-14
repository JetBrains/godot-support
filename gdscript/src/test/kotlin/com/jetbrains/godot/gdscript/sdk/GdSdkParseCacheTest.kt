package com.jetbrains.godot.gdscript.sdk

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.polySymbols.sdk.GdSdkParseCache
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests that the parse cache does not remember a read failure.
 *
 * The cache keys a parsed class by URL, and it resolves the URL again inside the cached closure.
 * A source that is unreadable now can be readable later, so the entry must go away at once.
 */
@RunWith(JUnit4::class)
class GdSdkParseCacheTest : BasePlatformTestCase() {

    private val classXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\" inherits=\"Object\"></class>"

    @Test
    fun testDoesNotRememberAReadFailure() {
        val cache = GdSdkParseCache.getInstance(project)

        // The URL of a file that exists for a moment. The fake file keeps the URL after the file is gone.
        val created = myFixture.addFileToProject("parseCache/Node.xml", classXml).virtualFile
        val url = created.url
        WriteCommandAction.runWriteCommandAction(project) { created.delete(this) }

        val reference = UrlOnlyFile(url, "Node.xml")

        // The URL resolves to nothing, so the parse reports a read failure.
        assertNull(cache.getOrParseClassData(reference))

        // The same URL resolves again. The cache must parse the file instead of serving the failure.
        myFixture.addFileToProject("parseCache/Node.xml", classXml)

        val parsed = cache.getOrParseClassData(reference)
        assertNotNull("The cache remembered the read failure.", parsed)
        assertEquals("Node", parsed!!.name)
    }

    @Test
    fun testParsesAFileThatTheUrlResolves() {
        val cache = GdSdkParseCache.getInstance(project)
        val file = myFixture.addFileToProject("parseCacheGood/Node.xml", classXml).virtualFile

        assertEquals("Node", cache.getOrParseClassData(file)?.name)
        assertEquals("The second call must return the same data.", "Node", cache.getOrParseClassData(file)?.name)
    }

    /** Carries a URL without a backing file. The cache reads only the validity and the URL of its argument. */
    private class UrlOnlyFile(private val fileUrl: String, name: String) : LightVirtualFile(name) {
        override fun getUrl(): String = fileUrl
    }
}
