package com.jetbrains.godot.gdscript.codeInsight.documentation

import com.intellij.lang.documentation.impl.documentationTargets
import com.intellij.model.psi.PsiSymbolReferenceService
import com.intellij.openapi.util.TextRange
import com.intellij.platform.backend.documentation.DocumentationLinkHandler
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.text.CharArrayUtil
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.codeInsight.GdDocumentationTarget
import gdscript.codeInsight.GdDocumentationTargetProvider
import gdscript.codeInsight.generateGdDoc
import gdscript.codeInsight.documentation.GdDocLinkResolver
import gdscript.codeInsight.documentation.GdAnnotationAnchors
import gdscript.codeInsight.documentation.findGdDocComment
import gdscript.codeInsight.documentation.renderGdDocComment
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.config.GdAnnotationSymbol
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdAnnotationType
import gdscript.psi.GdRefIdRef
import gdscript.settings.GdDocProviderMode
import gdscript.settings.GdProjectSettingsState
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdDocumentationLinkResolveTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveSdkTypeAndMembers() {
        val context = myFixture.configureByText(
            "documentationLinks.gd",
            "extends Node\nfunc documented():\n\tpass\n",
        )

        val type = resolve("Node", context)
        assertNotNull(type)
        assertEquals("Node", type!!.text)
        assertEquals("Node.generated.gd", type.containingFile.name)

        val globalScope = resolve("@GlobalScope", context)
        assertNotNull(globalScope)
        assertEquals("@GlobalScope.generated.gd", globalScope!!.containingFile.name)

        val method = resolve("method:Node._ready", context)
        assertNotNull(method)
        assertEquals("_ready", method!!.text)
        assertEquals("Node.generated.gd", method.containingFile.name)

        val property = resolve("member:Node.owner", context)
        assertNotNull(property)
        assertEquals("owner", property!!.text)

        val signal = resolve("signal:Node.ready", context)
        assertNotNull(signal)
        assertEquals("ready", signal!!.text)

        val constant = resolve("constant:Node.NOTIFICATION_READY", context)
        assertNotNull(constant)
        assertEquals("NOTIFICATION_READY", constant!!.text)

        val enum = resolve("enum:Node.ProcessMode", context)
        assertNotNull(enum)
        assertEquals("ProcessMode", enum!!.text)
    }

    @Test
    fun testResolveRelativeSdkMemberFromSyntheticOwner() {
        val context = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, "Node")
            ?.syntheticSourceElement(project)

        assertNotNull(context)
        val resolved = resolve("method:_ready", context!!)

        assertNotNull(resolved)
        assertEquals("_ready", resolved!!.text)
        assertEquals("Node.generated.gd", resolved.containingFile.name)
    }

    @Test
    fun testSyntheticSdkDocumentationRendersReaderLinksAndKeepsOwnerContext() {
        val sourceElement = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, "Node")
            ?.syntheticSourceElement(project)

        assertNotNull(sourceElement)
        val file = sourceElement!!.containingFile
        val linkOffset = file!!.text.indexOf("[SceneTree]")
        assertTrue(linkOffset >= 0)

        val comment = findGdDocComment(file, TextRange(linkOffset, linkOffset + 1))
        assertNotNull(comment)
        val rendered = renderGdDocComment(comment!!)

        assertTrue(rendered.contains("psi_element://SceneTree"))
        assertTrue(rendered.contains("psi_element://method:_enter_tree"))

        val owner = comment.owner
        assertNotNull(owner)
        assertNotNull(resolve("method:_enter_tree", owner!!))
    }

    @Test
    fun testSyntheticSdkDocumentationExpandsDocsUrl() {
        val sourceElement = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, "Node")
            ?.syntheticSourceElement(project)

        assertNotNull(sourceElement)
        val file = sourceElement!!.containingFile
        val docsUrlPlaceholder = "$" + "DOCS_URL"
        val linkOffset = file!!.text.indexOf("[url=$docsUrlPlaceholder/tutorials/plugins/running_code_in_the_editor.html]")
        assertTrue(linkOffset >= 0)

        val comment = findGdDocComment(file, TextRange(linkOffset, linkOffset + 1))
        assertNotNull(comment)
        val rendered = renderGdDocComment(comment!!)

        assertTrue(rendered.contains("https://docs.godotengine.org/en/stable/tutorials/plugins/running_code_in_the_editor.html"))
        assertFalse(rendered.contains(docsUrlPlaceholder))
    }

    @Test
    fun testResolveProjectClassAndMember() {
        val context = myFixture.configureByText(
            "projectLinks.gd",
            "class_name LinkTarget\nextends Node\n\nfunc project_method():\n\tpass\n",
        )

        val type = resolve("LinkTarget", context)
        assertNotNull(type)
        assertEquals("LinkTarget", type!!.text)

        val method = resolve("method:LinkTarget.project_method", context)
        assertNotNull(method)
        assertEquals("project_method", method!!.text)
    }

    @Test
    fun testUnknownLinkDoesNotResolve() {
        val context = myFixture.configureByText("unknownLinks.gd", "extends Node\n")

        assertNull(resolve("method:Node.unknown_documentation_member", context))
        assertNull(resolve("unknown:res://missing.gd", context))
    }

    @Test
    fun testReaderModeTargetResolvesSdkLink() {
        val file = myFixture.configureByText(
            "fusion.gd",
            "## See [Object].\nclass_name Fusion\nextends Node\n\n## See [Object].\nfunc documented():\n\tpass\n",
        )
        assertLinkResolvesFromReaderModeTarget(file, file.text.indexOf("class_name"))
        assertLinkResolvesFromReaderModeTarget(file, file.text.indexOf("func documented"))

        val sdkFile = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, "Node")!!.syntheticSourceElement(project)!!.containingFile!!
        val sdkText = sdkFile.text
        val classDescription = findGdDocComment(sdkFile, TextRange.from(sdkText.indexOf("[SceneTree]"), 1))!!
        // Rider looks up the target at the first token after the comment. For the class description it is `#region`.
        val afterClassDescription = CharArrayUtil.shiftForward(sdkText, classDescription.textRange.endOffset, " \t\r\n")
        assertTrue(sdkText.startsWith("#region", afterClassDescription))
        assertLinkResolvesFromReaderModeTarget(sdkFile, afterClassDescription)
    }

    @Test
    fun testResolveAnnotationLinkToAnchor() {
        val context = myFixture.configureByText("annotationLinks.gd", "extends Node\n")

        listOf("annotation:@GDScript.@export", "annotation:@export", "annotation:export").forEach { link ->
            val anchor = resolve(link, context)
            assertNotNull(link, anchor)
            assertEquals(link, "# @export", anchor!!.text)
            assertEquals("@GDScript.generated.gd", anchor.containingFile.name)
        }
        // The prefix match must not mix up `@export` and `@export_range`.
        assertTrue(resolve("annotation:@export_range", context)!!.text.startsWith("# @export_range("))
        assertNull(resolve("annotation:@unknown_annotation", context))
    }

    @Test
    fun testAnnotationReferenceInDocComment() {
        val file = myFixture.configureByText("annotationReference.gd", "## Use [annotation @export] here.\nvar value: int\n")
        val comment = file.findElementAt(0)!!
        val reference = comment.references.single()
        assertEquals("@export", reference.canonicalText)
        assertEquals("# @export", reference.resolve()?.text)
    }

    @Test
    fun testAnnotationSymbolTargetsAnchor() {
        val data = GdSdkData.AnnotationData("export", null, isVariadic = false, parameters = emptyList())
        val symbol = GdAnnotationSymbol(project, GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME, "export", data)

        assertEquals("# @export", symbol.syntheticSourceElement(project)?.text)
        assertNotEmpty(symbol.getNavigationTargets(project))
        assertNotNull(symbol.getDocumentationTarget(null))
    }

    @Test
    fun testAnnotationInCodeResolvesToAnchor() {
        val file = myFixture.configureByText("annotationUsage.gd", "extends Node\n@onready var label: Label\n@unknown_annotation var other: int\n")
        val (known, unknown) = PsiTreeUtil.findChildrenOfType(file, GdAnnotationType::class.java).toList()

        val reference = PsiSymbolReferenceService.getService().getReferences(known).single()
        assertEquals(TextRange(1, known.textLength), reference.rangeInElement)
        val symbol = reference.resolveReference().single() as GdAnnotationSymbol
        assertEquals("onready", symbol.name)
        assertEquals("# @onready", symbol.syntheticSourceElement(project)?.text)
        assertNotEmpty(symbol.getNavigationTargets(project))

        // `GdAnnotationAnnotator` reports an unknown annotation, so it gets no reference.
        assertEmpty(PsiSymbolReferenceService.getService().getReferences(unknown))
    }

    @Test
    fun testAnnotationAnchorDocumentationAndReaderModeTarget() {
        val anchor = resolve("annotation:@export_range", myFixture.configureByText("anchorDoc.gd", "extends Node\n"))!!
        val file = anchor.containingFile

        // Reader Mode looks up the target at the token after the `##` block, which is the anchor.
        assertLinkResolvesFromReaderModeTarget(file, anchor.textRange.startOffset)

        assertTrue(generateGdDoc(anchor)!!.contains("@export_range(min: float, max: float"))

        // The test SDK has empty annotation descriptions, so a marked synthetic file gives the `##` block.
        val synthetic = myFixture.configureByText(
            "annotations.gd",
            "class_name Annotations\n\n## Exports a range. See [Object] and [annotation @export].\n# @export_range(min: float)\n",
        )
        synthetic.putUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY, "Annotations")
        val syntheticAnchor = GdAnnotationAnchors.find(synthetic, "export_range")!!
        val doc = generateGdDoc(syntheticAnchor)
        assertNotNull(doc)
        assertTrue(doc!!, doc.contains("@export_range(min: float)"))
        assertTrue(doc, doc.contains("Exports a range."))
        assertTrue(doc, doc.contains("psi_element://Object"))
        assertTrue(doc, doc.contains("psi_element://annotation:@export"))
        assertLinkResolvesFromReaderModeTarget(synthetic, syntheticAnchor.textRange.startOffset)
    }

    /**
     * The LSP server documents a real script file, but it does not know a generated SDK file.
     * The plugin creates that file in memory, so it must document it in the LSP mode too.
     */
    @Test
    fun testLspModeKeepsGeneratedSdkDocumentation() {
        val sdkElement = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, "Node")!!.syntheticSourceElement(project)!!
        val script = myFixture.configureByText("lspDoc.gd", "extends Node\n\n## A documented function.\nfunc documented():\n\tpass\n")
        val scriptElement = script.findElementAt(script.text.indexOf("documented()"))!!.parent
        val settings = GdProjectSettingsState.getInstance(project)
        val provider = GdDocumentationTargetProvider()

        assertNotNull(generateGdDoc(scriptElement))
        settings.state.docProvider = GdDocProviderMode.LSP
        try {
            assertNull(generateGdDoc(scriptElement))
            assertNull(provider.documentationTarget(scriptElement, null))

            assertNotNull(generateGdDoc(sdkElement))
            assertNotNull(provider.documentationTarget(sdkElement, null))
        } finally {
            settings.state.docProvider = GdDocProviderMode.GDSCRIPT
        }
    }

    /**
     * A Poly Symbol must build the GDScript documentation target.
     * The platform `createPsiDocumentationTarget` reaches the deprecated `DocumentationProvider` extension point only,
     * which GDScript no longer implements, so it gives an empty popup.
     */
    @Test
    fun testPolySymbolDocumentationTargetIsGdScriptTarget() {
        myFixture.configureByText(
            "symbolDoc.gd",
            "extends Node\n\n## A documented function.\nfunc documented():\n\tpass\n\nfunc caller():\n\t<caret>documented()\n",
        )
        val leaf = myFixture.file.findElementAt(myFixture.caretOffset)
        val refId = PsiTreeUtil.getParentOfType(leaf, GdRefIdRef::class.java, false)!!

        val psiSymbol = refId.resolveSymbolReferences().single()
        assertDocumentationContains(psiSymbol.getDocumentationTarget(refId), "A documented function.")

        val sdkSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, "Node")!!
        assertDocumentationContains(sdkSymbol.getDocumentationTarget(refId), "Node")
    }

    private fun assertDocumentationContains(target: DocumentationTarget?, text: String) {
        assertNotNull(target)
        val gdTarget = target as? GdDocumentationTarget
        assertNotNull("expected a GdDocumentationTarget, got ${target!!::class.java.name}", gdTarget)
        val doc = generateGdDoc(gdTarget!!.targetElement)
        assertNotNull(doc)
        assertTrue(doc!!, doc.contains(text))
    }

    /** Reader Mode resolves a link through the first documentation target at the token after the comment. */
    private fun assertLinkResolvesFromReaderModeTarget(file: PsiFile, offset: Int) {
        val target = documentationTargets(file, offset).firstOrNull()
        assertNotNull(target)
        val resolved = DocumentationLinkHandler.EP_NAME
            .computeSafeIfAny { it.resolveLink(target!!, "psi_element://Object") }
        assertNotNull(resolved)
    }

    private fun resolve(link: String, context: com.intellij.psi.PsiElement): com.intellij.psi.PsiElement? {
        return GdDocLinkResolver.resolve(myFixture.psiManager, link, context)
    }
}
