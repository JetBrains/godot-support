package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GdFactorEx extends GdExpr {

  @NotNull
  List<GdExpr> getExprList();

  @NotNull
  GdFactorSign getFactorSign();

}
