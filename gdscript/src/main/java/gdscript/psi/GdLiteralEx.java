package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdLiteralEx extends GdExpr {

  @Nullable
  GdRefIdRef getRefIdNm();

  @Nullable
  GdStringValRef getStringVal();

}
