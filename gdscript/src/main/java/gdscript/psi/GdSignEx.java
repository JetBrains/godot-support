package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdSignEx extends GdExpr {

  @Nullable
  GdExpr getExpr();

}
