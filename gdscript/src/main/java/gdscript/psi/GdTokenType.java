package gdscript.psi;

import com.intellij.psi.tree.IElementType;
import gdscript.GdLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GdTokenType extends IElementType {

    public GdTokenType(@NotNull @NonNls String debugName) {
        super(debugName, GdLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "GdTokenType." + super.toString();
    }

}