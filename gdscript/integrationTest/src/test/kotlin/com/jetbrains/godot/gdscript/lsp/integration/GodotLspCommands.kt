package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.application.EDT
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.platform.lsp.api.customization.LspFindReferencesCustomizer
import com.intellij.platform.lsp.testFramework.awaitFileOpenedByLspServer
import com.intellij.platform.lsp.testFramework.checkHighlightingRetrying
import com.intellij.polySymbols.testFramework.usagesAtCaret
import com.jetbrains.godot.gdscript.integration.GodotEditorFixture
import com.jetbrains.godot.test.project.godotProject
import gdscript.lsp.GodotLspIntegrationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.time.Duration.Companion.minutes

val defaultLspTestTimeout = 5.minutes

internal fun lspProject() = godotProject("LspTest") {}

suspend fun GodotEditorFixture.runHighlightingCheck() {
  waitForGodotLspStart()
  withContext(Dispatchers.EDT) {
    codeInsight.checkHighlightingRetrying(true)
  }
}

suspend fun GodotEditorFixture.runCompletionAndCheckItems(expectedCompletionItems: List<String>) {
  waitForGodotLspStart()
  val items = withContext(Dispatchers.EDT) {
    codeInsight.completeBasic()
      ?.map { it.lookupString }
      .orEmpty()
  }
  assertEquals(expectedCompletionItems.sorted(), items.sorted())
}

suspend fun GodotEditorFixture.runCompletionAndCheckAgainst(expected: String) {
  waitForGodotLspStart()
  withContext(Dispatchers.EDT) {
    codeInsight.completeBasic()
    codeInsight.finishLookup(Lookup.NORMAL_SELECT_CHAR)
    codeInsight.checkResult(expected)
  }
}

suspend fun GodotEditorFixture.runFindUsagesByMarker(expectedCount: Int) {
  waitForGodotLspStart()
  val usages = withContext(Dispatchers.EDT) {
    codeInsight.usagesAtCaret()
  }
  assertEquals(expectedCount, usages.size)
}

suspend fun GodotEditorFixture.waitForGodotLspStart() {
  awaitFileOpenedByLspServer(project, codeInsight.file.virtualFile)
}

/**
 * The LSP find-references customizer the connected Godot client uses. When it is
 * [LspFindReferencesSupport][com.intellij.platform.lsp.api.customization.LspFindReferencesSupport]
 * (the default), the LSP-backed find-references target duplicates GDScript's own PolySymbols one
 * in the "Find Usages"/"Show Usages" ambiguous-target chooser popup for every declaration - see
 * RIDER-142711.
 */
suspend fun GodotEditorFixture.godotFindReferencesCustomizer(): LspFindReferencesCustomizer {
    waitForGodotLspStart()
    return withContext(Dispatchers.EDT) {
        val client = LspClientManager.getInstance(project)
            .getClients(GodotLspIntegrationProvider::class.java)
            .single()
        client.descriptor.lspCustomization.findReferencesCustomizer
    }
}