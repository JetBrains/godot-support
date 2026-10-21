package com.jetbrains.godot.gdscript.lineMarker

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.gdNavigationElement
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.psi.GdMethodIdNmi
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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
@TestApplication
class GdInheritanceLineMarkerContributorTest {
    private val gd: GdSdkFixture by gdSdkFixture("highlighting")

    @Test
    fun testOverrideOfSdkVirtualMethodResolvesToEngineMethod() {
        gd.fixture.configureByText(
            "OverridesReady.gd", """
            |extends Node2D
            |
            |func _rea<caret>dy():
            |    pass
        """.trimMargin()
        )

        runReadActionBlocking {
            val element = gd.fixture.file.findElementAt(gd.fixture.caretOffset)
            val methodIdNmi = PsiTreeUtil.getParentOfType(element, GdMethodIdNmi::class.java)
            assertNotNull(methodIdNmi, "Expected caret to be inside a GdMethodIdNmi")

            val parentClass = GdSymbolResolverUtil.resolveOwnClassSymbol(methodIdNmi!!)?.resolveSuperClassSymbol()
            assertNotNull(parentClass, "Expected Node2D to resolve as the super class")

            val superMethod = GdSymbolResolverUtil.findMethodSymbol(parentClass, "_ready")
            assertNotNull(superMethod, "Expected _ready to resolve on Node2D's SDK ancestor chain")
            assertNotNull(superMethod!!.gdNavigationElement, "Expected a navigable element for the resolved SDK method")
        }
    }
}
