package com.jetbrains.godot

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo

/**
 * JUnit5-native replacement for [com.intellij.testFramework.fixtures.BasePlatformTestCase].
 *
 * Uses IdeaTestFixtureFactory for test fixtures. The IntelliJ application is initialised
 * by the fixture's own setUp() — no @TestApplication or TestCase inheritance needed.
 * This allows JUnit5 annotations (@Disabled, @ParameterizedTest, etc.) to work correctly.
 */
abstract class GdCodeInsightTestBase {

    protected lateinit var myFixture: CodeInsightTestFixture
    private lateinit var _testInfo: TestInfo
    private lateinit var _disposable: Disposable

    protected val project: Project get() = myFixture.project
    protected val testRootDisposable: Disposable get() = _disposable

    protected open fun getTestDataPath(): String? = null

    @BeforeEach
    fun setUpCodeInsightFixture(testInfo: TestInfo) {
        _testInfo = testInfo
        _disposable = Disposer.newDisposable("GdCodeInsightTestBase-${testInfo.displayName}")
        val factory = IdeaTestFixtureFactory.getFixtureFactory()
        val projectBuilder = factory.createLightFixtureBuilder(
            LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR,
            testInfo.displayName,
        )
        myFixture = factory.createCodeInsightFixture(projectBuilder.fixture)
        getTestDataPath()?.let { myFixture.testDataPath = it }
        myFixture.setUp()
    }

    @AfterEach
    fun tearDownCodeInsightFixture() {
        myFixture.tearDown()
        Disposer.dispose(_disposable)
    }

    /**
     * Returns the test method name with the "test" prefix stripped,
     * matching the JUnit3-era [com.intellij.testFramework.UsefulTestCase.getTestName] convention.
     */
    protected fun getTestName(lowercaseFirstLetter: Boolean): String {
        val raw = _testInfo.testMethod.get().name
        val stripped = if (raw.startsWith("test")) raw.removePrefix("test") else raw
        return if (lowercaseFirstLetter) stripped.replaceFirstChar { it.lowercaseChar() } else stripped
    }
}
