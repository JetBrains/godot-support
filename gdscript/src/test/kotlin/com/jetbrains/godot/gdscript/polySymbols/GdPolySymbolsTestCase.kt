package com.jetbrains.godot.gdscript.polySymbols

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.intellij.polySymbols.testFramework.PolySymbolsTestCase
import com.intellij.polySymbols.testFramework.resolvePolySymbolReference
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

/**
 * Base test case for GdScript PolySymbol feature tests (completion, highlighting, navigation, etc.).
 * Extends [PolySymbolsTestCase] to leverage its built-in test methods like
 * [doLookupTest], [doHighlightingTest], [doGotoDeclarationTest], etc.
 *
 * For model-only tests (symbol queries, inheritance, members), use [GdPolySymbolModelTestBase] instead.
 */
abstract class GdPolySymbolsTestCase(override val testCasePath: String) : PolySymbolsTestCase(HybridTestMode.CodeInsightFixture) {

    override val testDataRoot: String
        get() = getBaseTestDataPath().resolve("testData/gdscript/polySymbols").pathString

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
            val symbol = assertInstanceOf(myFixture.resolvePolySymbolReference(signature), expectedClass)
            assertEquals(expectedName, symbol.name)
        }
    }
}