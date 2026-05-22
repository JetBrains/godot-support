package com.jetbrains.godot.gdscript.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.inspection.GdCallableCallDeferredInspection
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdCallableCallDeferredInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(GdCallableCallDeferredInspection::class.java)
    }

    @Test
    fun testCallableCallDeferred() {
        myFixture.testHighlighting("CallableCallDeferred.gd")
    }

    @Test
    fun testCallableCallDeferredQuickFix() {
        myFixture.configureByText("test.gd", """
            func _load_installations() -> void:
              var array: Array = Array()
              <caret>call_deferred("_on_installations_loaded", array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent())
        val action = myFixture.findSingleIntention("Convert to '_on_installations_loaded.call_deferred(array)'")
        myFixture.launchAction(action)
        myFixture.checkResult("""
            func _load_installations() -> void:
              var array: Array = Array()
              _on_installations_loaded.call_deferred(array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent())
    }

    @Test
    fun testCallableCallDeferredWithBaseQuickFix() {
        myFixture.configureByText("test.gd", """
            func _load_installations() -> void:
              var array: Array = Array()
              self.<caret>call_deferred("_on_installations_loaded", array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent())
        val action = myFixture.findSingleIntention("Convert to 'self._on_installations_loaded.call_deferred(array)'")
        myFixture.launchAction(action)
        myFixture.checkResult("""
            func _load_installations() -> void:
              var array: Array = Array()
              self._on_installations_loaded.call_deferred(array)

            func _on_installations_loaded(array: Array):
              pass
        """.trimIndent())
    }

    @Test
    fun testCallableCallDeferredNoArgsQuickFix() {
        myFixture.configureByText("test.gd", """
            func test():
              <caret>call_deferred("my_method")
              
            func my_method():
                pass
        """.trimIndent())
        val action = myFixture.findSingleIntention("Convert to 'my_method.call_deferred()'")
        myFixture.launchAction(action)
        myFixture.checkResult("""
            func test():
              my_method.call_deferred()
              
            func my_method():
                pass
        """.trimIndent())
    }

    @Test
    fun testCallableCallDeferredStringNameQuickFix() {
        myFixture.configureByText("test.gd", """
            func test():
              <caret>call_deferred(&"_on_installations_loaded")

            func _on_installations_loaded():
                pass
        """.trimIndent())
        val action = myFixture.findSingleIntention("Convert to '_on_installations_loaded.call_deferred()'")
        myFixture.launchAction(action)
        myFixture.checkResult("""
            func test():
              _on_installations_loaded.call_deferred()

            func _on_installations_loaded():
                pass
        """.trimIndent())
    }

    @Test
    fun testNoFixForUnresolvedMethod() {
        myFixture.configureByText("test.gd", """
            func test():
              call_deferred("unresolved_method")
        """.trimIndent())
        myFixture.doHighlighting()
        val intentions = myFixture.availableIntentions
        val fix = intentions.find { it.text.contains("Convert to") }
        assertNull("Quick fix should not be available for unresolved method", fix)
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/inspection").pathString
    }
}
