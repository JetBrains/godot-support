package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GdShiftEx extends GdExpr {

  @NotNull
  List<GdExpr> getExprList();

}
