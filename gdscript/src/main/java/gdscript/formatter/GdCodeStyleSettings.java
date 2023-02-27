package gdscript.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

// TODO Could not get kotlin version working -> java.lang.NoSuchFieldException: LINES_BEFORE_FUNC
public class GdCodeStyleSettings extends CustomCodeStyleSettings {

    /* Black lines */
    public int LINES_BEFORE_FUNC = 2;
    public int LINES_AFTER_HEADER = 1;
    public int LINES_IN_BETWEEN_VARIABLE_GROUP = 0;
    public int LINES_AFTER_VARIABLE_GROUP = 0;

    /* Spacing */
    public boolean SPACE_BEFORE_COMMA = false;
    public boolean SPACE_AFTER_COMMA = true;
    public boolean SPACE_BEFORE_COLON = false;
    public boolean SPACE_AFTER_COLON = true;

    public GdCodeStyleSettings(CodeStyleSettings settings) {
        super("GdCodeStyleSettings", settings);
    }

}
