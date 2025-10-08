package com.jetbrains.godot.gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition
import java.io.IOException

abstract class GdParsingTestCase : ParsingTestCase("", "gd", GdParserDefinition()) {

    // I want to check ensureNoErrorElements first
    protected override fun doTest(checkResult: Boolean, ensureNoErrorElements: Boolean) {
        if (!ensureNoErrorElements) {
            super.ensureNoErrorElements()
        }

        val name = this.testName
        try {
            val text = this.loadFile(name + "." + this.myFileExt)
            this.parseFile(name, text)
            if (checkResult) {
                try {
                    this.checkResult(name, this.myFile)
                }
                finally {
                    if (ensureNoErrorElements) {
                        this.ensureNoErrorElements()
                    }
                }
            } else {
                toParseTreeText(this.myFile, this.skipSpaces(), this.includeRanges())
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}