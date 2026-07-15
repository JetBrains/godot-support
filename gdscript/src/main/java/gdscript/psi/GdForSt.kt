package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdForSt extends GdStmt {

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

  @Nullable
  GdVarNmi getVarNmi();

  @Nullable
  GdTyped getTyped();

}
