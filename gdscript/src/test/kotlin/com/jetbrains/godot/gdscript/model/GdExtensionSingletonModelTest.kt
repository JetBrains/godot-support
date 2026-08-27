package com.jetbrains.godot.gdscript.model

import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.gdscript.GdModelTestBase
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.highlighting.GdPolySymbolHighlightingCustomizer
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.index.GdSdkPolySymbolIndexUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdRefIdRef
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Covers the GDExtension singletons declared in the generated `@GDExtensionScope.xml`
 * (see [gdscript.library.GdGlobalSingletonsDocWriter]): they are global variables, so they behave exactly like the engine
 * singletons declared in Godot's own `@GlobalScope.xml`.
 */
@RunWith(JUnit4::class)
class GdExtensionSingletonModelTest : GdModelTestBase() {

    companion object {
        private const val SINGLETON_CLASS = "SQLite"
        private const val PLAIN_CLASS = "RiderLocator"
    }

    // Kept apart from the shared gdextensions test data, so the expectations of the other model tests are unaffected.
    override val sdkDirectories
        get() = super.sdkDirectories + "sdk/singletons"

    @Test
    fun testSingletonIsASelfTypedGlobalProperty() {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)

        val singleton = GdPolySymbolQueriesUtil.getSdkPropertySymbol(executor, GdKeywords.GDEXTENSION_SCOPE, SINGLETON_CLASS)
        assertNotNull("$SINGLETON_CLASS is not declared as a global variable", singleton)
        // Self-typed: this is the single hop the return type inference relies on.
        assertEquals(SINGLETON_CLASS, singleton!!.returnType)

        // A GDExtension class that isn't a singleton stays a plain class, so member access through it is still static.
        assertNull(GdPolySymbolQueriesUtil.getSdkPropertySymbol(executor, GdKeywords.GDEXTENSION_SCOPE, PLAIN_CLASS))
    }

    @Test
    fun testSingletonExposesInstanceMembers() {
        val classSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, SINGLETON_CLASS)
        assertNotNull("$SINGLETON_CLASS class not found", classSymbol)

        val classExecutor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(classSymbol!!.queryScope)
        }

        val openDb = "open_db"
        val openDbMethod = GdPolySymbolQueriesUtil.getSdkMethodSymbol(classExecutor, SINGLETON_CLASS, openDb, null)
        assertNotNull("$openDb method not found", openDbMethod)

        val defaultExtension = "default_extension"
        assertNotNull(
            "$defaultExtension property not found",
            GdPolySymbolQueriesUtil.getSdkPropertySymbol(classExecutor, SINGLETON_CLASS, defaultExtension)
        )
    }

    @Test
    fun testSingletonNameInfersItsClass() {
        val file = myFixture.configureByText(
            "singleton_type.gd",
            """
            |var db := $SINGLETON_CLASS
            """.trimMargin()
        )

        val declaration = file.children.filterIsInstance<GdClassVarDeclTl>().first { it.getName() == "db" }
        assertEquals(SINGLETON_CLASS, declaration.returnType)
    }

    @Test
    fun testSingletonIsColoredAsAGlobalVariable() {
        val file = myFixture.configureByText("singleton_coloring.gd", "var db := $SINGLETON_CLASS")
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)
        val singleton = GdPolySymbolQueriesUtil.getSdkPropertySymbol(executor, GdKeywords.GDEXTENSION_SCOPE, SINGLETON_CLASS)
        assertNotNull(singleton)

        // Same color as an engine singleton like `Input`, which is declared the same way in @GlobalScope.xml.
        val attributes = GdPolySymbolHighlightingCustomizer().getSymbolTextAttributes(file, singleton!!, 0)
        assertEquals(GdHighlighterColors.GLOBAL_VARIABLE_BUILT_IN, attributes)
    }

    /**
     * The name of a singleton matches both its class and the global variable declaring it. Resolving to
     * both leaves the platform unable to pick a single symbol, which costs the reference its coloring
     * and reports it as an unknown symbol - the bare name must resolve to the instance alone.
     */
    @Test
    fun testSingletonNameResolvesOnlyToTheGlobalVariable() {
        val file = myFixture.configureByText("singleton_resolve.gd", "var db := $SINGLETON_CLASS")
        val reference = PsiTreeUtil.findChildrenOfType(file, GdRefIdRef::class.java).single { it.text == SINGLETON_CLASS }

        val resolved = reference.resolveSymbolReferences()
        assertEquals(listOf(GdPolySymbolKind.PROPERTY), resolved.map { it.kind })

        // Navigation still lands on the class, not on the synthetic global scope stub.
        val declaration = (resolved.single() as GdSdkPolySymbol).syntheticSourceElement(project)
        assertEquals(SINGLETON_CLASS, declaration?.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY))
    }

    @Test
    fun testNonSingletonClassNameStillResolvesToItsClass() {
        val file = myFixture.configureByText("class_resolve.gd", "var locator := $PLAIN_CLASS")
        val reference = PsiTreeUtil.findChildrenOfType(file, GdRefIdRef::class.java).single { it.text == PLAIN_CLASS }

        assertEquals(listOf(GdPolySymbolKind.CLASS), reference.resolveSymbolReferences().map { it.kind })
    }

    @Test
    fun testChainedCallThroughSingletonInfersItsReturnType() {
        val file = myFixture.configureByText(
            "singleton_chained_type.gd",
            """
            |var opened := $SINGLETON_CLASS.open_db()
            """.trimMargin()
        )

        val declaration = file.children.filterIsInstance<GdClassVarDeclTl>().first { it.getName() == "opened" }
        assertEquals(GdKeywords.BOOL, declaration.returnType)
    }
}
