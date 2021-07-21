package gdscript.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class Indices {

    private static final FileBasedIndex index = FileBasedIndex.getInstance();

//    public static<K, V> List values(@NotNull ID<K, V> indexId, @NotNull K dataKey, @NotNull GlobalSearchScope filter) {
//        return FileBasedIndex.getInstance().getValues(indexId, dataKey, filter);
//    }

    public static<K, V> Collection<K> keys(@NotNull ID<K, V> indexId, @NotNull Project project) {
        return index.getAllKeys(indexId, project);
    }

    public static final ID<String, Void> FILE_NAMES = ID.create("GdFilenameIndex");
    public static final ID<String, Void> CLASS_NAMES = ID.create("GdClassnameIndex");

}
