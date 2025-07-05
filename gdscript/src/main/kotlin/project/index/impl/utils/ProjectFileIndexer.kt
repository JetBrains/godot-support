package project.index.impl.utils

import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent
import java.util.Collections

object ProjectFileIndexer : DataIndexer<String, Void, FileContent> {

    override fun map(inputData: FileContent): MutableMap<String, Void> {
        return Collections.singletonMap(inputData.file.parent.path, null)
    }

}
