package gdscript.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

// Could not get kotlin version working -> fields were not visible
public class GdCodeStyleSettings extends CustomCodeStyleSettings {

    public int LINES_BEFORE_CLASS_NAME = 0;

    GdCodeStyleSettings(CodeStyleSettings settings) {
        super("GdCodeStyleSettings", settings);
    }

}
