package gdscript.refactoring.introduce

import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.introduce.IntroduceHandler
import com.intellij.refactoring.introduce.PsiIntroduceTarget
import com.intellij.refactoring.introduce.inplace.AbstractInplaceIntroducer
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser
import com.intellij.usageView.UsageInfo
import gdscript.psi.GdExpr
import gdscript.psi.GdMethodDeclTl
import gdscript.refactoring.introduce.inplace.GdInplaceIntroducer

/**
 * Introduce Variable Handler for GDScript
 *
 * Supports only the current method as scope and a selection target
 */
class GdIntroduceVariableHandler : IntroduceHandler<PsiIntroduceTarget<GdExpr>, GdMethodDeclTl>() {

    override fun getRefactoringName(): String {
        return "Introduce Variable"
    }

    override fun getHelpID(): String? {
        return null // no help available
    }

    override fun findSelectionTarget(
        start: Int,
        end: Int,
        file: PsiFile,
        editor: Editor,
        project: Project
    ): PsiIntroduceTarget<GdExpr>? {
        val expr = PsiTreeUtil.findCommonParent(file.findElementAt(start), file.findElementAt(end - 1))
        if (expr is GdExpr) {
            return PsiIntroduceTarget(expr)
        }
        return null
    }

    override fun collectTargets(
        file: PsiFile,
        editor: Editor,
        project: Project
    ): Pair<MutableList<PsiIntroduceTarget<GdExpr>>, Int> {
        return Pair(mutableListOf(), 0) // not really implement. only for selections
    }

    override fun checkSelectedTarget(
        target: PsiIntroduceTarget<GdExpr>,
        file: PsiFile,
        editor: Editor,
        project: Project
    ): String? {
        return null // null == no error
    }

    override fun collectTargetScopes(
        target: PsiIntroduceTarget<GdExpr>,
        editor: Editor,
        file: PsiFile,
        project: Project
    ): MutableList<GdMethodDeclTl> {
        if (target.place == null) {
            return mutableListOf()
        }
        val method = PsiTreeUtil.getParentOfType(target.place!!, GdMethodDeclTl::class.java)
        if (method == null) {
            return mutableListOf()
        }
        return mutableListOf(method)
    }

    override fun getChooseScopeTitle(): String {
        TODO("Not yet implemented") // no scope chooser necessary
    }

    override fun getScopeRenderer(): PsiElementListCellRenderer<GdMethodDeclTl> {
        TODO("Not yet implemented") // no scope chooser necessary
    }

    override fun collectUsages(target: PsiIntroduceTarget<GdExpr>, scope: GdMethodDeclTl): MutableList<UsageInfo> {
        val usages = mutableListOf<UsageInfo>()
        val place = target.place
        // check if place is expression
        if (place is GdExpr) {
            usages.add(UsageInfo(place))
        }
        // TODO search the whole method for multiple occurences.
        return usages
    }

    override fun checkUsages(usages: MutableList<UsageInfo>): String? {
        return null // null == no error
    }

    override fun getIntroducer(
        target: PsiIntroduceTarget<GdExpr>,
        scope: GdMethodDeclTl,
        usages: MutableList<UsageInfo>,
        replaceChoice: OccurrencesChooser.ReplaceChoice,
        file: PsiFile,
        editor: Editor,
        project: Project
    ): AbstractInplaceIntroducer<*, *> {
        if (target.place == null) {
            throw IllegalArgumentException("Target place is null")
        }
        return GdInplaceIntroducer(project, editor, target.place!!, null, emptyArray(), "Introduce Variable", file)
    }
}