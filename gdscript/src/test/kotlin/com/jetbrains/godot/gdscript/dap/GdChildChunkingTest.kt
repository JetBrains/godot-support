package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.ValueKind
import com.jetbrains.dap.protocol.VariableAttribute
import gdscript.dap.remote.GdArrayReader
import gdscript.dap.remote.GdChildChunking
import gdscript.dap.remote.GdChildRange
import gdscript.dap.remote.GdNodeDescriptor
import gdscript.dap.remote.GdDapExpressions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

class GdChildChunkingTest {
    private fun rows(start: Int, length: Int): Sequence<GdChildRange> =
        GdChildChunking.splitIntoChunks(start, length, createGroup = { it })

    @ParameterizedTest
    @MethodSource("groupBoundaries")
    fun testGroupsAtFirstBoundary(start: Int, length: Int, expected: List<String>) {
        assertEquals(expected, labels(start, length))
    }

    @ParameterizedTest
    @MethodSource("nestedBoundaries")
    fun testNestedBoundaries(start: Int, length: Int, count: Int, first: String, last: String) {
        val groups = rows(start, length).toList()
        val firstRange = groups.first()
        assertAll(
            { assertEquals(count, groups.size) },
            { assertEquals(first, groups.first().label) },
            { assertEquals(last, groups.last().label) },
            { assertEquals(100, if (firstRange.length <= GdChildChunking.CHUNK_SIZE) firstRange.length
                else rows(firstRange.startIndex, firstRange.length).count()) },
        )
    }

    @ParameterizedTest
    @CsvSource("31, 10001", "31, 1000001")
    fun testRangesCoverEveryIndexWithoutGapsOrOverlaps(start: Int, length: Int) {
        assertEquals(length, coveredLength(start, length))
    }

    private fun coveredLength(start: Int, length: Int): Int {
        if (length <= 100) return length
        val level = rows(start, length).toList()
        assertTrue(level.size <= 100)
        var covered = 0
        for (range in level) {
            assertTrue(range.length in 1 until length)
            assertEquals(start + covered, range.startIndex)
            covered += coveredLength(range.startIndex, range.length)
        }
        assertEquals(length, covered)
        return covered
    }

    @Test
    fun testNothingBuiltUntilConsumed() {
        var consume = false
        val sequence = GdChildChunking.splitIntoChunks(0, 101,
            createGroup = {
                check(consume) { "The sequence constructed a group before consumption" }
                check(it.startIndex == 0) { "The sequence constructed an unconsumed sibling" }
                it
            })
        consume = true
        assertEquals(GdChildRange(0, 100), sequence.first())
    }

    @Test
    fun testExpandingNestedChunkKeepsTheSelectedRange() {
        val outer = rows(17, 10_001).first()
        val inner = rows(outer.startIndex, outer.length).first()
        assertEquals(GdChildRange(17, 100), inner)
    }

    @Test
    fun testDescriptorExpressionUsesOnlyRequestedAbsoluteIndices() {
        val expression = GdDapExpressions.descriptors(42L, 10, 2)
        val expected = (10..11).flatMap { index ->
            val child = "instance_from_id(42).get_child($index)"
            listOf("$child.get_instance_id()", "$child.name", "$child.get_class()")
        }.joinToString(", ", "[", "]")
        assertEquals(expected, expression)
    }

    @Test
    fun testReaderCapsDescriptorsAndPreservesIdentity() {
        val values = listOf(variable("size", "9"), variable("0", "101"), variable("1", "First"),
                            variable("2", "Node"), variable("3", "102"), variable("4", "Second"),
                            variable("5", "Node"), variable("6", "103"), variable("7", "Extra"), variable("8", "Node"))
        val reader = GdArrayReader(values)
        val descriptors = reader.readDescriptors(10, 2)
        assertEquals(listOf(
            GdNodeDescriptor(objectId = 101L, nodeName = "First", className = "Node"),
            GdNodeDescriptor(objectId = 102L, nodeName = "Second", className = "Node")
        ), descriptors)
    }

    @ParameterizedTest
    @MethodSource("descriptorCases")
    fun testDescriptorReadBounds(
        fromIndex: Int, count: Int, expectedIndices: List<Int>, entries: List<Pair<String, String>>
    ) {
        val result = GdArrayReader(entries.map { variable(it.first, it.second) }).readDescriptors(fromIndex, count)
        val expectedIds = expectedIndices.map { 101L + (it.toLong() - fromIndex) }
        assertEquals(expectedIds, result.map { it.objectId })
        assertEquals(expectedIds.map { "Node$it" }, result.map { it.nodeName })
        assertTrue(result.all { it.className == "Node" })
    }

