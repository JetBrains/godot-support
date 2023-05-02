package gdscript.settings.component

import com.intellij.icons.AllIcons.General
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import gdscript.library.GdLibraryManager
import gdscript.settings.GdDownloadSdk
import gdscript.settings.GdSettingsConfigurable
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

object GdSettingsComponents {

    fun addSdk(): JButton {
        val menu = JPopupMenu()
        val localBtn = JMenuItem("Local...")
        localBtn.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.title = "Select GdScript SDK folder"
            val file = FileChooser.chooseFile(descriptor, null, null)
            val path = file?.path ?: return@addActionListener

            GdLibraryManager.setUpLibrary(GdSettingsConfigurable.PROJECT, path)
        }

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

            val path = downloadModal.path
            if (path.isNotBlank()) {
                val version = downloadModal.version
                GdLibraryManager.download(version, path)
            }
        }

        menu.add(localBtn)
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
