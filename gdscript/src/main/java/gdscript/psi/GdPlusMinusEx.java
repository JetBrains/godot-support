package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdPlusMinusEx extends GdExpr {

  @NotNull
  GdExpr getExpr();

}
