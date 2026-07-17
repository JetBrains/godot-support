package com.jetbrains.godot.gdscript.lineMarker

import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.gdNavigationElement
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.psi.GdMethodIdNmi
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * `GdInheritanceLineMarkerContributor`'s "overrides superclass method" gutter icon used to be blind
 * to SDK/engine ancestors (it only walked project-file declarations) - overriding an engine virtual
 * like `_ready` with no intermediate project-defined parent never showed a gutter icon. Verifies the
 * exact resolution chain `GdInheritanceLineMarkerContributor.collectNavigationMarkers` relies on
 * (`resolveOwnClassSymbol` -> `resolveSuperClassSymbol` -> `findMethodSymbol` -> `gdNavigationElement`)
 * now reaches a real engine method, without any interface casting.
 *
 * Asserts on the resolution chain directly rather than going through the full highlighting pipeline
 * (`findGuttersAtCaret()`/`doHighlighting()`) - `PolySymbolHighlightingAnnotator` has a pre-existing,
 * unrelated range-computation bug that crashes on this fixture, independent of line-marker behavior.
 */
@RunWith(JUnit4::class)
class GdInheritanceLineMarkerContributorTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testOverrideOfSdkVirtualMethodResolvesToEngineMethod() {
        myFixture.configureByText(
            "OverridesReady.gd", """
            |extends Node2D
            |
            |func _rea<caret>dy():
            |    pass
        """.trimMargin()
        )

        val element = myFixture.file.findElementAt(myFixture.caretOffset)
        val methodIdNmi = PsiTreeUtil.getParentOfType(element, GdMethodIdNmi::class.java)
        assertNotNull("Expected caret to be inside a GdMethodIdNmi", methodIdNmi)

        val parentClass = GdSymbolResolverUtil.resolveOwnClassSymbol(methodIdNmi!!)?.resolveSuperClassSymbol()
        assertNotNull("Expected Node2D to resolve as the super class", parentClass)

        val superMethod = GdSymbolResolverUtil.findMethodSymbol(parentClass, "_ready")
        assertNotNull("Expected _ready to resolve on Node2D's SDK ancestor chain", superMethod)
        assertNotNull(
            "Expected a navigable element for the resolved SDK method",
            superMethod!!.gdNavigationElement,
        )
    }
}
