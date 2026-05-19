package gdscript.index.impl

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.*
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import common.index.ScalarIndexExtensionExt
import tscn.index.impl.TscnUidIndex

/*
   In Godot 4, resources are categorized into three groups regarding how they store UIDs:

   1. Native Text Resources .tscn, .tres: The UID is embedded directly in the file header e.g., [gd_scene uid="uid://..."].
      Handled via TscnSceneHeaderStub / TscnGdResourceHeaderStub (indexed during the regular TSCN stub build pass).
   2. Scripts and Text Assets .gd, .shader, .cs: These files don't have a header to store metadata. In Godot 4.4+, the
      engine creates a .uid sidecar file e.g., player.gd.uid to store the UID.
      Handled here: the .uid sidecar file is indexed directly; getContainingFiles() returns the .uid file, which is then
      mapped back to the actual source file.
   3. Imported Assets .png, .svg, .wav, etc.: These are binary or external formats. Godot stores their metadata,
      including the UID, in a .import sidecar file e.g., icon.svg.import.

   For the Autoloads feature we only need 1 and 2.
   We may need (3) for the goto file by click on a string literal with UID
 */

class GdFileUidIndex : ScalarIndexExtensionExt<String>() {

    companion object {
        val NAME = ID.create<String, Void>("gdscript.uid")
        const val VERSION = 3

        fun getFiles(uid: String, project: Project): Collection<VirtualFile> {
            // From .tscn / .tres: use the TSCN stub index (no extra lookup needed)
            val tscnFiles = TscnUidIndex.getFiles(uid, project)

            // From .gd / .cs / .gdshader: getContainingFiles returns the .uid sidecar;
            // map each back to the actual source file by stripping the .uid suffix
            val sidecarFiles = FileBasedIndex.getInstance()
                .getContainingFiles(NAME, uid, GlobalSearchScope.allScope(project))
                .mapNotNull { uidFile ->
                    val actualName = uidFile.name.removeSuffix(".uid")
                    uidFile.parent?.findChild(actualName)
                }

            return tscnFiles + sidecarFiles
        }
    }

    override val id = NAME

    override fun getIndexer(): DataIndexer<String, Void, FileContent> {
        return DataIndexer { fileContent ->
            val uid = fileContent.contentAsText.trim().toString()
            if (uid.startsWith("uid://")) mapOf(uid to null) else emptyMap()
        }
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE

    override fun getVersion(): Int = VERSION

    override fun getInputFilter(): FileBasedIndex.InputFilter = FileBasedIndex.InputFilter { file ->
        !file.isDirectory && file.name.endsWith(".uid")
    }

    override fun dependsOnFileContent(): Boolean = true
}
