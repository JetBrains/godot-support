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
import gdscript.settings.GdSettingsComponent
import gdscript.utils.GdSdkUtil.versionToSdkName
import java.awt.*
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
                    val state = value.modifiableModel.properties as GdLibraryProperties
                    myValue = state.version
                }

                return super.getListCellRendererComponent(list, myValue, index, isSelected, cellHasFocus)
            }
        }

        return comboBox
    }

    fun addSdk(selectSdk: ComboBox<Library>, settings: GdSettingsComponent): JButton {
        val menu = JPopupMenu()

        val localBtn = JMenuItem("Local...")
        localBtn.addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            descriptor.title = "Select GdScript SDK folder"
            val file = FileChooser.chooseFile(descriptor, null, null)
            val path = file?.path ?: return@addActionListener

            val library = GdLibraryManager.registerSdk(path)
            selectSdk.addItem(library)
            settings.sdkPath = path
        }

        val downloadBtn = JMenuItem("Download...")
        downloadBtn.addActionListener {
            val downloadModal = GdDownloadSdk()
            val hintPath = GdLibraryManager.listRegisteredSdks().firstOrNull()?.let { lib ->
                if (lib is LibraryEx && lib.kind is GdLibraryKind)
                    (lib.modifiableModel.properties as GdLibraryProperties).path
                else null
            } ?: ""

            val screenSize = Toolkit.getDefaultToolkit().screenSize
            downloadModal.pack()
            downloadModal.location = Point(
                screenSize.width / 2 - downloadModal.width / 2, screenSize.height / 2 - downloadModal.height / 2
            )
            downloadModal.locationFc.text = hintPath.substringBeforeLast("\\", hintPath.substringBeforeLast("/"))
            downloadModal.title = "Download GdScript SDK"
            downloadModal.isVisible = true

            val path = downloadModal.path
            if (path.isNotBlank()) {
                val version = downloadModal.version
                GdLibraryManager.download(version, path)
                val library = GdLibraryManager.registerSdk(Paths.get(path, version.versionToSdkName()).toString())
                selectSdk.addItem(library)
                selectSdk.selectedItem = library
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
