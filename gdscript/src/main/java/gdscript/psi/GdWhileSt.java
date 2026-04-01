package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdWhileSt extends GdStmt {

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
