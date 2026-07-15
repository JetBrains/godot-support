package gdscript.psi;

import org.jetbrains.annotations.NotNull;

public interface GdAttributeEx extends GdExpr {

  @NotNull
  GdExpr getExpr();
  GdRefIdRef getRefId();

}
