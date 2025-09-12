package com.jetbrains.godot.gdscript.resolve

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdRefIdRef
import gdscript.reference.GdClassMemberReference

class ResolveNestedClassMethodsTest : BasePlatformTestCase() {

    fun testResolveMethodsOnNestedClasses() {
        val code = """
            |class_name OuterClass
            |
            |func use():
            |    A1.new().ppa1()
            |    A1.B1.C1.D1.new().pp()
            |
            |class A1:
            |    func ppa1():
            |        pass
            |    class B1:
            |        class C1:
            |            class D1:
            |                func pp():
            |                    pass
        """.trimMargin()

        val file = myFixture.configureByText("NestedResolve.gd", code)

        // Caret at ppa1 and resolve
        val offsetPpa1 = file.text.indexOf("ppa1") + 2 // inside identifier
        val elementPpa1 = file.findElementAt(offsetPpa1)!!.parent as GdRefIdRef
        val declPpa1 = GdClassMemberReference(elementPpa1).resolveDeclaration()
        assertNotNull("ppa1 should resolve", declPpa1)
        assertTrue(declPpa1 is GdMethodDeclTl)
        assertEquals("ppa1", (declPpa1 as GdMethodDeclTl).name)

        // Caret at pp and resolve
        val offsetPp = file.text.indexOf("pp()") + 1
        val elementPp = file.findElementAt(offsetPp)!!.parent as GdRefIdRef
        val declPp = GdClassMemberReference(elementPp).resolveDeclaration()
        assertNotNull("pp should resolve", declPp)
        assertTrue(declPp is GdMethodDeclTl)
        assertEquals("pp", (declPp as GdMethodDeclTl).name)
    }
}
