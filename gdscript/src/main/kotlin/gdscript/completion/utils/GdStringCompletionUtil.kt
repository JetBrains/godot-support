package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import gdscript.completion.GdLookup
import gdscript.index.impl.GdUserFileIndex
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.utils.PsiElementUtil.getCallExprOfParam
import project.psi.util.ProjectInputUtil
import tscn.psi.utils.TscnNodeUtil

object GdStringCompletionUtil {

    // Taken from Inputs.gd func _(action: StringName)
    private val INPUT_METHODS = arrayOf(
        "action_press",
        "action_release",
        "get_action_raw_strength",
        "get_action_strength",
        "get_axis",
        "is_action_just_pressed",
        "is_action_just_released",
        "is_action_pressed",
    )

    private val META_METHODS = arrayOf(
        "has_meta",
        "get_meta",
        "set_meta",
        "remove_meta",
    )

    fun addInputs(element: PsiElement, result: CompletionResultSet) {
        var priority = GdLookup.USER_DEFINED
        val method = element.getCallExprOfParam()?.expr?.text
        if (method != null && INPUT_METHODS.contains(method)) priority = GdLookup.TOP

        ProjectInputUtil.listActions(element).forEach {
            result.addElement(
                GdLookup.create(
                    it,
                    priority = priority,
                    tail = " (input)",
                    color = GdLookup.COLOR_ANNOTATION,
                )
            )
        }
    }

    fun addGroups(element: PsiElement, result: CompletionResultSet) {
        var priority = GdLookup.USER_DEFINED
        val method = element.getCallExprOfParam()?.expr?.text
        if (method != null && method.contains("group")) priority = GdLookup.TOP

        TscnNodeUtil.listAllGroups(element).forEach {
            result.addElement(
                GdLookup.create(
                    it,
                    priority = priority,
                    tail = " (group)",
                    color = GdLookup.COLOR_GROUP,
                )
            )
        }
    }

    fun addUserFiles(element: PsiElement, result: CompletionResultSet) {
        GdUserFileIndex.getAllValues(element.project).forEach {
            result.addElement(
                GdLookup.create(
                    it.text.trim('"'),
                    priority = GdLookup.USER_DEFINED,
                )
            )
        }
    }

    fun addMetas(element: PsiElement, result: CompletionResultSet) {
        val method = element.getCallExprOfParam() ?: return
        val methodName = method.expr.text
        if (!META_METHODS.contains(methodName)) return

        // TODO tohle nevím jak vyřešit zatím... nějak musím naprasovat na aktuální .tscn, což metody asi jsou v balíčku... ?
        val calledUpon = GdClassMemberUtil.calledUpon(element)

        ProjectInputUtil.listActions(element).forEach {
            result.addElement(
                GdLookup.create(
                    it,
                    priority = GdLookup.TOP,
                    tail = " (meta)",
                    color = GdLookup.COLOR_ANNOTATION,
                )
            )
        }
    }

}
