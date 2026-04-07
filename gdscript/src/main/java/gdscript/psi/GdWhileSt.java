package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdWhileSt extends GdStmt {

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
