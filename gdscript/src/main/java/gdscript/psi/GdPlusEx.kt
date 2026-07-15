package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GdPlusEx extends GdExpr {

  @NotNull
  List<GdExpr> getExprList();

  @NotNull
  GdSign getSign();

}
