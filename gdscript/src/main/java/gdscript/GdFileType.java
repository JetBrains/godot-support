package gdscript;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GdFileType extends LanguageFileType {

    public static final GdFileType INSTANCE = new GdFileType();

    private GdFileType() {
        super(GdLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "GdScript File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "GdScript language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "gd";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return GdIcon.FILE;
    }

}
