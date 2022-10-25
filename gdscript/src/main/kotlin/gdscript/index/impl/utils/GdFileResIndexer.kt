package gdscript.index.impl.utils

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent
import gdscript.psi.GdClassNaming
import gdscript.psi.utils.PsiGdResourceUtil
import gdscript.utils.GdVirtualFileUtil.localPath
import java.util.Collections

object GdFileResIndexer : DataIndexer<String, String?, FileContent> {

    override fun map(inputData: FileContent): MutableMap<String, String?> {
        val resource = PsiGdResourceUtil.resourcePath(inputData.file.localPath());
        val className = PsiTreeUtil.getStubChildOfType(inputData.psiFile, GdClassNaming::class.java)?.classname;

        return Collections.singletonMap(resource, className);
    }

}
