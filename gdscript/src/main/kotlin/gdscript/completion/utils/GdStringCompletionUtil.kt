package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import gdscript.completion.GdLookup
import gdscript.psi.GdAttributeEx
import gdscript.utils.PsiElementUtil.getCallExprOfParam
import project.psi.util.ProjectInputUtil
import tscn.psi.utils.TscnNodeUtil

object GdStringCompletionUtil {

    /** Methods from Input, InputMap, InputEvent, InputEventAction, and TouchScreenButton that take an input action name. */
    private val INPUT_METHODS = arrayOf(
        "action_add_event",
        "action_erase_event",
        "action_erase_events",
        "action_get_deadzone",
        "action_get_events",
        "action_has_event",
        "action_press",
        "action_release",
        "action_set_deadzone",
        "add_action",
        "erase_action",
        "event_is_action",
        "get_action_description",
        "get_action_raw_strength",
        "get_action_strength",
        "get_axis",
        "get_vector",
        "has_action",
        "is_action",
        "is_action_just_pressed",
        "is_action_just_pressed_by_event",
        "is_action_just_released",
        "is_action_just_released_by_event",
        "is_action_pressed",
        "is_action_released",
        "set_action",
    )

    private val META_METHODS = arrayOf(
        "has_meta",
        "get_meta",
        "set_meta",
        "remove_meta",
    )

    /**
     * Gives input actions [GdLookup.TOP] priority when the string is an argument of a method in [INPUT_METHODS].
     * Matches only the method name, without the receiver type.
     * Calls to `Input.is_action_pressed(...)`, `event.is_action_pressed(...)`, and `is_action_pressed(...)` all match.
     */
    fun addInputs(element: PsiElement, result: CompletionResultSet) {
        var priority = GdLookup.USER_DEFINED
        val callee = element.getCallExprOfParam()?.expr
        val method = if (callee is GdAttributeEx) callee.refId?.text else callee?.text
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
