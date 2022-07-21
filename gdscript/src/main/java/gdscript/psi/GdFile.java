package gdscript.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import gdscript.GdFileType;
import gdscript.GdLanguage;
import org.jetbrains.annotations.NotNull;

public class GdFile extends PsiFileBase {

    public GdFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, GdLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return GdFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "GdScript File";
    }

}