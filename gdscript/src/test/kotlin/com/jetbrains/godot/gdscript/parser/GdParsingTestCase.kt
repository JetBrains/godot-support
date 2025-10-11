package com.jetbrains.godot.gdscript.parser

import com.intellij.platform.testFramework.core.FileComparisonFailedError
import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition

abstract class GdParsingTestCase : ParsingTestCase("", "gd", GdParserDefinition()) {

    protected override fun doTest(checkResult: Boolean, ensureNoErrorElements: Boolean) {
        try {
            super.doTest(checkResult, ensureNoErrorElements)
        } catch (e: FileComparisonFailedError) {
            val expectedText = e.getExpectedStringPresentation()
            val actualText = e.getActualStringPresentation()
            val expectedPath = e.getFilePath()
            val actualPath = e.getActualFilePath()

            val details = buildString {
                append("\n")
                if (e.isExpectedDefined && expectedPath != null) {
                    append("expected file: ").append(expectedPath)
                }
                else{
                    append("expected: \n").append(expectedText)
                    append("\n---\n")
                }
                if (e.isActualDefined && actualPath != null) {
                    append("actual file: ").append(actualPath)
                } else {
                    append("actual: \n").append(actualText)
                }
            }

            val newMessage = (e.message ?: "") + details
            throw FileComparisonFailedError(newMessage, expectedText, actualText, expectedPath, actualPath)
        }
    }
}