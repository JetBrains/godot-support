package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdAwaitEx extends GdExpr {

  @Nullable
  GdExpr getExpr();

}
