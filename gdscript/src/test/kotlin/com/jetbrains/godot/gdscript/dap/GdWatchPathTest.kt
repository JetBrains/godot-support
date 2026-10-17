package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapVariable
import com.intellij.testFramework.junit5.TestApplication
import gdscript.dap.remote.GdLocalExpressions
import gdscript.dap.remote.GdObjectMemberRecovery
import gdscript.dap.remote.GdOwnerExpression
import gdscript.dap.remote.GdValueKind
import gdscript.dap.remote.GdWatchNames
import gdscript.dap.remote.GdWatchPath
import org.jetbrains.concurrency.Promise
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.TimeUnit

@TestApplication
class GdWatchPathTest {
    private val self = GdWatchPath.nodeProperties("self")

    @ParameterizedTest
    @ValueSource(strings = ["Locals", "Members", "Globals"])
    fun testScopeRoots(scope: String) {
        val paths = GdWatchPath.scope(scope, listOf(variable("player", "Object"), variable("health", "int")))
        val prefix = if (scope == "Members") "self." else ""
        assertEquals(listOf("${prefix}player", "${prefix}health"), paths.map { it.text })
        val member = paths.first().children(variable("player", "Object"), listOf(variable("hp", "int"))).single()
        assertEquals("${prefix}player.hp", member.text)
    }

    @Test
    fun testSelfAndHiddenGlobals() {
        assertEquals("self", GdWatchPath.scope("Members", listOf(variable("self", "Object"))).single().text)
        val rows = listOf(variable("player", "Object"), variable("visible", "Object"))
        assertEquals(listOf(null, "visible"), GdWatchPath.scope("Globals", rows, setOf("player")).map { it.text })
        assertTrue(GdWatchPath.scope("Globals", rows, null).all { it.text == null })
        assertTrue(GdWatchPath.scope("Unknown", rows).all { it.text == null })
    }

    @ParameterizedTest
    @ValueSource(strings = ["hash", "if", "self", "await", "true", "null", "get", "set", "inf", "nan", "Vector2", "Basis",
        "PackedByteArray", "NodePath", "PI"])
    fun testReservedNamesOnAllBareRoots(name: String) {
        for (scope in listOf("Locals", "Globals")) {
            assertNull(GdWatchPath.scope(scope, listOf(variable(name, "int"))).single().text)
        }
    }

    @Test
    fun testReservedMemberTokens() {
        val names = listOf("in", "and", "or", "not", "self", "true", "false", "null", "PI", "TAU", "INF", "NAN",
                           "Nil", "bool", "int", "float", "String", "StringName", "NodePath", "RID", "Object", "Callable", "Signal",
                           "Dictionary", "Array") + GdValueKind.mathTypes + GdValueKind.arrayTypes
        for (name in names) {
            assertFalse(GdWatchNames.member(name), name)
            assertNull(self.properties("Members", listOf(variable(name, "int"))).single().text, name)
            if (name != "self") assertNull(GdWatchPath.scope("Members", listOf(variable(name, "int"))).single().text, name)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["hash", "sin", "print", "if", "await", "get", "set", "inf", "nan"])
    fun testIdentifierAndUtilityMemberTokens(name: String) {
        assertTrue(GdWatchNames.member(name))
        assertEquals("self.$name", self.properties("Members", listOf(variable(name, "int"))).single().text)
        assertEquals("self.$name", GdWatchPath.scope("Members", listOf(variable(name, "int"))).single().text)
    }

    @ParameterizedTest
    @ValueSource(strings = ["тест", "тест_1", "变量", "é", "\uD801\uDC00"])
    fun testUnicodeWatchNamesKeepOwnerRules(name: String) {
        assertTrue(GdWatchNames.bare(name))
        assertTrue(GdWatchNames.member(name))
        for (scope in listOf("Locals", "Globals", "Members")) {
            val prefix = if (scope == "Members") "self." else ""
            assertEquals(prefix + name, GdWatchPath.scope(scope, listOf(variable(name, "int"))).single().text)
        }
        assertEquals("self.$name", self.properties("Members", listOf(variable(name, "int"))).single().text)
        assertEquals("(result).$name", GdWatchPath.evaluated("result")
            .children(variable("result", "Object"), listOf(variable(name, "int"))).single().text)
        assertNull(GdLocalExpressions.name(name))
    }

    @ParameterizedTest
    @ValueSource(strings = ["☃", "emoji😀", "a\u200C", "a\u200D", "a\u0000", "\u0301a", "e\u0301", "\uFF41", "\uD800", "\$тест",
        "\u2E2F", "a\u2E2F"])
    fun testConservativeUnicodeRejections(name: String) {
        assertFalse(GdWatchNames.bare(name))
        assertFalse(GdWatchNames.member(name))
        assertNull(GdWatchPath.scope("Locals", listOf(variable(name, "int"))).single().text)
        assertNull(self.properties("Members", listOf(variable(name, "int"))).single().text)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a-b", "1a", "metadata/hp", "base.gd/nested/hp", "has space", "a\"b", "a\nb"])
    fun testInvalidMemberTokens(name: String) {
        assertFalse(GdWatchNames.member(name))
        assertNull(self.properties("Members", listOf(variable(name, "int"))).single().text)
    }

