package com.jetbrains.godot.tscn.toolWindow.model

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import tscn.toolWindow.model.SceneNodePathResolver

class SceneNodePathResolverTest : BasePlatformTestCase() {
    private data class Case(
        val start: String,
        val end: String,
        val dragNode: String,
        val srcNode: String,
        val expected: String
    )

    @Test
    fun testConstructRelativePath_cases() {
        val cases = listOf(
            Case(start = "", end = ".", dragNode = "Nested1", srcNode = "Root", expected = $$"$Nested1"),
            Case(start = "", end = "", dragNode = "Root", srcNode = "Root", expected = "$\".\""),
            Case(start = ".", end = ".", dragNode = "Nested", srcNode = "Nested", expected = "$\".\""),
            Case(start = "Nested", end = "Nested", dragNode = "Nested1", srcNode = "Nested1", expected = "$\".\""),
            Case(start = "", end = ".", dragNode = "Nested", srcNode = "Root", expected = $$"$Nested"),
            Case(start = "Nested", end = ".", dragNode = "Nested", srcNode = "Nested1.1", expected = "$\"..\""),
            Case(
                start = "Nested/Nested1.1",
                end = "Nested",
                dragNode = "Nested1.1",
                srcNode = "Nested1.1.1",
                expected = "$\"..\""
            ),
            Case(
                start = ".",
                end = ".",
                dragNode = "Nested2",
                srcNode = "Nested1",
                expected = "$\"../Nested2\""
            ),
            Case(
                start = ".",
                end = ".",
                dragNode = "nested",
                srcNode = "Nested",
                expected = "$\"../nested\""
            ),
            Case(
                start = "Nested",
                end = "Nested",
                dragNode = "Nested1.2",
                srcNode = "Nested1.1",
                expected = "$\"../Nested1.2\""
            ),
            Case(
                start = "Nested/Nested1.1",
                end = ".",
                dragNode = "Nested",
                srcNode = "Nested1.1.1",
                expected = "$\"../..\""
            ),
            Case(
                start = "Nested/Nested1.1",
                end = "",
                dragNode = "Root",
                srcNode = "Nested1.1.1",
                expected = "$\"../../..\""
            ),
            Case(
                start = "",
                end = "Nested1",
                dragNode = "Nested1.1",
                srcNode = "Root",
                expected = $$"$Nested1/Nested1.1"
            ),
            Case(
                start = "",
                end = "Nested1/Nested1.1",
                dragNode = "Nested1.1.1",
                srcNode = "Root",
                expected = $$"$Nested1/Nested1.1/Nested1.1.1"
            ),
            Case(
                start = ".",
                end = "",
                dragNode = "Root",
                srcNode = "Nested",
                expected = "$\"..\""
            ),
            Case(
                start = "Nested",
                end = "",
                dragNode = "Root",
                srcNode = "Nested1.1",
                expected = "$\"../..\""
            ),
            Case(
                start = "Nested/Nested1.1",
                end = "",
                dragNode = "Root",
                srcNode = "Nested1.1.1",
                expected = "$\"../../..\""
            ),

            Case(
                start = "Nested1",
                end = ".",
                dragNode = "Nested1",
                srcNode = "Nested1.1",
                expected = "$\"..\""
            ),

            Case(
                start = "Nested1/Nested1.1/Nested1.1.1",
                end = "Nested1",
                dragNode = "Nested1.1",
                srcNode = "Nested1.1.1.1",
                expected = "$\"../..\""
            ),

            Case(
                start = "Nested1/Nested1.1/Nested1.1.1",
                end = "Nested1/Nested2.1/Nested2.1.1",
                dragNode = "Nested2.1.1.1",
                srcNode = "Nested1.1.1.1",
                expected = "$\"../../../Nested2.1/Nested2.1.1/Nested2.1.1.1\""
            ),
            Case(
                start = "Nested/Nested1.1",
                end = "Nested/Nested2.1",
                dragNode = "Nested2.1.1",
                srcNode = "Nested1.1.1",
                expected = "$\"../../Nested2.1/Nested2.1.1\""
            ),
            Case(
                start = "Nested/Nested1.1",
                end = "Nested",
                dragNode = "Nested2.1",
                srcNode = "Nested1.1.1",
                expected = "$\"../../Nested2.1\""
            ),
            Case(
                start = "Nested/Nested1.1",
                end = "Nested/Nested2.1",
                dragNode = "Nested1.1",
                srcNode = "Nested1.1.1",
                expected = "$\"../../Nested2.1/Nested1.1\""
            ),

            )

        cases.forEachIndexed { i, c ->
            val actual = SceneNodePathResolver.constructRelativePath(c.start, c.end, c.dragNode, c.srcNode)
            assertEquals("Case #$i failed: $c", c.expected, actual)
        }
    }

}