package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.completion.handler.GdQuotedStringInsertHandler
import gdscript.completion.utils.GdStringCompletionUtil.DEFAULT_ACTIONS
import gdscript.completion.utils.GdStringCompletionUtil.INPUT_CLASSES
import gdscript.completion.utils.GdStringCompletionUtil.INPUT_METHODS
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.psi.GdArgExpr
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdPrimaryEx
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdStringValRef
import gdscript.psi.utils.GdNodeUtil
import gdscript.utils.PsiElementUtil.getCallExprOfParam
import gdscript.utils.VirtualFileUtil.resourcePath
import project.psi.util.ProjectInputUtil
import tscn.index.impl.TscnNodeIndex
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

    private data class StringArgument(val call: GdCallEx, val index: Int)

    private fun stringArgument(element: PsiElement): StringArgument? {
        // Is string argument?
        val string = PsiTreeUtil.getParentOfType(element, GdStringValRef::class.java, false) ?: return null
        val literal = string.parent as? GdLiteralEx ?: return null
        val argument = literal.parent as? GdArgExpr ?: return null
        if (argument.expr != literal) return null

        // Is inside call?
        val call = string.getCallExprOfParam() ?: return null
        val index = call.argList?.argExprList?.indexOf(argument) ?: return null
        if (index < 0) return null
        return StringArgument(call, index)
    }

    private fun GdCallEx.calleeReference(): GdRefIdRef? = when (val callee = expr) {
        is GdAttributeEx -> callee.refId
        is GdLiteralEx -> callee.refIdNm
        else -> null
    }

    /** Decides based on [INPUT_METHODS] and the [INPUT_CLASSES]. See also [InputContext] */
    fun inputContext(element: PsiElement): InputContext {
        val (call, index) = stringArgument(element) ?: return InputContext.OTHER

        // Check the callee before resolving input methods.
        val reference = call.calleeReference() ?: return InputContext.OTHER
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

        // TODO: Escape suggestion content that contains quotes or backslashes.
        val priority = when (context) {
            InputContext.ACTION_ARGUMENT, InputContext.UNRESOLVED_NAME_MATCH -> GdLookup.TOP
            InputContext.OTHER -> GdLookup.USER_DEFINED
        }
        (projectActions + defaults).forEach { action ->
            result.addElement(
                GdLookup.create(
                    action,
                    priority = if (action in projectActions) priority else priority - 1, // Prioritize project actions, including overrides.
                    tail = " (input)",
                    color = GdLookup.COLOR_ANNOTATION,
                    handler = GdQuotedStringInsertHandler,
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
                        it,
                        priority = priority,
                        tail = " (group)",
                        color = GdLookup.COLOR_GROUP,
                        handler = GdQuotedStringInsertHandler,
                    )
                )
        }
    }

    private val META_METHODS = setOf("get_meta", "set_meta", "has_meta", "remove_meta")

    fun metaCall(element: PsiElement): GdCallEx? {
        val (call, index) = stringArgument(element) ?: return null
        if (index != 0) return null

        // Is callee a meta method?
        // A same-named script function is the only gap in this name rule.
        return call.takeIf { call.calleeReference()?.text in META_METHODS }
    }

    fun addMetas(element: PsiElement, result: CompletionResultSet, call: GdCallEx) {
        val receiver = (call.expr as? GdAttributeEx)?.expr
        val allNodes by lazy { TscnNodeIndex.INSTANCE.getAllValues(element.project) }
        // TODO: In a static function, a call without a receiver has no owner node. Do not use the script nodes there.
        val nodes = if (receiver == null || receiver.text == "self") {
            val script = element.containingFile.originalFile.virtualFile?.resourcePath()
            // Boss in `level1.tscn` can instance `enemy.tscn` and add `is_boss` meta without a script line.
            // Completion in `enemy.gd` does not show `is_boss` from that instance. We would need to read all level files for that.
            allNodes.filter { it.scriptResource == script }
        } else {
            val path = (receiver as? GdPrimaryEx)?.nodePath
            if (path == null) {
                emptyList()
            } else {
                // A node path can include property names, such as `Child:position:x`. Keep only the node part.
                val nodeName = path.text.trim('$', '%', '"', '^', '\'').substringBefore(':')
                GdNodeUtil.listNodes(path).filter { node ->
                    node.relativePath.trim('$', '"', '\'') == nodeName || node.uniqueId?.trim('%') == nodeName
                }.map { node -> node.element }
            }
        }
        // Fallback to all metas from all nodes with lower priority, when no actual nodes found
        val names = (nodes.ifEmpty { allNodes }).flatMap { TscnNodeUtil.getMetas(it).toList() }
        val prefix = result.prefixMatcher.prefix
        val priority = if (nodes.isEmpty()) GdLookup.USER_DEFINED else GdLookup.TOP
        // TODO: Collect set_meta names from scripts and metadata from .tres files.
        // TODO: Show the value type and the scene name in each item.
        // TODO: Add go to declaration, find usages, rename, and an unknown-meta inspection.
        // Godot treats a meta name that starts with `_` as editor-only.
        // The Inspector hides it, so completion hides it until the user types `_`.
        names.distinct().filter { !it.startsWith('_') || prefix.startsWith('_') }.forEach {
            result.addElement(
                GdLookup.create(
                    it,
                    priority = priority,
                    tail = " (meta)",
                    color = GdLookup.COLOR_META,
                    handler = GdQuotedStringInsertHandler,
                )
            )
        }
    }

}
