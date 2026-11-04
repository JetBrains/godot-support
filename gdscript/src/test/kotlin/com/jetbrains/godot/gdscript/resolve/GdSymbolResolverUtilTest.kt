package com.jetbrains.godot.gdscript.resolve

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.polySymbols.gdDeclaringClassId
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
@TestApplication
class GdSymbolResolverUtilTest {
    private val gd: GdFixture by gdLightFixture("gdscript/highlighting")

    @Test
    fun testFindMethodSymbolReturnsNearestAncestorFirst() {
        gd.fixture.addFileToProject(
            "Grandparent.gd", """
            |class_name Grandparent
            |func greet() -> String:
            |    return "grandparent"
        """.trimMargin()
        )
        gd.fixture.configureByText(
            "Parent.gd", """
            |class_name Parent
            |extends Grandparent
            |func greet() -> String:
            |    return "parent"
        """.trimMargin()
        )

        runReadActionBlocking {
            val parentSymbol = GdSymbolResolverUtil.resolveCanonicalClassSymbol(gd.project, "Parent", gd.fixture.file)
            val greet = GdSymbolResolverUtil.findMethodSymbol(parentSymbol, "greet")

            assertNotNull(greet)
            assertEquals("Parent", greet!!.gdDeclaringClassId)
        }
    }
}
