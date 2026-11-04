package com.jetbrains.godot.gdscript.findUsages

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.findUsages.symbolUsages
import com.jetbrains.godot.findUsages.text
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdMethodDeclTl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 * Find Usages of a GDScript method runs through the PolySymbol of the declaration - see
 * `gdscript.codeInsight.GdUsageProvider.canFindUsagesFor`. This test keeps the plain script to
 * script case covered, because the classic PSI target no longer serves it.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Find usages"])
@TestApplication
class MethodFindUsagesTest {

    private val gd: GdFixture by gdLightFixture("gdscript/findUsages/method")

    @Test
    fun testCallListed() {
        gd.fixture.configureByFile("main.gd")
        val offset = runReadActionBlocking {
            val method = PsiTreeUtil.findChildrenOfType(gd.fixture.file, GdMethodDeclTl::class.java)
                .firstOrNull { it.getName() == "helper" }
                ?.methodIdNmi
            assertNotNull(method, "the script must declare the method")
            method!!.textOffset
        }

        val usages = runReadActionBlocking { gd.fixture.symbolUsages(offset) }
        assertEquals(1, usages.size, usages.joinToString("\n") { it.text })
        assertEquals("helper", usages.single().text)
    }
}
