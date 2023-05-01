package gdscript.settings.component

import com.intellij.icons.AllIcons.General
import gdscript.settings.GdDownloadSdk
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

object GdSettingsComponents {

    fun addSdk(): JButton {
        val menu = JPopupMenu()
        menu.add("Local...")


        val downloadBtn = JMenuItem("Download...")
        downloadBtn.addActionListener {
            val downloadModal = GdDownloadSdk()

            val screenSize = Toolkit.getDefaultToolkit().screenSize
            downloadModal.pack()
            downloadModal.location = Point(
                screenSize.width / 2 - downloadModal.width / 2, screenSize.height / 2 - downloadModal.height / 2
            )
            downloadModal.title = "Download GdScript SDK"
            downloadModal.isVisible = true
        }

//        menu.add("Download...")
        menu.add(downloadBtn)

        val button = JButton()
        val size = Dimension(30, 30)
        button.size = size
        button.maximumSize = size
        button.preferredSize = size
        button.isFocusPainted = false
        button.icon = General.Add
        button.addActionListener {
            val bounds = button.bounds
            menu.show(button, 0, bounds.height)
        }

        return button
    }

}
