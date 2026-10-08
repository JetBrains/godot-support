package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.codeInsight.documentation.render.DocRenderPassFactory
import com.intellij.lang.documentation.impl.documentationTargets
import com.intellij.openapi.util.TextRange
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.codeInsight.documentation.GdDocLinkResolver
import gdscript.codeInsight.documentation.GdInlineDocumentation
import gdscript.codeInsight.documentation.GdInlineDocumentationProvider
import gdscript.codeInsight.documentation.findGdDocComment
import gdscript.codeInsight.documentation.renderGdDocComment
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdProjectSettingsState
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

/** Covers Reader Mode support for GDScript `##` doc comments (RIDER-117542). */
@RunWith(JUnit4::class)
class GdReaderModeTest : BasePlatformTestCase() {

    private val inlineProvider = GdInlineDocumentationProvider()

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

        val blocks = inlineProvider.inlineDocumentationItems(file)
            .filterIsInstance<GdInlineDocumentation>()
            .map { it.comment }

        // The `documented_func` doc block (10 lines) must come in as a single merged comment, not one per line.
        val funcBlock = blocks.firstOrNull { it.comments.size > 1 && it.text.contains("This is a brief.") }
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

        val fromBrief = findGdDocComment(file, TextRange(briefLineOffset, briefLineOffset + 1))
        val fromDescription = findGdDocComment(file, TextRange(descriptionLineOffset, descriptionLineOffset + 1))

