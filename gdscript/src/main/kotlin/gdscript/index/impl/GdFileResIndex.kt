package gdscript.index.impl

import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import common.index.FileBasedIndexExtensionExt
import gdscript.index.Indices
import gdscript.index.impl.utils.GdFileResDataExternalizer
import gdscript.index.impl.utils.GdFileResIndexer
import gdscript.index.impl.utils.GdFileResInputFilter

object GdFileResIndex : FileBasedIndexExtensionExt<String, String>() {

    override val id: ID<String, String> = Indices.FILE_RES;

    override fun getIndexer(): DataIndexer<String, String, FileContent> {
        return GdFileResIndexer;
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    override fun getValueExternalizer(): DataExternalizer<String> {
        return GdFileResDataExternalizer;
    }

    override fun getVersion(): Int {
        return Indices.VERSION;
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return GdFileResInputFilter;
    }

    override fun dependsOnFileContent(): Boolean {
        return true;
    }

}
