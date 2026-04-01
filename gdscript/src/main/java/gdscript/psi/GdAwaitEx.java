package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdAwaitEx extends GdExpr {

  @Nullable
  GdExpr getExpr();

}