    @Test
    fun testSurplusCheckIncludesDeclaredAndIndexedElements() {
        val declared = GdArrayReader(listOf(variable("size", "9")))
        val indexed = GdArrayReader(listOf(variable("size", "6"), variable("6", "103")))
        val exact = GdArrayReader(listOf(variable("size", "6"), variable("5", "Node")))
        assertAll(
            { assertTrue(declared.hasElementsBeyondDescriptorCount(2)) },
            { assertFalse(declared.hasElementsBeyondDescriptorCount(3)) },
            { assertTrue(indexed.hasElementsBeyondDescriptorCount(2)) },
            { assertFalse(exact.hasElementsBeyondDescriptorCount(2)) },
        )
    }

    @ParameterizedTest
    @MethodSource("childCountCases")
    fun testChildCountRequiresOneElement(expected: Int?, entries: List<Pair<String, String>>) {
        assertEquals(expected, GdArrayReader(entries.map { variable(it.first, it.second) }).readChildCount())
    }

    private fun labels(start: Int, length: Int): List<String> = rows(start, length).map { it.label }.toList()

    private fun variable(nameArg: String, valueArg: String): DapVariable = object : DapVariable {
        override val name = nameArg
        override val value = valueArg
        override val type = "String"
        override val evaluateName: String? = null
        override val kind: ValueKind? = null
        override val attributes: List<VariableAttribute>? = null
    }

    companion object {
        @JvmStatic
        private fun groupBoundaries(): List<Arguments> = listOf(
            Arguments.of(0, 101, listOf("[0..99]", "[100..100]")),
            Arguments.of(17, 101, listOf("[17..116]", "[117..117]")),
        )

        @JvmStatic
        private fun nestedBoundaries(): List<Arguments> = listOf(
            Arguments.of(0, 10_000, 100, "[0..99]", "[9900..9999]"),
            Arguments.of(0, 10_001, 2, "[0..9999]", "[10000..10000]"),
            Arguments.of(0, 1_000_001, 2, "[0..999999]", "[1000000..1000000]"),
        )

        @JvmStatic
        private fun descriptorCases(): List<Arguments> {
            val first = listOf("0" to "101", "1" to "Node101", "2" to "Node")
            val second = listOf("3" to "102", "4" to "Node102", "5" to "Node")
            val later = listOf("6" to "103", "7" to "Node103", "8" to "Node")
            return listOf(
                Arguments.of(10, 2, listOf(10), listOf("size" to Int.MAX_VALUE.toString()) + first),
                Arguments.of(10, 2, listOf(10), first + (Int.MAX_VALUE.toString() to "101")),
                Arguments.of(10, 3, listOf(10, 12), listOf("size" to "9") + first + later),
                Arguments.of(10, 1, emptyList<Int>(), listOf("size" to "-1") + first),
                Arguments.of(10, 1, emptyList<Int>(), listOf("size" to "3") + first.dropLast(1)),
                Arguments.of(10, 3, listOf(10), listOf("size" to "3") + first + later),
                Arguments.of(10, 3, listOf(12), listOf("size" to "9") + first.dropLast(1) + later),
                Arguments.of(10, 2, listOf(10), listOf("size" to "4") + first + second),
                Arguments.of(10, 1, listOf(10), listOf("size" to "bad") + first),
                Arguments.of(10, 1, emptyList<Int>(), listOf("size" to "3", "0" to "bad") + first.drop(1)),
                Arguments.of(10, 0, emptyList<Int>(), first),
                Arguments.of(Int.MAX_VALUE, 3, listOf(Int.MAX_VALUE), first + later),
            )
        }

        @JvmStatic
        private fun childCountCases(): List<Arguments> = listOf(
            Arguments.of(0, listOf("size" to "1", "0" to "0")),
            Arguments.of(null, listOf("size" to "2", "0" to "0", "1" to "5")),
            Arguments.of(null, listOf("size" to "1", "0" to "0", "1" to "5")),
            Arguments.of(null, listOf("0" to "0")),
            Arguments.of(null, listOf("size" to "1", "1" to "5")),
            Arguments.of(null, listOf("size" to "1", "0" to "-1")),
        )
    }
}
