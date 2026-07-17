package com.jetbrains.godot.gdscript.resolve

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.polySymbols.gdDeclaringClassId
import gdscript.polySymbols.resolve.GdSymbolResolverUtil

class GdSymbolResolverUtilTest : BasePlatformTestCase() {

    fun testFindMethodSymbolReturnsNearestAncestorFirst() {
        myFixture.addFileToProject(
            "Grandparent.gd", """
            |class_name Grandparent
            |func greet() -> String:
            |    return "grandparent"
        """.trimMargin()
        )
        myFixture.configureByText(
            "Parent.gd", """
            |class_name Parent
            |extends Grandparent
            |func greet() -> String:
            |    return "parent"
        """.trimMargin()
        )

        val parentSymbol = GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, "Parent", myFixture.file)
        val greet = GdSymbolResolverUtil.findMethodSymbol(parentSymbol, "greet")

        assertNotNull(greet)
        assertEquals("Parent", greet!!.gdDeclaringClassId)
    }
}