    @Test
    fun testMembersPrefixRulesDoNotChangeOwnerRules() {
        val rows = listOf("base.gd/a", "scene.tscn::GDScript_1/b", "/c", "metadata/hp", "base.gd/nested/hp", "d")
            .map { variable(it, "String") }
        assertEquals(listOf("self.a", "self.b", "self.c", null, null, "self.d"), self.properties("Members", rows).map { it.text })
        assertEquals(listOf("a", "scene.tscn::GDScript_1/b", "/c", "metadata/hp", "nested/hp", "d"),
                     GdObjectMemberRecovery.readablePropertyNames("Members", rows))
        assertEquals(listOf("self.a", "self.b", "self.c", null, null, "self.d"), GdWatchPath.scope("Members", rows).map { it.text })
        for (category in listOf("Constants", "Node", "player.gd", "Theme")) {
            assertTrue(self.properties(category, rows.take(5)).all { it.text == null })
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["base.gd/hp", "scene.tscn::1/hp", "/hp"])
    fun testAmbiguousInheritedAndToolProperties(label: String) {
        val rows = listOf(variable(label, "int"), variable("hp", "int"), variable("metadata/hp", "int"))
        assertTrue(self.properties("Members", rows).all { it.text == null })
        assertTrue(GdWatchPath.scope("Members", rows).all { it.text == null })
    }

    @Test
    fun testCategoriesPassTheirPathThroughNestedCategories() {
        val category = self.children(variable("self", "Object"), listOf(structuredVariable("player.gd", "Category"))).single()
        assertNull(category.text)
        val nested = category.properties("player.gd", listOf(structuredVariable("Members", "Category"))).single()
        assertNull(nested.text)
        assertEquals("self.hp", nested.properties("Members", listOf(variable("base.gd/hp", "int"))).single().text)
        assertNull(nested.properties("Constants", listOf(variable("MAX", "int"))).single().text)
        assertTrue(nested.properties("Node", listOf(variable("path", "NodePath"), variable("multiplayer_authority", "int")))
                       .all { it.text == null })
    }

    @ParameterizedTest
    @ValueSource(strings = ["Array", "PackedByteArray", "PackedInt32Array", "PackedInt64Array", "PackedFloat32Array", "PackedFloat64Array",
        "PackedStringArray", "PackedVector2Array", "PackedVector3Array", "PackedColorArray", "PackedVector4Array"])
    fun testArrayIndicesAndSize(type: String) {
        val array = self.properties("Members", listOf(variable("items", type))).single()
        val paths = array.children(variable("items", type), listOf(variable("size", "int"), variable("0", "Object"), variable("3", "Object")))
        assertEquals(listOf("self.items.size()", "self.items[0]", "self.items[3]"), paths.map { it.text })
        assertEquals("self.items[3].hp", paths.last().children(variable("3", "Object"), listOf(variable("hp", "int"))).single().text)
        assertEquals("(self.items)[3]", GdOwnerExpression("self.items").child("Array", "3", 1)?.text)
        for (name in listOf("-1", "+1", "01", "-0", "word", "2147483648")) {
            assertNull(array.children(variable("items", type), listOf(variable(name, "int"))).single().text)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["player + other", "get_node_or_null(\"Player\")", "get_node_or_null(\"%Player\").inventory"])
    fun testEvaluatedRootsUseParentheses(expression: String) {
        val root = GdWatchPath.evaluated(expression)
        assertEquals(expression, root.text)
        assertEquals("($expression).hp", root.children(variable("result", "Object"), listOf(variable("hp", "int"))).single().text)
        assertEquals(listOf("($expression).size()", "($expression)[0]"),
                     root.children(variable("result", "Array"), listOf(variable("size", "int"), variable("0", "int"))).map { it.text })
    }

    @Test
    fun testTypeResolutionDoesNotMistakeMathTextForAnArray() {
        val rows = listOf(variable("x", "float"), variable("y", "float"), variable("w", "float"), variable("h", "float"))
        val root = GdWatchPath.evaluated("rect")
        for (type in listOf(null, "Nil")) {
            val rectangle = untyped(type, "[P: (0, 0), S: (1, 1)]")
            assertNull(GdValueKind.type(rectangle))
            assertEquals("Rect2", GdValueKind.type(rectangle, rows))
            assertEquals(listOf("(rect).position.x", "(rect).position.y", "(rect).size.x", "(rect).size.y"),
                         root.children(rectangle, rows).map { it.text })
            val dictionary = untyped(type, "{x: 0, y: 0, w: 1, h: 1}")
            assertEquals("Dictionary", GdValueKind.type(dictionary))
            assertTrue(root.children(dictionary, rows).all { it.role == "dictionary entry" && it.text == null })
            val objectValue = untyped(type, "<EncodedObjectAsID#42>")
            assertEquals("Object", GdValueKind.type(objectValue))
            assertEquals("(rect).hp", root.children(objectValue, listOf(variable("hp", "int"))).single().text)
        }
    }

    // Each case checks math paths, exact row matching, and the priority of a declared type or wrapper.
    @Test
    fun testVector2MappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Vector2", "x y", "float float", "x y")
    }

    @Test
    fun testVector2iMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Vector2i", "x y", "int int", "x y")
    }

