package com.jetbrains.godot.gdscript.reference

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * RIDER-143313 Go To Declaration on `super` navigates to the parent class method.
 */
@RunWith(JUnit4::class)
class GdSuperGotoDeclarationTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testBareSuperCallInOverriddenVirtual() =
        doSuperTest(fromSignature = "<caret>super()  # ready", declarationSignature = "func <caret>_ready")

    @Test
    fun testBareSuperCallInRegularMethod() =
        doSuperTest(fromSignature = "a := <caret>super()", declarationSignature = "func <caret>describe")

    @Test
    fun testBareSuperCallInConstructor() =
        doSuperTest(fromSignature = "<caret>super()  # init", declarationSignature = "func <caret>_init")

    @Test
    fun testQualifiedSuperCallOfSameMethod() =
        doSuperTest(fromSignature = "b := <caret>super", declarationSignature = "func <caret>describe")

    @Test
    fun testQualifiedSuperCallOfBaseOnlyMethod() =
        doSuperTest(fromSignature = "c := <caret>super", declarationSignature = "func <caret>base_only")

    @Test
    fun testBareSuperCallInSdkVirtual() =
        doGotoDeclarationTest(
            declarationSignature = "func <caret>_ready",
            fromSignature = "<caret>super()",
            expectedFileName = "Node.generated.gd",
            dirName = "gotoDeclarationSuperSdk",
            configureFileName = "gotoDeclarationSuperSdk.gd",
        )

    private fun doSuperTest(fromSignature: String, declarationSignature: String) =
        doGotoDeclarationTest(
            declarationSignature = declarationSignature,
            fromSignature = fromSignature,
            expectedFileName = "base.gd",
            dirName = "gotoDeclarationSuper",
            configureFileName = "gotoDeclarationSuper.gd",
        )
}
