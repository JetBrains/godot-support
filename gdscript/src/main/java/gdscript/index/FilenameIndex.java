package gdscript.index;

import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import gdscript.GdFileType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public final class FilenameIndex extends ScalarIndexExtension<String> {

    @Override
    public @NotNull ID<String, Void> getName() {
        return Indices.FILE_NAMES;
    }

    @Override
    public @NotNull DataIndexer<String, Void, FileContent> getIndexer() {
        return inputData -> {
            String filename = inputData.getFileName();
            return Collections.singletonMap(filename.substring(0, filename.length() - 3), null);
        };
    }

    @Override
    public @NotNull KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public FileBasedIndex.@NotNull InputFilter getInputFilter() {
        return file -> file.getFileType() == GdFileType.INSTANCE;
    }

    @Override
    public boolean dependsOnFileContent() {
        return false;
    }

}
