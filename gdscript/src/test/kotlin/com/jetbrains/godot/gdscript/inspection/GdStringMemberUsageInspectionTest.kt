package com.jetbrains.godot.gdscript.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.inspection.GdStringNameMemberCallInspection
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdStringMemberUsageInspectionTest : BasePlatformTestCase() {
    private fun getSdkPath(): Path {
        val path = getBaseTestDataPath().resolve("testData/gdscript/sdk")
        assertTrue(path.exists())
        return path
    }

    private fun setupSdkFiles() {
        val sdkRoot = getSdkPath()
        Files.walk(sdkRoot)
            .filter { it.isRegularFile() && it.fileName.toString().endsWith(".gd") }
            .forEach { file ->
                val rel = sdkRoot.relativize(file).toString()
                myFixture.copyFileToProject(file.pathString, "sdk/$rel")
            }
    }

    override fun setUp() {
        super.setUp()
        setupSdkFiles()
        myFixture.enableInspections(GdStringNameMemberCallInspection())
    }

    private val multiParamQfMethods = listOf<String>("rpc", "call", "call_deferred", "callv")
    private val multiParamQfSignals = listOf<String>("connect", "disconnect", "is_connected", "emit_signal")
    private val singleParamQfSignals = listOf<String>("get_signal_connection_list")
    private val nameMapper =
        hashMapOf<String, String>("emit_signal" to "emit", "get_signal_connection_list" to "get_connections")

    @Test
    fun testCallableCallDeferred() {
        myFixture.testHighlighting("CallableCallDeferred.gd")
    }

    @Test
    fun testNodeMethodsQuickFixCandidateCount() {
        myFixture.configureByText("test.gd", "")
        val inspection = GdStringNameMemberCallInspection()
        val candidateCount = inspection.getQfCandidateCount(myFixture.file)

        // If this breaks it means another method th looks quick fixable was added,
        // check if the method is quick fixable and adjust this test and GdStringNameMemberCallInspection.kt
        // accordingly.
        assertEquals(10, candidateCount)
    }

    @Test
    fun testCallableCallDeferredQuickFix() {
        for (method in multiParamQfMethods) {
            myFixture.configureByText(
                "test.gd", """
            func _load_installations() -> void:
              var array: Array = Array()
              <caret>$method("_on_installations_loaded", array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent()
            )
            myFixture.doHighlighting()
            val resolvedName = nameMapper[method] ?: method
            val action = myFixture.findSingleIntention("Convert to '_on_installations_loaded.$resolvedName(array)'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
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
                myFixture.findSingleIntention("Convert to 'self._on_installations_loaded.$resolvedName(array)'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
                "test.gd", """
            func test():
              <caret>$method("my_method")
              
            func my_method():
            	pass
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = myFixture.findSingleIntention("Convert to 'my_method.$resolvedName()'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
                "test.gd", """
            func test():
              <caret>$method(&"_on_installations_loaded")

            func _on_installations_loaded():
                pass
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = myFixture.findSingleIntention("Convert to '_on_installations_loaded.$resolvedName()'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
                "test.gd", """
            func test():
              $method("unresolved_method")
        """.trimIndent()
            )
            myFixture.doHighlighting()
            val intentions = myFixture.availableIntentions
            val fix = intentions.find { it.text.contains("Convert to") }
            assertNull("Quick fix should not be available for unresolved method", fix)
        }
    }

    @Test
    fun testSignalQuickFix() {
        for (rename in multiParamQfSignals) {
            myFixture.configureByText(
                "test.gd", """
            signal installations_loaded(array: Array)

            func _load_installations() -> void:
              var array: Array = Array()
              var callable := Callable()
              <caret>$rename("installations_loaded", callable)
        """.trimIndent()
            )
            val resolvedName = nameMapper[rename] ?: rename
            val action = myFixture.findSingleIntention("Convert to 'installations_loaded.$resolvedName(callable)'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
                "test.gd", """
            signal installations_loaded()

            func _load_installations() -> void:
              <caret>$method("installations_loaded")
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = myFixture.findSingleIntention("Convert to 'installations_loaded.$resolvedName()'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
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
                myFixture.findSingleIntention("Convert to 'self.installations_loaded.$resolvedName(callable)'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
                "test.gd", """
            signal installations_loaded

            func _load_installations() -> void:
              var callable := Callable()
              <caret>$method(&"installations_loaded", callable)
        """.trimIndent()
            )
            val resolvedName = nameMapper[method] ?: method
            val action = myFixture.findSingleIntention("Convert to 'installations_loaded.$resolvedName(callable)'")
            myFixture.launchAction(action)
            myFixture.checkResult(
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
            myFixture.configureByText(
                "test.gd", """
            func test():
              $method("unresolved_signal", Callable())
        """.trimIndent()
            )
            myFixture.doHighlighting()
            val intentions = myFixture.availableIntentions
            val fix = intentions.find { it.text.contains("Convert to") }
            assertNull("Quick fix should not be available for unresolved signal", fix)
        }
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/inspection").pathString
    }
}
