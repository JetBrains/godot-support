package gdscript.form.listener

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JComboBox

/**
 * KeyAdapter for Up/Down keys to iterate over ComboBox
 */
class ComboBoxChangeKeyAdapter(private val selector: JComboBox<*>) : KeyAdapter() {

    override fun keyPressed(e: KeyEvent?) {
        when (e?.keyCode) {
            KeyEvent.VK_UP -> {
                val index = selector.selectedIndex;
                selector.selectedIndex = if (index < 0) selector.itemCount -1 else index;
            }
            KeyEvent.VK_DOWN -> {
                val index = selector.selectedIndex;
                selector.selectedIndex = if (index >= selector.itemCount) 0 else index;
            }
        }
    }

}
