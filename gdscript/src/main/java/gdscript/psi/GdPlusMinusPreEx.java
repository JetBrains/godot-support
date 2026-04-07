package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdPlusMinusPreEx extends GdExpr {

  @Nullable
  GdExpr getExpr();

}
