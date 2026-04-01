package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdCastEx extends GdExpr {

  @NotNull
  GdExpr getExpr();

  @NotNull
  GdTypedVal getTypedVal();

}
