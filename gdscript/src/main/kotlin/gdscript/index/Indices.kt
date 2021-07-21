package gdscript.index

import com.intellij.openapi.project.Project
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import gdscript.psi.GdClassNaming

object Indices {

    private val index = FileBasedIndex.getInstance()

    val CLASS_NAMING_INDEX = StubIndexKey.createIndexKey<String, GdClassNaming>("gdscript.classNaming")

    // TODO simplicity
    //    public static<K, V> List values(@NotNull ID<K, V> indexId, @NotNull K dataKey, @NotNull GlobalSearchScope filter) {
    //        return FileBasedIndex.getInstance().getValues(indexId, dataKey, filter);
    //    }

    //    public static<K, V> List values(@NotNull ID<K, V> indexId, @NotNull K dataKey, @NotNull GlobalSearchScope filter) {
    //        return FileBasedIndex.getInstance().getValues(indexId, dataKey, filter);
    //    }

    fun <K, V> keys(indexId: ID<K, V>, project: Project): Collection<K> {
        return index.getAllKeys(indexId, project)
    }

    val FILE_NAMES = ID.create<String, Void>("gdscript.filename")
    val CLASS_NAMES = ID.create<String, Void>("gdscript.classname") // TODO remove
}