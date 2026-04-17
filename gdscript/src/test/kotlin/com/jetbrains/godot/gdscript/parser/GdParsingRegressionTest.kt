package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.GdCodeInsightTestBase
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GdParsingRegressionTest : GdCodeInsightTestBase() {

    @Test
    fun testMatchWhen() {
        val code = """
            func test():
                var x = 1
                match x:
                    var y when y > 0:
                        pass
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val errors = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)
        assertTrue(errors.isEmpty(), "Expected no parse errors but found: ${errors.map { it.errorDescription }}")
    }
}
