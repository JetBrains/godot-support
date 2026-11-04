package com.jetbrains.godot.gdscript.psi

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiCodeFragment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.godot.gdscript.getGdTestDataPath
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdFile
import gdscript.psi.GdPsiCodeFragment
import gdscript.psi.GdTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Debug/GDScript/Evaluation"])
@TestApplication
class GdCodeFragmentLifecycleTest {

    private val gd: GdFixture by gdLightFixture("gdscript")

    @BeforeEach
    fun disableAutoInsert() {
        disableSingleItemAutoInsert(gd.testRootDisposable)
    }

    private fun someContext(): PsiElement = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            gd.fixture.gdContextAt(
                "context.gd",
                """
        |var member := 1
        |
        |func _ready():
        |	<caret>pass
        """.trimMargin()
            )
        }
    }

    /** A reparse has to leave a tree a fresh parse would have produced, so these reuse [com.jetbrains.godot.gdscript.parser.GdCodeFragmentParserTest]'s gold. */
    private fun assertReparsedLikeAFreshParse(fragment: GdPsiCodeFragment, goldFileName: String) {
        PlatformTestUtil.assertSameLinesWithFile(
            "${getGdTestDataPath(GdCodeFragmentLifecycleTest::class.java)}/gdscript/parser/codeFragment/$goldFileName.txt",
            DebugUtil.psiToString(fragment, true)
        )
    }

    @Test
    fun testContextIsDroppedOnceItBecomesInvalid() {
        val context = someContext()
        val fragment = runReadActionBlocking { gd.fixture.gdFragment("member", context) }

        assertSame(context, runReadActionBlocking { fragment.context })

        WriteCommandAction.runWriteCommandAction(gd.project) { context.containingFile.delete() }

        assertFalse(runReadActionBlocking { context.isValid }, "Test setup is broken - the context element is still valid")
        assertNull(runReadActionBlocking { fragment.context }, "An invalidated context must not be handed out")
        assertNull(runReadActionBlocking { fragment.context }, "The dead element must not be handed out on a second call either")

        // The hot path that forced the raw-PsiElement design must stay usable, too.
        assertTrue(fragment.modificationStamp >= 0)
        assertEquals("member", fragment.text)
    }

    @Test
    fun testCompletionWithAnInvalidatedContextDoesNotThrow() {
        val context = someContext()
        gd.fixture.configureGdFragment("member<caret>", context)

        WriteCommandAction.runWriteCommandAction(gd.project) { context.containingFile.delete() }
        gd.fixture.completeBasic()

        assertEquals("member", gd.fixture.file.text)
    }

    @Test
    fun testHighlightingWithAnInvalidatedContextDoesNotThrow() {
        val context = someContext()
        gd.fixture.configureGdFragment("member", context)

        WriteCommandAction.runWriteCommandAction(gd.project) { context.containingFile.delete() }
        gd.fixture.doHighlighting()

        assertEquals("member", gd.fixture.file.text)
    }

    @Test
    fun testCompletionWithoutAContextDoesNotThrow() {
        gd.fixture.configureGdFragment("member<caret>")

        gd.fixture.completeBasic()

        assertEquals("member", gd.fixture.file.text)
    }

    /** Completion inserts its dummy identifier into the copy; leaking that into the evaluate window would be visible. */
    @Test
    fun testCompletionLeavesWhatTheUserTypedAlone() {
        gd.fixture.configureGdFragment("self.mem<caret>", someContext())

        gd.fixture.completeBasic()

        assertEquals("self.mem", gd.fixture.file.text)
    }

    /**
     * `CompletionInitializationUtil` copies the file before running completion on it, and the copy has to be just as
     * usable as the original - otherwise completion resolves nothing.
     */
    @Test
    fun testCopyKeepsFragmentIdentityAndContext() {
        val context = someContext()
        val fragment = runReadActionBlocking { gd.fixture.gdFragment("self.member", context) }

        val copy = runReadActionBlocking { fragment.copy() }

        val fragmentCopy = assertInstanceOf(GdPsiCodeFragment::class.java, copy)
        assertInstanceOf(GdFile::class.java, copy)
        runReadActionBlocking {
            assertInstanceOf(PsiCodeFragment::class.java, copy)
            assertSame(context, fragmentCopy.context, "Copy lost the context element")
            assertSame(GdTypes.CODE_FRAGMENT, fragmentCopy.contentElementType, "Copy lost the CODE_FRAGMENT content element type")
            assertFalse(fragmentCopy.isPhysical, "Completion copies must not be physical")
            assertSame(fragment, fragmentCopy.originalFile, "Copy does not point back at the original")
            assertEquals(fragment.text, fragmentCopy.text)
        }
    }

    @Test
    fun testCopyOfAContextLessFragmentHasNoContext() {
        val copy = runReadActionBlocking { gd.fixture.gdFragment("self.member").copy() as GdPsiCodeFragment }

        assertNull(copy.context)
    }

    /**
     * Regression test: committing an edited fragment document goes through the `PsiCodeFragment` branch of
     * `BlockSupportImpl.makeFullParse`, which re-parses a fresh chameleon parented to a `DummyHolder`. Asking
     * *that* chameleon for its PSI (instead of its tree parent's, the way `JavaParserUtil.parseFragment`
     * does) made `GdParserDefinition.createElement` throw
     * `AssertionError: Unknown element type: GdScriptCodeFragment`, the commit transaction aborted, and the
     * fragment was left with empty text and an empty tree.
     */
    @Test
    fun testDocumentCommitReparseKeepsTheFragmentIntact() {
        val context = someContext()
        val fragment = runReadActionBlocking { gd.fixture.gdFragment("self.mem", context) }
        val document = requireNotNull(runReadActionBlocking { fragment.viewProvider.document }) { "physical fragment has no document" }

        WriteCommandAction.runWriteCommandAction(gd.project) {
            document.insertString(document.textLength, "ber")
            PsiDocumentManager.getInstance(gd.project).commitDocument(document)
        }

        assertEquals("self.member", fragment.text)
        assertTrue(runReadActionBlocking { fragment.isValid }, "Reparse replaced the fragment PSI")
        assertSame(context, runReadActionBlocking { fragment.context }, "Reparse lost the context element")
        assertSame(GdTypes.CODE_FRAGMENT, runReadActionBlocking { fragment.contentElementType })
        assertReparsedLikeAFreshParse(fragment, "selfAttribute")
    }

    @Test
    fun testDocumentCommitReparseStillReportsLeftoverInput() {
        val fragment = runReadActionBlocking { gd.fixture.gdFragment("a") }
        val document = requireNotNull(runReadActionBlocking { fragment.viewProvider.document }) { "physical fragment has no document" }

        WriteCommandAction.runWriteCommandAction(gd.project) {
            document.setText("a\nb")
            PsiDocumentManager.getInstance(gd.project).commitDocument(document)
        }

        assertEquals("a\nb", fragment.text)
        assertReparsedLikeAFreshParse(fragment, "secondStatementOnANewLine")
    }
}
