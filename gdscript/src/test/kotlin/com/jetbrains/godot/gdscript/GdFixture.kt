package com.jetbrains.godot.gdscript

import com.intellij.openapi.application.PathManager
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.testFramework.HybridFixtureContext
import com.intellij.polySymbols.testFramework.HybridTestMode
import com.intellij.polySymbols.testFramework.PolySymbolsFixture
import com.intellij.polySymbols.testFramework.hybridFixture
import com.intellij.polySymbols.testFramework.resolveSymbolReference
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.testFramework.junit5.fixture.TestFixture
import org.jetbrains.annotations.TestOnly
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import java.nio.file.Path
import kotlin.io.path.pathString

/**
 * Creates a [GdFixture] for the test data directory [testCasePath]. Declare the result as an instance property.
 * Each test gets a new heavy project.
 */
@TestOnly
fun gdFixture(testCasePath: String): TestFixture<GdFixture> = gdFixture { GdFixture(it, testCasePath) }

/**
 * Like [gdFixture], but on the light project of `BasePlatformTestCase`, kept open across all tests in the class
 * instead of a new heavy project per test. Use for tests migrated from `BasePlatformTestCase` that do not need
 * the heavy `CodeInsightFixture` project (no SDK) - it is much faster. Declare the result as an instance property.
 */
@TestOnly
fun gdLightFixture(testCasePath: String): TestFixture<GdFixture> =
    hybridFixture({ HybridTestMode.BasePlatform }) { GdFixture(it, testCasePath) }

/**
 * Creates a sub-fixture of [GdFixture] with [create]. Each test gets a new heavy project.
 */
@TestOnly
fun <T : GdFixture> gdLightFixture(create: (HybridFixtureContext) -> T): TestFixture<T> =
    hybridFixture({ HybridTestMode.BasePlatform }, create)

/**
 * Creates a sub-fixture of [GdFixture] with [create]. Each test gets a new heavy project.
 */
@TestOnly
fun <T : GdFixture> gdFixture(create: (HybridFixtureContext) -> T): TestFixture<T> =
    hybridFixture({ HybridTestMode.CodeInsightFixture}, create)

/**
 * The fixture of the GDScript PolySymbol feature tests, for example completion, highlighting and navigation.
 * It is a [PolySymbolsFixture] with the GDScript test data root, the `gd` extension, and the directory mode.
 * Call a member from a background thread. For a model-only test, use [GdModelFixture].
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

internal fun getGdTestDataPath(testClass: Class<*>): String {
    val home = PathManager.getHomeDirFor(testClass)
    val base: Path = home?.resolve("dotnet/Plugins/godot-support/gdscript/src/test")
        ?: PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test")
    return base.resolve("testData").pathString
}