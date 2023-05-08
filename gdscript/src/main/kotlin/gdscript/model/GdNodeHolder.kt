package gdscript.model

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.completion.GdLookup
import gdscript.completion.handler.GdReplaceInsertHandler
import gdscript.utils.StringUtil.camelToSnakeCase
import tscn.psi.TscnNodeHeader

/**
 * Container for Node with processed path & hints
 */
data class GdNodeHolder(
    val element: TscnNodeHeader,
    val relativePath: String,
    val uniqueId: String?,
    val extraInfo: String?,
    val readableNodePath: String,
    val script: String? = null,
) {

    fun lookup(): LookupElement {
        return GdLookup.create(
            readableNodePath,
            color = GdLookup.COLOR_RESOURCE,
            priority = GdLookup.REMOTE_DEFINED,
            typed = element.type,
            tail = extraInfo,
            handler = GdReplaceInsertHandler(uniqueId ?: relativePath),
        );
    }

    fun variable_lookup(): LookupElement {
        var type = element.type
        if (type.isNotBlank()) {
            type = ": $type"
        }

        return GdLookup.create(
            readableNodePath,
            color = GdLookup.COLOR_RESOURCE,
            priority = GdLookup.REMOTE_DEFINED,
            typed = element.type,
            tail = extraInfo,
            handler = GdReplaceInsertHandler("@onready var ${element.name.camelToSnakeCase()}$type = ${uniqueId ?: relativePath}"), // +;
        );
    }

}
