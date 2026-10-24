package com.jetbrains.godot.tscn.toolWindow.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junitpioneer.jupiter.cartesian.CartesianTest
import tscn.toolWindow.model.SceneTreeEditorDropHandler.Companion.CaretInfo
import tscn.toolWindow.model.SceneTreeEditorDropHandler.Companion.DropBehavior
import tscn.toolWindow.model.SceneTreeEditorDropHandler.Companion.decideDropBehavior

class SceneTreeEditorDropBehaviorTest {
    @CartesianTest(name = CartesianTest.ARGUMENTS_PLACEHOLDER)
    fun dropInsideSelectionReplacesIt(
        @CartesianTest.Values(ints = [10, 12, 13]) offset: Int,
        @CartesianTest.Values(strings = ["", " \t ", "abcdef"]) line: String,
    ) {
        assertEquals(
            DropBehavior.ReplaceSelection,
            decideDropBehavior(CaretInfo(true, 10, 14, offset, 5), line),
        )
    }

    @ParameterizedTest
    @CsvSource(
        "9, 0, LineAbove",
        "14, 0, LineAbove",
        "15, 0, LineAbove",
        "9, 6, LineBellow",
        "14, 6, LineBellow",
        "15, 6, LineBellow",
    )
    fun dropOutsideSelectionUsesCaretColumn(offset: Int, caretColumn: Int, expected: DropBehavior) {
        assertEquals(expected, decideDropBehavior(CaretInfo(true, 10, 14, offset, caretColumn), "abcdef"))
    }

    @Test
    fun checkDropForLinesWithTabs() {
        assertEquals(DropBehavior.LineAbove, decideDropBehavior(CaretInfo(true, 10, 14, 4, 0), "\t\tabcdef"))
    }

    @ParameterizedTest
    @CsvSource("0, LineAbove", "6, LineBellow")
    fun inactiveSelectionDoesNotReplaceText(caretColumn: Int, expected: DropBehavior) {
        assertEquals(expected, decideDropBehavior(CaretInfo(false, 10, 14, 12, caretColumn), "abcdef"))
    }

    @ParameterizedTest
    @CsvSource("0, LineAbove", "6, LineBellow", "12, LineBellow")
    fun emptySelectionDoesNotReplaceText(caretColumn: Int, expected: DropBehavior) {
        assertEquals(expected, decideDropBehavior(CaretInfo(true, 12, 12, 12, caretColumn), "abcdef"))
    }

    @CartesianTest(name = CartesianTest.ARGUMENTS_PLACEHOLDER)
    fun blankLineUsesCurrentLine(
        @CartesianTest.Values(booleans = [true, false]) hasSelection: Boolean,
        @CartesianTest.Values(strings = ["", " ", "\t", " \t "]) line: String,
    ) {
        assertEquals(
            DropBehavior.CurrentLine,
            decideDropBehavior(CaretInfo(hasSelection, 10, 14, 14, 0), line),
        )
    }

    @ParameterizedTest
    @CsvSource(
        "abcdef, 0, LineAbove",
        "abcdef, 2, LineAbove",
        "abcdef, 3, LineBellow",
        "abcdef, 4, LineBellow",
        "abcdef, 6, LineBellow",
        "abcde, 0, LineAbove",
        "abcde, 1, LineAbove",
        "abcde, 2, LineAbove",
        "abcde, 3, LineBellow",
        "abcde, 4, LineBellow",
        "abcde, 5, LineBellow",
        "a, 0, LineAbove",
        "a, 1, LineBellow",
    )
    fun caretColumnChoosesLine(line: String, caretColumn: Int, expected: DropBehavior) {
        assertEquals(expected, decideDropBehavior(CaretInfo(false, 0, 0, 100 + caretColumn, caretColumn), line))
    }
}
