package com.jetbrains.godot.gdscript.psi

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiCodeFragment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.util.configureGdFragment
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.godot.gdscript.util.gdContextAt
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdFile
import gdscript.psi.GdPsiCodeFragment
import gdscript.psi.GdTypes
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdCodeFragmentLifecycleTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript").pathString

    override fun setUp() {
        super.setUp()
        disableSingleItemAutoInsert(testRootDisposable)
    }

    private fun someContext(): PsiElement = myFixture.gdContextAt(
        "context.gd",
        """
        |var member := 1
        |
        |func _ready():
        |	<caret>pass
        """.trimMargin()
    )

    /** A reparse has to leave a tree a fresh parse would have produced, so these reuse [com.jetbrains.godot.gdscript.parser.GdCodeFragmentParserTest]'s gold. */
    private fun assertReparsedLikeAFreshParse(fragment: GdPsiCodeFragment, goldFileName: String) {
        assertSameLinesWithFile(
            "$testDataPath/parser/codeFragment/$goldFileName.txt",
            DebugUtil.psiToString(fragment, true)
        )
    }

    @Test
    fun testContextIsDroppedOnceItBecomesInvalid() {
        val context = someContext()
        val fragment = myFixture.gdFragment("member", context)

        assertSame(context, fragment.context)

        WriteCommandAction.runWriteCommandAction(project) { context.containingFile.delete() }

        assertFalse("Test setup is broken - the context element is still valid", context.isValid)
        assertNull("An invalidated context must not be handed out", fragment.context)
        assertNull("The dead element must not be handed out on a second call either", fragment.context)

        // The hot path that forced the raw-PsiElement design must stay usable, too.
        assertTrue(fragment.modificationStamp >= 0)
        assertEquals("member", fragment.text)
    }

    @Test
    fun testCompletionWithAnInvalidatedContextDoesNotThrow() {
        val context = someContext()
        myFixture.configureGdFragment("member<caret>", context)

        WriteCommandAction.runWriteCommandAction(project) { context.containingFile.delete() }
        myFixture.completeBasic()

        assertEquals("member", myFixture.file.text)
    }

    @Test
    fun testHighlightingWithAnInvalidatedContextDoesNotThrow() {
        val context = someContext()
        myFixture.configureGdFragment("member", context)

        WriteCommandAction.runWriteCommandAction(project) { context.containingFile.delete() }
        myFixture.doHighlighting()

        assertEquals("member", myFixture.file.text)
    }

    @Test
    fun testCompletionWithoutAContextDoesNotThrow() {
        myFixture.configureGdFragment("member<caret>")

        myFixture.completeBasic()

        assertEquals("member", myFixture.file.text)
    }

    /** Completion inserts its dummy identifier into the copy; leaking that into the evaluate window would be visible. */
    @Test
    fun testCompletionLeavesWhatTheUserTypedAlone() {
        myFixture.configureGdFragment("self.mem<caret>", someContext())

        myFixture.completeBasic()

        assertEquals("self.mem", myFixture.file.text)
    }

    /**
     * `CompletionInitializationUtil` copies the file before running completion on it, and the copy has to be just as
     * usable as the original - otherwise completion resolves nothing.
     */
    @Test
    fun testCopyKeepsFragmentIdentityAndContext() {
        val context = someContext()
        val fragment = myFixture.gdFragment("self.member", context)

        val copy = fragment.copy()

        val fragmentCopy = assertInstanceOf(copy, GdPsiCodeFragment::class.java)
        assertInstanceOf(copy, GdFile::class.java)
        assertInstanceOf(copy, PsiCodeFragment::class.java)
        assertSame("Copy lost the context element", context, fragmentCopy.context)
        assertSame("Copy lost the CODE_FRAGMENT content element type", GdTypes.CODE_FRAGMENT, fragmentCopy.contentElementType)
        assertFalse("Completion copies must not be physical", fragmentCopy.isPhysical)
        assertSame("Copy does not point back at the original", fragment, fragmentCopy.originalFile)
        assertEquals(fragment.text, fragmentCopy.text)
    }

    @Test
    fun testCopyOfAContextLessFragmentHasNoContext() {
        val copy = myFixture.gdFragment("self.member").copy() as GdPsiCodeFragment

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
        val fragment = myFixture.gdFragment("self.mem", context)
        val document = requireNotNull(fragment.viewProvider.document) { "physical fragment has no document" }

        WriteCommandAction.runWriteCommandAction(project) {
            document.insertString(document.textLength, "ber")
            PsiDocumentManager.getInstance(project).commitDocument(document)
        }

        assertEquals("self.member", fragment.text)
        assertTrue("Reparse replaced the fragment PSI", fragment.isValid)
        assertSame("Reparse lost the context element", context, fragment.context)
        assertSame(GdTypes.CODE_FRAGMENT, fragment.contentElementType)
        assertReparsedLikeAFreshParse(fragment, "selfAttribute")
    }

    @Test
    fun testDocumentCommitReparseStillReportsLeftoverInput() {
        val fragment = myFixture.gdFragment("a")
        val document = requireNotNull(fragment.viewProvider.document) { "physical fragment has no document" }

        WriteCommandAction.runWriteCommandAction(project) {
            document.setText("a\nb")
            PsiDocumentManager.getInstance(project).commitDocument(document)
        }

        assertEquals("a\nb", fragment.text)
        assertReparsedLikeAFreshParse(fragment, "secondStatementOnANewLine")
    }
}
