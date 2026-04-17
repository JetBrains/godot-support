package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.util.descendantsOfType
import com.jetbrains.godot.GdCodeInsightTestBase
import gdscript.psi.GdForSt
import gdscript.psi.utils.GdCommonUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GdForInferenceTest : GdCodeInsightTestBase() {

    @Test
    fun testDictionaryKeyInference() {
        //language=GDScript
        val code = """
            func test():
                var dict: Dictionary = {}
                for key in dict:
                    pass
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val forStmt = file.descendantsOfType<GdForSt>().first()
        assertEquals("Variant", GdCommonUtil.returnType(forStmt))
    }

    @Test
    fun testTypedDictionaryKeyInference() {
        //language=GDScript
        val code = """
            func test():
                var dict: Dictionary[String, int] = {}
                for key in dict:
                    pass
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val forStmt = file.descendantsOfType<GdForSt>().first()
        assertEquals("String", GdCommonUtil.returnType(forStmt))
    }

    @Test
    fun testArrayInference() {
        //language=GDScript
        val code = """
            func test():
                var arr: Array = []
                for item in arr:
                    pass
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val forStmt = file.descendantsOfType<GdForSt>().first()
        assertEquals("Variant", GdCommonUtil.returnType(forStmt))
    }

    @Test
    fun testTypedArrayInference() {
        //language=GDScript
        val code = """
            func test():
                var arr: Array[int] = []
                for item in arr:
                    pass
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val forStmt = file.descendantsOfType<GdForSt>().first()
        assertEquals("int", GdCommonUtil.returnType(forStmt))
    }
}
