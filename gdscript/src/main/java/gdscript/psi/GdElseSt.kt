package gdscript.psi;

import org.jetbrains.annotations.Nullable;

public interface GdElseSt extends GdStmt {

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
