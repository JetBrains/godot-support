package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tscn.index.impl.TscnNodeIndex
import tscn.psi.TscnNodeHeader
import tscn.psi.utils.TscnNodeUtil

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Completion"])
@TestApplication
class GdMetaNameCompletionTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/completion")

    @Test
    fun testMetaOwnerOnly() {
        for (receiver in listOf("", "self.")) {
            for (method in listOf("get_meta", "set_meta", "has_meta", "remove_meta")) {
                for (literal in listOf("\"<caret>\"", "'<caret>'", "&\"<caret>\"", "&'<caret>'")) {
                    val value = if (method == "set_meta") ", 1" else ""
                    doMetaTest("$receiver$method($literal$value)") {
                        assertMetaNames("health", "shared")
                    }
                }
            }
        }
    }

    @Test
    fun testNoMetasInPlainString() = doMetaTest("var text = \"<caret>\"") {
        assertFalse(lookup!!.items.any { LookupElementPresentation.renderElement(it).tailText == " (meta)" })
        assertThat(lookupElementStrings!!).contains("enemies").doesNotContain("health")
    }

    @Test
    fun testNoMetasInWriteValue() = doMetaTest("set_meta(\"key\", \"<caret>\")") {
        assertThat(lookupElementStrings!!).contains("enemies").doesNotContain("health", "shared")
        assertFalse(lookup!!.items.any { LookupElementPresentation.renderElement(it).tailText == " (meta)" })
    }

    @Test
    fun testMetaOwnersInTwoScenes() = doMetaTest("get_meta(\"<caret>\")", "multi.gd") {
        assertMetaNames("first", "second", "shared")
    }

    @Test
    fun testMetaNodePathReceiverInTwoScenes() = doMetaTest("\$Child.get_meta(\"<caret>\")") {
        assertMetaNames("child_only", "second_child_only")
    }

    @Test
    fun testMetaUniqueNode() = doMetaTest("%Unique.get_meta(\"<caret>\")") { assertMetaNames("unique_only") }

    @Test
    fun testMetaUnknownReceiver() {
        for (expression in listOf(
            "var other\n\tother.get_meta(\"<caret>\")",
            "get_parent().get_meta(\"<caret>\")",
            "\$Child.get_parent().get_meta(\"<caret>\")",
        )) {
            doMetaTest(expression) { assertProjectMetas() }
        }
    }

    @Test
    fun testMetaMissingPathFallback() = doMetaTest("\$Missing.get_meta(\"<caret>\")") { assertProjectMetas() }

    @Test
    fun testMetaEmptyNode() = doMetaTest("\$Empty.get_meta(\"<caret>\")") {
        // An empty result opens no lookup and inserts no name.
        assertNull(lookup)
        assertEquals(metaScript("\$Empty.get_meta(\"\")"), editor.document.text)
    }

    @Test
    fun testMetaUnderscoreFilter() {
        // Godot treats a meta name that starts with `_` as editor-only, so completion hides it.
        doMetaTest("get_meta(\"<caret>\")") { assertThat(lookupElementStrings!!).contains("health", "shared").doesNotContain("_private") }
        doMetaTest("get_meta(\"_<caret>\")") { assertMetaNames("_private") }
    }

    @Test
    fun testMetaLiteralInsertion() {
        for ((literal, expected, selectChar) in listOf(
            Triple("\"he<caret>\"", "\"health\"", Lookup.NORMAL_SELECT_CHAR),
            Triple("'he<caret>'", "'health'", Lookup.NORMAL_SELECT_CHAR),
            Triple("&\"he<caret>\"", "&\"health\"", Lookup.NORMAL_SELECT_CHAR),
            Triple("&'he<caret>'", "&'health'", Lookup.NORMAL_SELECT_CHAR),
            Triple("&\"he<caret>xyz\"", "&\"health\"", Lookup.REPLACE_SELECT_CHAR),
        )) {
            doMetaTest("get_meta($literal)") {
                val name = "health"
                lookup!!.currentItem = lookup!!.items.single { it.lookupString == name }
                finishLookup(selectChar)
                assertEquals(metaScript("get_meta($expected)"), editor.document.text)
            }
        }
    }

    // Read the metas from the stubs before the AST loads, then compare them with the metas from the AST.
    @Test
    fun testMetaSavedRecordsMatchAst() = gd.doConfiguredTest(
        dir = true,
        dirName = "metaRecords",
        configureFileName = "owner.gd",
    ) {
        val nodes = TscnNodeIndex.INSTANCE.getAllValues(project).filter { it.containingFile.name == "saved.tscn" }
        assertEquals(2, nodes.size)
        val scene = nodes.first().containingFile as PsiFileImpl
        assertFalse(scene.isContentsLoaded)
        val saved = nodes.associate { node ->
            assertNotNull(node.stub)
            node.name to TscnNodeUtil.getMetas(node).toList()
        }
        assertFalse(scene.isContentsLoaded)
        assertEquals(mapOf("WithMeta" to listOf("health", "shared"), "WithoutMeta" to emptyList()), saved)
        scene.node
        val parsed = PsiTreeUtil.findChildrenOfType(scene, TscnNodeHeader::class.java).associate {
            assertNull(it.stub)
            it.name to TscnNodeUtil.getMetas(it).toList()
        }
        assertEquals(saved, parsed)
    }

    private fun CodeInsightTestFixture.assertMetaNames(vararg names: String) {
        assertThat(lookupElementStrings!!).containsExactlyInAnyOrder(*names)
        assertEquals(names.size, lookupElementStrings!!.size)
        assertTrue(lookup!!.items.all { LookupElementPresentation.renderElement(it).tailText == " (meta)" })
    }

    private fun CodeInsightTestFixture.assertProjectMetas() =
        assertMetaNames("health", "shared", "root_only", "child_only", "second_child_only", "unique_only", "first", "second")

    private fun metaScript(expression: String) = "extends Node\n\nfunc _ready():\n\t$expression\n"

    // Write into the existing script, so its resource path matches the path in the scenes.
    private fun doMetaTest(
        expression: String,
        fileName: String = "owner.gd",
        check: CodeInsightTestFixture.() -> Unit,
    ) {
        // Keep single-item results visible for assertions and insertion.
        disableSingleItemAutoInsert(gd.testRootDisposable)
        gd.doConfiguredTest(
            dir = true,
            dirName = "metas",
            configureFile = false,
        ) {
            // Keep the resource path that the scenes use for the script.
            WriteAction.run<RuntimeException> {
                VfsUtil.saveText(tempDirFixture.getFile(fileName)!!, metaScript(expression))
            }
            configureFromTempProjectFile(fileName)
            assertEquals(fileName, file.name)
            completeBasic()
            check()
            // Save the script before the next table entry copies the fixture.
            FileDocumentManager.getInstance().saveAllDocuments()
        }
    }
}
