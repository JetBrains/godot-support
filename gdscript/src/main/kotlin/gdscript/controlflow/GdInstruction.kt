package gdscript.controlflow

import com.intellij.codeInsight.controlflow.ControlFlowBuilder
import com.intellij.codeInsight.controlflow.impl.InstructionImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.psi.GdTypes

class GdInstruction(builder: ControlFlowBuilder, element: PsiElement) : InstructionImpl(builder, element) {

    override fun getElementPresentation(): String {
        return if (element.elementType == GdTypes.PASS) "PASS" else super.elementPresentation
    }
}