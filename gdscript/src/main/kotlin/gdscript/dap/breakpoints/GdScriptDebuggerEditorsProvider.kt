package gdscript.dap.breakpoints

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.psi.GdPsiCodeFragment

class GdScriptDebuggerEditorsProvider: XDebuggerEditorsProviderBase() {
    override fun getFileType(): FileType = GdFileType

    public override fun createExpressionCodeFragment(project: Project, text: String, context: PsiElement?, isPhysical: Boolean): PsiFile {
        require(isPhysical) {
            "A debugger code fragment must be physical: GDScript resolve dereferences it (TscnResourceUtil)."
        }
        return GdPsiCodeFragment(project, FRAGMENT_NAME, text, context, physical = true)
    }

    private companion object {
        /** Must not end in `.gd`: the Godot LSP claims every `.gd` file, and this one exists only in memory. */
        const val FRAGMENT_NAME: String = "expression.gdscript"
    }
}
