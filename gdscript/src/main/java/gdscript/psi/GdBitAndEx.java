package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;

public interface GdBitAndEx extends GdExpr {

  @NotNull
  GdBitAndSign getBitAndSign();

  @NotNull
  List<GdExpr> getExprList();

}
