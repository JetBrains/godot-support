package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;

public interface GdComparisonEx extends GdExpr {

  @NotNull
  List<GdExpr> getExprList();

  @NotNull
  GdOperator getOperator();

}
