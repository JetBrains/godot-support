package com.jetbrains.godot.gdscript.returntype

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdClassVarDeclTl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Type inference"])
@TestApplication
class ReturnTypeTest {
    private val gd: GdFixture by gdLightFixture("gdscript/parser/returnType")

    @Test
    fun testReturnTypesOfNestedClassInstantiations() {
        val file = gd.fixture.configureByFile("ReturnTypesOfNestedClassInstantiations.gd")
        val varDeclarations = runReadActionBlocking { file.children.filterIsInstance<GdClassVarDeclTl>() }
        val var1Declaration = runReadActionBlocking { varDeclarations.first { it.getName() == "var1" } }
        assertEquals("OuterClass.InnerClassLevel1", runReadActionBlocking { var1Declaration.returnType })
        val var2Declaration = runReadActionBlocking { varDeclarations.first { it.getName() == "var2" } }
        assertEquals("OuterClass.InnerClassLevel1.InnerClassLevel2", runReadActionBlocking { var2Declaration.returnType })
    }

    @Test
    fun testReturnTypesOfDictionaryKeyReferences() {
        val file = gd.fixture.configureByFile("ReturnTypesOfDictionaryKeyReferences.gd")
        runReadActionBlocking {
            val varDeclarations = file.children.filterIsInstance<GdClassVarDeclTl>()
            // Technically, Godot would throw an error in var1's declaration
            // because it doesn't support type inference for dictionary keys that are not explicitly typed.
            // But we use this test so that we can ensure the type of the expression is inferred as Variant.
            val var1Declaration = varDeclarations.first { it.getName() == "var1" }
            assertEquals("Variant", var1Declaration.returnType)
            val var2Declaration = varDeclarations.first { it.getName() == "var2" }
            assertEquals("int", var2Declaration.returnType)
        }
    }

    @Test
    fun testReturnTypesOfNanInfLiterals() {
        val file = gd.fixture.configureByFile("ReturnTypesOfNanInfLiterals.gd")

        runReadActionBlocking {
            val varDeclarations = file.children.filterIsInstance<GdClassVarDeclTl>()
            val var1Declaration = varDeclarations.first { it.getName() == "var1" }
            val var2Declaration = varDeclarations.first { it.getName() == "var2" }

            assertEquals("float", var1Declaration.returnType)
            assertEquals("float", var2Declaration.returnType)
        }
    }
}
