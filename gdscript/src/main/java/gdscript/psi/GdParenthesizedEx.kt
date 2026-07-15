package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdParenthesizedEx extends GdPrimaryEx {

    @Nullable
    GdExpr getContainedExpression();
}
