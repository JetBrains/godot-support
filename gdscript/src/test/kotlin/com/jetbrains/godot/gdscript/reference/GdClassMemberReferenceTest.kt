package com.jetbrains.godot.gdscript.reference

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import gdscript.polySymbols.psi.GdPsiPropertySymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import org.junit.jupiter.api.Test

@TestApplication
class GdClassMemberReferenceTest {
    private val gd: GdSdkFixture by gdSdkFixture("reference")

    @Test
    fun testResolveClassMemberSdkProperty() =
        gd.doResolveSymbolTest(
            "node.<caret>position",
            GdSdkPropertySymbol::class.java,
            "position",
        )

    @Test
    fun testResolveClassMemberSdkMethod() =
        gd.doResolveSymbolTest(
            "node.<caret>apply_scale",
            GdSdkMethodSymbol::class.java,
            "apply_scale",
        )

    @Test
    fun testResolveClassMemberPsiProperty() =
        gd.doResolveSymbolTest(
            "obj.<caret>my_prop",
            GdPsiPropertySymbol::class.java,
            "my_prop",
        )

    @Test
    fun testResolveClassMemberPsiMethod() =
        gd.doResolveSymbolTest(
            "obj.<caret>my_method",
            GdPsiMethodSymbol::class.java,
            "my_method",
        )

    // RIDER-141201 guard test: `SuperInner` doesn't resolve as a *reference* in `class Inner extends
    // SuperInner:` (bare nested classes aren't reachable by simple name yet - see
    // GdInheritanceReferenceTest/GdRefIdReferenceTest for the reference-resolution side of that bug),
    // but the actual inheritance chain used for member lookups goes through a separate, already-working
    // path (GdClassUtil.getClassIdElement's GdClassDeclIndex fallback) - so `foo`, inherited from
    // `SuperInner`, must already resolve through `Inner.new()` regardless.
    @Test
    fun testResolveInheritedMethodThroughBareSiblingClassExtends() =
        gd.doResolveSymbolTest(
            "Inner.new().<caret>foo()",
            GdPsiMethodSymbol::class.java,
            "foo",
        )
}
