package gdscript.model

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.completion.GdLookup
import gdscript.completion.handler.GdQuotedNodePathInsertHandler
import gdscript.completion.handler.GdReplaceInsertHandler
import gdscript.psi.utils.GdNodeUtil.nodeNameToIdentifier
import gdscript.psi.utils.GdNodeUtil.quoteIfNeeded
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
        /** Usually [element]'s own type, but an instanced node from scene takes it from its sub scene root. */
        val type: String = element.type,
) {

    /**
     * Text to insert for the unique name: `%Name`, or `%"Name"` when the node name does not fit
     * the unquoted node path alphabet. [uniqueId] itself stays unquoted because it is the key
     * [gdscript.psi.utils.GdNodeUtil.findNode] resolves against.
     */
    private val uniqueNamepath: String?
        get() = if (uniqueId == null) null else "%${quoteIfNeeded(element.name)}"

    fun lookups(): List<LookupElement> {
        val uniqueRef = this.uniqueNamepath

        return listOfNotNull(
                GdLookup.create(
                        readableNodePath,
                        color = GdLookup.COLOR_RESOURCE,
                        priority = GdLookup.REMOTE_DEFINED,
                        typed = type,
                        tail = extraInfo,
                        handler = GdReplaceInsertHandler(uniqueRef ?: relativePath),
                ),
                if (uniqueRef != null)
                    GdLookup.create(
                            uniqueRef,
                            color = GdLookup.COLOR_RESOURCE,
                            priority = GdLookup.REMOTE_DEFINED,
                            typed = type,
                            handler = GdReplaceInsertHandler(uniqueRef),
                    ) else null
        )
    }

    /** Lookups for the quoted unique name context `%"<caret>"`. */
    fun quotedUniqueLookups(): List<LookupElement> {
        if (uniqueId == null) return emptyList()

        return listOf(
                GdLookup.create(
                        "%\"${element.name}\"",
                        color = GdLookup.COLOR_RESOURCE,
                        priority = GdLookup.REMOTE_DEFINED,
                        typed = type,
                        handler = GdQuotedNodePathInsertHandler,
                )
        )
    }

    fun variableLookups(): List<LookupElement> {
        val typeHint = if (type.isNotBlank()) ": $type" else ""
        val uniqueRef = this.uniqueNamepath

        return listOfNotNull(
                GdLookup.create(
                        readableNodePath,
                        color = GdLookup.COLOR_RESOURCE,
                        priority = GdLookup.REMOTE_DEFINED,
                        typed = type,
                        tail = extraInfo,
                        handler = GdReplaceInsertHandler("@onready var ${nodeNameToIdentifier(element.name)}$typeHint = ${uniqueRef ?: relativePath}"),
                ),
                if (uniqueRef != null)
                    GdLookup.create(
                            uniqueRef,
                            color = GdLookup.COLOR_RESOURCE,
                            priority = GdLookup.REMOTE_DEFINED,
                            typed = type,
                            handler = GdReplaceInsertHandler("@onready var ${nodeNameToIdentifier(element.name)}$typeHint = $uniqueRef"),
                    ) else null
        )
    }

}
