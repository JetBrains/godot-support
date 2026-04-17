package com.jetbrains.godot.tscn.parser

import com.intellij.psi.impl.DebugUtil
import com.jetbrains.godot.GdCodeInsightTestBase
import org.opentest4j.AssertionFailedError
import java.io.File

/**
 * JUnit5-native replacement for TSCN/TRES parser tests that previously extended
 * [com.intellij.testFramework.ParsingTestCase] with skipSpaces=false and includeRanges=true.
 *
 * @param fileExt the file extension of the source test data files (e.g. "tscn" or "tres")
 */
abstract class TscnParsingTestBase(private val fileExt: String) : GdCodeInsightTestBase() {

    /**
     * Parses the test data file and compares the PSI tree against a golden .txt file.
     * TSCN/TRES parser tests use includeRanges=true (like the original ParsingTestCase subclasses).
     */
    protected fun doTest(checkResult: Boolean) {
        val testName = getTestName(false)
        val testDataPath = myFixture.testDataPath
        val text = File("$testDataPath/$testName.$fileExt").readText()

        // Create an in-memory PSI file (same as ParsingTestCase.createFile)
        val file = myFixture.configureByText("$testName.$fileExt", text)

        if (checkResult) {
            // TSCN/TRES tests: skipSpaces=false, includeRanges=true
            val actual = DebugUtil.psiToString(file, false, true)
            val expectedFile = File("$testDataPath/$testName.txt")

            if (!expectedFile.exists()) {
                expectedFile.writeText(actual)
                throw AssertionError(
                    "Golden file ${expectedFile.absolutePath} did not exist — created from actual output. Re-run the test."
                )
            }

            val expected = expectedFile.readText()
            if (expected != actual) {
                throw AssertionFailedError(
                    "Parser tree mismatch for $testName — expected file: ${expectedFile.absolutePath}",
                    expected, actual
                )
            }
        }
    }
}
