package com.jetbrains.godot.gdscript.findUsages

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdMethodDeclTl
import kotlin.io.path.pathString

/**
 * Find Usages of a GDScript method runs through the PolySymbol of the declaration - see
 * `gdscript.codeInsight.GdUsageProvider.canFindUsagesFor`. This test keeps the plain script to
 * script case covered, because the classic PSI target no longer serves it.
 */
class MethodFindUsagesTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/findUsages/method").pathString
    }

    fun testCallListed() {
        myFixture.configureByFile("main.gd")
        val method = PsiTreeUtil.findChildrenOfType(myFixture.file, GdMethodDeclTl::class.java)
            .firstOrNull { it.getName() == "helper" }
            ?.methodIdNmi
        assertNotNull("the script must declare the method", method)

        val usages = myFixture.symbolUsages(method!!.textOffset)
        assertEquals(usages.joinToString("\n") { it.text }, 1, usages.size)
        assertEquals("helper", usages.single().text)
    }
}
