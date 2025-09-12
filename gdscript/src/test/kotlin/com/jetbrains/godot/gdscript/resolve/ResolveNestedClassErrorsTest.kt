package com.jetbrains.godot.gdscript.resolve

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdRefIdRef
import gdscript.reference.GdClassMemberReference

class ResolveNestedClassErrorsTest : BasePlatformTestCase() {
    fun testUnresolvableMembersOnNestedClasses() {
        val code = """
            |class_name Outer
            |
            |var var1 := A1.new()
            |
            |func in_the_outer_wrong():
            |    var1.pp()
            |    A1.new().pp()
            |    A1.C1.new()
            |
            |class A1:
            |    class B1:
            |        class C1:
            |            class D1:
            |                func pp():
            |                    pass
        """.trimMargin()
        val file = myFixture.configureByText("NestedErrors.gd", code)

        // var1.pp() -> 'pp' should NOT resolve
        val offPp1 = file.text.indexOf("var1.pp()") + "var1.".length
        val elPp1 = file.findElementAt(offPp1)!!.parent as GdRefIdRef
        val declPp1 = GdClassMemberReference(elPp1).resolveDeclaration()
        assertNull("pp on A1 instance must not resolve", declPp1)

        // A1.new().pp() -> 'pp' should NOT resolve
        val offPp2 = file.text.indexOf("A1.new().pp()") + "A1.new().".length
        val elPp2 = file.findElementAt(offPp2)!!.parent as GdRefIdRef
        val declPp2 = GdClassMemberReference(elPp2).resolveDeclaration()
        assertNull("pp on A1.new() must not resolve", declPp2)

        // A1.C1.new() -> 'C1' should NOT resolve under A1
        val offC1 = file.text.indexOf("A1.C1.new()") + "A1.".length
        val elC1 = file.findElementAt(offC1)!!.parent as GdRefIdRef
        val declC1 = GdClassMemberReference(elC1).resolveDeclaration()
        assertNull("C1 must not resolve directly under A1", declC1)
    }
}
