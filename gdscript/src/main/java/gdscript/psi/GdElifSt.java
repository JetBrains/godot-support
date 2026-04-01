package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdElifSt extends GdStmt {

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
