package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdFlowSt extends GdStmt {

  @NotNull
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @NotNull
  String getType();

}
