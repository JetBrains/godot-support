package com.jetbrains.godot.gdscript.returntype

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.utils.GdClassUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.readText

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Type inference"])
@TestApplication
class ReturnTypeDumpTest {

    private val gd: GdFixture by gdLightFixture("gdscript/returnTypeDump")

    @Test
    fun testNestedClassInstantiations() {
        val file = gd.fixture.configureByFile("NestedClassInstantiations.gd")
        val document = gd.fixture.getDocument(file)!!

        val rendered = runReadActionBlocking {
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
            buildString {
                for (i in lines.indices) {
                    append(lines[i])
                    lineToDump[i]?.let { append(it) }
                    if (i < lines.lastIndex) append('\n')
                }
            }
        }

        val expectedPath = gd.fixture.testDataPath + "/" + "NestedClassInstantiations" + ".txt"
        val expectedText = Path.of(expectedPath).readText()
        // Compare as-is including newlines
        assertEquals(expectedText, rendered)
    }
}
