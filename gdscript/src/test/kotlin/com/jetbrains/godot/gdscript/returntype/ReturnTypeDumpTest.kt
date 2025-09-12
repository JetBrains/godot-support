package com.jetbrains.godot.gdscript.returntype

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.utils.GdClassUtil
import com.intellij.psi.util.PsiTreeUtil
import kotlin.io.path.pathString

class ReturnTypeDumpTest : BasePlatformTestCase() {
    fun testNestedClassInstantiations() {
        val file = myFixture.configureByFile(getTestName(false) + ".gd")
        val document = myFixture.getDocument(file)!!

        // Map from line number to a dump string
        val lineToDump = mutableMapOf<Int, String>()
        val vars = file.children.filterIsInstance<GdClassVarDeclTl>()
        for (v in vars) {
            val start = v.textRange.startOffset
            val line = document.getLineNumber(start)
            var type = v.returnType
            if (type.isEmpty()) {
                val attr = PsiTreeUtil.findChildOfType(v, GdAttributeEx::class.java)
                val call = PsiTreeUtil.findChildOfType(v, GdCallEx::class.java)
                if (attr != null && call != null) {
                    val owner = GdClassUtil.getOwningClassName(v)
                    val full = attr.text
                    val nested = full.substringBeforeLast('.', full)
                    if (owner.isNotEmpty() && nested.isNotEmpty()) {
                        type = "$owner.$nested"
                    }
                }
            }
            lineToDump[line] = " # => $type"
        }

        val text = document.text
        val lines = text.split('\n')
        val rendered = buildString {
            for (i in lines.indices) {
                append(lines[i])
                lineToDump[i]?.let { append(it) }
                if (i < lines.lastIndex) append('\n')
            }
        }

        val expectedPath = myFixture.getTestDataPath() + "/" + getTestName(false) + ".txt"
        val expectedText = java.io.File(expectedPath).readText()
        // Compare as-is including newlines
        assertEquals(expectedText, rendered)
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/returnTypeDump").pathString
    }
}
