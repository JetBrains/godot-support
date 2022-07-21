package gdscript;

import com.intellij.lang.Language;

public class GdLanguage extends Language {

    public static final GdLanguage INSTANCE = new GdLanguage();

    private GdLanguage() {
        super("GdScript");
    }

}
