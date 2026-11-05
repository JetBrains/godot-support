package com.jetbrains.godot.gdscript.completion

import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.godot.gdscript.util.disableSingleItemAutoInsert
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Completion"])
@TestApplication
class GdSignalNameCompletionTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/completion")

    @ParameterizedTest(name = "{0}")
    @MethodSource("approvedOperations")
    fun testApprovedOperations(expression: String) = complete(expression) {
        assertEquals(setOf("local_signal", "base_signal", "property_list_changed", "script_changed"), items().toSet())
    }

    @ParameterizedTest(name = "other.{0}")
    @MethodSource("approvedOperations")
    fun testExplicitReceiver(expression: String) = complete("other.$expression") {
        assertEquals(setOf("other_signal", "base_signal", "property_list_changed", "script_changed"), items().toSet())
    }

    // Own signals come first, then the project base classes, then the engine classes.
    @Test
    fun testSignalOrder() = complete("connect(\"<caret>\")") {
        assertEquals(listOf("local_signal", "base_signal", "property_list_changed", "script_changed"), items())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("signalConstructors")
    fun testSignalConstructor(expression: String, ownSignal: String) = complete(expression) {
        assertEquals(setOf(ownSignal, "base_signal", "property_list_changed", "script_changed"), items().toSet())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("partialNames")
    fun testPartialNames(expression: String, expected: String) = complete(expression) {
        assertEquals(listOf(expected), items())
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("unknownOwners")
    fun testUnknownOwners(expression: String) = complete(expression) { assertThat(items()).isEmpty() }

    @ParameterizedTest(name = "{0}")
    @MethodSource("userSignalOperations")
    fun testUserSignalOperationsDontComplete(expression: String) = complete(expression) { assertThat(items()).isEmpty() }

    @ParameterizedTest(name = "{0}")
    @MethodSource("unrelatedArguments")
    fun testUnrelatedArgumentsDontComplete(expression: String) = complete(expression) { assertNoSignals() }

    @ParameterizedTest(name = "custom.connect({0})")
    @MethodSource("literals")
    fun testProjectMethodWithSameNameDoesntComplete(literal: String) = complete("custom.connect($literal)") { assertNoSignals() }

    private fun CodeInsightTestFixture.items(): List<String> = lookupElementStrings.orEmpty()

    private fun CodeInsightTestFixture.assertNoSignals() {
        assertThat(items()).doesNotContain("local_signal", "other_signal", "base_signal", "property_list_changed", "script_changed")
    }

    private fun complete(expression: String, check: CodeInsightTestFixture.() -> Unit) {
        disableSingleItemAutoInsert(gd.testRootDisposable)
        gd.doConfiguredTest(
            dir = false,
            configureFileName = "signal_completion.gd",
            fileContents = """
                extends SignalBase
                signal local_signal
                var other: SignalOther
                var custom: CustomMethods

                func test():
                    $expression
            """.trimIndent() + "\n",
        ) {
            addFileToProject("SignalBase.gd", "class_name SignalBase\nextends Object\nsignal base_signal\n")
            addFileToProject("SignalOther.gd", "class_name SignalOther\nextends SignalBase\nsignal other_signal\n")
            addFileToProject(
                "CustomMethods.gd", """
                class_name CustomMethods
                extends Object
                func connect(signal: StringName):
                    pass
            """.trimIndent() + "\n"
            )
            completeBasic()
            check()
        }
    }

    companion object {
        private val operations = listOf(
            "connect", "disconnect", "emit_signal", "is_connected",
            "has_signal", "has_connections", "get_signal_connection_list"
        )

        @JvmStatic
        private fun literals(): List<String> = listOf("\"<caret>\"", "'<caret>'", "&\"<caret>\"", "&'<caret>'")

        @JvmStatic
        private fun approvedOperations(): List<String> =
            operations.flatMap { operation -> literals().map { literal -> "$operation($literal)" } }

        @JvmStatic
        private fun signalConstructors(): List<Arguments> = literals().flatMap { literal ->
            listOf(
                Arguments.of("Signal(other, $literal)", "other_signal"),
                Arguments.of("Signal(self, $literal)", "local_signal"),
            )
        }

        @JvmStatic
        private fun partialNames(): List<Arguments> = listOf(
            Arguments.of("connect('local_s<caret>')", "local_signal"),
            Arguments.of("Signal(other, &\"other_s<caret>\")", "other_signal"),
        )

        @JvmStatic
        private fun unknownOwners(): List<String> = literals().flatMap { literal ->
            operations.map { operation -> "unknown.$operation($literal)" } + "Signal(unknown, $literal)"
        }

        @JvmStatic
        private fun userSignalOperations(): List<String> = literals().flatMap { literal ->
            listOf("add_user_signal", "has_user_signal", "remove_user_signal").map { operation -> "$operation($literal)" }
        }

        @JvmStatic
        private fun unrelatedArguments(): List<String> = literals().flatMap { literal ->
            listOf(
                "var s = $literal", "connect('local_signal', $literal)",
                "Signal($literal, 'local_signal')", "print($literal)",
                "connect(str($literal))", "connect([$literal])"
            )
        }
    }
}
