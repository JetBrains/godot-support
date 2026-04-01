package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdExprSt extends GdStmt {

  @NotNull
  GdEndStmt getEndStmt();

  @NotNull
  GdExpr getExpr();

}
