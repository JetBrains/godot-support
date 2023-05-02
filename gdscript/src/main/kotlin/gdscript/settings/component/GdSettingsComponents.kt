package gdscript.settings.component

import com.intellij.icons.AllIcons.General
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.ui.ComboBox
import gdscript.library.GdLibraryKind
import gdscript.library.GdLibraryManager
import gdscript.library.GdLibraryProperties
import gdscript.settings.GdDownloadSdk
import gdscript.utils.GdSdkUtil.versionToSdkName
import java.awt.Component
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.nio.file.Paths
import javax.swing.*

object GdSettingsComponents {

    fun selectSdk(): ComboBox<Library> {
        val comboBox = ComboBox<Library>()
        GdLibraryManager.listRegisteredSdks().forEach {
            comboBox.addItem(it)
        }

        comboBox.renderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                var myValue = value
                if (value is LibraryEx && value.kind is GdLibraryKind) {
                    myValue = (value.properties as GdLibraryProperties).config?.version ?: value
                }

                return super.getListCellRendererComponent(list, myValue, index, isSelected, cellHasFocus)
            }
        }

        return comboBox
    }

    fun addSdk(): JButton {
        val menu = JPopupMenu()
        val localBtn = JMenuItem("Local...")
        localBtn.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.title = "Select GdScript SDK folder"
            val file = FileChooser.chooseFile(descriptor, null, null)
            val path = file?.path ?: return@addActionListener

            GdLibraryManager.registerSdk(path)
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
                GdLibraryManager.registerSdk(Paths.get(path, version.versionToSdkName()).toString())
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
