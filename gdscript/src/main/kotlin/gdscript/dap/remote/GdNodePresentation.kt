package gdscript.dap.remote

import com.intellij.xdebugger.frame.presentation.XValuePresentation
import gdscript.GdIcon
import javax.swing.Icon

internal data class GdObjectPresentation(val className: String, val text: String) {
    fun presentation(): XValuePresentation = object : XValuePresentation() {
        override fun getSeparator(): String = " : "
        override fun renderValue(renderer: XValueTextRenderer) {
            renderer.renderValue(className)
            renderer.renderComment("  $text")
        }
    }
}

internal interface GdPresentedVariable {
    val objectPresentation: GdObjectPresentation
}

/** The icon and [XValuePresentation] of one node row. */
internal data class GdNodeRow(val icon: Icon, val presentation: XValuePresentation)

/**
 * Builds node presentations. The class name supplies the Godot editor icon.
 * Each factory selects the value text and the comment.
 */
internal object GdNodePresentation {
    /**
     * Uses the class name as value text and the ObjectID as a comment.
     * The row name already contains the node name.
     */
    fun byClassName(descriptor: GdNodeDescriptor): GdNodeRow =
        GdNodeRow(GdIcon.getEditorIcon(descriptor.className), presentation(descriptor.className, "  #${descriptor.objectId}"))

    /**
     * Uses the node name as value text and the class and ObjectID as a comment.
     * A missing node name uses the class name as value text.
     */
    fun byNodeName(descriptor: GdNodeDescriptor): GdNodeRow =
        GdNodeRow(
            GdIcon.getEditorIcon(descriptor.className),
            presentation(descriptor.nodeName ?: descriptor.className, "  ${descriptor.className} #${descriptor.objectId}")
        )

    private fun presentation(value: String, comment: String): XValuePresentation = object : XValuePresentation() {
        override fun getSeparator(): String = " : "

        override fun renderValue(renderer: XValueTextRenderer) {
            renderer.renderValue(value)
            renderer.renderComment(comment)
        }
    }
}
