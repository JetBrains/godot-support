package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdIsEx extends GdExpr {

  @NotNull
  GdExpr getExpr();

  @NotNull
  GdTypedVal getTypedVal();

}
