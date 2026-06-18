package com.jetbrains.godot.gdscript.returntype

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdClassVarDeclTl
import kotlin.io.path.pathString

class ReturnTypeTest : BasePlatformTestCase() {

    fun testReturnTypesOfNestedClassInstantiations() {
        val file = myFixture.configureByFile(getTestName(false) + ".gd")
        val varDeclarations = file.children.filterIsInstance<GdClassVarDeclTl>()
        val var1Declaration = varDeclarations.first { it.name == "var1" }
        assertEquals("OuterClass.InnerClassLevel1", var1Declaration.returnType)
        val var2Declaration = varDeclarations.first { it.name == "var2" }
        assertEquals("OuterClass.InnerClassLevel1.InnerClassLevel2", var2Declaration.returnType)
    }

    fun testReturnTypesOfDictionaryKeyReferences() {
        val file = myFixture.configureByFile(getTestName(false) + ".gd")
        val varDeclarations = file.children.filterIsInstance<GdClassVarDeclTl>()
        // Technically, Godot would throw an error in var1's declaration
        // because it doesn't support type inference for dictionary keys that are not explicitly typed.
        // But we use this test so that we can ensure the type of the expression is inferred as Variant.
        val var1Declaration = varDeclarations.first { it.name == "var1" }
        assertEquals("Variant", var1Declaration.returnType)
        val var2Declaration = varDeclarations.first { it.name == "var2" }
        assertEquals("int", var2Declaration.returnType)
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/returnType").pathString
    }
}
