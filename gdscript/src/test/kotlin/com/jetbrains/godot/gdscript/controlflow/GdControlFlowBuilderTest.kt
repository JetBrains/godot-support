package com.jetbrains.godot.gdscript.controlflow

import com.intellij.codeInsight.controlflow.ControlFlow
import com.intellij.codeInsight.controlflow.Instruction
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.controlflow.GdControlFlowBuilder
import gdscript.psi.GdMethodDeclTl
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdControlFlowBuilderTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript/controlflow").pathString


    @Test
    fun testJustPass() {
        doCfgTest()
    }


    private fun toDot(cfg: ControlFlow): String {
        // Python compares the `StringUtil.join(flow.getInstructions(), Object::toString, "\n");`

        fun StringBuilder.appendOffset(string: String): StringBuilder {
            append(" ".repeat(2))
            appendLine(string)
            return this
        }

        // `nX` as in Node X
        fun Instruction.nodeName(): String = "n${num()}"

        // TODO: Needs quotes escaping and so on
        fun Instruction.label(): String = "[label=\"${num()}:${this.elementPresentation}\"]"

        return buildString {
            appendLine("digraph CFG {")
            // First render nodes
            cfg.instructions.forEach { instruction ->
                appendOffset("${instruction.nodeName()} ${instruction.label()}")
            }
            // Then render connections
            appendLine()
            cfg.instructions.forEach { instruction ->
                instruction.allSucc().forEach { succ ->
                    appendOffset("${instruction.nodeName()} -> ${succ.nodeName()}")
                }
            }
            appendLine("}")
        }
    }

    /** Expects only one top level method, for which it builds the CFG for */
    private fun doCfgTest() {
        val testName = getTestName(true)
        val dotPath = "$testDataPath/$testName.dot"

        myFixture.configureByFile("$testName.gd")
        val function = myFixture.file.children.filterIsInstance<GdMethodDeclTl>().single()
        val flow = GdControlFlowBuilder().buildControlFlow(function)
        val dotCfg = toDot(flow)
        assertSameLinesWithFile(dotPath, dotCfg, true)
    }
}