package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;

public interface GdLogicEx extends GdExpr {

  @NotNull
  List<GdExpr> getExprList();

}
