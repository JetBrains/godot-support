package tscn.toolWindow.model

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.dnd.DragSource
import java.awt.dnd.DragSourceAdapter
import java.awt.dnd.DragSourceDragEvent
import java.awt.dnd.DragSourceDropEvent
import java.awt.dnd.DragSourceListener
import java.awt.event.InputEvent


data class ModifierStatus(val ctrlDown: Boolean, val altDown: Boolean)

// We cannot drop this tracker and let the native calls
// be the source of truth, since Wayland does not seem to have
// a native way of getting keyboard state :)
@Service(Service.Level.APP)
class ModifierTracker : Disposable {
    @Volatile
    private var ctrlDown: Boolean = false

    @Volatile
    private var altDown: Boolean = false

    private val keyEventDispatcher = KeyEventDispatcher { e ->
        ctrlDown = e.isControlDown || e.isMetaDown
        altDown = e.isAltDown
        false
    }

    private val dragSourceListener: DragSourceListener = object : DragSourceAdapter() {
        override fun dragDropEnd(dsde: DragSourceDropEvent?) {
            ctrlDown = false
            altDown = false
        }

        override fun dragOver(dsde: DragSourceDragEvent) = updateFromDrag(dsde)
        override fun dropActionChanged(dsde: DragSourceDragEvent) = updateFromDrag(dsde)
        override fun dragEnter(dsde: DragSourceDragEvent?) {
            if (dsde != null) {
                updateFromDrag(dsde)
            }
        }
    }

    init {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher)
        val dragSource = DragSource.getDefaultDragSource()
        dragSource.addDragSourceListener(dragSourceListener)
    }

    override fun dispose() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher)
        val dragSource = DragSource.getDefaultDragSource()
        dragSource.removeDragSourceListener(dragSourceListener)
    }

    private fun updateFromDrag(dsde: DragSourceDragEvent) {
        val mods = dsde.gestureModifiersEx
        ctrlDown = (mods and InputEvent.CTRL_DOWN_MASK) != 0 || (mods and InputEvent.META_DOWN_MASK) != 0
        altDown = (mods and InputEvent.ALT_DOWN_MASK) != 0
    }


    fun getModifiers(): ModifierStatus {
        val ctrlDown = NativeKeyState.isCtrlDown() ?: ctrlDown
        val altDown = NativeKeyState.isAltDown() ?: altDown

        return ModifierStatus(ctrlDown, altDown)
    }

    companion object {
        fun getInstance(): ModifierTracker = service<ModifierTracker>()
    }
}

object SceneNodeUtil {
    fun checkModifiers(): ModifierStatus = ModifierTracker.getInstance().getModifiers()
}
