package gdscript.controlflow

import com.intellij.codeInsight.controlflow.ControlFlow
import com.intellij.codeInsight.controlflow.ControlFlowBuilder
import com.intellij.codeInsight.controlflow.impl.ControlFlowImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import gdscript.psi.GdFlowSt
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParamList
import gdscript.psi.GdStmt
import gdscript.psi.GdVisitor


class GdControlFlowBuilder : GdVisitor() {

    protected val builder: ControlFlowBuilder = ControlFlowBuilder()

    fun buildControlFlow(function: GdMethodDeclTl): ControlFlow {
        val flow = builder.build(this, function)
        val instructions = flow.instructions
        for (i in instructions.indices) {
            check(i == instructions[i]!!.num())
        }
        return ControlFlowImpl(instructions)
    }

    override fun visitParamList(o: GdParamList) {
        for (param in o.paramList) {
            param.expr?.let(::visitExpr) // defaults to visitPsiElement
        }
    }

    override fun visitPsiElement(o: PsiElement) {
        if (o is PsiNamedElement && false) { // TODO: Maybe it should also not be a keyword?
            builder.startNode(o)
            builder.addNode(TODO("Write instruction"))
        }
        super.visitPsiElement(o)
    }

    override fun visitFlowSt(o: GdFlowSt) {
        builder.addNode(GdInstruction(builder, o))
        super.visitFlowSt(o)
    }


    // TODO: Just copying PyControlFlowBuilder - the platform's ControlFlowBuilder calls this on just the children
    override fun visitMethodDeclTl(o: GdMethodDeclTl) {
        builder.startNode(o)
        o.paramList?.let(::visitParamList)

        val instruction = TODO("Create instruction")

        builder.addNodeAndCheckPending(instruction)
    }
}
