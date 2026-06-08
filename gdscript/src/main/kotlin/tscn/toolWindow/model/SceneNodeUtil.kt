package tscn.toolWindow.model

import java.awt.dnd.DragSource
import java.awt.dnd.DragSourceAdapter
import java.awt.dnd.DragSourceDragEvent
import java.awt.event.InputEvent
import java.awt.KeyboardFocusManager


data class ModifierStatus(val ctrlDown: Boolean, val altDown: Boolean)

object ModifierTracker {
    @Volatile private var ctrlDown: Boolean = false
    @Volatile private var altDown: Boolean = false

    init {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { e ->
            ctrlDown = e.isControlDown || e.isMetaDown
            altDown = e.isAltDown
            false
        }
        DragSource.getDefaultDragSource().addDragSourceListener(object : DragSourceAdapter() {
            override fun dragOver(dsde: DragSourceDragEvent) = updateFromDrag(dsde)
            override fun dropActionChanged(dsde: DragSourceDragEvent) = updateFromDrag(dsde)
        })
    }

    private fun updateFromDrag(dsde: DragSourceDragEvent) {
        val mods = dsde.gestureModifiersEx
        ctrlDown = (mods and InputEvent.CTRL_DOWN_MASK) != 0 || (mods and InputEvent.META_DOWN_MASK) != 0
        altDown = (mods and InputEvent.ALT_DOWN_MASK) != 0
    }

    fun getModifiers(): ModifierStatus = ModifierStatus(ctrlDown, altDown)
}

object SceneNodeUtil {
    fun checkModifiers(): ModifierStatus = ModifierTracker.getModifiers()
}
