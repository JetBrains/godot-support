package com.jetbrains.godot.gdscript.polySymbols.refactoring

import com.intellij.polySymbols.testFramework.UsagesTestHelper
import com.intellij.polySymbols.testFramework.checkGotoDeclaration
import com.intellij.polySymbols.testFramework.checkListByFile
import com.intellij.polySymbols.testFramework.moveToOffsetBySignature
import com.intellij.polySymbols.testFramework.usagesAtCaret
import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCaseWithSdk
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests that "Find Usages" works when invoked from a synthetic SDK-generated GDScript file.
 *
 * [GdSdkPolySymbol.getNavigationTargets] creates a temporary, non-physical file so that the user
 * can inspect the SDK declaration. This test navigates there via a real Goto Declaration from an
 * actual `Node2D` reference, confirms the synthetic file opened, then runs Find Usages from it.
 */
@RunWith(JUnit4::class)
class GdSdkPolySymbolFindUsagesTest : GdPolySymbolsTestCaseWithSdk("refactoring") {

    @Test
    @Ignore
    fun testFindUsagesFromSdkClass() {
        doConfiguredTest(configureFileName = "main.gd") {
            checkGotoDeclaration(
                fromSignature = "var node: <caret>Node2D = Node2D.new()",
                declarationSignature = "class_name <caret>Node2D",
                expectedFileName = "Node2D.gd",
            )
            moveToOffsetBySignature("class_name <caret>Node2D")
            checkListByFile(
                usagesAtCaret(scope = null, usagesTestHelper = UsagesTestHelper.Default),
                "findUsagesFromSdkClass/usages.txt",
                false,
            )
        }
    }
}
