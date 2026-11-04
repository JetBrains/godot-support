package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.codeInsight.documentation.render.DocRenderPassFactory
import com.intellij.lang.documentation.impl.documentationTargets
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.openapi.util.TextRange
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.codeInsight.documentation.GdDocLinkResolver
import gdscript.codeInsight.documentation.GdInlineDocumentation
import gdscript.codeInsight.documentation.GdInlineDocumentationProvider
import gdscript.codeInsight.documentation.findGdDocComment
import gdscript.codeInsight.documentation.renderGdDocComment
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdProjectSettingsState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** Covers Reader Mode support for GDScript `##` doc comments (RIDER-117542). */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Reader mode"])
@TestApplication
class GdReaderModeTest {

    private val gd: GdFixture by gdLightFixture("gdscript/parser/godotTestCases")

    private val inlineProvider = GdInlineDocumentationProvider()

    @BeforeEach
    fun setUpDocProvider() {
        GdProjectSettingsState.getInstance(gd.project).state.docProvider = GdDocProviderMode.GDSCRIPT
    }

    @Test
    fun testCollectDocCommentsMergesContiguousLinesIntoOneBlock() {
        val file = gd.fixture.configureByFile("documentation_comments.gd")

        val blocks = runReadActionBlocking {
            inlineProvider.inlineDocumentationItems(file)
                .filterIsInstance<GdInlineDocumentation>()
                .map { it.comment }
        }

        // The `documented_func` doc block (10 lines) must come in as a single merged comment, not one per line.
        val funcBlock = blocks.firstOrNull { it.comments.size > 1 && it.text.contains("This is a brief.") }
        assertNotNull(funcBlock)
        assertEquals(10, funcBlock!!.comments.size)
    }

    @Test
    fun testFindDocCommentReturnsTheSameBlockForAnyLineInside() {
        val file = gd.fixture.configureByFile("documentation_comments.gd")
        val text = file.text

        val briefLineOffset = text.indexOf("This is a brief.")
        val descriptionLineOffset = text.indexOf("It has multiple lines.")
        assertTrue(briefLineOffset >= 0)
        assertTrue(descriptionLineOffset >= 0)

        val fromBrief = runReadActionBlocking { findGdDocComment(file, TextRange(briefLineOffset, briefLineOffset + 1)) }
        val fromDescription = runReadActionBlocking { findGdDocComment(file, TextRange(descriptionLineOffset, descriptionLineOffset + 1)) }

        assertNotNull(fromBrief)
        assertNotNull(fromDescription)
        assertEquals(fromBrief!!.textRange, fromDescription!!.textRange)
        assertEquals(10, fromBrief.comments.size)
    }

    @Test
    fun testGenerateRenderedDocRendersBriefAndDescription() {
        val file = gd.fixture.configureByFile("documentation_comments.gd")
        val text = file.text
        val offset = text.indexOf("This is a brief.")

        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)

