package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.codeInsight.documentation.render.DocRenderPassFactory
import com.intellij.psi.PsiDocCommentBase
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.codeInsight.GdDocumentationProvider
import gdscript.codeInsight.documentation.GdVirtualDocComment
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdProjectSettingsState
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.function.Consumer
import kotlin.io.path.pathString

/** Covers Reader Mode support for GDScript `##` doc comments (RIDER-117542). */
@RunWith(JUnit4::class)
class GdReaderModeTest : BasePlatformTestCase() {

    private val provider = GdDocumentationProvider()

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases").pathString
    }

    override fun setUp() {
        super.setUp()
        GdProjectSettingsState.getInstance(project).state.docProvider = GdDocProviderMode.GDSCRIPT
    }

    @Test
    fun testCollectDocCommentsMergesContiguousLinesIntoOneBlock() {
        val file = myFixture.configureByFile("documentation_comments.gd")

        val blocks = mutableListOf<PsiDocCommentBase>()
        provider.collectDocComments(file) { blocks.add(it) }

        // The `documented_func` doc block (10 lines) must come in as a single merged comment, not one per line.
        val funcBlock = blocks.filterIsInstance<GdVirtualDocComment>()
            .firstOrNull { it.comments.size > 1 && it.text.contains("This is a brief.") }
        assertNotNull(funcBlock)
        assertEquals(10, funcBlock!!.comments.size)
    }

    @Test
    fun testFindDocCommentReturnsTheSameBlockForAnyLineInside() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text

        val briefLineOffset = text.indexOf("This is a brief.")
        val descriptionLineOffset = text.indexOf("It has multiple lines.")
        assertTrue(briefLineOffset >= 0)
        assertTrue(descriptionLineOffset >= 0)

        val fromBrief = provider.findDocComment(file, com.intellij.openapi.util.TextRange(briefLineOffset, briefLineOffset + 1))
        val fromDescription = provider.findDocComment(file, com.intellij.openapi.util.TextRange(descriptionLineOffset, descriptionLineOffset + 1))

        assertNotNull(fromBrief)
        assertNotNull(fromDescription)
        assertEquals(fromBrief!!.textRange, fromDescription!!.textRange)
        assertEquals(10, (fromBrief as GdVirtualDocComment).comments.size)
    }

    @Test
    fun testGenerateRenderedDocRendersBriefAndDescription() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text
        val offset = text.indexOf("This is a brief.")

        val comment = provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1))
        assertNotNull(comment)

        val rendered = provider.generateRenderedDoc(comment!!)
        assertNotNull(rendered)
        assertTrue(rendered!!.contains("This is a brief."))
        assertTrue(rendered.contains("This is a description."))
    }

    @Test
    fun testReaderModeRenderingIsIndependentOfLspMode() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text
        val offset = text.indexOf("This is a brief.")
        val comment = provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1))
        assertNotNull(comment)

        // Reader Mode / gutter rendering is a separate presentation surface from the quick-doc popup and
        // must keep working even when `docProvider` is set to LSP (unlike `generateDoc`/`generateHoverDoc`).
        GdProjectSettingsState.getInstance(project).state.docProvider = GdDocProviderMode.LSP
        assertNotNull(provider.generateRenderedDoc(comment!!))
        assertNotNull(provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1)))

        val blocks = mutableListOf<PsiDocCommentBase>()
        provider.collectDocComments(file, Consumer { blocks.add(it) })
        assertFalse(blocks.isEmpty())
    }

    @Test
    fun testBlankLineBreaksDocCommentBlock() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text

        // "## This is a comment." is followed by a blank line, then a separate doc block for `static_func`.
        val offset = text.indexOf("## This is a comment.") + "## ".length
        val block = provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1))
        assertNotNull(block)
        assertEquals(1, (block as GdVirtualDocComment).comments.size)
    }

    /**
     * The gutter "Toggle Rendered View" icon (`ACTION_TOGGLE_RENDERED_DOC`) is populated by
     * [DocRenderPassFactory.calculateItemsToRender], which relies solely on [GdDocumentationProvider.collectDocComments]/
     * [GdDocumentationProvider.findDocComment] through the platform's generic `InlineDocumentationProvider` mechanism.
     * This test proves it is wired up for regular (writable) `.gd` files, unlike Reader Mode which additionally
     * requires the file to be read-only.
     */
    @Test
    fun testDocRenderItemsAreCollectedForGutterToggleAction() {
        myFixture.configureByFile("documentation_comments.gd")

        val items = DocRenderPassFactory.calculateItemsToRender(myFixture.editor, myFixture.file)
        assertFalse(items.isEmpty)
    }

    /**
     * Navigation of a `psi_element://` link rendered inside a Reader Mode doc comment must use the same
     * `context` production code passes: [GdVirtualDocComment.getOwner], not the identifier of the owner.
     */
    @Test
    fun testGetDocumentationElementForLinkResolvesBracketedReferenceFromVirtualCommentOwner() {
        val file = myFixture.configureByText(
            "linkTarget.gd",
            """
            class_name CTestClass

            ## See [CTestClass] for details.
            func documented_func():
            	pass
            """.trimIndent()
        )
        val text = file.text
        val offset = text.indexOf("See [CTestClass]")
        assertTrue(offset >= 0)

        val comment = provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1))
        assertNotNull(comment)
        val context = (comment as GdVirtualDocComment).owner
        assertNotNull("GdVirtualDocComment.getOwner() must return the declaration following the doc comment", context)

        val resolved = provider.getDocumentationElementForLink(myFixture.psiManager, "CTestClass", context)
        assertNotNull(
            "Navigating a [CTestClass] link rendered in Reader Mode must resolve to the class_name declaration",
            resolved
        )
    }

    /**
     * A bracketed reference can also name the type used by a member (var/param/const) of the current
     * class, e.g. an inner class without a global `class_name`. Such a reference is only resolvable via
     * [gdscript.psi.utils.GdClassUtil.getClassIdElement]'s non-deprecated overload, which additionally
     * looks up [gdscript.index.impl.GdClassDeclIndex.getInFile] scoped to the `context` file.
     */
    @Test
    fun testGetDocumentationElementForLinkResolvesMemberTypeFromVirtualCommentOwner() {
        val file = myFixture.configureByText(
            "memberType.gd",
            """
            class Inner:
            	var x = 1

            var field: Inner

            ## References the inner [Inner] type used above.
            func documented_func():
            	pass
            """.trimIndent()
        )
        val text = file.text
        val offset = text.indexOf("inner [Inner]")
        assertTrue(offset >= 0)

        val comment = provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1))
        assertNotNull(comment)
        val context = (comment as GdVirtualDocComment).owner
        assertNotNull("GdVirtualDocComment.getOwner() must return the declaration following the doc comment", context)

        val resolved = provider.getDocumentationElementForLink(myFixture.psiManager, "Inner", context)
        assertNotNull(
            "Navigating an [Inner] link rendered in Reader Mode must resolve to the inner class declaration",
            resolved
        )
    }

    @Test
    fun testGenerateRenderedDocKeepsCodeblocksFormattedAndDoesNotLinkifyCsharpAttributes() {
        val file = myFixture.configureByText(
            "validateProperty.gd",
            """
            ## See [method _get_property_list].
            ## [codeblocks]
            ## [gdscript]
            ## func _validate_property(property: Dictionary):
            ## 	pass
            ## [/gdscript]
            ## [csharp]
            ## [Tool]
            ## public partial class MyNode : Node { }
            ## [/csharp]
            ## [/codeblocks]
            func _validate_property(property: Dictionary) -> void:
            	pass
            """.trimIndent()
        )
        val offset = file.text.indexOf("[codeblocks]")
        assertTrue(offset >= 0)

        val comment = provider.findDocComment(file, com.intellij.openapi.util.TextRange(offset, offset + 1))
        assertNotNull(comment)

        val rendered = provider.generateRenderedDoc(comment!!)
        assertNotNull(rendered)
        assertTrue(rendered!!.contains("<pre><code>"))
        // Highlighted GDScript is split into spans, so only check token fragments.
        assertTrue(rendered.contains("_validate_property"))
        assertFalse(rendered.contains("<strong>C#</strong>"))
        assertFalse(rendered.contains("[Tool]"))
        assertFalse(rendered.contains("public partial class MyNode"))
        assertFalse(rendered.contains("psi_element://Tool"))
        assertFalse(rendered.contains("[method _get_property_list]"))
    }

}
