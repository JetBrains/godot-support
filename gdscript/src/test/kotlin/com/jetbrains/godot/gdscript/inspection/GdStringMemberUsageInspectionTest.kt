package com.jetbrains.godot.gdscript.inspection

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.inspection.GdStringNameMemberCallInspection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@TestApplication
class GdStringMemberUsageInspectionTest {
    private val gd: GdSdkFixture by gdSdkFixture("inspection")

    @BeforeEach
    fun enableStringNameMemberCallInspection() {
        gd.fixture.enableInspections(GdStringNameMemberCallInspection())
    }

    private val multiParamQfMethods = listOf<String>("rpc", "call", "call_deferred", "callv")
    private val multiParamQfSignals = listOf<String>("connect", "disconnect", "is_connected", "emit_signal")
    private val singleParamQfSignals = listOf<String>("get_signal_connection_list")
    private val nameMapper =
        hashMapOf<String, String>("emit_signal" to "emit", "get_signal_connection_list" to "get_connections")

    @Test
    fun testCallableCallDeferred() {
        gd.fixture.testHighlighting("CallableCallDeferred.gd")
    }

    @Test
    fun testNodeMethodsQuickFixCandidateCount() {
        gd.fixture.configureByText("test.gd", "")
        val inspection = GdStringNameMemberCallInspection()
        val candidateCount = runReadActionBlocking { inspection.getQfCandidateCount(gd.fixture.file) }

        // If this breaks it means another method th looks quick fixable was added,
        // check if the method is quick fixable and adjust this test and GdStringNameMemberCallInspection.kt
        // accordingly.
        assertEquals(10, candidateCount)
    }

