package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdArrEx extends GdExpr {

  @NotNull
  List<GdExpr> getExprList();

  @Nullable
  default GdExpr getBaseExpr() {
      List<GdExpr> exprs = getExprList();
      return exprs.isEmpty() ? null : exprs.getFirst();
  }

  @Nullable
  default GdExpr getIndexExpr() {
    List<GdExpr> exprs = getExprList();
    return exprs.size() < 2 ? null : exprs.get(1);
  }

}
