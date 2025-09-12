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

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/returnType").pathString
    }
}
