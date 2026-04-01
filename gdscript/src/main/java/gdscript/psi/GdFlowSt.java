package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdFlowSt extends GdStmt {

  @NotNull
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @NotNull
  String getType();

}
