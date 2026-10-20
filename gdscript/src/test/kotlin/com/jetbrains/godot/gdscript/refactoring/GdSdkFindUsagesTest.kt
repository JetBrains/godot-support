package com.jetbrains.godot.gdscript.refactoring

import com.intellij.polySymbols.testFramework.UsagesTestHelper
import com.intellij.polySymbols.testFramework.checkGotoDeclaration
import com.intellij.polySymbols.testFramework.checkListByFile
import com.intellij.polySymbols.testFramework.moveToOffsetBySignature
import com.intellij.polySymbols.testFramework.usagesAtCaret
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Tests that "Find Usages" works when invoked from a synthetic SDK-generated GDScript file.
 *
 * [GdSdkPolySymbol.getNavigationTargets] creates a temporary, non-physical file so that the user
 * can inspect the SDK declaration. This test navigates there via a real Goto Declaration from an
 * actual `Node2D` reference, confirms the synthetic file opened, then runs Find Usages from it.
 */
@TestApplication
class GdSdkFindUsagesTest : GdTestCaseWithSdk("refactoring") {

    @Test
    @Disabled
    fun testFindUsagesFromSdkClass() {
        gd.doConfiguredTest(configureFileName = "main.gd") {
            checkGotoDeclaration(
                fromSignature = "var node: <caret>Node2D = Node2D.new()",
                declarationSignature = "class_name <caret>Node2D",
                expectedFileName = "Node2D.generated.gd",
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
