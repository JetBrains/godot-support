package gdscript.psi;

import com.intellij.psi.tree.IElementType;
import gdscript.GdLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GdElementType extends IElementType {

    public GdElementType(@NotNull @NonNls String debugName) {
        super(debugName, GdLanguage.INSTANCE);
    }

}
