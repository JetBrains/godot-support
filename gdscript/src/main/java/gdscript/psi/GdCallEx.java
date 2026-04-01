package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdCallEx extends GdExpr {

  @Nullable
  GdArgList getArgList();

  @NotNull
  GdExpr getExpr();

}
