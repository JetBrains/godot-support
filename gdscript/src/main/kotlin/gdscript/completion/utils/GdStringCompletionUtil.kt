package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import gdscript.completion.GdLookup
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
                    "\"$it\"",
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
            if (it.isNotBlank())
                result.addElement(
                    GdLookup.create(
                        "\"$it\"",
                        priority = priority,
                        tail = " (group)",
                        color = GdLookup.COLOR_GROUP,
                    )
                )
        }
    }

    fun addMetas(element: PsiElement, result: CompletionResultSet) {
        var priority = GdLookup.USER_DEFINED
        val method = element.getCallExprOfParam()?.expr?.text
        if (method != null && method.contains("meta")) priority = GdLookup.TOP

        TscnNodeUtil.listAllMetas(element).forEach {
            result.addElement(
                GdLookup.create(
                    "\"$it\"",
                    priority = priority,
                    tail = " (meta)",
                    color = GdLookup.COLOR_META,
                )
            )
        }
    }

}
