package com.jetbrains.godot.gdscript.resolve

class ResolveNestedClassErrorsTest : ResolveTestBase() {
    fun testUnresolvableMembersOnNestedClasses() {
        val file = loadByTestName()
        val annotated = dumpResolvesWithInlineMarkers(file)
        assertGold(annotated)
    }
}
