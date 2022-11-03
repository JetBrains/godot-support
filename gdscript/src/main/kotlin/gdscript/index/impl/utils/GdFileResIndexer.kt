package gdscript.index.impl.utils

import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent
import gdscript.psi.utils.PsiGdResourceUtil
import java.util.Collections

object GdFileResIndexer : DataIndexer<String, Void, FileContent> {

    override fun map(inputData: FileContent): MutableMap<String, Void> {
        val resource = PsiGdResourceUtil.resourcePath(inputData.file);

        return Collections.singletonMap(resource, null);
    }

}
