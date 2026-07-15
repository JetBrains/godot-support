package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdCallEx extends GdExpr {

  @Nullable
  GdArgList getArgList();

  @NotNull
  GdExpr getExpr();

}
