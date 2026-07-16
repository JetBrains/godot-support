package com.jetbrains.godot.gdscript

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.intellij.polySymbols.testFramework.PolySymbolsTestCase
import com.intellij.polySymbols.testFramework.resolveSymbolReference
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

/**
 * Base test case for GdScript PolySymbol feature tests (completion, highlighting, navigation, etc.).
 * Extends [PolySymbolsTestCase] to leverage its built-in test methods like
 * [doLookupTest], [doHighlightingTest], [doGotoDeclarationTest], etc.
 *
 * For model-only tests (symbol queries, inheritance, members), use [GdModelTestBase] instead.
 */
abstract class GdTestCase(override val testCasePath: String) : PolySymbolsTestCase(HybridTestMode.CodeInsightFixture) {

    override val testDataRoot: String
        get() = getBaseTestDataPath().resolve("testData/gdscript").pathString

    override val defaultExtension: String = "gd"

    override val defaultDependencies: Map<String, String> = emptyMap()

    override val dirModeByDefault: Boolean = true

    protected fun <T : PolySymbol> doResolveSymbolTest(
        signature: String,
        expectedClass: Class<T>,
        expectedName: String,
        chosenTestName: String? = null,
    ) {
        val dirName = chosenTestName ?: testName
        val fileName = (chosenTestName ?: testName).plus(".$defaultExtension")
        doConfiguredTest(dirName = dirName, configureFileName = fileName) {
            // Unwrap PolySymbolDelegate wrappers (e.g. GdAliasedNameSymbol, used wherever the
            // querying text differs from the resolved symbol's own name) the same way production
            // code does via GdSymbolResolverUtil.resolveSymbolReferences() - resolveSymbolReference()
            // itself does not.
            val referenced = myFixture.resolveSymbolReference(signature)
            val resolved = (referenced as? PolySymbol)?.unwrapAllDelegates() ?: referenced
            val symbol = assertInstanceOf(resolved, expectedClass)
            assertEquals(expectedName, symbol.name)
        }
    }
}