package gdscript.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

// Could not get kotlin version working -> fields were not visible
// TODO companion object ??
public class GdCodeStyleSettings extends CustomCodeStyleSettings {

    public int LINES_BEFORE_FUNC = 2;

    GdCodeStyleSettings(CodeStyleSettings settings) {
        super("GdCodeStyleSettings", settings);
    }

}
