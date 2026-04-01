package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdSignEx extends GdExpr {

  @Nullable
  GdExpr getExpr();

}