    @Test
    fun testVector3MappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Vector3", "x y z", "float float float", "x y z")
    }

    @Test
    fun testVector3iMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Vector3i", "x y z", "int int int", "x y z")
    }

    @Test
    fun testRect2MappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Rect2", "x y w h", "float float float float", "position.x position.y size.x size.y")
    }

    @Test
    fun testRect2iMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Rect2i", "x y w h", "int int int int", "position.x position.y size.x size.y")
    }

    @Test
    fun testTransform2DMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Transform2D", "x y origin", "Vector2 Vector2 Vector2", "x y origin")
    }

    @Test
    fun testAabbMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("AABB", "position size", "Vector3 Vector3", "position size")
    }

    @Test
    fun testPlaneMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Plane", "d normal", "float Vector3", "d normal")
    }

    @Test
    fun testQuaternionMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Quaternion", "x y z w", "float float float float", "x y z w")
    }

    @Test
    fun testColorMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Color", "r g b a", "float float float float", "r g b a")
    }

    @Test
    fun testBasisMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Basis", "x y z", "Vector3 Vector3 Vector3", "transposed().x transposed().y transposed().z")
    }

    @Test
    fun testTransform3DMappingsAndExactTypeResolution() {
        assertMathMappingsAndExactTypeResolution("Transform3D", "basis origin", "Basis Vector3", "basis origin")
    }

    private fun assertMathMappingsAndExactTypeResolution(type: String, names: String, types: String, members: String) {
        val childTypes = types.split(' ')
        val rows = names.split(' ').mapIndexed { index, name -> variable(name, childTypes[index]) }
        val text = mathValueText(type)
        val parents = listOf(variable("value", type, text), untyped(null, text), untyped("Nil", text))
        for (parent in parents) {
            assertEquals(type, GdValueKind.type(parent, rows))
            val paths = GdWatchPath.evaluated("value + other").children(parent, rows)
            assertEquals(members.split(' ').map { "(value + other).$it" }, paths.map { it.text })
            val property = self.properties("Members", listOf(parentWithName(parent, "value"))).single()
            assertEquals(members.split(' ').map { "self.value.$it" }, property.children(parent, rows).map { it.text })
            for (path in paths) {
                assertEquals(path.text, path.calculateEvaluationExpression { error("A math path must not log") }
                    .blockingGet(0, TimeUnit.SECONDS)?.expression)
            }
        }
        val invalidRows = listOf(
            rows.reversed(), (if (type == "AABB") rows.dropLast(1) else rows.drop(1)), rows + variable("extra", "float"),
            listOf(variable(rows.first().name, "bool")) + rows.drop(1),
            listOf(variable("unknown", rows.first().type!!)) + rows.drop(1),
            listOf(object : DapVariable by rows.first() { override val type: String? = null }) + rows.drop(1),
        )
        for (rowsToReject in invalidRows) {
            for (unknownType in listOf(null, "Nil")) {
                val parent = untyped(unknownType, text)
                assertNull(GdValueKind.type(parent, rowsToReject))
                GdWatchPath.evaluated("value").children(parent, rowsToReject).forEach(::assertNoWatch)
            }
        }
        val unknownChild = variable("unknown", "float")
        assertNoWatch(GdWatchPath.evaluated("value").children(parents.first(), listOf(unknownChild)).single())
        val wrongType = variable(rows.first().name, "bool")
        assertNoWatch(GdWatchPath.evaluated("value").children(parents.first(), listOf(wrongType)).single())
        assertEquals("Custom", GdValueKind.type(variable("value", "Custom", text), rows))
        for (text in listOf("{math-shaped keys}", "<EncodedObjectAsID#42>")) {
            val parent = untyped(null, text)
            assertEquals(if (text.startsWith("{")) "Dictionary" else "Object", GdValueKind.type(parent, rows))
            if (text.startsWith("{")) GdWatchPath.evaluated("value").children(parent, rows).forEach(::assertNoWatch)
            else assertEquals(rows.map { "(value).${it.name}" },
                              GdWatchPath.evaluated("value").children(parent, rows).map { it.text })
        }
    }

    // A size row alone identifies an empty array when the type is absent or Nil.
    @ParameterizedTest
    @MethodSource("noUsableType")
    fun testUnknownEmptyArrayRequiresOnlySize(type: String?) {
        val parent = untyped(type, "[1, 2]")
        val rows = listOf(variable("size", "int", "0"))
        assertEquals("Array", GdValueKind.type(parent, rows))
        assertEquals(listOf("([1, 2]).size()"), GdWatchPath.evaluated("[1, 2]").children(parent, rows).map { it.text })
    }

    // A populated array requires a size row and consecutive indices from zero.
    @ParameterizedTest
    @MethodSource("noUsableType")
    fun testUnknownPopulatedArrayRequiresConsecutiveIndices(type: String?) {
        val parent = untyped(type, "[1, 2]")
        val rows = listOf(variable("size", "int", "2"), variable("0", "float"), variable("1", "Object"))
        assertEquals("Array", GdValueKind.type(parent, rows))
        assertEquals(listOf("([1, 2]).size()", "([1, 2])[0]", "([1, 2])[1]"),
                     GdWatchPath.evaluated("[1, 2]").children(parent, rows).map { it.text })
    }

    // Dictionary text takes priority over an array shape.
    @ParameterizedTest
    @MethodSource("noUsableType")
    fun testDictionaryWithOnlySizeDoesNotBecomeAnArray(type: String?) {
        val parent = untyped(type, "{size: 2, 0: 1, 1: 2}")
        val rows = listOf(variable("size", "int", "0"))
        assertEquals("Dictionary", GdValueKind.type(parent, rows))
        GdWatchPath.evaluated("[1, 2]").children(parent, rows).forEach(::assertNoWatch)
    }

    @ParameterizedTest
    @MethodSource("noUsableType")
    fun testDictionaryWithIndicesDoesNotBecomeAnArray(type: String?) {
        val parent = untyped(type, "{size: 2, 0: 1, 1: 2}")
        val rows = listOf(variable("size", "int", "2"), variable("0", "float"), variable("1", "Object"))
        assertEquals("Dictionary", GdValueKind.type(parent, rows))
        GdWatchPath.evaluated("[1, 2]").children(parent, rows).forEach(::assertNoWatch)
    }

    @Test
    fun testUnknownArrayRejectsNoRows() {
        assertUnknownArrayRejected(emptyList())
    }

    @Test
    fun testUnknownArrayRejectsMissingSize() {
        assertUnknownArrayRejected(listOf(variable("0", "int"), variable("1", "int")))
    }

    @Test
    fun testUnknownArrayRejectsMissingZeroIndex() {
        assertUnknownArrayRejected(listOf(variable("size", "int"), variable("1", "int")))
    }

    @Test
    fun testUnknownArrayRejectsAnIndexGap() {
        assertUnknownArrayRejected(listOf(variable("size", "int"), variable("0", "int"), variable("2", "int")))
    }

    @Test
    fun testUnknownArrayRejectsSizeAfterAnIndex() {
        assertUnknownArrayRejected(listOf(variable("0", "int"), variable("size", "int")))
    }

    @Test
    fun testUnknownArrayRejectsDuplicateIndices() {
        assertUnknownArrayRejected(listOf(variable("size", "int"), variable("0", "int"), variable("0", "int")))
    }

    @Test
    fun testUnknownArrayRejectsDuplicateSize() {
        assertUnknownArrayRejected(listOf(variable("size", "int"), variable("size", "int")))
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1", "+0", "00", "-0", "2147483648", "word"])
    fun testUnknownArrayRejectsInvalidIndex(name: String) {
        assertUnknownArrayRejected(listOf(variable("size", "int"), variable(name, "int")))
    }

    /** Checks that an incomplete array shape gives no type or child watch for an absent type and Nil. */
    private fun assertUnknownArrayRejected(rows: List<DapVariable>) {
        for (type in listOf(null, "Nil")) {
            val parent = untyped(type, "[1, 2]")
            assertEquals("Array", GdValueKind.type(parent))
            assertNull(GdValueKind.type(parent, rows))
            GdWatchPath.evaluated("[1, 2]").children(parent, rows).forEach(::assertNoWatch)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["Vector4", "Vector4i", "Projection"])
    fun testUnexpandedMathTypesKeepOnlyTheWholeValuePath(type: String) {
        val root = GdWatchPath.evaluated("value")
        assertEquals("value", root.text)
        assertFalse(GdWatchNames.bare(type))
        assertFalse(GdWatchNames.member(type))
        val rows = listOf(variable("x", "Vector4"), variable("y", "Vector4"), variable("z", "Vector4"), variable("w", "Vector4"))
        val text = mathValueText(type)
        root.children(variable("value", type, text), rows).forEach(::assertNoWatch)
        assertNull(GdValueKind.type(untyped(null, text), rows))
        root.children(untyped(null, text), rows).forEach(::assertNoWatch)
    }

    private fun parentWithName(parent: DapVariable, name: String): DapVariable = object : DapVariable by parent {
        override val name = name
    }

    @Test
    fun testPendingCategoriesPassTheReadToDescendants() {
        val entry = GdWatchPath.evaluated("items").children(variable("items", "Dictionary"), listOf(variable("key", "Object"))).single()
        assertTrue(entry.requiresKeyRead)
        val category = entry.category().category()
        assertFalse(category.requiresKeyRead)
        assertNull(category.text)
        assertTrue(category.properties("Members", listOf(variable("hp", "int"))).single().requiresKeyRead)
        val unsupported = GdWatchPath.unsupported("value", "no root").category()
        assertFalse(unsupported.requiresKeyRead)
        assertFalse(unsupported.properties("Members", listOf(variable("hp", "int"))).single().requiresKeyRead)
    }

    @Test
    fun testSharedObjectWrapperRule() {
        for (text in listOf("<EncodedObjectAsID#42>", "<EncodedObjectAsID#-42>", "<EncodedObjectAsID#0>")) {
            assertEquals("Object", GdValueKind.type(untyped(null, text)))
            assertEquals("Object", GdValueKind.type(untyped("Nil", text)))
        }
        for (text in listOf("prefix <EncodedObjectAsID#42>", "<EncodedObjectAsID#42> suffix", "<EncodedObjectAsID#+42>",
                            "<EncodedObjectAsID#>", "<EncodedObjectAsID#word>")) {
            assertNull(GdValueKind.type(untyped(null, text)))
        }
    }

    @Test
    fun testUnsupportedActionsLogAndCompleteOnce() {
        val rows = listOf(variable("unknown", "float"))
        val root = GdWatchPath.evaluated("value")
        val unsupported = buildList {
            GdValueKind.mathTypes.flatMapTo(this) { root.children(variable("result", it), rows) }
            addAll(root.children(variable("result", "Dictionary"), rows))
            addAll(root.children(this@GdWatchPathTest.untyped(null, "[unknown]"), rows))
            addAll(root.properties("Constants", rows))
            add(root.category())
            add(GdWatchPath.unsupported("value", "no root"))
            addAll(this@GdWatchPathTest.self.properties("Members", listOf(variable("Vector2", "int"), variable("true", "int"), variable("☃", "int"))))
            addAll(GdWatchPath.scope("Locals", listOf(variable("☃", "int"))))
        }
        unsupported.forEach(::assertNoWatch)
        val supported = root.calculateEvaluationExpression { error("A valid path must not log a failure") }
        assertEquals("value", supported.blockingGet(0, TimeUnit.SECONDS)?.expression)
    }

    /** Checks that an unsupported path logs its role and reason once and completes once with no watch. */
    private fun assertNoWatch(path: GdWatchPath) {
        val messages = mutableListOf<String>()
        var completions = 0
        val promise = path.calculateEvaluationExpression(messages::add)
        promise.onSuccess { completions++ }
        promise.onError { error("An unsupported row must not reject") }
        assertEquals(Promise.State.SUCCEEDED, promise.state)
        assertNull(promise.blockingGet(0, TimeUnit.SECONDS))
        assertEquals(1, completions)
        assertEquals(1, messages.size)
        assertTrue(messages.single().contains(path.role))
        assertTrue(messages.single().contains(path.failure ?: "dictionary key reader unavailable"))
    }

    private fun untyped(type: String?, text: String): DapVariable = object : DapVariable by variable("result", "Nil", text) {
        override val type = type
    }

    companion object {
        /** Supplies the two parent types without a usable type: no type and the Godot Nil type. */
        @JvmStatic
        fun noUsableType(): List<String?> = listOf(null, "Nil")
    }
}