        val rendered = runReadActionBlocking { renderGdDocComment(comment!!) }
        assertTrue(rendered.contains("This is a brief."))
        assertTrue(rendered.contains("This is a description."))
    }

    /**
     * A malformed container type must not break the rendering, and a second bullet marker belongs to the text.
     * `[Array[]` once made the link builder read past the end of the type name.
     * This test has no SDK, so no type resolves and the renderer shows each type as plain text.
     */
    @Test
    fun testGenerateRenderedDocWithMalformedTypesAndDoubleBullets() {
        val file = gd.fixture.configureByText(
            "malformedDoc.gd",
            "extends Node\n\n" +
                "## Bad types: [Array[], [Array[int], [Array[Array[int]]], [Dictionary[String, int]].\n" +
                "## Bullets:\n" +
                "## - * double marker\n" +
                "## * - double marker\n" +
                "func demo():\n\tpass\n",
        )
        val offset = file.text.indexOf("Bad types")
        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)

        val rendered = runReadActionBlocking { renderGdDocComment(comment!!) }
        assertFalse(rendered.contains("psi_element://"))
        assertFalse(rendered.contains("<span"), rendered)
        assertTrue(rendered.contains("Array[int"), rendered)
        assertTrue(rendered.contains("Array[Array[int"), rendered)
        assertTrue(rendered.contains("Dictionary[String, int"), rendered)
        assertTrue(rendered.contains("<li>* double marker</li>"), rendered)
        assertTrue(rendered.contains("<li>- double marker</li>"), rendered)
    }

    @Test
    fun testReaderModeRenderingIsIndependentOfLspMode() {
        val file = gd.fixture.configureByFile("documentation_comments.gd")
        val text = file.text
        val offset = text.indexOf("This is a brief.")
        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)

        // Reader Mode / gutter rendering is a separate presentation surface from the quick-doc popup and
        // must keep working even when `docProvider` is set to LSP (unlike the documentation target).
        GdProjectSettingsState.getInstance(gd.project).state.docProvider = GdDocProviderMode.LSP
        assertTrue(runReadActionBlocking { renderGdDocComment(comment!!).isNotEmpty() })
        assertNotNull(runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) })

        assertFalse(runReadActionBlocking { inlineProvider.inlineDocumentationItems(file).isEmpty() })
    }

    @Test
    fun testBlankLineBreaksDocCommentBlock() {
        val file = gd.fixture.configureByFile("documentation_comments.gd")
        val text = file.text

        // "## This is a comment." is followed by a blank line, then a separate doc block for `static_func`.
        val offset = text.indexOf("## This is a comment.") + "## ".length
        val block = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
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
        gd.fixture.configureByFile("documentation_comments.gd")

        val items = runReadActionBlocking { DocRenderPassFactory.calculateItemsToRender(gd.fixture.editor, gd.fixture.file) }
        assertFalse(items.isEmpty)
    }

    /**
     * Navigation of a `psi_element://` link rendered inside a Reader Mode doc comment must use the same
     * `context` production code passes: [gdscript.codeInsight.documentation.GdVirtualDocComment.getOwner], not the identifier of the owner.
     */
    @Test
    fun testGetDocumentationElementForLinkResolvesBracketedReferenceFromVirtualCommentOwner() {
        val file = gd.fixture.configureByText(
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

        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)
        val context = comment!!.owner
        assertNotNull(context, "GdVirtualDocComment.getOwner() must return the declaration following the doc comment")

        val resolved = runReadActionBlocking { GdDocLinkResolver.resolve(gd.fixture.psiManager, "CTestClass", context!!) }
        assertNotNull(
            resolved,
            "Navigating a [CTestClass] link rendered in Reader Mode must resolve to the class_name declaration"
        )
    }

    @Test
    fun testReaderModeCommentProvidesOwnerTargetForLinkNavigation() {
        val file = gd.fixture.configureByText(
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

        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)
        val inlineDocumentation = GdInlineDocumentation(comment!!)

        assertNotNull(inlineDocumentation.ownerTarget)
        assertTrue(runReadActionBlocking { renderGdDocComment(comment).isNotEmpty() })
    }

    /**
     * A bracketed reference can also name the type used by a member (var/param/const) of the current
     * class, e.g. an inner class without a global `class_name`. Such a reference is only resolvable via
     * [gdscript.psi.utils.GdClassUtil.getClassIdElement]'s non-deprecated overload, which additionally
     * looks up [gdscript.index.impl.GdClassDeclIndex.getInFile] scoped to the `context` file.
     */
    @Test
    fun testGetDocumentationElementForLinkResolvesMemberTypeFromVirtualCommentOwner() {
        val file = gd.fixture.configureByText(
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

        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)
        val context = comment!!.owner
        assertNotNull(context, "GdVirtualDocComment.getOwner() must return the declaration following the doc comment")

        val resolved = runReadActionBlocking { GdDocLinkResolver.resolve(gd.fixture.psiManager, "Inner", context!!) }
        assertNotNull(
            resolved,
            "Navigating an [Inner] link rendered in Reader Mode must resolve to the inner class declaration"
        )
    }

    /**
     * Rider resolves Reader Mode links through the documentation target at the first token after the comment.
     */
    @Test
    fun testDocumentationTargetAfterDocCommentIsTheDocumentedDeclaration() {
        val file = gd.fixture.configureByText(
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

        runReadActionBlocking {
            assertTrue(documentationTargets(file, text.indexOf("class_name")).isNotEmpty())
            assertTrue(documentationTargets(file, text.indexOf("@warning_ignore")).isNotEmpty())
            assertTrue(documentationTargets(file, text.indexOf("func documented_func")).isNotEmpty())
            assertTrue(documentationTargets(file, text.indexOf("extends")).isEmpty())
        }
    }

    @Test
    fun testBracketedReferenceInDocCommentResolves() {
        gd.fixture.addFileToProject("fusion_spawner.gd", "class_name FusionSpawner\nextends Node\n")
        gd.fixture.configureByText(
            "fusion.gd",
            """
            ## Coordinates all [Fusion<caret>Spawner] and [b]bold[/b] nodes, see [method documented_func].
            class_name Fusion
            extends Node

            func documented_func():
            	pass
            """.trimIndent()
        )

        runReadActionBlocking {
            val resolved = gd.fixture.getReferenceAtCaretPositionWithAssertion().resolve()
            assertNotNull(resolved)
            assertEquals("fusion_spawner.gd", resolved!!.containingFile.name)

            val references = gd.fixture.file.findElementAt(0)!!.references
            assertEquals(listOf("FusionSpawner", "documented_func"), references.map { it.canonicalText })
            assertNotNull(references[1].resolve())
        }
    }

    @Test
    fun testParamReferenceInDocCommentResolvesToParameter() {
        val file = gd.fixture.configureByText(
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
        val references = runReadActionBlocking { secondLine.references }
        assertEquals(listOf("default_value", "missing"), references.map { it.canonicalText })
        val resolved = references[0].resolve()
        assertNotNull(resolved)
        assertEquals(text.indexOf("default_value: Variant"), runReadActionBlocking { resolved!!.textRange.startOffset })
        assertNull(references[1].resolve())

        // Reader Mode resolves the rendered link with the function name as the context.
        val functionName = file.findElementAt(text.indexOf("get_setting("))!!
        val fromReaderMode = runReadActionBlocking { GdDocLinkResolver.resolve(gd.fixture.psiManager, "param:name", functionName) }
        assertNotNull(fromReaderMode)
        assertEquals(text.indexOf("name: String"), runReadActionBlocking { fromReaderMode!!.textRange.startOffset })
    }

    @Test
    fun testGenerateRenderedDocKeepsCodeblocksFormatted() {
        val file = gd.fixture.configureByText(
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

        val comment = runReadActionBlocking { findGdDocComment(file, TextRange(offset, offset + 1)) }
        assertNotNull(comment)

        val rendered = runReadActionBlocking { renderGdDocComment(comment!!) }
        assertTrue(rendered.contains("<pre><code>"))
        // Highlighted GDScript is split into spans, so only check token fragments.
        assertTrue(rendered.contains("_validate_property"))
        assertFalse(rendered.contains("[method _get_property_list]"))
    }

}
