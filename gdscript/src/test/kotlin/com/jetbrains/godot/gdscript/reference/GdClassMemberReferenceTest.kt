package com.jetbrains.godot.gdscript.reference

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import gdscript.polySymbols.psi.GdPsiPropertySymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdClassMemberReferenceTest : GdTestCaseWithSdk("reference") {

    @Test
    fun testResolveClassMemberSdkProperty() =
        doResolveSymbolTest(
            "node.<caret>position",
            GdSdkPropertySymbol::class.java,
            "position",
        )

    @Test
    fun testResolveClassMemberSdkMethod() =
        doResolveSymbolTest(
            "node.<caret>apply_scale",
            GdSdkMethodSymbol::class.java,
            "apply_scale",
        )

    @Test
    fun testResolveClassMemberPsiProperty() =
        doResolveSymbolTest(
            "obj.<caret>my_prop",
            GdPsiPropertySymbol::class.java,
            "my_prop",
        )

    @Test
    fun testResolveClassMemberPsiMethod() =
        doResolveSymbolTest(
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
        doResolveSymbolTest(
            "Inner.new().<caret>foo()",
            GdPsiMethodSymbol::class.java,
            "foo",
        )
}
