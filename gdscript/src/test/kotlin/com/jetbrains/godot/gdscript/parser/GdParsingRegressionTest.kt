package com.jetbrains.godot.gdscript.parser

import com.jetbrains.godot.GdCodeInsightTestBase
import org.junit.jupiter.api.Assertions.assertFalse
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
        val errors = com.intellij.psi.util.PsiTreeUtil.findChildrenOfType(
            file, com.intellij.psi.PsiErrorElement::class.java
        )
        assertFalse(errors.isNotEmpty(), "Expected no parse errors but found: ${errors.map { it.errorDescription }}")
    }
}
