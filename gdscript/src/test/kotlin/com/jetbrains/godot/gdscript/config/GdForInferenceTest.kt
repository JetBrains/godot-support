package com.jetbrains.godot.gdscript.config

import com.intellij.psi.util.descendantsOfType
import com.jetbrains.godot.gdscript.GdModelTestBase
import gdscript.psi.GdForSt
import gdscript.psi.utils.GdCommonUtil

class GdForInferenceTest : GdModelTestBase() {

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