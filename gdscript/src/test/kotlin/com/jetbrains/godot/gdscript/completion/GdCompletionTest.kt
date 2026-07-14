package com.jetbrains.godot.gdscript.completion

import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdCompletionTest : GdPolySymbolsTestCaseWithSdk("completion") {

    @Test
    fun testCompleteClassMembers() = doLookupTest(
        dir = false
    )

    @Test
    fun testCompletionAfterConstructedInnerClass() = doLookupTest(
        dir = false,
        fileContents = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.new().<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
            |            class D1:
            |                func pp():
            |                    pass
        """.trimMargin(),
    )

    @Test
    fun testCompletionAfterClass() = doLookupTest(
        dir = false,
        fileContents = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
        """.trimMargin(),
    )

    @Test
    fun testCompletionAfterClass2() = doLookupTest(
        dir = false,
        fileContents = """
            |class_name NestedClassErrors
            |
            |func in_the_outer_wrong():
            |    A1.B1.<caret>
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
        """.trimMargin(),
    )

    @Test
    fun testWithSdk01() = doLookupTest(
        dir = false,
        fileContents = """
            |func a():
            |	var previous_mode := Input.<caret> #anonymous enum in the static class context
        """.trimMargin(),
        lookupItemFilter = {
            it.displayText?.startsWith("MOUSE_") == true
        }
    )

    @Test
    fun testWithSdk02() = doLookupTest(
        dir = false,
        fileContents = """
            |func a():
            |	var previous_mode := Input.<caret> #anonymous enum in the static class context
        """.trimMargin(),
        lookupItemFilter = {
            it.displayText?.startsWith("action_") == true
        }
    )

}
