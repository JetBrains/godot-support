package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.codeInsight.navigation.actions.GotoDeclarationOrUsageHandler2.GTDUOutcome
import com.intellij.polySymbols.testFramework.checkDocumentationAtCaret
import com.intellij.polySymbols.testFramework.moveToOffsetBySignature
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCase
import org.junit.jupiter.api.Test

/**
 * RIDER-143313 `super` resolves to the parent class method, so the code insight of that method works on `super`.
 * All tests except the rename share the `super` test data directory.
 */
@TestApplication
class GdSuperCodeInsightTest : GdTestCase("codeInsight") {

    @Test
    fun testDocumentationOnBareSuperCall() =
        doSuperDocumentationTest(signature = "own := <caret>super()", fileSuffix = ".bare")

    @Test
    fun testDocumentationOnQualifiedSuperCall() =
        doSuperDocumentationTest(signature = "own + <caret>super.describe", fileSuffix = ".qualified")

    @Test
    fun testParameterInfoOnBareSuperCall() =
        gd.doParameterInfoTest(
            dirName = SUPER_DIR,
            configureFileName = DERIVED_FILE,
            caretPosSignature = "super(<caret>who",
            goldFileName = "$SUPER_DIR/greet.param-info.html",
        )

    @Test
    fun testParameterInfoOnSuperConstructorCall() =
        gd.doParameterInfoTest(
            dirName = SUPER_DIR,
            configureFileName = DERIVED_FILE,
            caretPosSignature = "super(<caret>value",
            goldFileName = "$SUPER_DIR/init.param-info.html",
        )

    @Test
    fun testFindUsagesFromBareSuperCall() =
        doSuperFindUsagesTest(signature = "return <caret>super(who", goldName = "greet")

    @Test
    fun testFindUsagesFromQualifiedSuperCall() =
        doSuperFindUsagesTest(signature = "own + <caret>super.describe", goldName = "describe")

    /** The rename of the parent method replaces the member after `super.`, but never the `super` keyword. */
    @Test
    fun testRenameParentMethodKeepsSuper() =
        gd.doSymbolRenameTest(mainFile = "base.gd", newName = "describe_base", signature = "func <caret>describe")

    private fun doSuperDocumentationTest(signature: String, fileSuffix: String) =
        gd.doConfiguredTest(dirName = SUPER_DIR, configureFileName = DERIVED_FILE) {
            moveToOffsetBySignature(signature)
            checkDocumentationAtCaret(fileSuffix = fileSuffix, directory = SUPER_DIR)
        }

    private fun doSuperFindUsagesTest(signature: String, goldName: String) =
        gd.doFindUsagesTest(
            expectedFileName = "$SUPER_DIR/$goldName.usages.txt",
            gotoDeclarationOrUsageOutcome = GTDUOutcome.GTD,
            dirName = SUPER_DIR,
            configureFileName = DERIVED_FILE,
            caretPosSignature = signature,
        )

    private companion object {
        const val SUPER_DIR = "super"
        const val DERIVED_FILE = "derived.gd"
    }
}
