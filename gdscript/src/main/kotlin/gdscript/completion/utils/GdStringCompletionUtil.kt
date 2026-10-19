package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.completion.utils.GdStringCompletionUtil.DEFAULT_ACTIONS
import gdscript.completion.utils.GdStringCompletionUtil.INPUT_CLASSES
import gdscript.completion.utils.GdStringCompletionUtil.INPUT_METHODS
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.psi.GdArgExpr
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdStringValRef
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

    private val INPUT_CLASSES = setOf("Input", "InputMap", "InputEvent", "InputEventAction", "TouchScreenButton")
    private val INPUT_PARAMETER_TYPES = setOf(GdKeywords.STR, GdKeywords.STR_NAME)

    enum class InputContext {
        /** The resolved SDK method takes an action name at this argument position e.g. `Input.is_action_pressed("<caret>")` */
        ACTION_ARGUMENT,

        /** The call has a known input method name but resolves to no symbols e.g. `unresolved_variable.is_action_pressed("<caret>")` */
        UNRESOLVED_NAME_MATCH,

        /** The literal does not qualify as an action argument e.g. `not_action_method("<caret>")` */
        OTHER,
    }

    /** Decides based on [INPUT_METHODS] and the [INPUT_CLASSES]. See also [InputContext] */
    fun inputContext(element: PsiElement): InputContext {
        // Is string argument?
        val string = PsiTreeUtil.getParentOfType(element, GdStringValRef::class.java, false)
            ?: return InputContext.OTHER
        val literal = string.parent as? GdLiteralEx ?: return InputContext.OTHER
        val argument = literal.parent as? GdArgExpr ?: return InputContext.OTHER
        if (argument.expr != literal) return InputContext.OTHER

        // Is inside call?
        val call = string.getCallExprOfParam() ?: return InputContext.OTHER
        val index = call.argList?.argExprList?.indexOf(argument) ?: return InputContext.OTHER
        if (index < 0) return InputContext.OTHER

        // Is callee an input method, which might expect an action?
        val callee = call.expr
        val reference = when (callee) {
            is GdAttributeEx -> callee.refId
            is GdLiteralEx -> callee.refIdNm
            else -> null
        } ?: return InputContext.OTHER
        if (reference.text !in INPUT_METHODS) return InputContext.OTHER

        // Resolve the callee.
        val symbols = reference.resolveSymbolReferences()
        if (symbols.isEmpty()) return InputContext.UNRESOLVED_NAME_MATCH
        return if (symbols.all {
                it is GdSdkMethodSymbol &&
                    it.declaringClassName in INPUT_CLASSES &&
                    it.gdSignature?.parameters?.getOrNull(index)?.type in INPUT_PARAMETER_TYPES
            }) InputContext.ACTION_ARGUMENT else InputContext.OTHER
    }

    // Canonical default actions from Godot 4.7.2 ProjectSettings.xml.
    private val DEFAULT_ACTIONS = listOf(
        "ui_accept",
        "ui_accessibility_drag_and_drop",
        "ui_cancel",
        "ui_close_dialog",
        "ui_colorpicker_delete_preset",
        "ui_copy",
        "ui_cut",
        "ui_down",
        "ui_end",
        "ui_filedialog_delete",
        "ui_filedialog_find",
        "ui_filedialog_focus_path",
        "ui_filedialog_refresh",
        "ui_filedialog_show_hidden",
        "ui_filedialog_up_one_level",
        "ui_focus_mode",
        "ui_focus_next",
        "ui_focus_prev",
        "ui_graph_delete",
        "ui_graph_duplicate",
        "ui_graph_follow_left",
        "ui_graph_follow_right",
        "ui_home",
        "ui_left",
        "ui_menu",
        "ui_page_down",
        "ui_page_up",
        "ui_paste",
        "ui_redo",
        "ui_right",
        "ui_select",
        "ui_swap_input_direction",
        "ui_text_add_selection_for_next_occurrence",
        "ui_text_backspace",
        "ui_text_backspace_all_to_left",
        "ui_text_backspace_word",
        "ui_text_caret_add_above",
        "ui_text_caret_add_below",
        "ui_text_caret_document_end",
        "ui_text_caret_document_start",
        "ui_text_caret_down",
        "ui_text_caret_left",
        "ui_text_caret_line_end",
        "ui_text_caret_line_start",
        "ui_text_caret_page_down",
        "ui_text_caret_page_up",
        "ui_text_caret_right",
        "ui_text_caret_up",
        "ui_text_caret_word_left",
        "ui_text_caret_word_right",
        "ui_text_clear_carets_and_selection",
        "ui_text_completion_accept",
        "ui_text_completion_query",
        "ui_text_completion_replace",
        "ui_text_dedent",
        "ui_text_delete",
        "ui_text_delete_all_to_right",
        "ui_text_delete_word",
        "ui_text_indent",
        "ui_text_newline",
        "ui_text_newline_above",
        "ui_text_newline_blank",
        "ui_text_scroll_down",
        "ui_text_scroll_up",
        "ui_text_select_all",
        "ui_text_select_word_under_caret",
        "ui_text_skip_selection_for_next_occurrence",
        "ui_text_submit",
        "ui_text_toggle_insert_mode",
        "ui_undo",
        "ui_unicode_start",
        "ui_up",
    )

    /**
     * Adds only project actions at [GdLookup.USER_DEFINED] for [InputContext.OTHER].
     * Adds project actions at [GdLookup.TOP] and defaults one rank below for the [InputContext.ACTION_ARGUMENT] and [InputContext.UNRESOLVED_NAME_MATCH].
     * Reads defaults from SDK ProjectSettings properties, or uses [DEFAULT_ACTIONS] if none exist.
     */
    fun addInputs(
        element: PsiElement,
        result: CompletionResultSet,
        context: InputContext,
        stringName: Boolean = false,
    ) {
        val projectActions = ProjectInputUtil.listActions(element).toSet()
        val defaults = when (context) {
            InputContext.OTHER -> emptyList()
            InputContext.ACTION_ARGUMENT, InputContext.UNRESOLVED_NAME_MATCH ->
                GdPolySymbolQueriesUtil.getSdkClassSymbol(element.project, "ProjectSettings")?.data?.properties
                    ?.map { it.name }
                    ?.filter { it.startsWith("input/") }
                    ?.map { it.removePrefix("input/") }
                    .orEmpty()
                    .ifEmpty { DEFAULT_ACTIONS }
                    .filterNot { '.' in it } // Exclude entries such as `input/ui_close_dialog.macos`.
        }

        val quote = if (stringName) element.text[1] else '"' // Preserve single quotes `'` in StringName literals.
        val priority = when (context) {
            InputContext.ACTION_ARGUMENT, InputContext.UNRESOLVED_NAME_MATCH -> GdLookup.TOP
            InputContext.OTHER -> GdLookup.USER_DEFINED
        }
        (projectActions + defaults).forEach { action ->
            result.addElement(
                GdLookup.create(
                    if (stringName) action else "\"$action\"",
                    priority = if (action in projectActions) priority else priority - 1, // Prioritize project actions, including overrides.
                    tail = " (input)",
                    color = GdLookup.COLOR_ANNOTATION,
                    handler = if (stringName) { insertion, _ ->
                        // Tab replaces the rest of the literal, including the closing quote.
                        // Restore the closing quote if it is missing.
                        if (insertion.completionChar == Lookup.REPLACE_SELECT_CHAR) {
                            val tail = insertion.tailOffset
                            if (insertion.document.charsSequence.getOrNull(tail) != quote) {
                                insertion.document.insertString(tail, quote.toString())
                            }
                        }
                    } else null,
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
