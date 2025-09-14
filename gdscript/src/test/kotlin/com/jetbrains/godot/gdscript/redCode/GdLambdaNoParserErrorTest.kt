package com.jetbrains.godot.gdscript.redCode

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.testUtil.collectErrors

class GdLambdaNoParserErrorTest : BasePlatformTestCase() {

  // There should be no parser errors in this valid lambda-in-connect snippet
  fun testNoParserErrorsInConnectLambda() {
    val code = """
      |func test():
      |	var projectile := Node2D.new()
      |	var lifetime := Timer.new()
      |	lifetime.timeout.connect(func():
      |		if is_instance_valid(projectile):
      |			projectile.queue_free()
      |	)
      |""".trimMargin()

    val psiFile = myFixture.configureByText("a.gd", code)

    val errors = psiFile.children.flatMap { collectErrors(it) }

    assertTrue(
      "Expected no parser errors for valid lambda in connect; actual errors: " +
        errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
      errors.isEmpty()
    )
  }
}
