package gdscript.psi;

import org.jetbrains.annotations.NotNull;

public interface GdCastEx extends GdExpr {

  @NotNull
  GdExpr getExpr();

  @NotNull
  GdTypedVal getTypedVal();

}
