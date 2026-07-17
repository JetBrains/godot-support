package com.jetbrains.godot.gdscript.inspection

import com.intellij.lang.annotation.HighlightSeverity
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.inspection.GdMissingMatchBranchesInspection
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import project.psi.util.ProjectAutoloadUtil

/**
 * `GdMissingMatchBranchesInspection` used to be blind to SDK-backed matches in two independent
 * ways: the matched identifier itself resolving to an SDK symbol (`symbol.gdPsiSourceElement?.parent
 * ?: return`), and the matched value's declared type hint resolving to an SDK-defined enum
 * (`Node.ProcessMode`, `Input.MouseMode`, etc.) - meaning this inspection could never fire for any
 * built-in Godot enum, only user-declared ones. Verifies the cast-free rewrite
 * (`gdNavigationElement`/`gdEnumValues`/`gdDeclaringClassName`/`gdIsEngineSymbol`) fixes this, using
 * the real bundled `Input.mouse_mode` SDK property (declared type `MouseMode`, 6 real values - see
 * `sdk/4.5.0/Input.xml`).
 *
 * Confirmed empirically before writing these fixtures (not assumed): resolving
 * `Input.MOUSE_MODE_VISIBLE` yields a plain `GdSdkConstantSymbol`, not a nested enum-value access -
 * SDK "enums" (grouped from doc-XML `<constant enum="...">` entries) are flat class constants in
 * real GDScript syntax, unlike user-declared named enums which require the `EnumName.VALUE`
 * qualifier. This is why the production fix needed a `gdIsEngineSymbol` branch in the prefix
 * computation, not just swapping `gdPsiSourceElement` for `gdNavigationElement`.
 */
@RunWith(JUnit4::class)
class GdMissingMatchBranchesInspectionTest : GdTestCaseWithSdk("highlighting") {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(GdMissingMatchBranchesInspection::class.java)
    }

    private fun hasMissingEnumOptionsWarning(): Boolean {
        // Warm up ProjectAutoloadUtil.listGlobals's cached value outside doHighlighting()'s
        // AST-loading-restricted zone first. This inspection's own GdInheritanceUtil.isExtending(...)
        // call (unchanged production code) reaches an autoload-alias resolution fallback for SDK
        // owning-class ids - previously unreachable for any enum, since every SDK enum bailed via
        // gdPsiSourceElement before getting this far. Computing listGlobals for the first time from
        // inside doHighlighting() trips an AstLoadingException reading project.godot's PSI tree; a
        // pre-existing gap in that fallback's caching, now newly exposed by SDK-enum support, not a
        // bug in this inspection's own logic. Same "prime the cache once outside the restricted call"
        // shape as the GdParamAnnotatorTest/GdInlayParameterHintsSdkTest warm-up.
        ProjectAutoloadUtil.listGlobals(project)
        return myFixture.doHighlighting(HighlightSeverity.WEAK_WARNING).any { it.description == "Missing enum options" }
    }

    @Test
    fun testProjectEnumFromSelfContextWarnsWhenBranchesMissing() {
        myFixture.configureByText(
            "Test.gd", """
            |class_name Outer
            |enum State { IDLE, RUNNING, DONE }
            |var s: State
            |
            |func f():
            |	match s:
            |		State.IDLE:
            |			pass
        """.trimMargin()
        )
        assertTrue(hasMissingEnumOptionsWarning())
    }

    @Test
    fun testProjectEnumFromOuterClassContextNotWarnedWhenAllBranchesCovered() {
        myFixture.configureByText(
            "Test.gd", """
            |class_name Outer
            |enum State { IDLE, RUNNING, DONE }
            |var s: State
            |
            |func f():
            |	match s:
            |		State.IDLE:
            |			pass
            |		State.RUNNING:
            |			pass
            |		State.DONE:
            |			pass
        """.trimMargin()
        )
        assertFalse(hasMissingEnumOptionsWarning())
    }

    // Uses a project-declared property with an explicit qualified type hint to the SDK enum
    // (`Input.MouseMode`), rather than matching an SDK property directly (e.g. `Input.mouse_mode`):
    // confirmed empirically that the synthetic GDScript file XmlToGd generates for a class renders
    // an enum-typed <member> using its raw `int` XML type, not its `enum` attribute - a separate,
    // pre-existing gap in that conversion, unrelated to this inspection. Matching a real, explicitly
    // type-hinted project property instead exercises the SDK-enum-type-hint fix (the more impactful
    // of the two gaps this task closes) without depending on that unrelated gap being fixed too.

    @Test
    fun testSdkEnumPartiallyMatchedWarns() {
        myFixture.configureByText(
            "Test.gd", """
            |class_name Outer
            |var mode: Input.MouseMode
            |
            |func f():
            |	match mode:
            |		Input.MOUSE_MODE_VISIBLE:
            |			pass
        """.trimMargin()
        )
        assertTrue(hasMissingEnumOptionsWarning())
    }

    @Test
    fun testSdkEnumFullyMatchedDoesNotWarn() {
        myFixture.configureByText(
            "Test.gd", """
            |class_name Outer
            |var mode: Input.MouseMode
            |
            |func f():
            |	match mode:
            |		Input.MOUSE_MODE_VISIBLE:
            |			pass
            |		Input.MOUSE_MODE_HIDDEN:
            |			pass
            |		Input.MOUSE_MODE_CAPTURED:
            |			pass
            |		Input.MOUSE_MODE_CONFINED:
            |			pass
            |		Input.MOUSE_MODE_CONFINED_HIDDEN:
            |			pass
            |		Input.MOUSE_MODE_MAX:
            |			pass
        """.trimMargin()
        )
        assertFalse(hasMissingEnumOptionsWarning())
    }
}