    @Test
    fun testCallableCallDeferredQuickFix() {
        for (method in multiParamQfMethods) {
            gd.fixture.configureByText(
                "test.gd", """
            func _load_installations() -> void:
              var array: Array = Array()
              <caret>$method("_on_installations_loaded", array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent()
            )
            gd.fixture.doHighlighting()
            val resolvedName = nameMapper[method] ?: method
            val action = gd.fixture.findSingleIntention("Convert to '_on_installations_loaded.$resolvedName(array)'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            func _load_installations() -> void:
              var array: Array = Array()
              _on_installations_loaded.$resolvedName(array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent()
            )
        }
    }

    @Test
    fun testCallableCallDeferredWithBaseQuickFix() {
        for (method in multiParamQfMethods) {
            gd.fixture.configureByText(
                "test.gd", """
            func _load_installations() -> void:
              var array: Array = Array()
              self.<caret>$method("_on_installations_loaded", array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action =
                gd.fixture.findSingleIntention("Convert to 'self._on_installations_loaded.$resolvedName(array)'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            func _load_installations() -> void:
              var array: Array = Array()
              self._on_installations_loaded.$resolvedName(array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent()
            )
        }
    }

    @Test
    fun testCallableCallDeferredNoArgsQuickFix() {
        for (method in multiParamQfMethods) {
            gd.fixture.configureByText(
                "test.gd", """
            func test():
              <caret>$method("my_method")
              
            func my_method():
            	pass
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = gd.fixture.findSingleIntention("Convert to 'my_method.$resolvedName()'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            func test():
            	my_method.$resolvedName()
              
            func my_method():
            	pass
        """.trimIndent()
            )
        }
    }

    @Test
    fun testCallableCallDeferredStringNameQuickFix() {
        for (method in multiParamQfMethods) {
            gd.fixture.configureByText(
                "test.gd", """
            func test():
              <caret>$method(&"_on_installations_loaded")

            func _on_installations_loaded():
                pass
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = gd.fixture.findSingleIntention("Convert to '_on_installations_loaded.$resolvedName()'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            func test():
            	_on_installations_loaded.$resolvedName()

            func _on_installations_loaded():
                pass
        """.trimIndent()
            )
        }
    }

    @Test
    fun testNoFixForUnresolvedMethod() {
        for (method in multiParamQfMethods) {
            // Warm-up statement: resolving an SDK class symbol for the very first time in a fresh
            // file can otherwise trip a pre-existing, unrelated PolySymbolHighlightingAnnotator
            // cold-cache crash during doHighlighting() - see GdParamAnnotatorTest for the same
            // documented workaround.
            gd.fixture.configureByText(
                "test.gd", """
            var warmup = Vector2.new()

            func test():
              $method("unresolved_method")
        """.trimIndent()
            )
            gd.fixture.doHighlighting()
            val intentions = gd.fixture.availableIntentions
            val fix = intentions.find { it.text.contains("Convert to") }
            assertNull(fix, "Quick fix should not be available for unresolved method")
        }
    }

    @Test
    fun testSignalQuickFix() {
        for (rename in multiParamQfSignals) {
            gd.fixture.configureByText(
                "test.gd", """
            signal installations_loaded(array: Array)

            func _load_installations() -> void:
              var array: Array = Array()
              var callable := Callable()
              <caret>$rename("installations_loaded", callable)
        """.trimIndent()
            )
            val resolvedName = nameMapper[rename] ?: rename
            val action = gd.fixture.findSingleIntention("Convert to 'installations_loaded.$resolvedName(callable)'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            signal installations_loaded(array: Array)

            func _load_installations() -> void:
              var array: Array = Array()
              var callable := Callable()
              installations_loaded.$resolvedName(callable)
        """.trimIndent()
            )
        }
    }

    @Test
    fun testSignalNoParamQuickFix() {
        for (method in singleParamQfSignals) {
            gd.fixture.configureByText(
                "test.gd", """
            signal installations_loaded()

            func _load_installations() -> void:
              <caret>$method("installations_loaded")
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = gd.fixture.findSingleIntention("Convert to 'installations_loaded.$resolvedName()'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            signal installations_loaded()

            func _load_installations() -> void:
            	installations_loaded.$resolvedName()
        """.trimIndent()
            )
        }
    }


    @Test
    fun testSignalWithBaseQuickFix() {
        for (method in multiParamQfSignals) {
            gd.fixture.configureByText(
                "test.gd", """
            signal installations_loaded(array: Array)

            func _load_installations() -> void:
              var array: Array = Array()
              var callable := Callable()
              self.<caret>$method("installations_loaded", callable)
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action =
                gd.fixture.findSingleIntention("Convert to 'self.installations_loaded.$resolvedName(callable)'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            signal installations_loaded(array: Array)

            func _load_installations() -> void:
              var array: Array = Array()
              var callable := Callable()
              self.installations_loaded.$resolvedName(callable)
        """.trimIndent()
            )
        }
    }

    @Test
    fun testSignalStringNameQuickFix() {
        for (method in multiParamQfSignals) {
            gd.fixture.configureByText(
                "test.gd", """
            signal installations_loaded

            func _load_installations() -> void:
              var callable := Callable()
              <caret>$method(&"installations_loaded", callable)
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = gd.fixture.findSingleIntention("Convert to 'installations_loaded.$resolvedName(callable)'")
            gd.fixture.launchAction(action)
            gd.fixture.checkResult(
                """
            signal installations_loaded

            func _load_installations() -> void:
              var callable := Callable()
              installations_loaded.$resolvedName(callable)
        """.trimIndent()
            )
        }
    }

    @Test
    fun testNoFixForUnresolvedSignal() {
        for (method in multiParamQfSignals) {
            // Warm-up statement - see testNoFixForUnresolvedMethod for why.
            gd.fixture.configureByText(
                "test.gd", """
            var warmup = Vector2.new()

            func test():
              $method("unresolved_signal", Callable())
        """.trimIndent()
            )
            gd.fixture.doHighlighting()
            val intentions = gd.fixture.availableIntentions
            val fix = intentions.find { it.text.contains("Convert to") }
            assertNull(fix, "Quick fix should not be available for unresolved signal")
        }
    }
}
