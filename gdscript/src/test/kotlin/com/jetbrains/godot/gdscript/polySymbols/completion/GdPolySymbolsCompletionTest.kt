package com.jetbrains.godot.gdscript.polySymbols.completion

import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCaseWithSdk
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdPolySymbolsCompletionTest : GdPolySymbolsTestCaseWithSdk("completion") {

    @Test
    @Ignore("Completion not implemented yet")
    fun testCompleteClassMembers() = doLookupTest() //TODO change items.txt once we have completion
}