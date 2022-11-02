package gdscript.index.impl

import com.intellij.util.indexing.*
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import common.index.ScalarIndexExtensionExt
import gdscript.index.Indices
import gdscript.index.impl.utils.GdFileResIndexer
import gdscript.index.impl.utils.GdFileResInputFilter

object GdFileResIndex : ScalarIndexExtensionExt<String>() {

    override val id: ID<String, Void> = Indices.FILE_RES;

    override fun getIndexer(): DataIndexer<String, Void, FileContent> {
        return GdFileResIndexer;
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    override fun getVersion(): Int {
        return Indices.VERSION;
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return GdFileResInputFilter;
    }

    override fun dependsOnFileContent(): Boolean {
        return false;
    }

}
