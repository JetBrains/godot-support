package com.jetbrains.godot.gdscript.polySymbols.refactoring

import com.intellij.codeInsight.navigation.actions.GotoDeclarationOrUsageHandler2
import com.intellij.polySymbols.testFramework.UsagesTestHelper
import com.intellij.polySymbols.testFramework.checkGTDUOutcome
import com.intellij.polySymbols.testFramework.checkListByFile
import com.intellij.polySymbols.testFramework.usagesAtCaret
import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCaseWithSdk
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.GdFile
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests that "Find Usages" works when invoked from a synthetic SDK-generated GDScript file.
 *
 * [GdSdkPolySymbol.getNavigationTargets] creates a temporary, non-physical file so that the user
 * can inspect the SDK declaration.  This test verifies that calling Find Usages from that file
 * finds the expected usages in the rest of the project.
 *
 * The test simulates the synthetic file by marking a physical test file with
 * [GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY], which is the same mechanism used at runtime.
 */
@RunWith(JUnit4::class)
class GdSdkPolySymbolRefactoringTest : GdPolySymbolsTestCaseWithSdk("refactoring") {

    @Test
    fun testFindUsagesFromSdkMethod() {
        doConfiguredTest(dir = true, configureFileName = "Node2D.gd") {
            (file as? GdFile)?.putUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY, "Node2D")
            checkGTDUOutcome(GotoDeclarationOrUsageHandler2.GTDUOutcome.SU)
            checkListByFile(
                usagesAtCaret(scope = null, usagesTestHelper = UsagesTestHelper.Default),
                "findUsagesFromSdkMethod/usages.txt",
                false,
            )
        }
    }
}
