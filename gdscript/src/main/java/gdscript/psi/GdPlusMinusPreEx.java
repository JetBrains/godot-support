package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdPlusMinusPreEx extends GdExpr {

  @Nullable
  GdExpr getExpr();

}
