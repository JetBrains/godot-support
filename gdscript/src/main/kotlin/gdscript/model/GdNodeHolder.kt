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
        val nodePath: String? = null,
) {

    fun lookups(): List<LookupElement> {
        return listOfNotNull(
                GdLookup.create(
                        readableNodePath,
                        color = GdLookup.COLOR_RESOURCE,
                        priority = GdLookup.REMOTE_DEFINED,
                        typed = element.type,
                        tail = extraInfo,
                        handler = GdReplaceInsertHandler(uniqueId ?: relativePath),
                ),
                if (uniqueId != null)
                    GdLookup.create(
                            uniqueId,
                            color = GdLookup.COLOR_RESOURCE,
                            priority = GdLookup.REMOTE_DEFINED,
                            typed = element.type,
                            handler = GdReplaceInsertHandler(uniqueId),
                    ) else null
        )
    }

    fun variable_lookups(): List<LookupElement> {
        var type = element.type
        if (type.isNotBlank()) {
            type = ": $type"
        }

        return listOfNotNull(
                GdLookup.create(
                        readableNodePath,
                        color = GdLookup.COLOR_RESOURCE,
                        priority = GdLookup.REMOTE_DEFINED,
                        typed = element.type,
                        tail = extraInfo,
                        handler = GdReplaceInsertHandler("@onready var ${element.name.camelToSnakeCase()}$type = ${uniqueId ?: relativePath}"),
                ),
                if (uniqueId != null)
                    GdLookup.create(
                            uniqueId,
                            color = GdLookup.COLOR_RESOURCE,
                            priority = GdLookup.REMOTE_DEFINED,
                            typed = element.type,
                            handler = GdReplaceInsertHandler("@onready var ${element.name.camelToSnakeCase()}$type = $uniqueId"),
                    ) else null
        )
    }

}