        assertNotNull(fromBrief)
        assertNotNull(fromDescription)
        assertEquals(fromBrief!!.textRange, fromDescription!!.textRange)
        assertEquals(10, fromBrief.comments.size)
    }

    @Test
    fun testGenerateRenderedDocRendersBriefAndDescription() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text
        val offset = text.indexOf("This is a brief.")

        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)

        val rendered = renderGdDocComment(comment!!)
        assertTrue(rendered.contains("This is a brief."))
        assertTrue(rendered.contains("This is a description."))
    }

    /**
     * A malformed container type must not break the rendering, and a second bullet marker belongs to the text.
     * `[Array[]` once made the link builder read past the end of the type name.
     */
    @Test
    fun testGenerateRenderedDocWithMalformedTypesAndDoubleBullets() {
        val file = myFixture.configureByText(
            "malformedDoc.gd",
            "extends Node\n\n" +
                "## Bad types: [Array[], [Array[int], [Array[Array[int]]], [Dictionary[String, int]].\n" +
                "## Bullets:\n" +
                "## - * double marker\n" +
                "## * - double marker\n" +
                "func demo():\n\tpass\n",
        )
        val offset = file.text.indexOf("Bad types")
        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)

        val rendered = renderGdDocComment(comment!!)
        assertTrue(rendered, rendered.contains("psi_element://Array\""))
        assertTrue(rendered, rendered.contains("psi_element://int\""))
        assertTrue(rendered, rendered.contains("psi_element://Dictionary\""))
        assertTrue(rendered, rendered.contains("<li>* double marker</li>"))
        assertTrue(rendered, rendered.contains("<li>- double marker</li>"))
    }

    @Test
    fun testReaderModeRenderingIsIndependentOfLspMode() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text
        val offset = text.indexOf("This is a brief.")
        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)

        // Reader Mode / gutter rendering is a separate presentation surface from the quick-doc popup and
        // must keep working even when `docProvider` is set to LSP (unlike the documentation target).
        GdProjectSettingsState.getInstance(project).state.docProvider = GdDocProviderMode.LSP
        assertTrue(renderGdDocComment(comment!!).isNotEmpty())
        assertNotNull(findGdDocComment(file, TextRange(offset, offset + 1)))

        assertFalse(inlineProvider.inlineDocumentationItems(file).isEmpty())
    }

    @Test
    fun testBlankLineBreaksDocCommentBlock() {
        val file = myFixture.configureByFile("documentation_comments.gd")
        val text = file.text

        // "## This is a comment." is followed by a blank line, then a separate doc block for `static_func`.
        val offset = text.indexOf("## This is a comment.") + "## ".length
        val block = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(block)
        assertEquals(1, block!!.comments.size)
    }

    /**
     * The gutter "Toggle Rendered View" icon (`ACTION_TOGGLE_RENDERED_DOC`) is populated by
     * [DocRenderPassFactory.calculateItemsToRender], which relies solely on [GdInlineDocumentationProvider.inlineDocumentationItems].
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
     * `context` production code passes: [gdscript.codeInsight.documentation.GdVirtualDocComment.getOwner], not the identifier of the owner.
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

        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)
        val context = comment!!.owner
        assertNotNull("GdVirtualDocComment.getOwner() must return the declaration following the doc comment", context)

        val resolved = GdDocLinkResolver.resolve(myFixture.psiManager, "CTestClass", context!!)
        assertNotNull(
            "Navigating a [CTestClass] link rendered in Reader Mode must resolve to the class_name declaration",
            resolved
        )
    }

    @Test
    fun testReaderModeCommentProvidesOwnerTargetForLinkNavigation() {
        val file = myFixture.configureByText(
            "readerLinks.gd",
            """
            class_name ReaderLinkTarget

            ## See [ReaderLinkTarget] for details.
            func documented_func():
            \tpass
            """.trimIndent()
        )
        val offset = file.text.indexOf("See [ReaderLinkTarget]")
        assertTrue(offset >= 0)

        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)
        val inlineDocumentation = GdInlineDocumentation(comment!!)

        assertNotNull(inlineDocumentation.ownerTarget)
        assertTrue(renderGdDocComment(comment).isNotEmpty())
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

        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)
        val context = comment!!.owner
        assertNotNull("GdVirtualDocComment.getOwner() must return the declaration following the doc comment", context)

        val resolved = GdDocLinkResolver.resolve(myFixture.psiManager, "Inner", context!!)
        assertNotNull(
            "Navigating an [Inner] link rendered in Reader Mode must resolve to the inner class declaration",
            resolved
        )
    }

    /**
     * Rider resolves Reader Mode links through the documentation target at the first token after the comment.
     */
    @Test
    fun testDocumentationTargetAfterDocCommentIsTheDocumentedDeclaration() {
        val file = myFixture.configureByText(
            "fusion.gd",
            """
            ## Fusion manages the connection. See [FusionSpawner].
            class_name Fusion
            extends Node

            ## See [Fusion] for details.
            @warning_ignore("unused")
            func annotated_func():
            	pass

            ## See [Fusion] for details.
            func documented_func():
            	pass
            """.trimIndent()
        )
        val text = file.text

        assertNotEmpty(documentationTargets(file, text.indexOf("class_name")))
        assertNotEmpty(documentationTargets(file, text.indexOf("@warning_ignore")))
        assertNotEmpty(documentationTargets(file, text.indexOf("func documented_func")))
        assertEmpty(documentationTargets(file, text.indexOf("extends")))
    }

    @Test
    fun testBracketedReferenceInDocCommentResolves() {
        myFixture.addFileToProject("fusion_spawner.gd", "class_name FusionSpawner\nextends Node\n")
        myFixture.configureByText(
            "fusion.gd",
            """
            ## Coordinates all [Fusion<caret>Spawner] and [b]bold[/b] nodes, see [method documented_func].
            class_name Fusion
            extends Node

            func documented_func():
            	pass
            """.trimIndent()
        )

        val resolved = myFixture.getReferenceAtCaretPositionWithAssertion().resolve()
        assertNotNull(resolved)
        assertEquals("fusion_spawner.gd", resolved!!.containingFile.name)

        val references = myFixture.file.findElementAt(0)!!.references
        assertEquals(listOf("FusionSpawner", "documented_func"), references.map { it.canonicalText })
        assertNotNull(references[1].resolve())
    }

    @Test
    fun testParamReferenceInDocCommentResolvesToParameter() {
        val file = myFixture.configureByText(
            "settings.gd",
            """
            ## Returns the value of the setting identified by [param name].
            ## If [param default_value] is specified, it is returned. [param missing] is not a parameter.
            func get_setting(name: String, default_value: Variant) -> Variant:
            	pass
            """.trimIndent()
        )
        val text = file.text
        val secondLine = file.findElementAt(text.indexOf("If [param"))!!
        val references = secondLine.references
        assertEquals(listOf("default_value", "missing"), references.map { it.canonicalText })
        val resolved = references[0].resolve()
        assertNotNull(resolved)
        assertEquals(text.indexOf("default_value: Variant"), resolved!!.textRange.startOffset)
        assertNull(references[1].resolve())

        // Reader Mode resolves the rendered link with the function name as the context.
        val functionName = file.findElementAt(text.indexOf("get_setting("))!!
        val fromReaderMode = GdDocLinkResolver.resolve(myFixture.psiManager, "param:name", functionName)
        assertNotNull(fromReaderMode)
        assertEquals(text.indexOf("name: String"), fromReaderMode!!.textRange.startOffset)
    }

    @Test
    fun testGenerateRenderedDocKeepsCodeblocksFormatted() {
        val file = myFixture.configureByText(
            "validateProperty.gd",
            """
            ## See [method _get_property_list].
            ## [codeblocks]
            ## [gdscript]
            ## func _validate_property(property: Dictionary):
            ## 	pass
            ## [/gdscript]
            ## [/codeblocks]
            func _validate_property(property: Dictionary) -> void:
            	pass
            """.trimIndent()
        )
        val offset = file.text.indexOf("[codeblocks]")
        assertTrue(offset >= 0)

        val comment = findGdDocComment(file, TextRange(offset, offset + 1))
        assertNotNull(comment)

        val rendered = renderGdDocComment(comment!!)
        assertTrue(rendered.contains("<pre><code>"))
        // Highlighted GDScript is split into spans, so only check token fragments.
        assertTrue(rendered.contains("_validate_property"))
        assertFalse(rendered.contains("[method _get_property_list]"))
    }

}
