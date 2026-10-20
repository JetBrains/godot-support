package com.jetbrains.godot.gdscript

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.testFramework.HybridFixtureContext
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.intellij.polySymbols.testFramework.PolySymbolsFixture
import com.intellij.polySymbols.testFramework.hybridFixture
import com.intellij.polySymbols.testFramework.resolveSymbolReference
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf

/**
 * Base test case for GdScript PolySymbol feature tests (completion, highlighting, navigation, etc.).
 * The fixture [gd] is a [PolySymbolsFixture] with its built-in test methods like
 * [GdFixture.doLookupTest], [GdFixture.doHighlightingTest], [GdFixture.doGotoDeclarationTest], etc.
 * Each test gets a new heavy project.
 *
 * For model-only tests (symbol queries, inheritance, members), use [GdModelTestBase] instead.
 */
abstract class GdTestCase protected constructor(
    testCasePath: String,
    createFixture: (HybridFixtureContext, String) -> GdFixture,
) {

    constructor(testCasePath: String) : this(testCasePath, ::GdFixture)

    protected val gd: GdFixture by hybridFixture({ HybridTestMode.CodeInsightFixture }) { createFixture(it, testCasePath) }

    protected val myFixture: CodeInsightTestFixture get() = gd.fixture

    protected val project: Project get() = gd.project
}

/**
 * The state of [GdTestCase]: a [PolySymbolsFixture] with the GDScript test data root, the `gd` extension, and the directory mode.
 * Call a member from a background thread.
 */
open class GdFixture(context: HybridFixtureContext, testCasePath: String) : PolySymbolsFixture(
    context,
    testDataRoot = getGdTestDataPath(context.testClass),
    testCasePath = testCasePath,
    defaultExtension = "gd",
    defaultDependencies = emptyMap(),
    dirModeByDefault = true,
) {

    /**
     * Configures the test directory and checks that the reference at [signature] resolves to a symbol of [expectedClass]
     * with the name [expectedName]. [chosenTestName] replaces the test name as the directory and the file name.
     * The check runs on the EDT.
     */
    fun <T : PolySymbol> doResolveSymbolTest(
        signature: String,
        expectedClass: Class<T>,
        expectedName: String,
        chosenTestName: String? = null,
    ) {
        val dirName = chosenTestName ?: testName
        val fileName = (chosenTestName ?: testName).plus(".$defaultExtension")
        doConfiguredTest(dirName = dirName, configureFileName = fileName) {
            // Unwrap PolySymbolDelegate wrappers (e.g. an aliased-name symbol from withName(), used
            // wherever the querying text differs from the resolved symbol's own name) the same way production
            // code does via GdSymbolResolverUtil.resolveSymbolReferences() - resolveSymbolReference()
            // itself does not.
            val referenced = resolveSymbolReference(signature)
            val resolved = (referenced as? PolySymbol)?.unwrapAllDelegates() ?: referenced
            val symbol = assertInstanceOf(expectedClass, resolved)
            assertEquals(expectedName, symbol.name)
        }
    }
}