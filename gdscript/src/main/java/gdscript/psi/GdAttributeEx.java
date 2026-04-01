package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdAttributeEx extends GdExpr {

  @NotNull
  GdExpr getExpr();
  GdRefIdRef getRefId();

}
